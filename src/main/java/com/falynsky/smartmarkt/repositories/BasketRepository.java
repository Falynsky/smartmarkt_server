package com.falynsky.smartmarkt.repositories;

import com.falynsky.smartmarkt.models.Basket;
import com.falynsky.smartmarkt.models.DTO.BasketDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Integer> {

    Optional<Basket> findByName(String name);

    Basket findFirstByOrderByIdDesc();

    @Query("SELECT new com.falynsky.smartmarkt.models.DTO.BasketDTO(b.id, b.name, b.userId.id) FROM Basket b")
    List<BasketDTO> retrieveAccountAsDTO();

    @Query("SELECT new com.falynsky.smartmarkt.models.DTO.BasketDTO(b.id, b.name, b.userId.id) FROM Basket b where b.id = :basketId")
    BasketDTO retrieveAccountAsDTObyId(@Param("basketId") Integer basketId);
}
