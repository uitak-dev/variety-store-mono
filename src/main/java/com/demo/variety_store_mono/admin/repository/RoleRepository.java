package com.demo.variety_store_mono.admin.repository;

import com.demo.variety_store_mono.admin.entity.Role;
import com.demo.variety_store_mono.admin.repository.custom.CustomRoleRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long>, CustomRoleRepository {

    Optional<Role> findByName(String roleName);
}
