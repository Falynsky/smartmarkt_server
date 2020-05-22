package com.falynsky.smartmarkt.repositories;

import com.falynsky.smartmarkt.models.DTO.ProductDTO;
import com.falynsky.smartmarkt.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findByName(String name);

    Product findFirstByOrderByIdDesc();

    @Query("SELECT new com.falynsky.smartmarkt.models.DTO.ProductDTO(p.id, p.name, p.quantity) FROM Product p")
    List<ProductDTO> retrieveProductAsDTO();

    @Query("SELECT new com.falynsky.smartmarkt.models.DTO.ProductDTO(p.id, p.name, p.quantity) FROM Product p where p.id = :productId")
    ProductDTO retrieveProductAsDTObyId(@Param("productId") Integer productId);
}
