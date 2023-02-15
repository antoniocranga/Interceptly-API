package com.interceptly.api.dto.get;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class OverviewGetDto {
    private String header;
    private Long total;
    private Long thisMonth;
    private Long lastMonth;
    private Double percentage;
    private Boolean isDecreasing;
    private String tooltip;

    public OverviewGetDto(String header, Long total, Long thisMonth, Long lastMonth,String tooltip) {
        this.header = header;
        this.total = total;
        this.thisMonth = thisMonth;
        this.tooltip = tooltip;
        if (lastMonth != null) {
            this.lastMonth = lastMonth;
            this.percentage = (double) (100 * (Math.abs(this.thisMonth - this.lastMonth)) / (this.lastMonth > 0 ? this.lastMonth : 1));
            this.isDecreasing = this.thisMonth < this.lastMonth;
        }
    }
}