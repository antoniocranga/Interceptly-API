package com.interceptly.api.dto.patch;

import com.interceptly.api.util.enums.IssueStatusEnum;
import lombok.Data;

import java.util.List;

@Data
public class IssuesPatchDto {
    private List<Integer> ids;
    private IssueStatusEnum status;
}
