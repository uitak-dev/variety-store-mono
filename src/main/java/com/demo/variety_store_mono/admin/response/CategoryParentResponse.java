package com.demo.variety_store_mono.admin.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryParentResponse {
    private Long id;
    private String name;
}
