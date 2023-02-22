package com.interceptly.api.util.enums.converters;

import org.springframework.data.domain.Sort;

import java.beans.PropertyEditorSupport;

public class DirectionEnumConverter extends PropertyEditorSupport {

    public void setAsText(final String text) throws IllegalArgumentException {
        setValue(Sort.Direction.valueOf(text.toUpperCase()));
    }
}
