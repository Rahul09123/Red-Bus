package com.shubilet.member_service.controllers.Impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shubilet.member_service.common.enums.Gender;
import com.shubilet.member_service.models.Admin;
import com.shubilet.member_service.models.Company;
import com.shubilet.member_service.models.Customer;

import com.shubilet.member_service.common.util.ErrorUtils;
import com.shubilet.member_service.common.util.StringUtils;
import com.shubilet.member_service.common.util.ValidationUtils;

import com.shubilet.member_service.dataTransferObjects.requests.AdminRegistrationDTO;
import com.shubilet.member_service.dataTransferObjects.requests.CompanyRegistrationDTO;
import com.shubilet.member_service.dataTransferObjects.requests.CustomerRegistrationDTO;
import com.shubilet.member_service.dataTransferObjects.responses.MessageDTO;

import com.shubilet.member_service.controllers.RegistrationController;
import com.shubilet.member_service.services.RegistrationService;

@RestController
@RequestMapping("/api/register")
public class RegistrationControllerImpl implements RegistrationController {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationControllerImpl.class);
    private final RegistrationService registrationService;

    RegistrationControllerImpl(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    /**
     * Operation: Register Customer
     * <p>
     * Handles the customer registration workflow by validating input fields,
     * enforcing formatting rules, checking business constraints such as email uniqueness,
     * and delegating user creation to the registration service. Returns meaningful
     * error responses when validation fails or when service-level issues occur.
     * </p>
     * Uses:
     *
     * <ul>
     * <li>RegistrationService for user creation and uniqueness checks</li>
     * <li>ValidationUtils for gender, email, and password format validation</li>
     * <li>ErrorUtils for standardized error response construction</li>
     * <li>StringUtils for null or blank checks</li>
     * </ul>
     * </p>
     *
     * @param customerRegistrationDTO the payload containing customer identity and authentication details
     * @return a response entity indicating either successful customer creation or the corresponding validation or system error
     * @author Murat Furkan Sen — https://github.com/MuratFurkanSen
     */
    @PostMapping("/customer")
    public ResponseEntity<MessageDTO> registerCustomer(@RequestBody CustomerRegistrationDTO customerRegistrationDTO) {
        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.MessageDTO);

        // DTO Existence Check
        if (customerRegistrationDTO == null) {
            logger.warn("customerRegistrationDTO is null.");
            return errorUtils.criticalError();
        }

        // Attributes Null or Blank Check
        if (StringUtils.isNullOrBlank(customerRegistrationDTO.getName())) {
            logger.warn("Given name is null or blank");
            return errorUtils.isNull("Name");
        }
        if (StringUtils.isNullOrBlank(customerRegistrationDTO.getSurname())) {
            logger.warn("Given surname is null or blank");
            return errorUtils.isNull("Surname");
        }

        if (StringUtils.isNullOrBlank(customerRegistrationDTO.getGender())) {
            logger.warn("Given gender is null or blank");
            return errorUtils.isNull("Gender");
        }

        if (StringUtils.isNullOrBlank(customerRegistrationDTO.getEmail())) {
            logger.warn("Given email is null or blank");
            return errorUtils.isNull("Email");
        }

        if (StringUtils.isNullOrBlank(customerRegistrationDTO.getPassword())) {
            logger.warn("Given password is null or blank");
            return errorUtils.isNull("Password");
        }

        // Validation Check
        if (!ValidationUtils.isValidGender(customerRegistrationDTO.getGender())) {
            logger.warn("Given gender is not valid. Choices are 'Male|Female'. Given Gender is '{}'.", customerRegistrationDTO.getGender());
            return errorUtils.isInvalidFormat("Gender");
        }

        if (!ValidationUtils.isValidEmail(customerRegistrationDTO.getEmail())) {
            logger.warn("Given email is not in valid format. Given email is {}", customerRegistrationDTO.getEmail());
            return errorUtils.isInvalidFormat("Email");
        }
        if (!ValidationUtils.isValidPassword(customerRegistrationDTO.getPassword())) {
            logger.warn("Given password is not in valid format. Given password is {}", customerRegistrationDTO.getPassword());
            return errorUtils.isInvalidFormat("Password");
        }

        // Uniqueness Check
        if (registrationService.isUserExistsByEmail(customerRegistrationDTO.getEmail())) {
            logger.warn("Email already exists in DB. Given Email is {}", customerRegistrationDTO.getEmail());
            return errorUtils.alreadyExists("User Email");
        }

        Customer customer = new Customer(
                customerRegistrationDTO.getName(),
                customerRegistrationDTO.getSurname(),
                Gender.fromValue(customerRegistrationDTO.getGender()),
                customerRegistrationDTO.getEmail(),
                customerRegistrationDTO.getPassword()
        );

        if (!registrationService.registerCustomer(customer)) {
            logger.warn("A serious problem occurred in service level while creating a customer account with email{}", customerRegistrationDTO.getEmail());
            return errorUtils.criticalError();
        }
        logger.info("Customer registered successfully with email {}", customerRegistrationDTO.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageDTO("Customer Creation Successful."));
    }

    /**
     * Operation: Register Company
     * <p>
     * Registers a company account by validating the incoming DTO, enforcing required field and format rules,
     * checking email uniqueness, constructing a Company domain object, and delegating persistence to the registration service.
     * Returns clear error responses for null/blank payloads, invalid email/password formats, duplicate email, or service-level failures.
     * </p>
     * <p>
     * Uses:
     *
     * <ul>
     * <li>RegistrationService for company creation and uniqueness checks</li>
     * <li>ValidationUtils for email and password format validation</li>
     * <li>ErrorUtils for standardized error response construction</li>
     * <li>StringUtils for null or blank checks</li>
     * <li>Company domain object as the entity to persist</li>
     * <li>Logger for request and error logging</li>
     * </ul>
     * </p>
     *
     * @param companyRegistrationDTO the payload containing company title, email, and password
     * @return a ResponseEntity containing a success message (HTTP 201) on successful registration or an error MessageDTO with an appropriate HTTP status when validation or processing fails
     */
    @PostMapping("/company")
    public ResponseEntity<MessageDTO> registerCompany(@RequestBody CompanyRegistrationDTO companyRegistrationDTO) {
        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.MessageDTO);
        // DTO Existence Check
        if (companyRegistrationDTO == null) {
            logger.warn("companyRegistrationDTO is null.");
            return errorUtils.isNull("Creation Data");
        }

        // Attributes Null or Blank Check
        if (StringUtils.isNullOrBlank(companyRegistrationDTO.getTitle())) {
            logger.warn("Given Title is null or blank");
            return errorUtils.isNull("Title");
        }
        if (StringUtils.isNullOrBlank(companyRegistrationDTO.getEmail())) {
            logger.warn("Given Email is null or blank");
            return errorUtils.isNull("Email");
        }

        if (StringUtils.isNullOrBlank(companyRegistrationDTO.getPassword())) {
            logger.warn("Given Password is null or blank");
            return errorUtils.isNull("Password");
        }

        // Validation Check
        if (!ValidationUtils.isValidEmail(companyRegistrationDTO.getEmail())) {
            logger.warn("Given email is not in valid format. Given email is {}", companyRegistrationDTO.getEmail());
            return errorUtils.isInvalidFormat("Email");
        }
        if (!ValidationUtils.isValidPassword(companyRegistrationDTO.getPassword())) {
            logger.warn("Given password is not in valid format. Given password is {}", companyRegistrationDTO.getPassword());
            return errorUtils.isInvalidFormat("Password");
        }

        // Uniqueness Check
        if (registrationService.isUserExistsByEmail(companyRegistrationDTO.getEmail())) {
            logger.warn("Email already exists in DB. Given Email is {}", companyRegistrationDTO.getEmail());
            return errorUtils.alreadyExists("User Email");
        }

        Company company = new Company(
                companyRegistrationDTO.getTitle(),
                companyRegistrationDTO.getEmail(),
                companyRegistrationDTO.getPassword()
        );

        if (!registrationService.registerCompany(company)) {
            logger.warn("A serious problem occurred in service level while creating a company account with email{}", companyRegistrationDTO.getEmail());
            return errorUtils.criticalError();
        }
        
        logger.info("Customer registered successfully with email {}", companyRegistrationDTO.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageDTO("Company Registration Successful."));
    }

    /**
     * Operation: Register Admin
     * <p>
     * Handles administrator account registration by validating the incoming DTO for required fields and formats,
     * enforcing business constraints such as email uniqueness, constructing an Admin domain object, and delegating persistence
     * to the registration service. Produces detailed and standardized error responses for null/blank payloads, invalid formats,
     * duplicate emails, or service-level failures and writes diagnostic logs for each failure path.
     * </p>
     * <p>
     * Uses:
     *
     * <ul>
     * <li>RegistrationService for admin creation and uniqueness checks</li>
     * <li>ValidationUtils for email and password format validation</li>
     * <li>ErrorUtils for standardized error response construction</li>
     * <li>StringUtils for null or blank checks</li>
     * <li>Admin domain object as the entity to persist</li>
     * <li>Logger for request and error logging</li>
     * </ul>
     * </p>
     *
     * @param adminRegistrationDTO the payload containing administrator name, surname, email, and password
     * @return a ResponseEntity containing a success MessageDTO (HTTP 201) on successful registration or an error MessageDTO with an appropriate HTTP status when validation or processing fails
     */
    @PostMapping("/admin")
    public ResponseEntity<MessageDTO> registerAdmin(@RequestBody AdminRegistrationDTO adminRegistrationDTO) {
        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.MessageDTO);

        // DTO Existence Check
        if (adminRegistrationDTO == null) {
            logger.warn("adminRegistrationDTO is null.");
            return errorUtils.criticalError();
        }

        // Attributes Null or Blank Check
        if (StringUtils.isNullOrBlank(adminRegistrationDTO.getName())) {
            logger.warn("Given name is null or blank");
            return errorUtils.isNull("Name");
        }
        if (StringUtils.isNullOrBlank(adminRegistrationDTO.getSurname())) {
            logger.warn("Given surname is null or blank");
            return errorUtils.isNull("Surname");
        }

        if (StringUtils.isNullOrBlank(adminRegistrationDTO.getEmail())) {
            logger.warn("Given email is null or blank");
            return errorUtils.isNull("Email");
        }

        if (StringUtils.isNullOrBlank(adminRegistrationDTO.getPassword())) {
            logger.warn("Given password is null or blank");
            return errorUtils.isNull("Password");
        }

        // Validation Check
        if (!ValidationUtils.isValidEmail(adminRegistrationDTO.getEmail())) {
            logger.warn("Given email is not in valid format. Given email is {}", adminRegistrationDTO.getEmail());
            return errorUtils.isInvalidFormat("Email");
        }
        if (!ValidationUtils.isValidPassword(adminRegistrationDTO.getPassword())) {
            logger.warn("Given password is not in valid format. Given password is {}", adminRegistrationDTO.getPassword());
            return errorUtils.isInvalidFormat("Password");
        }

        // Uniqueness Check
        if (registrationService.isUserExistsByEmail(adminRegistrationDTO.getEmail())) {
            logger.warn("Email already exists in DB. Given Email is {}", adminRegistrationDTO.getEmail());
            return errorUtils.alreadyExists("User Email");
        }

        Admin admin = new Admin(
                adminRegistrationDTO.getName(),
                adminRegistrationDTO.getSurname(),
                adminRegistrationDTO.getEmail(),
                adminRegistrationDTO.getPassword()
        );

        if (!registrationService.registerAdmin(admin)) {
            logger.warn("A serious problem occurred in service level while creating a company account with email {}", adminRegistrationDTO.getEmail());
            return errorUtils.criticalError();
        }
        logger.info("Admin registered successfully with email {}", adminRegistrationDTO.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageDTO("Admin Registration Successful."));
    }
}

