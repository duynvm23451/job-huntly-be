package com.phenikaa.jobhuntly.specification;

import com.phenikaa.jobhuntly.entity.Application;
import com.phenikaa.jobhuntly.entity.Company;
import com.phenikaa.jobhuntly.enums.ApplicationStatus;
import com.phenikaa.jobhuntly.enums.JobLevel;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class ApplicationSpecification {

    public static Specification<Application> byUserId(final Integer userId) {
        return (root, query, cb) -> cb.equal(root.join("user").get("id"), userId);
    }

    public static Specification<Application> byCompanyId(final Integer companyId) {
        return (root, query, cb) -> cb.equal(root.join("job").join("company").get("id"), companyId);
    }

    public static Specification<Application> byJobId(final Integer jobId) {
        return (root, query, cb) -> cb.equal(root.join("job").get("id"), jobId);
    }

    public static Specification<Application> hasStatus(ApplicationStatus status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<Application> containJobTitle(String jobTitle) {
        return (root, query, cb) -> cb.like(root.join("job").get("title"), "%" + jobTitle.toLowerCase() + "%");
    }

    public static Specification<Application> closestToNowAndGreaterThanNow() {
        return (Root<Application> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            LocalDateTime now = LocalDateTime.now();

            Predicate greaterThanNow = cb.greaterThan(root.get("interviewTime"), now);

            query.orderBy(cb.asc(root.get("interviewTime")));

            return greaterThanNow;
        };
    }

    public static Specification<Application> haveCompanyIdAndStatus(Integer companyId,ApplicationStatus status) {
        return (root, query, cb) -> {
            // Join the company entity
            var companyJoin = root.join("job").join("company");

            // Combine the two predicates
            return cb.and(
                    cb.equal(companyJoin.get("id"), companyId), // Filter by companyId
                    cb.equal(root.get("status"), status)       // Filter by status
            );
        };
    }

    public static Specification<Application> haveCompanyAndJobLevel(Company company, JobLevel jobLevel) {
        return (root, query, cb) -> {
            // Join the company entity
            var jobJoin = root.join("job");
            Predicate statusNotInExcludedStatuses = cb.not(root.get("status").in(ApplicationStatus.HIRED, ApplicationStatus.CANCELLED));


            // Combine the two predicates
            return cb.and(
                    cb.equal(jobJoin.get("company"), company), // Filter by companyId
                    cb.equal(jobJoin.get("jobLevel"), jobLevel),
                    statusNotInExcludedStatuses// Filter by status
            );
        };
    }

    public static Specification<Application> statusNotInHIREDAndCANCELLED(Company company) {
        return (root, query, cb) -> {
            // Create a predicate that filters out HIRED and CANCELLED statuses
            Predicate appliableJobs = cb.not(root.get("status").in(ApplicationStatus.HIRED, ApplicationStatus.CANCELLED));

            var jobJoin = root.join("job");
            return cb.and(
                    cb.equal(jobJoin.get("company"), company), // Filter by companyId
                    appliableJobs// Filter by status
            );
        };
    }


}
