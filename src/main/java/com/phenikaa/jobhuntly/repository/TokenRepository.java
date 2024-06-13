package com.phenikaa.jobhuntly.repository;

import com.phenikaa.jobhuntly.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {
}
