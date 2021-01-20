package com.falynsky.smartmarkt.repositories;

import com.falynsky.smartmarkt.models.BasketHistory;
import com.falynsky.smartmarkt.models.DTO.BasketHistoryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BasketHistoryRepository extends JpaRepository<BasketHistory, Integer> {

    Optional<BasketHistory> findById(int id);

    BasketHistory findFirstByOrderByIdDesc();

    BasketHistory findByClosedFalse();

    @Query("SELECT new com.falynsky.smartmarkt.models.DTO.BasketHistoryDTO(b.id, b.purchased, b.closed, b.basketId.id) FROM BasketHistory b")
    List<BasketHistoryDTO> retrieveBasketsAsDTO();

    @Query("SELECT new com.falynsky.smartmarkt.models.DTO.BasketHistoryDTO(b.id, b.purchased,b.closed,  b.basketId.id) FROM BasketHistory b where b.purchased = false")
    BasketHistoryDTO retrieveUnPurchasedBasketProductSummaryAsDTO();

    @Query("SELECT new com.falynsky.smartmarkt.models.DTO.BasketHistoryDTO(b.id, b.purchased, b.closed, b.basketId.id) FROM BasketHistory b where b.id = :basketHistoryId")
    BasketHistoryDTO retrieveBasketAsDTObyId(@Param("basketHistoryId") Integer basketHistoryId);
}
