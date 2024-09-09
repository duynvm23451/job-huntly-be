package com.phenikaa.jobhuntly.repository;

import com.phenikaa.jobhuntly.entity.Company;
import com.phenikaa.jobhuntly.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByEmail(String email);
    List<User> findUsersByCompany(Company company);
}
