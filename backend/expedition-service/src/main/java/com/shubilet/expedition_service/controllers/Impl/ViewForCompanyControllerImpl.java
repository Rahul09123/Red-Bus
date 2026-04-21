package com.shubilet.expedition_service.controllers.Impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shubilet.expedition_service.common.util.ErrorUtils;
import com.shubilet.expedition_service.common.util.StringUtils;
import com.shubilet.expedition_service.common.util.ValidationUtils;
import com.shubilet.expedition_service.controllers.ViewForCompanyController;
import com.shubilet.expedition_service.dataTransferObjects.requests.CompanyIdDTO;
import com.shubilet.expedition_service.dataTransferObjects.requests.ExpeditionViewByDateDTO;
import com.shubilet.expedition_service.dataTransferObjects.requests.ExpeditionViewByIdDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.base.ExpeditionForCompanyDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.base.SeatForCompanyDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.complex.ExpeditionsForCompanyDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.complex.SeatsForCompanyDTO;
import com.shubilet.expedition_service.services.ExpeditionService;
import com.shubilet.expedition_service.services.SeatService;

/****

    Domain: Expedition

    Serves as the REST controller responsible for company-scoped expedition viewing operations. This controller
    exposes endpoints that allow companies to query their expeditions by date, retrieve all active (upcoming)
    expeditions, list all expeditions regardless of status, and inspect detailed seat information for a specific
    expedition. It acts as a read-focused boundary layer, performing request validation, enforcing company context,
    and delegating data retrieval responsibilities to underlying expedition and seat services.

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
@RequestMapping("/api/view/company")
public class ViewForCompanyControllerImpl implements ViewForCompanyController {
    private static final Logger logger = LoggerFactory.getLogger(ViewForCompanyControllerImpl.class);

    private final ExpeditionService expeditionService;
    private final SeatService seatService;
    
    public ViewForCompanyControllerImpl(
        ExpeditionService expeditionService,
        SeatService seatService
    ) {
        this.expeditionService = expeditionService;
        this.seatService = seatService;
    }
    
    /****

        Operation: ViewByDate

        Retrieves the list of expeditions for a specific company filtered by the given date. Validates the incoming
        {@link ExpeditionViewByDateDTO} request by checking company identity and date format, then delegates the lookup
        to the expedition service to fetch matching expeditions. Returns an {@link ExpeditionsForCompanyDTO} containing
        the expedition list when results are found, or a standardized error response when the request is invalid or
        no expeditions exist for the specified criteria.

        <p>

            Uses:

            <ul>
                <li>{@link ExpeditionViewByDateDTO} for companyId and date filter input</li>
                <li>{@link ErrorUtils} for building standardized {@link ExpeditionsForCompanyDTO}-based error responses</li>
                <li>{@link StringUtils} for null/blank checks</li>
                <li>{@link ValidationUtils} for date format validation</li>
                <li>{@link ExpeditionService} for querying expeditions by date and companyId</li>
                <li>{@link ExpeditionsForCompanyDTO} as the response wrapper containing expedition results</li>
                <li>{@link ExpeditionForCompanyDTO} as the per-expedition response representation</li>
                <li>{@link Logger} for audit and diagnostic logging</li>
            </ul>

        </p>

        @param expeditionViewByDateDTO the request payload containing the companyId and date filter

        @return a response entity containing an {@link ExpeditionsForCompanyDTO} with expeditions for the given date,
        or an error response when the request is invalid or no expeditions are found
    */
    @PostMapping("/expeditionsByDate")
    public ResponseEntity<ExpeditionsForCompanyDTO> viewExpeditionsByDate(@RequestBody ExpeditionViewByDateDTO expeditionViewByDateDTO) {
        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.EXPEDITIONS_FOR_COMPANY_DTO);

        //STEP 1: Classic validation
        if(expeditionViewByDateDTO == null) {
            logger.error("ExpeditionViewByDateDTO is null");
            return errorUtils.criticalError();
        }

        int companyId = expeditionViewByDateDTO.getCompanyId();
        String date = expeditionViewByDateDTO.getDate();

        if(companyId <= 0) {
            logger.error("Company Id is invalid: {}", companyId);
            return errorUtils.isInvalidFormat("Company Id");
        }

        if(StringUtils.isNullOrBlank(date)) {
            logger.error("Date is null or blank");
            return errorUtils.isInvalidFormat("Date");
        }

        if(!ValidationUtils.isValidDate(date)) {
            logger.error("Date format is invalid: {}", date);
            return errorUtils.isInvalidFormat("Date");
        }

        //STEP 2: Spesific validations
        
        //STEP 3: Business Logic
        List<ExpeditionForCompanyDTO> expeditions = expeditionService.findExpeditionsByInstantAndCompanyId(date, companyId);

        if(expeditions.isEmpty()) {
            logger.error("No expeditions found for date: {}", date);
            return errorUtils.notFound("Expeditions");
        }

        logger.info("Expeditions found for date: {}", date);
        return ResponseEntity.ok().body(new ExpeditionsForCompanyDTO("Expeditions found", expeditions));
    }

    /****

        Operation: ViewActive

        Retrieves all upcoming (active) expeditions associated with the specified company. Validates the incoming
        {@link CompanyIdDTO} request to ensure a valid company identifier is provided, then delegates the lookup
        to the expedition service to fetch expeditions that are scheduled for future execution. Returns an
        {@link ExpeditionsForCompanyDTO} containing the active expeditions when available, or a standardized
        error response when the request is invalid or no active expeditions are found.

        <p>

            Uses:

            <ul>
                <li>{@link CompanyIdDTO} for company identifier input</li>
                <li>{@link ErrorUtils} for building standardized {@link ExpeditionsForCompanyDTO}-based error responses</li>
                <li>{@link ExpeditionService} for querying upcoming expeditions by companyId</li>
                <li>{@link ExpeditionsForCompanyDTO} as the response wrapper containing expedition results</li>
                <li>{@link ExpeditionForCompanyDTO} as the per-expedition response representation</li>
                <li>{@link Logger} for audit and diagnostic logging</li>
            </ul>

        </p>

        @param companyIdDTO the request payload containing the companyId whose active expeditions will be retrieved

        @return a response entity containing an {@link ExpeditionsForCompanyDTO} with upcoming expeditions for the company,
        or an error response when the request is invalid or no active expeditions are found
    */
    @PostMapping("/activeExpeditions")
    public ResponseEntity<ExpeditionsForCompanyDTO> viewActiveExpeditions(@RequestBody CompanyIdDTO companyIdDTO) {
        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.EXPEDITIONS_FOR_COMPANY_DTO);

        //STEP 1: Classic validation
        if(companyIdDTO == null) {
            logger.error("CompanyIdDTO is null");
            return errorUtils.criticalError();
        }

        int companyId = companyIdDTO.getCompanyId();

        if(companyId <= 0) {
            logger.error("Company Id is invalid: {}", companyId);
            return errorUtils.isInvalidFormat("Company Id");
        }

        //STEP 2: Spesific validations

        //STEP 3: Business Logic
        List<ExpeditionForCompanyDTO> expeditions = expeditionService.findUpcomingExpeditions(companyId);

        if(expeditions.isEmpty()) {
            logger.error("No active expeditions found for company id: {}", companyId);
            return errorUtils.notFound("Expeditions");
        }

        logger.info("Active expeditions found for company id: {}", companyId);
        return ResponseEntity.ok().body(new ExpeditionsForCompanyDTO("Expeditions found", expeditions));
    }

    /****

        Operation: ViewAll

        Retrieves all expeditions associated with the specified company, regardless of their temporal state
        (past, active, or upcoming). Validates the incoming {@link CompanyIdDTO} to ensure a valid company identifier
        is provided, then delegates the lookup to the expedition service to fetch the complete expedition list.
        Returns an {@link ExpeditionsForCompanyDTO} containing the expeditions when results are available, or a
        standardized error response when the request is invalid or no expeditions are found.

        <p>

            Uses:

            <ul>
                <li>{@link CompanyIdDTO} for company identifier input</li>
                <li>{@link ErrorUtils} for building standardized {@link ExpeditionsForCompanyDTO}-based error responses</li>
                <li>{@link ExpeditionService} for querying all expeditions by companyId</li>
                <li>{@link ExpeditionsForCompanyDTO} as the response wrapper containing expedition results</li>
                <li>{@link ExpeditionForCompanyDTO} as the per-expedition response representation</li>
                <li>{@link Logger} for audit and diagnostic logging</li>
            </ul>

        </p>

        @param companyIdDTO the request payload containing the companyId whose expeditions will be retrieved

        @return a response entity containing an {@link ExpeditionsForCompanyDTO} with all expeditions for the company,
        or an error response when the request is invalid or no expeditions are found
    */

    @PostMapping("/allExpeditions")
    public ResponseEntity<ExpeditionsForCompanyDTO> viewAllExpeditions(@RequestBody CompanyIdDTO companyIdDTO) {
        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.EXPEDITIONS_FOR_COMPANY_DTO);

        //STEP 1: Classic validation
        if(companyIdDTO == null) {
            logger.error("CompanyIdDTO is null");
            return errorUtils.criticalError();
        }

        int companyId = companyIdDTO.getCompanyId();

        if(companyId <= 0) {
            logger.error("Company Id is invalid: {}", companyId);
            return errorUtils.isInvalidFormat("Company Id");
        }

        //STEP 2: Spesific validations

        //STEP 3: Business Logic
        List<ExpeditionForCompanyDTO> expeditions = expeditionService.findAllExpeditions(companyId);

        if(expeditions.isEmpty()) {
            logger.error("No expeditions found for company id: {}", companyId);
            return errorUtils.notFound("Expeditions");
        }

        logger.info("Expeditions found for company id: {}", companyId);
        return ResponseEntity.ok().body(new ExpeditionsForCompanyDTO("Expeditions found", expeditions));
    }

    /****

        Operation: ViewDetails

        Retrieves detailed seat information for a specific expedition belonging to the given company. Validates the
        incoming {@link ExpeditionViewByIdDTO} request to ensure both company and expedition identifiers are valid,
        then delegates the lookup to the seat service to fetch seat-level details for the expedition. Returns a
        {@link SeatsForCompanyDTO} containing the seat list when data is available, or a standardized error response
        when the request is invalid or no seat information is found.

        <p>

            Uses:

            <ul>
                <li>{@link ExpeditionViewByIdDTO} for companyId and expeditionId input</li>
                <li>{@link ErrorUtils} for building standardized {@link SeatsForCompanyDTO}-based error responses</li>
                <li>{@link SeatService} for querying seats by expeditionId and companyId</li>
                <li>{@link SeatsForCompanyDTO} as the response wrapper containing seat details</li>
                <li>{@link SeatForCompanyDTO} as the per-seat response representation</li>
                <li>{@link Logger} for audit and diagnostic logging</li>
            </ul>

        </p>

        @param expeditionViewById the request payload containing the companyId and expeditionId to inspect

        @return a response entity containing a {@link SeatsForCompanyDTO} with seat details for the expedition,
        or an error response when the request is invalid or no seat data is found
    */
   
    @PostMapping("/expeditionDetails")
    public ResponseEntity<SeatsForCompanyDTO> viewExpeditionDetails(@RequestBody ExpeditionViewByIdDTO expeditionViewById) {
        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.SEATS_FOR_COMPANY_DTO);
        
        //STEP 1: Classic validation
        if(expeditionViewById == null) {
            logger.error("ExpeditionViewByIdDTO is null");
            return errorUtils.criticalError();
        }

        int companyId = expeditionViewById.getCompanyId();
        int expeditionId = expeditionViewById.getExpeditionId();

        if(companyId <= 0) {
            logger.error("Company Id is invalid: {}", companyId);
            return errorUtils.isInvalidFormat("Company Id");
        }
        if(expeditionId <= 0) {
            logger.error("Expedition Id is invalid: {}", expeditionId);
            return errorUtils.isInvalidFormat("Expedition Id");
        }

        //STEP 2: Spesific validations

        //STEP 3: Business Logic
        List<SeatForCompanyDTO> seats = seatService.getSeatsByExpeditionIdAndCompanyId(expeditionId, companyId);

        if(seats.isEmpty()) {
            logger.error("No seats found for expedition id: {}", expeditionId);
            return errorUtils.notFound("Seats");
        }

        logger.info("Expedition details found for expedition id: {}", expeditionId);
        return ResponseEntity.ok().body(new SeatsForCompanyDTO("Expedition details found", seats));
    }
}
