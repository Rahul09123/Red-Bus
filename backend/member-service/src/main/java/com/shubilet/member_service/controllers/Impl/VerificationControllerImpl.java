package com.shubilet.member_service.controllers.Impl;

import com.shubilet.member_service.common.util.ErrorUtils;
import com.shubilet.member_service.controllers.VerificationController;
import com.shubilet.member_service.dataTransferObjects.requests.AdminVerificationDTO;
import com.shubilet.member_service.dataTransferObjects.requests.CompanyVerificationDTO;
import com.shubilet.member_service.dataTransferObjects.requests.resourceDTOs.AdminIdDTO;
import com.shubilet.member_service.dataTransferObjects.responses.MessageDTO;
import com.shubilet.member_service.dataTransferObjects.responses.UnverifiedAdminsDTO;
import com.shubilet.member_service.dataTransferObjects.responses.UnverifiedCompaniesDTO;
import com.shubilet.member_service.services.VerificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/verification")
public class VerificationControllerImpl implements VerificationController {
    private final Logger logger = LoggerFactory.getLogger(VerificationControllerImpl.class);
    private final VerificationService verificationService;

    public VerificationControllerImpl(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @PostMapping("/verify/company")
    public ResponseEntity<MessageDTO> verifyCompany(@RequestBody CompanyVerificationDTO companyVerificationDTO) {

        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.MessageDTO);
        // DTO Existence Check
        if (companyVerificationDTO == null) {
            logger.warn("Company Verification DTO is null");
            return errorUtils.criticalError();
        }

        // Validation Check
        if (companyVerificationDTO.getAdminId() < 0) {
            logger.warn("Invalid Admin ID: {}", companyVerificationDTO.getAdminId());
            return errorUtils.isInvalidFormat("Admin ID");
        }

        if (companyVerificationDTO.getCandidateCompanyId() < 0) {
            return errorUtils.isInvalidFormat("Candidate Company ID");
        }

        if (!verificationService.isAdminExists(companyVerificationDTO.getAdminId())) {
            return errorUtils.notFound("Admin with Given ID does not Exist");
        }

        if (!verificationService.isCompanyExists(companyVerificationDTO.getCandidateCompanyId())) {
            return errorUtils.notFound("Candidate Company with Given ID does not Exist");
        }

        if (!verificationService.hasClearance(companyVerificationDTO.getAdminId())) {
            logger.warn("Admin with ID {} does not have the Necessary Clearance", companyVerificationDTO.getAdminId());
            return errorUtils.unauthorized("Admin does not have the Necessary Clearance");
        }

        if (!verificationService.markCompanyVerified(companyVerificationDTO.getAdminId(), companyVerificationDTO.getCandidateCompanyId())){
            logger.error("Critical Error while marking Company ID {} as verified by Admin ID {}", companyVerificationDTO.getCandidateCompanyId(), companyVerificationDTO.getAdminId());
            return errorUtils.criticalError();
        }

        logger.info("Admin ID {} successfully marked Candidate Company ID {} as verified", companyVerificationDTO.getAdminId(), companyVerificationDTO.getCandidateCompanyId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageDTO("Candidate Company Marked as Verified Successfully"));
    }

    @PostMapping("/verify/admin")
    public ResponseEntity<MessageDTO> verifyAdmin(@RequestBody AdminVerificationDTO adminVerificationDTO)   {
        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.MessageDTO);

        // DTO Existence Check
        if (adminVerificationDTO == null) {
            logger.warn("Admin Verification DTO is null");
            return errorUtils.criticalError();
        }

        logger.info("Admin Verification Request Received. IDs: {} {}", adminVerificationDTO.getAdminId(), adminVerificationDTO.getCandidateAdminId());
        // Validation Check
        if (adminVerificationDTO.getAdminId() < 0) {
            logger.warn("Invalid Admin ID: {}", adminVerificationDTO.getAdminId());
            return errorUtils.isInvalidFormat("Admin ID");
        }

        if (adminVerificationDTO.getCandidateAdminId() < 0) {
            logger.warn("Invalid Candidate Admin ID: {}", adminVerificationDTO.getCandidateAdminId());
            return errorUtils.isInvalidFormat("Candidate Admin ID");
        }

