package com.demo.variety_store_mono.common.controller.api;

import com.demo.variety_store_mono.utility.FileStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

@RestController
@RequestMapping("/api/images")
public class ImageRestController {

    private final FileStore fileStore;

    public ImageRestController(@Qualifier("localFileStore") FileStore fileStore) {
        this.fileStore = fileStore;
    }

    @GetMapping("/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }
}
