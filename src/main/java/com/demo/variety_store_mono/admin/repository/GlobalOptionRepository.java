package com.demo.variety_store_mono.admin.repository;

import com.demo.variety_store_mono.admin.entity.GlobalOption;
import com.demo.variety_store_mono.admin.repository.custom.CustomOptionRepository;
import org.modelmapper.internal.bytebuddy.agent.builder.AgentBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GlobalOptionRepository extends JpaRepository<GlobalOption, Long>, CustomOptionRepository {

    @Query("select go from GlobalOption go join fetch go.globalOptionValues where go.name = :optionName")
    Optional<GlobalOption> findByName(String optionName);

    @Query("select go from GlobalOption go join fetch go.globalOptionValues where go.id = :id")
    Optional<GlobalOption> findByIdWithValues(@Param("id") Long id);
}
