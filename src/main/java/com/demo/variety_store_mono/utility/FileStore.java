package com.demo.variety_store_mono.utility;

import com.demo.variety_store_mono.common.entity.UploadFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
public interface FileStore {
    UploadFile storeFile(MultipartFile file) throws IOException;
    List<UploadFile> storeFiles(List<MultipartFile> files) throws IOException;
    String getFullPath(String storeFileName);
}
