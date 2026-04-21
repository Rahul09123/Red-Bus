package com.shubilet.member_service.controllers.Impl;

import com.shubilet.member_service.common.util.ErrorUtils;
import com.shubilet.member_service.common.util.StringUtils;
import com.shubilet.member_service.common.util.ValidationUtils;
import com.shubilet.member_service.controllers.AuthController;
import com.shubilet.member_service.dataTransferObjects.requests.MemberCredentialsDTO;
import com.shubilet.member_service.dataTransferObjects.responses.MemberSessionDTO;
import com.shubilet.member_service.services.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthControllerImpl implements AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthControllerImpl.class);
    private final AuthService authService;
    

    public AuthControllerImpl(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/checkCredentials")
    public ResponseEntity<MemberSessionDTO> check(@RequestBody MemberCredentialsDTO memberCredentialsDTO) {
        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.MemberSessionDTO);

        // DTO Existence Check
        if (memberCredentialsDTO == null) {
            logger.warn("Given MemberCredentialsDTO is null");
            return errorUtils.criticalError();
        }

        // Attributes Null or Blank Check
        if (StringUtils.isNullOrBlank(memberCredentialsDTO.getEmail())) {
            logger.warn("Given email is null or blank");
            return errorUtils.isNull("email");
        }

        if (StringUtils.isNullOrBlank(memberCredentialsDTO.getPassword())) {
            logger.warn("Given password is null or blank");
            return errorUtils.isNull("password");
        }

        // ValidationCheck
        if (!ValidationUtils.isValidEmail(memberCredentialsDTO.getEmail())) {
            logger.warn("Given email is not in valid format. Given email is {}", memberCredentialsDTO.getEmail());
            return errorUtils.isInvalidFormat("email");
        }
        if (!ValidationUtils.isValidPassword(memberCredentialsDTO.getPassword())) {
            logger.warn("Given password is not in valid format. Given password is {}", memberCredentialsDTO.getPassword());
            return errorUtils.isInvalidFormat("password");
        }
        String email = memberCredentialsDTO.getEmail();
        String password = memberCredentialsDTO.getPassword();
        MemberSessionDTO memberSessionDTO = authService.checkMemberCredentials(email, password);
        if (memberSessionDTO == null) {
            logger.warn("Credentials is not match any record in system");
            return errorUtils.notFound("Member with given credentials");
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(memberSessionDTO);
    }
}
