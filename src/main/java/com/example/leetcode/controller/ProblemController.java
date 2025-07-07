package com.example.leetcode.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.leetcode.dto.ProblemDetailDTO;
import com.example.leetcode.dto.ProblemSummaryDTO;
import com.example.leetcode.service.ProblemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/problems")
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;

    @GetMapping
    public ResponseEntity<List<ProblemSummaryDTO>> getAllProblems() {
        List<ProblemSummaryDTO> problems = problemService.getAllProblems();
        return ResponseEntity.ok(problems);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ProblemDetailDTO> getProblemBySlug(@PathVariable String slug) {
        ProblemDetailDTO problem = problemService.getProblemBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Problem not found"));
        return ResponseEntity.ok(problem);
    }

}
