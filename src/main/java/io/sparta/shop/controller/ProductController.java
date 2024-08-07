package io.sparta.shop.controller;

import io.sparta.shop.dto.ProductMypriceRequestDto;
import io.sparta.shop.dto.ProductRequestDto;
import io.sparta.shop.dto.ProductResponseDto;
import io.sparta.shop.security.UserDetailsImpl;
import io.sparta.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public Page<ProductResponseDto> getProducts(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestParam("page") int page,
        @RequestParam("size") int size,
        @RequestParam("sortBy") String sortBy,
        @RequestParam("isAsc") boolean isAsc
    ) {
        return productService.getProducts(
            userDetails.getUser(),
            page - 1,
            size,
            sortBy,
            isAsc
        );
    }

    @PostMapping("/products/{productId}/folder")
    public void addFolder(
        @PathVariable Long productId,
        @RequestParam Long folderId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        productService.addFolder(productId, folderId, userDetails.getUser());
    }

    @GetMapping("/folders/{folderId}/products")
    public Page<ProductResponseDto> getProductsInFolder(
        @PathVariable Long folderId,
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestParam("page") int page,
        @RequestParam("size") int size,
        @RequestParam("sortBy") String sortBy,
        @RequestParam("isAsc") boolean isAsc
    ) {
        return productService.getProductsInFolder(
            folderId,
            page - 1,
            size,
            sortBy,
            isAsc,
            userDetails.getUser()
        );
    }
}
