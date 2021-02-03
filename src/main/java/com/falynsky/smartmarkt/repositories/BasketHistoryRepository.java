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

    BasketHistory findById(int id);

    BasketHistory findFirstByOrderByIdDesc();

    BasketHistory findByClosedFalse();

    @Query("SELECT new com.falynsky.smartmarkt.models.DTO.BasketHistoryDTO(b.id, b.purchased, b.closed, b.basketId.id) FROM BasketHistory b where b.basketId.id = :basketId ORDER BY b.id DESC")
    List<BasketHistoryDTO> retrieveBasketsHistoryAsDTObyBasketId(@Param("basketId") Integer basketId);
}
