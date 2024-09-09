package com.phenikaa.jobhuntly.specification.filter;

import com.phenikaa.jobhuntly.enums.JobType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class JobFilter {
    private String title;
    private String location;
    private List<String> types;
    private List<String> categories;
    private List<String> levels;
    private Integer minSalary;
    private Integer maxSalary;
}
