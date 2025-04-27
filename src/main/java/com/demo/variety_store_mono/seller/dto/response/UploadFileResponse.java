package com.demo.variety_store_mono.seller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadFileResponse {

    private String uploadFileName;
    private String storeFileName;
}
