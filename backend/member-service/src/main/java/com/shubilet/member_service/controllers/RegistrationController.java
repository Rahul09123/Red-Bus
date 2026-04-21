package com.shubilet.member_service.controllers;

import com.shubilet.member_service.dataTransferObjects.requests.AdminRegistrationDTO;
import com.shubilet.member_service.dataTransferObjects.requests.CompanyRegistrationDTO;
import com.shubilet.member_service.dataTransferObjects.requests.CustomerRegistrationDTO;
import com.shubilet.member_service.dataTransferObjects.responses.MessageDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface RegistrationController {

    /**
     * Operation: Register Customer
     * <p>
     * Declares the contract for registering a new customer by accepting a customer
     * registration payload and returning a response indicating either successful creation
     * or a corresponding validation or processing error. Implementations are expected
     * to enforce field validation, uniqueness checks, and domain rules.
     * </p>
     * <p>
     * Uses:
     *
     * <ul>
     * <li>CustomerRegistrationDTO as the input payload</li>
     * <li>MessageDTO as the standardized response wrapper</li>
     * </ul>
     * </p>
     *
     * @param customerRegistrationDTO the payload containing customer registration data
     * @return a ResponseEntity containing a MessageDTO indicating the result of the registration process
     */
    public ResponseEntity<MessageDTO> registerCustomer(@RequestBody CustomerRegistrationDTO customerRegistrationDTO);

    /**
     * Operation: Register Company
     *
     * <p>
     * Defines the contract for registering a new company account by accepting a company
     * registration payload and returning a wrapped response indicating whether the creation
     * succeeded or failed. Implementations are responsible for performing input validation,
     * format checks, and enforcing business constraints such as uniqueness.
     * </p>
     * <p>
     * Uses:
     *
     * <ul>
     * <li>CompanyRegistrationDTO as the input payload</li>
     * <li>MessageDTO as the standardized response wrapper</li>
     * </ul>
     * </p>
     *
     * @param companyRegistrationDTO the payload containing company registration details
     * @return a ResponseEntity containing a MessageDTO describing the outcome of the registration attempt
     */
    public ResponseEntity<MessageDTO> registerCompany(@RequestBody CompanyRegistrationDTO companyRegistrationDTO);

    /**
     * Operation: Create
     *
     * <p>
     * Defines the contract for registering a new administrator account by accepting an
     * administrator registration payload and returning a response that reflects the result
     * of the creation process. Implementations should validate required fields, verify
     * credential format, and enforce business rules such as email uniqueness.
     * </p>
     * <p>
     * Uses:
     *
     * <ul>
     * <li>AdminRegistrationDTO as the input payload</li>
     * <li>MessageDTO as the standardized response wrapper</li>
     * </ul>
     * </p>
     *
     * @param adminRegistrationDTO the payload containing administrator registration details
     * @return a ResponseEntity containing a MessageDTO describing the outcome of the registration attempt
     */
    public ResponseEntity<MessageDTO> registerAdmin(@RequestBody AdminRegistrationDTO adminRegistrationDTO);
}
