package io.sparta.shop.repository;

import io.sparta.shop.entity.ProductFolder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductFolderRepository extends JpaRepository<ProductFolder, Long> {
}
