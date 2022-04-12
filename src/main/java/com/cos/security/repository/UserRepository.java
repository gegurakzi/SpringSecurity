package com.cos.security.repository;

import com.cos.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    //findBy 이후 작명에 따라 자동적인 문법으로 적용됨
    //select * from user where username = ?1
    // JPA query method라고 함
    User findByUsername(String username);
}
