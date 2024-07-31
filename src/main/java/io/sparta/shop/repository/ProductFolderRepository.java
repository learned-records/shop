package io.sparta.shop.repository;

import io.sparta.shop.entity.Folder;
import io.sparta.shop.entity.Product;
import io.sparta.shop.entity.ProductFolder;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductFolderRepository extends JpaRepository<ProductFolder, Long> {
    Optional<ProductFolder> findByProductAndFolder(Product product, Folder folder);
}
