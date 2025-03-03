package com.demo.variety_store_mono.common.repository;

import com.demo.variety_store_mono.common.entity.User;
import com.demo.variety_store_mono.common.repository.custom.CustomUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {

    boolean existsByUserName(String userName);
    Optional<User> findByUserName(String userName);

    /** 사용자 상세 정보 조회. */
    @Query("SELECT u FROM User u " +
            "LEFT JOIN FETCH u.adminDetail " +
            "LEFT JOIN FETCH u.customerDetail " +
            "LEFT JOIN FETCH u.sellerDetail " +
            "WHERE u.id = :userId")
    Optional<User> findByIdWithDetails(@Param("userId") Long userId);
}
