package com.demo.variety_store_mono.admin.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalOptionRequest {

    private String name;
    private Set<GlobalOptionValueRequest> globalOptionValues = new LinkedHashSet<>();
}
