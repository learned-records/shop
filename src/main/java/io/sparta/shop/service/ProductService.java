package io.sparta.shop.service;

import io.sparta.shop.dto.ProductMypriceRequestDto;
import io.sparta.shop.dto.ProductRequestDto;
import io.sparta.shop.dto.ProductResponseDto;
import io.sparta.shop.entity.Folder;
import io.sparta.shop.entity.Product;
import io.sparta.shop.entity.ProductFolder;
import io.sparta.shop.entity.User;
import io.sparta.shop.entity.UserRoleEunm;
import io.sparta.shop.naver.dto.ItemDto;
import io.sparta.shop.repository.FolderRepository;
import io.sparta.shop.repository.ProductFolderRepository;
import io.sparta.shop.repository.ProductRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final FolderRepository folderRepository;
    private final ProductFolderRepository productFolderRepository;

    public static final int MIN_MY_PRICE = 100;

    public ProductResponseDto createProduct(ProductRequestDto productRequestDto, User user) {
        Product product = productRepository.save(new Product(productRequestDto, user));
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

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getProducts(
        User user,
        int page,
        int size,
        String sortBy,
        boolean isAsc
    ) {
        Sort.Direction direction = isAsc ? Direction.ASC : Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        UserRoleEunm userRoleEunm = user.getRole();

        Page<Product> products;

        if (userRoleEunm == UserRoleEunm.USER) {
            products = productRepository.findAllByUser(user, pageable);
        } else {
            products = productRepository.findAll(pageable);
        }

        return products.map(ProductResponseDto::new);
    }

    @Transactional
    public void updateBySearch(Long id, ItemDto itemDto) {
        Product product = productRepository.findById(id).orElseThrow(() ->
            new NullPointerException("해당 상품은 존재하지 않습니다.")
        );

        product.updateByItemDto(itemDto);
    }

    public void addFolder(Long productId, Long folderId, User user) {
        Product product = productRepository.findById(productId).orElseThrow(() ->
            new NullPointerException("해당 상품은 존재하지 않습니다"));

        Folder folder = folderRepository.findById(folderId).orElseThrow(() ->
            new NullPointerException("해당 폴더가 존재하지 않습니다"));

        if (!product.getUser().getId().equals(user.getId())
            || !folder.getUser().getId().equals(user.getId())
        ) {
            throw new IllegalArgumentException("회원님의 관심상품이 아니거나, 회원님의 폴더가 아닙니다.");
        }

        Optional<ProductFolder> overlapFolder = productFolderRepository.findByProductAndFolder(product, folder);
        if (overlapFolder.isPresent()) {
            throw new IllegalArgumentException("중복된 폴더입니다.");
        }

        productFolderRepository.save(new ProductFolder(product, folder));
    }
}
