package com.phenikaa.jobhuntly.specification.filter;

import com.phenikaa.jobhuntly.enums.JobType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JobFilter {
    private String title;
    private String location;
    private int type;
    private String category;
    private int level;
    private int minSalary;
    private int maxSalary;
}
