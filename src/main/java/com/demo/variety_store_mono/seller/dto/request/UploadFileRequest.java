package com.demo.variety_store_mono.seller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadFileRequest {

    private String uploadFileName;
    private String storeFileName;
}
