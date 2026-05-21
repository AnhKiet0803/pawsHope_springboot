package group3.paws_hope.service;

import group3.paws_hope.dto.req.ProductReq;
import group3.paws_hope.dto.res.ProductRes;
import group3.paws_hope.entity.Product;
import group3.paws_hope.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<ProductRes> getAll() {
        return productRepository.findAll().stream()
                .map(ProductRes::toJson)
                .toList();
    }

    public List<ProductRes> getActiveProducts() {
        return productRepository.findByIsActiveTrue().stream()
                .map(ProductRes::toJson)
                .toList();
    }

    public ProductRes findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return ProductRes.toJson(product);
    }

    public ProductRes create(ProductReq req) {
        try {
            Product product = new Product();

            product.setProductName(req.getProductName());
            product.setDescription(req.getDescription());
            product.setPrice(req.getPrice());
            product.setStockQuantity(req.getStockQuantity() != null ? req.getStockQuantity() : 0);
            product.setImageUrl(req.getImageUrl());
            product.setIsActive(req.getIsActive() != null ? req.getIsActive() : true);

            return ProductRes.toJson(productRepository.save(product));
        } catch (Exception e) {
            return null;
        }
    }

    public ProductRes update(Long id, ProductReq req) {
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            product.setProductName(req.getProductName());
            product.setDescription(req.getDescription());
            product.setPrice(req.getPrice());
            product.setStockQuantity(req.getStockQuantity() != null ? req.getStockQuantity() : product.getStockQuantity());
            product.setImageUrl(req.getImageUrl());
            product.setIsActive(req.getIsActive() != null ? req.getIsActive() : product.getIsActive());

            return ProductRes.toJson(productRepository.save(product));
        } catch (Exception e) {
            return null;
        }
    }

    public ProductRes updateActive(Long id, Boolean isActive) {
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            product.setIsActive(isActive);

            return ProductRes.toJson(productRepository.save(product));
        } catch (Exception e) {
            return null;
        }
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}