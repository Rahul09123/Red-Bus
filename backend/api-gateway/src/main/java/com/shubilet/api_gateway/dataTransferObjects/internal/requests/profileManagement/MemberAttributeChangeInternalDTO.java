package com.shubilet.api_gateway.dataTransferObjects.internal.requests.profileManagement;

public class MemberAttributeChangeInternalDTO {
    private int memberId;
    private String attribute;

    public MemberAttributeChangeInternalDTO() {

    }

    public MemberAttributeChangeInternalDTO(int memberId, String attribute) {
        this.memberId = memberId;
        this.attribute = attribute;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
}
