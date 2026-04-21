package com.shubilet.member_service.services;


import com.shubilet.member_service.dataTransferObjects.responses.MemberSessionDTO;

public interface AuthService {
    public MemberSessionDTO checkMemberCredentials(String email, String password);
}