package com.shubilet.member_service.controllers;

import com.shubilet.member_service.dataTransferObjects.requests.AdminVerificationDTO;
import com.shubilet.member_service.dataTransferObjects.requests.CompanyVerificationDTO;
import com.shubilet.member_service.dataTransferObjects.responses.MessageDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface VerificationController {
    public ResponseEntity<MessageDTO> verifyCompany(@RequestBody CompanyVerificationDTO companyVerificationDTO);

    public ResponseEntity<MessageDTO> verifyAdmin(@RequestBody AdminVerificationDTO adminVerificationDTO);
}
