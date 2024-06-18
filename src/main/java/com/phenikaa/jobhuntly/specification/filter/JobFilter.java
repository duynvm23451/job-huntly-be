package com.phenikaa.jobhuntly.specification.filter;

import com.phenikaa.jobhuntly.enums.JobType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JobFilter {
    private String title;
    private String location;
    private Integer type;
    private String category;
    private Integer level;
    private Integer minSalary;
    private Integer maxSalary;
}
