package com.example.leetcode.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.leetcode.dto.ProblemDetailDTO;
import com.example.leetcode.dto.ProblemSummaryDTO;
import com.example.leetcode.repository.ProblemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;

    public List<ProblemSummaryDTO> getAllProblems() {
        return problemRepository.findAllProblemSummaries();
    }

    public Optional<ProblemDetailDTO> getProblemBySlug(String slug) {
        return problemRepository.findProblemDetailBySlug(slug);
    }

}
