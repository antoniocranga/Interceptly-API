package com.interceptly.api.util.enums;

import lombok.Getter;

@Getter
public enum IssueSortEnum {
    FIRST_SEEN("firstSeen"), LAST_SEEN("lastSeen"), EVENTS_COUNT("eventsCount");

    private final String value;

    IssueSortEnum(String value) {
        this.value = value;
    }

    public static IssueSortEnum fromValue(String value) {
        for (IssueSortEnum issueSortEnum : values()) {
            if (issueSortEnum.value.equalsIgnoreCase(value)) {
                return issueSortEnum;
            }
        }
        throw new IllegalArgumentException("");
    }
}
