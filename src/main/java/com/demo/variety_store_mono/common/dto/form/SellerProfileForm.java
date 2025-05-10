package com.demo.variety_store_mono.common.dto.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellerProfileForm {

    private String userName;

    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "올바른 이메일 형식을 입력해주세요.")
    private String email;

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min = 2, max = 50, message = "이름은 최소 2자 이상 최대 50자 이하로 입력해주세요.")
    private String firstName;

    @NotBlank(message = "성을 입력해주세요.")
    @Size(min = 2, max = 50, message = "성은 최소 2자 이상 최대 50자 이하로 입력해주세요.")
    private String lastName;

    @NotBlank(message = "핸드폰 번호는 필수 입력값입니다.")
    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "핸드폰 번호 형식이 올바르지 않습니다. (예: 010-1234-5678)")
    private String phoneNumber;

    @NotBlank(message = "회사명은 필수 입력값입니다.")
    @Size(min = 2, max = 100, message = "회사명은 2자 이상 100자 이하로 입력해주세요.")
    private String companyName;

    @NotBlank(message = "사업자 번호는 필수 입력값입니다.")
    @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{5}$", message = "사업자 번호 형식이 올바르지 않습니다. (예: 123-45-67890)")
    private String businessLicenseNumber;
}
