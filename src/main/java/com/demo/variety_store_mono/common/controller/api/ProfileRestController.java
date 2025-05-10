package com.demo.variety_store_mono.common.controller.api;

import com.demo.variety_store_mono.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileRestController {

    private final UserRepository userRepository;

    @GetMapping("/userName/check")
    public ResponseEntity<Map<String, Boolean>> checkUsername(@RequestParam String userName) {
        boolean isAvailable = !userRepository.existsByUserName(userName);
        Map<String, Boolean> response = Collections.singletonMap("available", isAvailable);
        return ResponseEntity.ok(response);
    }
}
