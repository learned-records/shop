package io.sparta.shop.repository;

import io.sparta.shop.entity.Product;
import io.sparta.shop.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByUser(User user);
}
