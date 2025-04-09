package com.demo.variety_store_mono.admin.controller.api;

import com.demo.variety_store_mono.admin.dto.request.ProductStatusUpdateRequest;
import com.demo.variety_store_mono.admin.service.ProductManagementService;
import com.demo.variety_store_mono.seller.entity.Product;
import com.demo.variety_store_mono.seller.entity.ProductStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductManagementRestController {

    private final ProductManagementService productManagementService;

    // 상품의 등록 상태를 업데이트 합니다.
    @PutMapping("/{productId}/status")
    public ResponseEntity<?> updateProductStatus(@PathVariable Long productId,
                                                 @RequestBody ProductStatusUpdateRequest request) {
        try {
            ProductStatus newStatus = ProductStatus.valueOf(request.getStatus());
            Product updatedProduct = productManagementService.updateProductStatus(productId, newStatus);

            Map<String, Object> response = new HashMap<>();
            response.put("status", updatedProduct.getStatus().getDisplayName());

            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            // 상품 상태 변경 조건 위반.
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (IllegalArgumentException e) {
            // 유효하지 않은 상태 코드가 전달된 경우
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid product status provided"));
        } catch (Exception e) {
            // 그 외 예외 처리
            return ResponseEntity.status(500).body(Map.of("message", "Error updating product status"));
        }
    }
}
