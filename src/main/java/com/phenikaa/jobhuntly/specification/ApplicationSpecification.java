package com.phenikaa.jobhuntly.specification;

import com.phenikaa.jobhuntly.entity.Application;
import com.phenikaa.jobhuntly.enums.ApplicationStatus;
import org.springframework.data.jpa.domain.Specification;

public class ApplicationSpecification {

    public static Specification<Application> byUserId(final Integer userId) {
        return (root, query, cb) -> cb.equal(root.join("user").get("id"), userId);
    }

    public static Specification<Application> hasStatus(ApplicationStatus status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<Application> containJobTitle(String jobTitle) {
        return (root, query, cb) -> cb.like(root.join("job").get("title"), "%" + jobTitle.toLowerCase() + "%");
    }
}
