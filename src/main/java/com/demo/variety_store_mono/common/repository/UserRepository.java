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
    @Query("select u from User u " +
            "left join fetch u.adminDetail " +
            "left join fetch u.customerDetail " +
            "left join fetch u.sellerDetail " +
            "left join fetch u.userRoles ur " +
            "left join fetch ur.role " +
            "where u.id = :userId")
    Optional<User> findByIdWithDetails(@Param("userId") Long userId);
}
