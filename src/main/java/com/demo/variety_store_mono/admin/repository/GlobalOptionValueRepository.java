package com.demo.variety_store_mono.admin.repository;

import com.demo.variety_store_mono.admin.entity.GlobalOptionValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GlobalOptionValueRepository extends JpaRepository<GlobalOptionValue, Long> {

    Optional<GlobalOptionValue> findByOptionValue(String optionValue);
}
