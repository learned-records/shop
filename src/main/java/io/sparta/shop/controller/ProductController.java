package io.sparta.shop.controller;

import io.sparta.shop.dto.ProductMypriceRequestDto;
import io.sparta.shop.dto.ProductRequestDto;
import io.sparta.shop.dto.ProductResponseDto;
import io.sparta.shop.security.UserDetailsImpl;
import io.sparta.shop.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {
    private final String PRODUCTS_BASE_URI = "/products";
    private final String SPECIFIC_PRODUCTS_URI_FORMAT = PRODUCTS_BASE_URI + "/{id}";
    private final ProductService productService;

    @PostMapping(PRODUCTS_BASE_URI)
    public ProductResponseDto createProduct(
        @RequestBody ProductRequestDto productRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return productService.createProduct(productRequestDto, userDetails.getUser());
    }

    @PutMapping(SPECIFIC_PRODUCTS_URI_FORMAT)
    public ProductResponseDto updateProduct(@PathVariable Long id, @RequestBody ProductMypriceRequestDto requestDto) {
        return productService.updateProduct(id, requestDto);
    }

    @GetMapping(PRODUCTS_BASE_URI)
    public List<ProductResponseDto> getProducts(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return productService.getProducts(userDetails.getUser());
    }

    @GetMapping("/admin/products")
    public List<ProductResponseDto> getAllProducts() {
        return productService.getAllProducts();
    }
}
