package com.demo.variety_store_mono.common.repository;

import com.demo.variety_store_mono.common.entity.User;
import com.demo.variety_store_mono.common.repository.custom.CustomUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {

    boolean existsByUserName(String userName);
    Optional<User> findByUserName(String userName);

}
