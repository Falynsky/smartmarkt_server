package com.falynsky.smartmarkt.repositories;

import com.falynsky.smartmarkt.models.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@EnableJpaRepositories(basePackageClasses = ProductRepository.class)
@EntityScan(basePackageClasses = Product.class)
class ProductRepositoryTest {

    private static Product product;
    @Autowired
    private static ProductRepository productRepository;

    @BeforeAll
    static void start() {
        product = new Product(0, "test", 10);
        productRepository.save(product);

    }

    @Test
    void addition() {
        Product product1 = productRepository.findByName("test").get();
        assertEquals("test", product1.getName());
    }
}