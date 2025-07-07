package com.example.leetcode.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Difficulty {

    EASY, MEDIUM, HARD;

    @JsonCreator
    public static Difficulty fromString(String value) {
        return Difficulty.valueOf(value.trim().toUpperCase());
    }

}
