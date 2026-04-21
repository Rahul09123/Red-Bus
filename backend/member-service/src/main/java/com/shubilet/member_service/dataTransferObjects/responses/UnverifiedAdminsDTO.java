package com.shubilet.member_service.dataTransferObjects.responses;

import java.util.LinkedList;
import java.util.List;

public class UnverifiedAdminsDTO {
    private String message;
    private List<UnverifiedAdminDTO> admins;

    public UnverifiedAdminsDTO() {

    }
    public UnverifiedAdminsDTO(String message){
        this.message = message;
        admins = new LinkedList<>();
    }

    public UnverifiedAdminsDTO(String message, List<UnverifiedAdminDTO> admins) {
        this.message = message;
        this.admins = admins;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<UnverifiedAdminDTO> getAdmins() {
        return admins;
    }

    public void setAdmins(List<UnverifiedAdminDTO> admins) {
        this.admins = admins;
    }
}
