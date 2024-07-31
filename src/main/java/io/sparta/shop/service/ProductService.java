package io.sparta.shop.service;

import io.sparta.shop.dto.ProductMypriceRequestDto;
import io.sparta.shop.dto.ProductRequestDto;
import io.sparta.shop.dto.ProductResponseDto;
import io.sparta.shop.entity.Product;
import io.sparta.shop.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public static final int MIN_MY_PRICE = 100;

    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        Product product = productRepository.save(new Product(productRequestDto));
        return new ProductResponseDto(product);
    }

    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductMypriceRequestDto requestDto) {
        int myprice = requestDto.getMyprice();
        if (myprice < MIN_MY_PRICE) {
            throw new IllegalArgumentException("유효하지 않은 관심 과격입니다. 최소 " + MIN_MY_PRICE + "원 이상으로 설정해 주세요.");
        }

        Product product = productRepository.findById(id).orElseThrow(() -> new NullPointerException("해당 상품을 찾을 수 없습니다."));
        product.update(requestDto);
        return new ProductResponseDto(product);
    }

    public List<ProductResponseDto> getProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream()
            .map(ProductResponseDto::new)
            .toList();
    }
}