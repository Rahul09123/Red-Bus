package com.shubilet.api_gateway.dataTransferObjects.external.requests.profileManagement;

public class MemberAttributeChangeExternalDTO {
    private String attribute;

    public MemberAttributeChangeExternalDTO() {

    }

    public MemberAttributeChangeExternalDTO(String attribute) {
        this.attribute = attribute;
    }


    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
}
