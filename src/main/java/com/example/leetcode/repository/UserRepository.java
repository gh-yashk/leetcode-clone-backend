package com.example.leetcode.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.leetcode.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
