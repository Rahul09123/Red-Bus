package com.shubilet.expedition_service.common.enums;

public enum SeatStatusForModel {
        AVAILABLE("Available"),
        RESERVED("Reserved"),
        BLOCKED("Blocked");

        private final String displayName;

        SeatStatusForModel(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
