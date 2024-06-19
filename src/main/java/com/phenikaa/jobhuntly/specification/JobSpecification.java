package com.phenikaa.jobhuntly.specification;

import com.phenikaa.jobhuntly.entity.Job;
import org.springframework.data.jpa.domain.Specification;

public class JobSpecification {

    public static Specification<Job> containsTitle(String providedTitle) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + providedTitle.toLowerCase() + "%");
    }

    public static Specification<Job> containsCompanyLocation(String providedCompanyLocation) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.join("companies").get("location"), "%" + providedCompanyLocation.toLowerCase() + "%");
    }

    public static Specification<Job> hasJobType(Integer jobType) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("type"), jobType);
    }

    public static Specification<Job> hasCategory(String category) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.join("categories").join("category").get("name"), category);
    }

    public static Specification<Job> hasJobLevel(Integer jobLevel) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("jobLevel"), jobLevel);
    }

    public static Specification<Job> greaterThanMinSalary(Integer minSalary) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("minSalary"), minSalary);
    }

    public static Specification<Job> lessThanMaxSalary(Integer maxSalary) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("minSalary"), maxSalary);
    }

}
