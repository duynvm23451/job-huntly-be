package com.phenikaa.jobhuntly.specification;

import com.phenikaa.jobhuntly.entity.Job;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class JobSpecification {

    public static Specification<Job> containsTitle(String providedTitle) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + providedTitle.toLowerCase() + "%");
    }

    public static Specification<Job> containsCompanyLocation(String providedCompanyLocation) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.join("companies").get("location"), "%" + providedCompanyLocation.toLowerCase() + "%");
    }

    public static Specification<Job> hasJobTypes(List<Integer> jobTypes) {
        return (root, query, criteriaBuilder) -> root.get("jobTypes").in(jobTypes);
    }

    public static Specification<Job> hasCategories(List<String> categories) {
        return (root, query, criteriaBuilder) -> root.join("categories").join("category").get("name").in(categories);
    }

    public static Specification<Job> hasJobLevels(List<Integer> jobLevels) {
        return (root, query, criteriaBuilder) -> root.get("jobLevel").in(jobLevels);
    }

    public static Specification<Job> greaterThanMinSalary(Integer minSalary) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("minSalary"), minSalary);
    }

    public static Specification<Job> lessThanMaxSalary(Integer maxSalary) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("minSalary"), maxSalary);
    }

}
