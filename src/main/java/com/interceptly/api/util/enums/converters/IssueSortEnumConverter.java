package com.interceptly.api.util.enums.converters;

import com.interceptly.api.util.enums.IssueSortEnum;

import java.beans.PropertyEditorSupport;

public class IssueSortEnumConverter  extends PropertyEditorSupport {

    public void setAsText(final String text) throws IllegalArgumentException{
        setValue(IssueSortEnum.fromValue(text));
    }
}
