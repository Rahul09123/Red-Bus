package com.shubilet.member_service.controllers.Impl;

import com.shubilet.member_service.common.enums.Gender;
import com.shubilet.member_service.common.util.ErrorUtils;
import com.shubilet.member_service.common.util.StringUtils;
import com.shubilet.member_service.common.util.ValidationUtils;
import com.shubilet.member_service.dataTransferObjects.requests.FavoriteCompanyDeletionDTO;
import com.shubilet.member_service.dataTransferObjects.requests.resourceDTOs.AdminIdDTO;
import com.shubilet.member_service.dataTransferObjects.requests.resourceDTOs.CardCreationDTO;
import com.shubilet.member_service.dataTransferObjects.requests.MemberAttributeChangeDTO;
import com.shubilet.member_service.dataTransferObjects.requests.profile.AdminProfileDTO;
import com.shubilet.member_service.dataTransferObjects.requests.profile.CompanyProfileDTO;
import com.shubilet.member_service.dataTransferObjects.requests.profile.CustomerProfileDTO;
import com.shubilet.member_service.dataTransferObjects.requests.CustomerIdDTO;
import com.shubilet.member_service.dataTransferObjects.requests.FavoriteCompanyAdditionDTO;
import com.shubilet.member_service.dataTransferObjects.requests.resourceDTOs.CardDeletionDTO;
import com.shubilet.member_service.dataTransferObjects.requests.resourceDTOs.CompanyIdDTO;
import com.shubilet.member_service.dataTransferObjects.responses.MessageDTO;
import com.shubilet.member_service.models.Admin;
import com.shubilet.member_service.models.Company;
import com.shubilet.member_service.models.Customer;
import com.shubilet.member_service.models.FavoriteCompany;
import com.shubilet.member_service.services.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@RestController
@RequestMapping("/api/profile")
public class ProfileControllerImpl {
    private Logger logger = LoggerFactory.getLogger(ProfileControllerImpl.class);
    private ProfileService profileService;
    private RestTemplate restTemplate;

    ProfileControllerImpl(RestTemplate restTemplate, ProfileService profileService) {
        this.restTemplate = restTemplate;
        this.profileService = profileService;
    }

    @PostMapping("/customer/edit/name")
    public ResponseEntity<MessageDTO> editCustomerProfileName(@RequestBody MemberAttributeChangeDTO memberAttributeChangeDTO) {

        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.MessageDTO);
        // DTO Existence Check
        if (memberAttributeChangeDTO == null) {
            logger.warn("Given MemberAttributeChangeDTO is null");
            return errorUtils.criticalError();
        }

        // Attributes Null or Blank Check
        if (StringUtils.isNullOrBlank(memberAttributeChangeDTO.getAttribute())) {
            logger.warn("Given attribute is null or blank");
            return errorUtils.isNull("Attribute");
        }

        // Validation Check
        if (memberAttributeChangeDTO.getMemberId() <= 0) {
            logger.warn("Given MemberId is invalid: {}", memberAttributeChangeDTO.getMemberId());
            return errorUtils.isInvalidFormat("MemberId");
        }

        if (!profileService.isCustomerExists(memberAttributeChangeDTO.getMemberId())) {
            logger.warn("Customer not found with given MemberId: {}", memberAttributeChangeDTO.getMemberId());
            return errorUtils.notFound("Customer");
        }

        if (!profileService.editName(memberAttributeChangeDTO.getMemberId(), memberAttributeChangeDTO.getAttribute())) {
            logger.error("Critical error occurred while editing name for MemberId: {}", memberAttributeChangeDTO.getMemberId());
            return errorUtils.criticalError();
        }

