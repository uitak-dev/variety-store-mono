package com.demo.variety_store_mono.seller.controller.api;

import com.demo.variety_store_mono.common.entity.UploadFile;
import com.demo.variety_store_mono.seller.dto.response.UploadFileResponse;
import com.demo.variety_store_mono.seller.service.ProductService;
import com.demo.variety_store_mono.utility.FileStore;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductFileRestController {

    private final ProductService productService;
    private final FileStore fileStore;
    private final ModelMapper modelMapper;

    public ProductFileRestController(@Qualifier("localFileStore") FileStore fileStore,
                                     ModelMapper modelMapper, ProductService productService) {
        this.productService = productService;
        this.fileStore = fileStore;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/images")
    public ResponseEntity<List<UploadFileResponse>> uploadImages(@RequestParam("files") List<MultipartFile> files) throws IOException {
        List<UploadFile> uploadFiles = fileStore.storeFiles(files);
        return ResponseEntity.ok(uploadFiles.stream().map(uploadFile ->
                modelMapper.map(uploadFile, UploadFileResponse.class))
                .toList());
    }

    @DeleteMapping("/{productId}/images/{storeFileName}")
    public ResponseEntity<Void> deleteImages(@PathVariable Long productId, @PathVariable String storeFileName) {
         productService.deleteProductImage(productId, storeFileName);
        return ResponseEntity.noContent().build();
    }
}
