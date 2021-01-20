package com.falynsky.smartmarkt.repositories;

import com.falynsky.smartmarkt.models.Basket;
import com.falynsky.smartmarkt.models.BasketProduct;
import com.falynsky.smartmarkt.models.DTO.BasketProductDTO;
import com.falynsky.smartmarkt.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BasketProductRepository extends JpaRepository<BasketProduct, Integer> {

    Optional<BasketProduct> findById(int id);

    BasketProduct findFirstByOrderByIdDesc();

    Optional<BasketProduct> findFirstByProductIdAndBasketIdAndClosedFalse(Product product, Basket basket);


    @Query("SELECT new com.falynsky.smartmarkt.models.DTO.BasketProductDTO" +
            "(b.id,  b.quantity, b.quantityType, b.purchased, b.closed, b.basketId.id, b.productId.id) " +
            "FROM BasketProduct b WHERE b.basketId.id = :basketId AND b.closed = FALSE")
    List<BasketProductDTO> retrieveBasketProductAsDTObyBasketIdAndNotClosedYet(@Param("basketId") Integer basketId);

}
