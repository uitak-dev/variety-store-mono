package com.demo.variety_store_mono.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalOptionRequest {

    @NotBlank(message = "옵션 템플릿 이름은 반드시 입력해야 합니다.")
    @Size(max = 20, message = "옵션 템플릿 이름은 최대 20자 까지 입력 가능합니다.")
    private String name;

    @Size(min = 1, max = 4, message = "옵션 값은 1~4개 까지 등록 가능합니다.")
    private List<GlobalOptionValueRequest> globalOptionValues = new ArrayList<>();
}
