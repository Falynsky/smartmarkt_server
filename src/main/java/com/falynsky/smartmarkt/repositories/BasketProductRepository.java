package com.falynsky.smartmarkt.repositories;

import com.falynsky.smartmarkt.models.objects.Basket;
import com.falynsky.smartmarkt.models.objects.BasketProduct;
import com.falynsky.smartmarkt.models.dto.BasketProductDTO;
import com.falynsky.smartmarkt.models.objects.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BasketProductRepository extends JpaRepository<BasketProduct, Integer> {

    BasketProduct findById(int id);

    BasketProduct findFirstByOrderByIdDesc();

    BasketProduct findFirstByProductIdAndBasketIdAndClosedFalse(Product product, Basket basket);


    @Query("SELECT new com.falynsky.smartmarkt.models.dto.BasketProductDTO" +
            "(b.id,  b.quantity, b.quantityType, b.weight, b.purchasedPrice, b.purchased, b.purchaseDateTime, b.closed, b.basketId.id, b.productId.id) " +
            "FROM BasketProduct b WHERE b.basketId.id = :basketId AND b.closed = FALSE")
    List<BasketProductDTO> retrieveBasketProductAsDTObyBasketIdAndNotClosedYet(@Param("basketId") Integer basketId);


    @Query("SELECT new com.falynsky.smartmarkt.models.dto.BasketProductDTO" +
            "(b.id,  b.quantity, b.quantityType, b.weight, b.purchasedPrice, b.purchased, b.purchaseDateTime, b.closed, b.basketId.id, b.productId.id) " +
            "FROM BasketProduct b WHERE b.basketId.id = :basketId AND b.basketsHistoryId.id = :basketHistoryId  AND b.closed = TRUE ORDER BY b.purchaseDateTime")
    List<BasketProductDTO> retrieveBasketProductAsDTObyBasketIdAndHistoryAndClosed(@Param("basketId") Integer basketId, @Param("basketHistoryId") Integer basketHistoryId);

}
