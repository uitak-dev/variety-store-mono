package com.demo.variety_store_mono.admin.response;

import com.demo.variety_store_mono.admin.request.GlobalOptionValueRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalOptionResponse {

    private Long id;
    private String name;
    private Set<GlobalOptionValueResponse> globalOptionValues = new LinkedHashSet<>();
}
