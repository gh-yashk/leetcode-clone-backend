package com.example.leetcode.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    private Long githubId; // id

    private String username; // login
    private String name; // name
    private String email; // email
    private String avatarUrl; // avatar_url

}
