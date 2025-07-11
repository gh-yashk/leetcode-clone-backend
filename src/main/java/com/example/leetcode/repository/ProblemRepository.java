package com.example.leetcode.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.leetcode.dto.ProblemDetailDTO;
import com.example.leetcode.dto.ProblemSummaryDTO;
import com.example.leetcode.model.Problem;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

    @Query("SELECT new com.example.leetcode.dto.ProblemSummaryDTO(p.id, p.title, p.slug, p.difficulty) FROM Problem p")
    List<ProblemSummaryDTO> findAllProblemSummaries();

    @Query("SELECT new com.example.leetcode.dto.ProblemDetailDTO(p.id, p.title, p.slug, p.difficulty, p.description, p.starterCode, p.languages) FROM Problem p WHERE p.slug = :slug")
    Optional<ProblemDetailDTO> findProblemDetailBySlug(String slug);

}
