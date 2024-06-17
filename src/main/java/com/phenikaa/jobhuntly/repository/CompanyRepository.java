package com.phenikaa.jobhuntly.repository;

import com.phenikaa.jobhuntly.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
}
