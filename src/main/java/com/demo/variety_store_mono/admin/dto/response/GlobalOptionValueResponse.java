package com.demo.variety_store_mono.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalOptionValueResponse {

    private Long id;
    private String optionValue;
}