        if (!verificationService.isAdminExists(adminVerificationDTO.getAdminId())) {
            logger.warn("Admin with ID {} does not exist", adminVerificationDTO.getAdminId());
            return errorUtils.notFound("Admin with Given ID does not Exist");
        }

        if (!verificationService.isAdminExists(adminVerificationDTO.getCandidateAdminId())) {
            logger.warn("Candidate Admin with ID {} does not exist", adminVerificationDTO.getCandidateAdminId());
            return errorUtils.notFound("Candidate Admin with Given ID does not Exist");
        }

        if (!verificationService.hasClearance(adminVerificationDTO.getAdminId())) {
            logger.warn("Admin with ID {} does not have the Necessary Clearance", adminVerificationDTO.getAdminId());
            return errorUtils.unauthorized("Admin does not have the Necessary Clearance");
        }
        if (!verificationService.markAdminVerified(adminVerificationDTO.getAdminId(), adminVerificationDTO.getCandidateAdminId())){
            logger.error("Critical Error while marking Admin ID {} as verified by Admin ID {}", adminVerificationDTO.getCandidateAdminId(), adminVerificationDTO.getAdminId());
            return errorUtils.criticalError();
        }

        logger.info("Admin ID {} successfully marked Candidate Admin ID {} as verified", adminVerificationDTO.getAdminId(), adminVerificationDTO.getCandidateAdminId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageDTO("Candidate Admin Marked as Verified Successfully"));
    }

    @PostMapping("/get/unverified/admins")
    public ResponseEntity<UnverifiedAdminsDTO> sendUnverifiedAdmins(@RequestBody AdminIdDTO adminIdDTO) {
        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.UnVerifiedAdminsDTO);

        // DTO Existence Check
        if (adminIdDTO == null) {
            logger.warn("Admin ID DTO is null");
            return errorUtils.criticalError();
        }

        // Validation Check
        if (adminIdDTO.getAdminId() < 0) {
            logger.warn("Invalid Admin ID: {}", adminIdDTO.getAdminId());
            return errorUtils.isInvalidFormat("Admin ID");
        }

        logger.info("Admin Verification Request Received. ID: {}", adminIdDTO.getAdminId());
        if (!verificationService.isAdminExists(adminIdDTO.getAdminId())) {
            return errorUtils.notFound("Admin with Given ID does not Exist");
        }

        if (!verificationService.hasClearance(adminIdDTO.getAdminId())) {
            return errorUtils.unauthorized("Admin does not have the Necessary Clearance");
        }

        UnverifiedAdminsDTO unverifiedAdminDTO = verificationService.getUnverifiedAdmins();
        logger.info("Unverified Admins sent to Admin ID {}", adminIdDTO.getAdminId());
        return ResponseEntity.status(HttpStatus.OK).body(unverifiedAdminDTO);
    }

    @PostMapping("/get/unverified/companies")
    public ResponseEntity<UnverifiedCompaniesDTO> sendUnverifiedCompanies(@RequestBody AdminIdDTO adminIdDTO) {
        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.UnVerifiedCompaniesDTO);

        // DTO Existence Check
        if (adminIdDTO == null) {
            logger.warn("Admin ID DTO is null");
            return errorUtils.criticalError();
        }

        // Validation Check
        if (adminIdDTO.getAdminId() < 0) {
            logger.warn("Invalid Admin ID: {}", adminIdDTO.getAdminId());
            return errorUtils.isInvalidFormat("Admin ID");
        }

        logger.info("Admin Verification Request Received. ID: {}", adminIdDTO.getAdminId());
        if (!verificationService.isAdminExists(adminIdDTO.getAdminId())) {
            return errorUtils.notFound("Admin with Given ID does not Exist");
        }

        if (!verificationService.hasClearance(adminIdDTO.getAdminId())) {
            return errorUtils.unauthorized("Admin does not have the Necessary Clearance");
        }

        UnverifiedCompaniesDTO unverifiedCompaniesDTO = verificationService.getUnverifiedCompanies();
        logger.info("Unverified Companies sent to Admin ID {}", adminIdDTO.getAdminId());
        return ResponseEntity.status(HttpStatus.OK).body(unverifiedCompaniesDTO);
    }
}
