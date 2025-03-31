package com.demo.variety_store_mono.security.repository;

import com.demo.variety_store_mono.admin.dto.response.AdminDetailResponse;
import com.demo.variety_store_mono.security.entity.User;
import com.demo.variety_store_mono.security.repository.custom.CustomUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {

    boolean existsByUserName(String userName);

    Optional<User> findByUserName(String userName);

    /** 사용자 상세 정보 조회. */
    @Query("select u from User u " +
            "left join fetch u.admin " +
            "left join fetch u.customer " +
            "left join fetch u.seller " +
            "left join fetch u.userRoles ur " +
            "left join fetch ur.role " +
            "where u.id = :userId")
    Optional<User> findUserDetailsById(@Param("userId") Long userId);
}
