package com.phenikaa.jobhuntly.specification.filter;

import com.phenikaa.jobhuntly.entity.Industry;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class CompanyFilter {
    private String name;
    private String location;
    private List<String> industries;
    private Integer minEmployees;
    private Integer maxEmployees;
}
