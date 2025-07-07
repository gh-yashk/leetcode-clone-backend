package com.example.leetcode.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "problems")
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(unique = true, nullable = false)
    private String slug;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String starterCode; // JSON: { "python": "...", "java": "...", ... }

    @Column(columnDefinition = "TEXT")
    private String executionWrapper; // JSON: { "python": "...", "java": "...", ... }

    @Column(columnDefinition = "TEXT")
    private String languages; // JSON array of { id, name }

    @Column(columnDefinition = "TEXT")
    private String testCases; // JSON array of { input, expectedOutput }

}
