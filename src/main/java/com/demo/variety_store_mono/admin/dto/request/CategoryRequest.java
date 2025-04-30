package com.demo.variety_store_mono.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {

    private Long parentId;

    @NotBlank(message = "카테고리 이름은 반드시 입력해야 합니다.")
    @Size(max = 15, message = "카테고리 이름은 최대 15자 까지 입력 가능합니다.")
    private String name;

    private Set<Long> optionIds;
}
