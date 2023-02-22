package com.interceptly.api.dto.get;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@Builder
public class StatisticsGetDto {
    private List<Map<String, Object>> issues;
    private List<Map<String, Object>> events;
    private List<Map<String, Object>> solvedIssues;
    private List<Map<String, Object>> eventsByBrowser;
    private List<Map<String, Object>> eventsByDeviceType;
    private List<String> browsers;
    private List<String> deviceTypes;

    private List<OverviewGetDto> overview;
}
