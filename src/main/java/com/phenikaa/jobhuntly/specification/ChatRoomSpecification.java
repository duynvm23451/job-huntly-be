package com.phenikaa.jobhuntly.specification;

import com.phenikaa.jobhuntly.entity.ChatRoom;
import org.springframework.data.jpa.domain.Specification;

public class ChatRoomSpecification {
    public static Specification<ChatRoom> containsCompanyName(String companyName) {
        return (root, query, cb) -> {
            query.where(cb.like(root.join("company").get("name"), "%" + companyName.toLowerCase() + "%"));
            query.orderBy(cb.asc(root.get("updatedAt")));
            return query.getRestriction();
        };
    }

    public static Specification<ChatRoom> byUserId(Integer userId) {
        return (root, query, cb) -> cb.equal(root.join("user").get("id"), userId);
    }

    public static Specification<ChatRoom> byCompanyId(Integer companyId) {
        return (root, query, cb) -> cb.equal(root.join("company").get("id"), companyId);
    }

    public static Specification<ChatRoom> containsUserFullName(String userFullName) {
        return (root, query, cb) -> cb.like(root.get("user").get("fullName"), "%" + userFullName.toLowerCase() + "%");
    }
}
