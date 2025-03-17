package com.demo.variety_store_mono.admin.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {

    private Long id;
    private String name;
    private CategoryResponse parent;

    private List<GlobalOptionResponse> globalOptions = new ArrayList<>();
}
