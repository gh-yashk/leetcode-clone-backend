package com.example.leetcode.dto;

import com.example.leetcode.model.Difficulty;

public record ProblemDetailDTO(Long id, String title, String slug, Difficulty difficulty, String description,
        String starterCode, String languages) {

}