        logger.info("Successfully edited name for MemberId: {}", memberAttributeChangeDTO.getMemberId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageDTO("Success"));
    }

    @PostMapping("/customer/edit/surname")
    public ResponseEntity<MessageDTO> editCustomerProfileSurname(@RequestBody MemberAttributeChangeDTO memberAttributeChangeDTO) {
        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.MessageDTO);

        // DTO Existence Check
        if (memberAttributeChangeDTO == null) {
            logger.warn("Given MemberAttributeChangeDTO is null");
            return errorUtils.criticalError();
        }

        // Attributes Null or Blank Check
        if (StringUtils.isNullOrBlank(memberAttributeChangeDTO.getAttribute())) {
            logger.warn("Given attribute is null or blank");
            return errorUtils.isNull("Attribute");
        }

        // Validation Check
        if (memberAttributeChangeDTO.getMemberId() <= 0) {
            logger.warn("Given MemberId is invalid: {}", memberAttributeChangeDTO.getMemberId());
            return errorUtils.isInvalidFormat("MemberId");
        }

        if (!profileService.isCustomerExists(memberAttributeChangeDTO.getMemberId())) {
            logger.warn("Customer not found with given MemberId: {}", memberAttributeChangeDTO.getMemberId());
            return errorUtils.notFound("Customer");
        }

        if (!profileService.editSurname(memberAttributeChangeDTO.getMemberId(), memberAttributeChangeDTO.getAttribute())) {
            logger.error("Critical error occurred while editing surname for MemberId: {}", memberAttributeChangeDTO.getMemberId());
            return errorUtils.criticalError();
        }

        logger.info("Successfully edited surname for MemberId: {}", memberAttributeChangeDTO.getMemberId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageDTO("Success"));
    }

    @PostMapping("/customer/edit/gender")
    public ResponseEntity<MessageDTO> editCustomerProfileGender(@RequestBody MemberAttributeChangeDTO memberAttributeChangeDTO) {
        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.MessageDTO);   
        // DTO Existence Check
        if (memberAttributeChangeDTO == null) {
            logger.warn("Given MemberAttributeChangeDTO is null");
            return errorUtils.criticalError();
        }

        // Attributes Null or Blank Check
        if (StringUtils.isNullOrBlank(memberAttributeChangeDTO.getAttribute())) {
            logger.warn("Given attribute is null or blank");
            return errorUtils.isNull("Attribute");

        }

        // Validation Check
        if (memberAttributeChangeDTO.getMemberId() <= 0) {
            logger.warn("Given MemberId is invalid: {}", memberAttributeChangeDTO.getMemberId());
            return errorUtils.isInvalidFormat("MemberId");
        }
        if (!ValidationUtils.isValidGender(memberAttributeChangeDTO.getAttribute())) {
            logger.warn("Given gender is not valid: {}", memberAttributeChangeDTO.getAttribute());
            return errorUtils.isInvalidFormat("Gender");
        }

        if (!profileService.isCustomerExists(memberAttributeChangeDTO.getMemberId())) {
            logger.warn("Customer not found with given MemberId: {}", memberAttributeChangeDTO.getMemberId()); 
            return errorUtils.notFound("Customer");
        }

        if (!profileService.editGender(memberAttributeChangeDTO.getMemberId(), Gender.fromValue(memberAttributeChangeDTO.getAttribute()))) {
            logger.error("Critical error occurred while editing gender for MemberId: {}", memberAttributeChangeDTO.getMemberId());
            return errorUtils.criticalError();
        }
        
        logger.info("Successfully edited gender for MemberId: {}", memberAttributeChangeDTO.getMemberId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageDTO("Success"));
    }

    @PostMapping("/customer/edit/email")
    public ResponseEntity<MessageDTO> editCustomerProfileEmail(@RequestBody MemberAttributeChangeDTO memberAttributeChangeDTO) {
        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.MessageDTO);
        // DTO Existence Check
        if (memberAttributeChangeDTO == null) {
            logger.warn("Given MemberAttributeChangeDTO is null");
            return errorUtils.criticalError();
        }

        // Attributes Null or Blank Check
        if (StringUtils.isNullOrBlank(memberAttributeChangeDTO.getAttribute())) {
            logger.warn("Given attribute is null or blank");
            return errorUtils.isNull("Attribute");

        }

        // Validation Check
        if (memberAttributeChangeDTO.getMemberId() <= 0) {
            logger.warn("Given MemberId is invalid: {}", memberAttributeChangeDTO.getMemberId());
            return errorUtils.isInvalidFormat("MemberId");
        }
        if (!ValidationUtils.isValidEmail(memberAttributeChangeDTO.getAttribute())) {
            logger.warn("Given email is not in valid format: {}", memberAttributeChangeDTO.getAttribute());
            return errorUtils.isInvalidFormat("Email");
        }

        if (!profileService.isCustomerExists(memberAttributeChangeDTO.getMemberId())) {
            logger.warn("Customer not found with given MemberId: {}", memberAttributeChangeDTO.getMemberId());
            return errorUtils.notFound("Customer");
        }

        if (!profileService.editEmail(memberAttributeChangeDTO.getMemberId(), memberAttributeChangeDTO.getAttribute())) {
            logger.error("Critical error occurred while editing email for MemberId: {}", memberAttributeChangeDTO.getMemberId());
            return errorUtils.criticalError();
        }

        logger.info("Successfully edited email for MemberId: {}", memberAttributeChangeDTO.getMemberId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageDTO("Success"));
    }

    @PostMapping("/customer/edit/password")
    public ResponseEntity<MessageDTO> editCustomerProfilePassword(@RequestBody MemberAttributeChangeDTO memberAttributeChangeDTO) {
        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.MessageDTO);
        // DTO Existence Check
        if (memberAttributeChangeDTO == null) {
            logger.warn("Given MemberAttributeChangeDTO is null");
            return errorUtils.criticalError();
        }

        // Attributes Null or Blank Check
        if (StringUtils.isNullOrBlank(memberAttributeChangeDTO.getAttribute())) {
            logger.warn("Given attribute is null or blank");
            return errorUtils.isNull("Attribute");

        }

        // Validation Check
        if (memberAttributeChangeDTO.getMemberId() <= 0) {
            logger.warn("Given MemberId is invalid: {}", memberAttributeChangeDTO.getMemberId());
            return errorUtils.isInvalidFormat("MemberId");
        }
        if (!ValidationUtils.isValidPassword(memberAttributeChangeDTO.getAttribute())) {
            logger.warn("Given password is not in valid format: {}", memberAttributeChangeDTO.getAttribute());
            return errorUtils.isInvalidFormat("Password");
        }

        if (!profileService.isCustomerExists(memberAttributeChangeDTO.getMemberId())) {
            logger.warn("Customer not found with given MemberId: {}", memberAttributeChangeDTO.getMemberId());
            return errorUtils.notFound("Customer");
        }

        if (!profileService.editPassword(memberAttributeChangeDTO.getMemberId(), memberAttributeChangeDTO.getAttribute())) {
            logger.error("Critical error occurred while editing password for MemberId: {}", memberAttributeChangeDTO.getMemberId());
            return errorUtils.criticalError();
        }

        logger.info("Successfully edited password for MemberId: {}", memberAttributeChangeDTO.getMemberId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageDTO("Success"));
    }

    @PostMapping("/customer/favoriteCompany/add")
    public ResponseEntity<MessageDTO> customerProfileAddFavoriteCompany(@RequestBody FavoriteCompanyAdditionDTO favoriteCompanyAdditionDTO) {
        // DTO Existence Check
        if (favoriteCompanyAdditionDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDTO("Body can't be null"));
        }

        // Validation Check
        if (favoriteCompanyAdditionDTO.getCustomerId() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDTO("Invalid Customer Id"));
        }
        if (favoriteCompanyAdditionDTO.getCompanyId() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDTO("Invalid Company Id"));
        }

        if (!profileService.isCustomerExists(favoriteCompanyAdditionDTO.getCustomerId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDTO("Customer not Found with Given ID"));
        }
        if (!profileService.isCompanyExists(favoriteCompanyAdditionDTO.getCompanyId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDTO("Company not Found with Given ID"));
        }
        int customerId = favoriteCompanyAdditionDTO.getCustomerId();
        int companyId = favoriteCompanyAdditionDTO.getCompanyId();
        FavoriteCompany favoriteCompany = new FavoriteCompany(customerId, companyId);

        if (!profileService.addFavoriteCompany(favoriteCompany)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDTO("Critical Error"));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageDTO("Successfully Added"));
    }

    @PostMapping("/customer/favoriteCompany/delete")
    public ResponseEntity<MessageDTO> customerProfileDeleteFavoriteCompany(@RequestBody FavoriteCompanyDeletionDTO favoriteCompanyDeletionDTO) {
        // DTO Existence Check
        if (favoriteCompanyDeletionDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDTO("Body can't be null"));
        }

        // Validation Check
        if (favoriteCompanyDeletionDTO.getRelationId() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDTO("Invalid Relation Id"));
        }

        if (!profileService.isRelationExists(favoriteCompanyDeletionDTO.getRelationId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDTO("Relation not Found with Given ID"));
        }

        if (!profileService.deleteFavoriteCompany(favoriteCompanyDeletionDTO.getRelationId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDTO("Critical Error"));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageDTO("Successfully Deleted Favorite Company"));
    }

    @PostMapping("/customer/card/add")
    public ResponseEntity<MessageDTO> customerProfileAddCard(@RequestBody CardCreationDTO cardCreationDTO) {
        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.MessageDTO);
        // DTO Existence Check
        if (cardCreationDTO == null) {
            logger.error("Given CardCreationDTO is null");
            return errorUtils.criticalError();
        }

        // Attributes Null or Blank Check
        if (StringUtils.isNullOrBlank(cardCreationDTO.getCardHolderName())) {
            logger.warn("Given Card Holder Name is null or blank");
            return errorUtils.isNull("Card Holder Name");
        }

        if (StringUtils.isNullOrBlank(cardCreationDTO.getCardNumber())) {
            logger.warn("Given Card Number is null or blank");
            return errorUtils.isNull("Card Number");
        }

        if (StringUtils.isNullOrBlank(cardCreationDTO.getExpirationYear())) {
            logger.warn("Given Card Expiration Year is null or blank");
            return errorUtils.isNull("Card Expiration Year");
        }
        if (StringUtils.isNullOrBlank(cardCreationDTO.getExpirationMonth())) {
            logger.warn("Given Card Expiration Month is null or blank");
            return errorUtils.isNull("Card Expiration Month");
        }


        if (StringUtils.isNullOrBlank(cardCreationDTO.getCvc())) {
            logger.warn("Given Card CVC is null or blank");
            return errorUtils.isNull("Card CVC");
        }

        // Validation Check
        if (cardCreationDTO.getCustomerId() <= 0) {
            logger.warn("Given Customer Id is invalid: {}", cardCreationDTO.getCustomerId());
            return errorUtils.isInvalidFormat("Customer Id");
        }

        String requestId = UUID.randomUUID().toString();

        logger.info("Start Card Addition (requestId={})", requestId);

        String paymentService = "http://payment-service/cards/newcard";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Request-Id", requestId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Send Request to Security Service for Checking Existing Session
        HttpEntity<CardCreationDTO> paymentServiceRequest = new HttpEntity<>(cardCreationDTO, headers);
        ResponseEntity<MessageDTO> paymentServiceResponse = restTemplate.exchange(paymentService, HttpMethod.POST, paymentServiceRequest, MessageDTO.class);

        // OK
        if (paymentServiceResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("End Card Addition Successfully (requestId={})", requestId);
        }

        // Bad Request
        if (paymentServiceResponse.getStatusCode().is4xxClientError()) {
            logger.warn("End Card Addition Failed (requestId={})", requestId);
            return errorUtils.customError(paymentServiceResponse, paymentServiceResponse.getBody().getMessage());
        }

        // Internal Server Error
        if (paymentServiceResponse.getStatusCode().is5xxServerError()) {
            logger.warn("End Card Addition Failed (requestId={})", requestId);
            return errorUtils.customError(paymentServiceResponse, paymentServiceResponse.getBody().getMessage());
        }

        logger.info("End Card Addition Successfully (requestId={})", requestId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageDTO("Success"));
    }

    @PostMapping("/customer/card/delete")
    public ResponseEntity<MessageDTO> customerProfileDeleteCard(@RequestBody CardDeletionDTO cardDeletionDTO) {
        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.MessageDTO);

        // DTO Existence Check
        if (cardDeletionDTO == null) {
            logger.warn("Given Card Deletion DTO is null");
            return errorUtils.criticalError();
        }


        // Validation Check
        if (cardDeletionDTO.getCustomerId() <= 0) {
            logger.warn("Given Customer Id is invalid: {}", cardDeletionDTO.getCustomerId());
            return errorUtils.isInvalidFormat("Customer Id");
        }

        if (cardDeletionDTO.getCardId() <= 0) {
            logger.warn("Given Card Id is invalid: {}", cardDeletionDTO.getCardId());
            return errorUtils.isInvalidFormat("Card Id");
        }

        if (!profileService.isCustomerExists(cardDeletionDTO.getCustomerId())) {
            logger.warn("Customer not found with given CustomerId: {}", cardDeletionDTO.getCustomerId());
            return errorUtils.notFound("Customer");
        }
        String requestId = UUID.randomUUID().toString();

        logger.info("Start Card Deletion (requestId={})", requestId);

        String paymentService = "http://payment-service/cards/deactivate";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Request-Id", requestId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Send Request to Security Service for Checking Existing Session
        HttpEntity<CardDeletionDTO> paymentServiceRequest = new HttpEntity<>(cardDeletionDTO, headers);
        ResponseEntity<MessageDTO> paymentServiceResponse = restTemplate.exchange(paymentService, HttpMethod.POST, paymentServiceRequest, MessageDTO.class);

        // OK
        if (paymentServiceResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("End Card Deletion Successfully (requestId={})", requestId);
        }

        // Bad Request
        if (paymentServiceResponse.getStatusCode().is4xxClientError()) {
            logger.warn("End Card Deletion Failed (requestId={})", requestId);
            return errorUtils.customError(paymentServiceResponse, paymentServiceResponse.getBody().getMessage());
        }

        // Internal Server Error
        if (paymentServiceResponse.getStatusCode().is5xxServerError()) {
            logger.warn("End Card Deletion Failed (requestId={})", requestId);
            return errorUtils.customError(paymentServiceResponse, paymentServiceResponse.getBody().getMessage());
        }

        logger.info("End Card Deletion Successfully (requestId={})", requestId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageDTO("Success"));
    }

    @PostMapping("/customer/get")
    public ResponseEntity<CustomerProfileDTO> sendCustomerProfile(@RequestBody CustomerIdDTO customerIdDTO) {
        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.CustomerProfileDTO);
        if (customerIdDTO == null) {
            logger.warn("Given CustomerIdDTO is null");
            return errorUtils.criticalError();
        }
        if (customerIdDTO.getCustomerId() <= 0) {
            logger.warn("Given Customer Id is invalid: {}", customerIdDTO.getCustomerId());
            return errorUtils.isInvalidFormat("Customer Id");
        }
        if (!profileService.isCustomerExists(customerIdDTO.getCustomerId())) {
            logger.warn("Customer not found with given CustomerId: {}", customerIdDTO.getCustomerId());
            return errorUtils.notFound("Customer");
        }
        Customer customer = profileService.getCustomerById(customerIdDTO.getCustomerId());
        CustomerProfileDTO customerProfileDTO = new CustomerProfileDTO(
                customer.getName(),
                customer.getSurname(),
                customer.getGender().toString(),
                customer.getEmail()
        );
        logger.info("Successfully retrieved profile for CustomerId: {}", customerIdDTO.getCustomerId());
        return ResponseEntity.status(HttpStatus.OK).body(customerProfileDTO);
    }

    @PostMapping("/company/get")
    public ResponseEntity<CompanyProfileDTO> sendCompanyProfile(@RequestBody CompanyIdDTO companyIdDTO) {
        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.MessageDTO);
        if (companyIdDTO == null) {
            logger.warn("Given CompanyIdDTO is null");
            return errorUtils.criticalError();
        }
        if (companyIdDTO.getCompanyId() <= 0) {
            logger.warn("Given Company Id is invalid: {}", companyIdDTO.getCompanyId());
            return errorUtils.isInvalidFormat("Company Id");
        }
        if (!profileService.isCompanyExists(companyIdDTO.getCompanyId())) {
            logger.warn("Company not found with given CompanyId: {}", companyIdDTO.getCompanyId());
            return errorUtils.notFound("Company");
        }
        Company company = profileService.getCompanyById(companyIdDTO.getCompanyId());
        CompanyProfileDTO companyProfileDTO = new CompanyProfileDTO(
                company.getName(),
                company.getEmail()
        );
        logger.info("Successfully retrieved profile for CompanyId: {}", companyIdDTO.getCompanyId());
        return ResponseEntity.status(HttpStatus.OK).body(companyProfileDTO);
    }

    @PostMapping("/admin/get")
    public ResponseEntity<AdminProfileDTO> sendAdminProfile(@RequestBody AdminIdDTO adminIdDTO) {
        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.MessageDTO);
        if (adminIdDTO == null) {
            logger.warn("Given AdminIdDTO is null");
            return errorUtils.criticalError();
        }
        if (adminIdDTO.getAdminId() <= 0) {
            logger.warn("Given Admin Id is invalid: {}", adminIdDTO.getAdminId());
            return errorUtils.isInvalidFormat("Admin Id");
        }
        if (!profileService.isCustomerExists(adminIdDTO.getAdminId())) {
            logger.warn("Admin not found with given AdminId: {}", adminIdDTO.getAdminId());
            return errorUtils.notFound("Admin");
        }
        Admin admin = profileService.getAdminById(adminIdDTO.getAdminId());
        AdminProfileDTO responseDTO = new AdminProfileDTO(
                admin.getName(),
                admin.getSurname(),
                admin.getEmail()
        );
        logger.info("Successfully retrieved profile for AdminId: {}", adminIdDTO.getAdminId());
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

}

