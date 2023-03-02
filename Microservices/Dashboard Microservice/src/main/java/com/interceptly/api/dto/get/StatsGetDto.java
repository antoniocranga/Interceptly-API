package com.interceptly.api.dto.get;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatsGetDto {
    private Long eventsCount;
    private Long projectsCount;
    private Long issuesCount;
    private Long usersCount;
}
