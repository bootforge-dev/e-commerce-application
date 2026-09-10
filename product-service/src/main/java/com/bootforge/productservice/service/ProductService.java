package com.bootforge.productservice.service;

import com.bootforge.productservice.dto.CreateProductRequest;
import com.bootforge.productservice.dto.ProductResponse;
import com.bootforge.productservice.dto.UpdateProductRequest;
import com.bootforge.productservice.entity.Product;
import com.bootforge.productservice.exception.ProductNotFoundException;
import com.bootforge.productservice.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse createProduct(@Valid CreateProductRequest request) {
        if (productRepository.existsByName(request.name())) {
            throw new ProductNotFoundException("Product already exists with product name: " + request.name());
        }
        Product product = toEntity(request);
        Product savedProduct = productRepository.save(product);
        return this.toResponse(savedProduct);
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("Product not found with the productID: " + productId)
        );
        return this.toResponse(product);
    }

    public ProductResponse updateProduct(@Valid UpdateProductRequest request, Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("Product not found with the productID: " + productId)
        );

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setQuantity(request.quantity());

        Product savedProduct = productRepository.save(product);

        return this.toResponse(savedProduct);
    }

    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("Product not found with the productID: " + productId)
        );
        productRepository.delete(product);
    }

    private Product toEntity(CreateProductRequest request) {
        return Product.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .quantity(request.quantity())
                .build();
    }

    private ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
    }
}
