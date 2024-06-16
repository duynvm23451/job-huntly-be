package com.phenikaa.jobhuntly.specification;

import com.phenikaa.jobhuntly.entity.Job;
import org.springframework.data.jpa.domain.Specification;

public class JobSpecification {

    public static Specification<Job> hasId(String providedId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), providedId);
    }

    public static Specification<Job> containsName(String providedName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get("name")),"%" + providedName.toLowerCase() + "%");
    }

    public static Specification<Job> containsDescription(String providedDescription) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get("name")),"%" + providedDescription.toLowerCase() + "%");
    }


}
