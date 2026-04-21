package com.shubilet.api_gateway.controllers;

import com.shubilet.api_gateway.dataTransferObjects.MessageDTO;
import com.shubilet.api_gateway.dataTransferObjects.external.requests.auth.AdminRegistrationDTO;
import com.shubilet.api_gateway.dataTransferObjects.external.requests.auth.CompanyRegistrationDTO;
import com.shubilet.api_gateway.dataTransferObjects.external.requests.auth.CustomerRegistrationDTO;
import com.shubilet.api_gateway.dataTransferObjects.external.requests.auth.MemberCredentialsDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthController {
    @PostMapping("/register/customer")
    ResponseEntity<MessageDTO> checkSession(HttpSession httpSession);

    @PostMapping("/register/customer")
    ResponseEntity<MessageDTO> registerCustomer(HttpSession httpSession, @RequestBody CustomerRegistrationDTO customerRegistrationDTO);

    @PostMapping("/register/company")
    ResponseEntity<MessageDTO> registerCompany(HttpSession httpSession, @RequestBody CompanyRegistrationDTO companyRegistrationDTO);

    @PostMapping("/register/admin")
    ResponseEntity<MessageDTO> registerAdmin(HttpSession httpSession, @RequestBody AdminRegistrationDTO adminRegistrationDTO);

    @PostMapping("/session/check/admin")
    ResponseEntity<MessageDTO> checkAdminSession(HttpSession httpSession);

    @PostMapping("/session/check/company")
    ResponseEntity<MessageDTO> checkCompanySession(HttpSession httpSession);

    @PostMapping("/session/check/customer")
    ResponseEntity<MessageDTO> checkCustomerSession(HttpSession httpSession);

    @PostMapping("/login")
    ResponseEntity<MessageDTO> login(HttpSession session, @RequestBody MemberCredentialsDTO memberCredentialsDTO);


    @PostMapping("/logout")
    public ResponseEntity<MessageDTO> logout(HttpSession httpSession);
}
