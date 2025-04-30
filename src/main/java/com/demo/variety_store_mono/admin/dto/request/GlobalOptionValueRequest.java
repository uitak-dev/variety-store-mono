package com.demo.variety_store_mono.admin.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalOptionValueRequest {

    @NotNull(message = "옵션 템플릿 값은 필수 입력 값입니다.")
    @NotEmpty(message = "옵션 템플릿 값은 반드시 입력해야 합니다.")
    @Size(max = 20, message = "옵션 템플릿 값은 최대 20자 까지 입력 가능합니다.")
    private String optionValue;
}
