package com.demo.variety_store_mono.seller.controller.api;

import com.demo.variety_store_mono.common.entity.UploadFile;
import com.demo.variety_store_mono.seller.dto.response.UploadFileResponse;
import com.demo.variety_store_mono.utility.FileStore;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/products/files")
public class ProductFileRestController {

    private final FileStore fileStore;
    private final ModelMapper modelMapper;

    public ProductFileRestController(@Qualifier("localFileStore") FileStore fileStore,
                                     ModelMapper modelMapper) {
        this.fileStore = fileStore;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/upload")
    public ResponseEntity<UploadFileResponse> upload(@RequestParam("file") MultipartFile file) throws IOException {
        UploadFile uploadFile = fileStore.storeFile(file);
        return ResponseEntity.ok(modelMapper.map(uploadFile, UploadFileResponse.class));
    }
}
