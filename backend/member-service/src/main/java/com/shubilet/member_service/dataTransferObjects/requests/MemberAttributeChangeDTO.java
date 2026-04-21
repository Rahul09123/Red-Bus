package com.shubilet.member_service.dataTransferObjects.requests;

public class MemberAttributeChangeDTO {
    private int memberId;
    private String attribute;

    public MemberAttributeChangeDTO() {

    }

    public MemberAttributeChangeDTO(int memberId, String attribute) {
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
