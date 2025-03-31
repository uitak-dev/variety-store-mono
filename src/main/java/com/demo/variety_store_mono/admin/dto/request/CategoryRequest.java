package com.demo.variety_store_mono.admin.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {

    private Long parentId;
    private String name;
    private Set<Long> optionIds;
}
