package com.shubilet.member_service.controllers.Impl;


import com.shubilet.member_service.common.util.ErrorUtils;
import com.shubilet.member_service.dataTransferObjects.requests.CustomerIdDTO;
import com.shubilet.member_service.dataTransferObjects.requests.resourceDTOs.CompanyIdDTO;
import com.shubilet.member_service.dataTransferObjects.responses.CompanyIdNameMapDTO;
import com.shubilet.member_service.dataTransferObjects.responses.CustomerIdNameMapDTO;
import com.shubilet.member_service.services.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/get")
public class ResourceControllerImpl {
    private static final Logger logger = LoggerFactory.getLogger(ResourceControllerImpl.class);
    private final ResourceService resourceService;

    public ResourceControllerImpl(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @PostMapping("/company/name")
    public ResponseEntity<CompanyIdNameMapDTO> sendCompanyNames(@RequestBody List<CompanyIdDTO> companyIDsDTO) {
        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.CompanyIdNameMapDTO);
        // DTO Existence Check
        if (companyIDsDTO == null) {
            logger.warn("Company IDs DTO is null");
            return errorUtils.criticalError();
        }

        // Attributes Null or Blank Check
        if (companyIDsDTO.isEmpty()) {
            logger.info("Company IDs list is empty");
            return errorUtils.isNull("Company IDs List");
        }
        HashMap<Integer, String> companyNamesMap = resourceService.sendCompanyNames(companyIDsDTO);

        if (companyNamesMap == null || companyNamesMap.isEmpty()) {
            logger.warn("No Matching Company has been Found for the given IDs");
            return errorUtils.notFound("Company Names for given IDs");
        }

        logger.info("Company Names have been successfully retrieved for the given IDs");
        return ResponseEntity.ok(new CompanyIdNameMapDTO(companyNamesMap, "Successfully Retrieved Company Names"));
    }

    @PostMapping("/customer/name")
    public ResponseEntity<CustomerIdNameMapDTO> sendCustomerNames(@RequestBody List<CustomerIdDTO> customerIDsDTO) {
        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.CustomerIdNameMapDTO);
        // DTO Existence Check
        if (customerIDsDTO == null) {
            logger.warn("Customer IDs DTO is null");
            return errorUtils.criticalError();
        }

        // Attributes Null or Blank Check
        if (customerIDsDTO.isEmpty()) {
            logger.info("Customer IDs list is empty");
            return errorUtils.isNull("Customer IDs List");
        }
        HashMap<Integer, String> customerNamesMap = resourceService.sendCustomerNames(customerIDsDTO);


        if (customerNamesMap == null || customerNamesMap.isEmpty()) {
            customerNamesMap = new HashMap<>();
        }

        logger.info("Customer Names have been successfully retrieved for the given IDs");
        return ResponseEntity.ok(new CustomerIdNameMapDTO(customerNamesMap, "Successfully Retrieved Customer Names"));

    }
}
