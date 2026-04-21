package com.shubilet.expedition_service.controllers.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.shubilet.expedition_service.common.util.ErrorUtils;
import com.shubilet.expedition_service.common.util.StringUtils;
import com.shubilet.expedition_service.common.util.ValidationUtils;
import com.shubilet.expedition_service.controllers.CreationController;
import com.shubilet.expedition_service.dataTransferObjects.requests.ExpeditionCreationDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.message.MessageDTO;
import com.shubilet.expedition_service.services.CityService;
import com.shubilet.expedition_service.services.ExpeditionService;
import com.shubilet.expedition_service.services.SeatService;



/****

    Domain: Expedition

    Acts as the REST controller responsible for expedition creation operations within the system. This class
    serves as the entry point for company-driven expedition generation requests and orchestrates the validation,
    business rule enforcement, and delegation of responsibilities to underlying domain services. It ensures that
    expeditions are created with consistent route data, valid scheduling and pricing information, and appropriate
    capacity constraints, while also triggering dependent processes such as seat generation.

    <p>

        Technologies:

        <ul>
            <li>Spring Web</li>
            <li>SLF4J</li>
        </ul>

    </p>

    @author Abdullah (Mirliva) GÜNDÜZ - https://github.com/MrMilriva

    @version 1.0
*/
@RestController
@RequestMapping("/api/expeditions")
public class CreationControllerImpl implements CreationController {
    private static final Logger logger = LoggerFactory.getLogger(CreationControllerImpl.class);

    private final CityService cityService;
    private final ExpeditionService expeditionService;
    private final SeatService seatService;

    public CreationControllerImpl(
        CityService cityService,
        ExpeditionService expeditionService,
        SeatService seatService
    ) {
        this.cityService = cityService;
        this.expeditionService = expeditionService;
        this.seatService = seatService;
    }
    
    /****

        Operation: Create

        Handles the creation of a new expedition by validating the incoming {@link ExpeditionCreationDTO} request and
        enforcing business rules such as city consistency, date and price format correctness, capacity limits, and
        company authorization. Upon successful validation, delegates expedition persistence to the expedition service
        and triggers seat generation based on the defined capacity. Returns a success message when the expedition and
        its seats are created successfully, or an error response when validation or creation fails.

        <p>

            Uses:

            <ul>
                <li>{@link ExpeditionCreationDTO} as the request payload carrying expedition details</li>
                <li>{@link ErrorUtils} for building standardized {@link MessageDTO}-based error responses</li>
                <li>{@link StringUtils} for null/blank and equality checks</li>
                <li>{@link ValidationUtils} for numeric and date format validation</li>
                <li>{@link CityService} for validating departure and arrival city existence</li>
                <li>{@link ExpeditionService} for expedition persistence</li>
                <li>{@link SeatService} for automatic seat generation</li>
                <li>{@link Logger} for audit and diagnostic logging</li>
            </ul>

        </p>

        @param request the expedition creation request containing route, schedule, pricing, capacity, and company details

        @return a response entity containing a {@link MessageDTO} with a success message, or an error response when
        validation fails or expedition creation cannot be completed
    */
    @PostMapping("/create")
    public ResponseEntity<MessageDTO> createExpedition(@RequestBody ExpeditionCreationDTO request) {
        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.MESSAGE_DTO);

        ///STEP 1: Classic validations
        if(request == null) {
            logger.error("Expedition creation request is null");
            return errorUtils.criticalError();
        }

        String departureCity = request.getDepartureCity();
        String arrivalCity = request.getArrivalCity();
        String date = request.getDate();
        String time = request.getTime();
        double price = request.getPrice();
        int duration = request.getDuration();
        int companyId = request.getCompanyId();
        int capacity = request.getCapacity();
        
        if(StringUtils.isNullOrBlank(arrivalCity)) {
            logger.error("Arrival city is null or blank");
            return errorUtils.isNull("Arrival city");
        }

        if(StringUtils.isNullOrBlank(departureCity)) {
            logger.error("Departure city is null or blank");
            return errorUtils.isNull("Departure city");
        }

        if(StringUtils.isNullOrBlank(date)) {
            logger.error("Date is null or blank");
            return errorUtils.isNull("Date");
        }
        
        if(StringUtils.isNullOrBlank(time)) {
            logger.error("Time is null or blank");
            return errorUtils.isNull("Time");
        }

        if(!ValidationUtils.isValidBigDouble(price)) {
            logger.error("Price is invalid: {}", price);
            return errorUtils.isInvalidFormat("Price");
        }

        if(duration < 0) {
            logger.error("Duration is invalid: {}", duration);
            return errorUtils.isInvalidFormat("Duration");
        }

        if(companyId <= 0) {
            logger.error("Company Id is invalid: {}", companyId);
            return errorUtils.unauthorized();
        }

        if(capacity <= 0) {
            logger.error("Capacity is invalid: {}", capacity);
            return errorUtils.isInvalidFormat("Capacity");
        }

        //STEP 2: Spesific validations
        if(StringUtils.nullSafeEquals(arrivalCity, departureCity)) {
            logger.error("Arrival city and Departure city are the same: {}", arrivalCity);
            return errorUtils.sameCityError();
        }

        if(!cityService.cityExists(arrivalCity)) {
            logger.error("Arrival city not found: {}", arrivalCity);
            return errorUtils.notFound("Arrival city");
        }

        if(!cityService.cityExists(departureCity)) {
            logger.error("Departure city not found: {}", departureCity);
            return errorUtils.notFound("Departure city");
        }

        if(!ValidationUtils.isValidDate(date)) {
            logger.error("Date format is invalid: {}", date);
            return errorUtils.isInvalidFormat("Date");
        }

        if(capacity > 1000) {
            logger.error("Capacity exceeds maximum limit: {}", capacity);
            return errorUtils.isInvalidFormat("Capacity");
        }

        //STEP 3: Generation logic 
        int expeditionId = expeditionService.createExpedition(
            companyId,
            departureCity,
            arrivalCity,
            date,
            time,
            capacity,
            price,
            duration
        );

        if(expeditionId == -1) {
            logger.error("Failed to create expedition due to invalid city IDs.");
            return errorUtils.criticalError();
        }

        seatService.generateSeats(expeditionId, capacity);

        logger.info("Expedition created successfully with Id: {}", expeditionId);
        return ResponseEntity.ok(new MessageDTO("Expedition created successfully."));
    }
    
}
