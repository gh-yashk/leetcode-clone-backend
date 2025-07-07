package com.example.leetcode.dto;

import com.example.leetcode.model.Difficulty;

public record ProblemSummaryDTO(Long id, String title, String slug, Difficulty difficulty) {

}
