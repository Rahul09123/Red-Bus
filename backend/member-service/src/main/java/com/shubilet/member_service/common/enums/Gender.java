package com.shubilet.member_service.common.enums;

public enum Gender {
    MALE("Male"), FEMALE("Female");
    private String value;

    private Gender(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Gender fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (Gender e : Gender.values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        return null;
    }
    public static boolean isValidGender(String gender) {
        for (Gender e : Gender.values()) {
            if (e.value.equals(gender)) {
                return true;
            }
        }
        return false;
    }
}