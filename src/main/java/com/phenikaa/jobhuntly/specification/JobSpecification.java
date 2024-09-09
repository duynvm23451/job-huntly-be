package com.phenikaa.jobhuntly.specification;

import com.phenikaa.jobhuntly.entity.Application;
import com.phenikaa.jobhuntly.entity.Company;
import com.phenikaa.jobhuntly.entity.Job;
import com.phenikaa.jobhuntly.enums.ApplicationStatus;
import com.phenikaa.jobhuntly.enums.JobLevel;
import com.phenikaa.jobhuntly.enums.JobType;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class JobSpecification {

    public static Specification<Job> containsTitle(String providedTitle) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + providedTitle.toLowerCase() + "%");
    }

    public static Specification<Job> containsCompanyLocation(String providedCompanyLocation) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.join("company").get("location"), "%" + providedCompanyLocation.toLowerCase() + "%");
    }

    public static Specification<Job> hasJobTypes(List<JobType> jobTypes) {
        return (root, query, criteriaBuilder) -> root.get("type").in(jobTypes);
    }

    public static Specification<Job> hasCategories(List<String> categories) {
        return (root, query, criteriaBuilder) -> root.join("categories").join("category").get("name").in(categories);
    }

    public static Specification<Job> hasJobLevels(List<JobLevel> jobLevels) {
        return (root, query, criteriaBuilder) -> root.get("jobLevel").in(jobLevels);
    }

    public static Specification<Job> greaterThanMinSalary(Integer minSalary) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("minSalary"), minSalary);
    }

    public static Specification<Job> lessThanMaxSalary(Integer maxSalary) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("maxSalary"), maxSalary);
    }

    public static Specification<Job> canApplyByCompany(Company company) {
        return (root, query, cb) -> {
            // Predicate to check if the job belongs to the given company
            Predicate jobBelongsToCompany = cb.equal(root.get("company"), company);

            // Subquery to count the number of HIRED applications for the job
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Application> subqueryRoot = subquery.from(Application.class);

            subquery.select(cb.count(subqueryRoot))
                    .where(
                            cb.equal(subqueryRoot.get("job"), root),
                            cb.equal(subqueryRoot.get("status"), ApplicationStatus.HIRED)
                    );

            // Compare the count from the subquery with the numberOfRecruits
            Expression<Long> numberOfRecruits = root.get("numberOfRecruits");
            Predicate countLessThanRecruits = cb.lessThan(subquery, numberOfRecruits);

            // Combine both predicates
            return cb.and(jobBelongsToCompany, countLessThanRecruits);
        };
    }

}
