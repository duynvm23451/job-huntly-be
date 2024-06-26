package com.phenikaa.jobhuntly.specification;

import com.phenikaa.jobhuntly.entity.Company;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Set;

public class CompanySpecification {
    public static Specification<Company> containsName(String name) {
        return (root, query, cb) -> cb.like(root.get("name"), "%" + name.toUpperCase() + "%");
    }

    public static Specification<Company> containsLocation(String location) {
        return (root, query, cb) -> cb.like(root.get("location"), "%" + location + "%");
    }

    public static Specification<Company> hasIndustries(List<String> industries) {
        return (root, query, cb) -> root.join("industries").get("name").in(industries);
    }

    public static Specification<Company> inRangeEmployees(Integer minEmployees, Integer maxEmployees) {
        return (root, query, cb) -> {
            System.out.println("min: " + minEmployees);
            System.out.println("max: " + maxEmployees);
            Predicate minPredicate = cb.greaterThanOrEqualTo(root.get("employees"), minEmployees);
            if (maxEmployees != null) {
                Predicate maxPredicate = cb.lessThanOrEqualTo(root.get("employees"), maxEmployees);
                return cb.and(minPredicate, maxPredicate);
            }
            return cb.and(minPredicate);
        };
    }
}
