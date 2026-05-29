package group3.paws_hope.controller;

import group3.paws_hope.common.ResponseHandler;
import group3.paws_hope.dto.common.ResponseDTO;
import group3.paws_hope.dto.req.ProductReq;
import group3.paws_hope.dto.res.ProductRes;
import group3.paws_hope.enums.StatusCode;
import group3.paws_hope.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<ProductRes>>> getAll() {
        return ResponseHandler.success(productService.getAll(), "Success");
    }

    @GetMapping("/active")
    public ResponseEntity<ResponseDTO<List<ProductRes>>> getActiveProducts() {
        return ResponseHandler.success(productService.getActiveProducts(), "Success");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<ProductRes>> findById(@PathVariable Long id) {
        try {
            return ResponseHandler.success(productService.findById(id), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<ProductRes>> create(@Valid @RequestBody ProductReq req) {
        ProductRes res = productService.create(req);
        if (res != null) {
            return ResponseHandler.success(res, "Product created successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Create product failed");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<ProductRes>> update(@PathVariable Long id, @Valid @RequestBody ProductReq req) {
        ProductRes res = productService.update(id, req);
        if (res != null) {
            return ResponseHandler.success(res, "Product updated successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Update product failed");
    }

    @PatchMapping("/{id}/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<ProductRes>> updateActive(@PathVariable Long id, @RequestParam Boolean isActive) {
        ProductRes res = productService.updateActive(id, isActive);
        if (res != null) {
            return ResponseHandler.success(res, "Product active status updated.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Update active status failed");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseHandler.success("Product deleted successfully.", "Success");
    }
}