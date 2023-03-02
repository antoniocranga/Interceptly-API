package com.interceptly.api.util.enums.converters;

import com.interceptly.api.util.enums.IssueStatusEnum;

import java.beans.PropertyEditorSupport;

public class IssueStatusEnumConverter extends PropertyEditorSupport {
    public void setAsText(final String text) throws IllegalArgumentException {
        setValue(IssueStatusEnum.valueOf(text.toUpperCase()));
    }
}
