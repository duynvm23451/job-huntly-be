package com.phenikaa.jobhuntly.repository;

import com.phenikaa.jobhuntly.entity.Industry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndustryRepository extends JpaRepository<Industry, Industry> {
}
