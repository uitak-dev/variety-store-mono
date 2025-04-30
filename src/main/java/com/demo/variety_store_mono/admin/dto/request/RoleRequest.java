package com.demo.variety_store_mono.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleRequest {

    private Long id;

    @NotBlank(message = "역할 이름은 반드시 입력해야 합니다.")
    @Size(max = 20, message = "역할 이름은 최대 20자 까지 입력 가능합니다.")
    private String name;

    @NotBlank(message = "역할 설명은 반드시 입력해야 합니다.")
    @Size(max = 500, message = "역할 설명은 최대 500자 까지 입력 가능합니다.")
    private String description;
}
