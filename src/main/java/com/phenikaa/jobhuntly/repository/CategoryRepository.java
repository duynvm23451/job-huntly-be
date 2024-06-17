package com.phenikaa.jobhuntly.repository;

import com.phenikaa.jobhuntly.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
