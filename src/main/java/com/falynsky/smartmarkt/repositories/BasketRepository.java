package com.falynsky.smartmarkt.repositories;

import com.falynsky.smartmarkt.models.objects.Basket;
import com.falynsky.smartmarkt.models.dto.BasketDTO;
import com.falynsky.smartmarkt.models.objects.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Integer> {

    Basket findByUserId(User user);

    Basket findFirstByOrderByIdDesc();

    @Query("SELECT new com.falynsky.smartmarkt.models.dto.BasketDTO(b.id, b.name, b.userId.id) FROM Basket b")
    List<BasketDTO> retrieveBasketsAsDTO();

    @Query("SELECT new com.falynsky.smartmarkt.models.dto.BasketDTO(b.id, b.name, b.userId.id) FROM Basket b where b.id = :basketId")
    BasketDTO retrieveBasketAsDTObyId(@Param("basketId") Integer basketId);


    @Query("SELECT new com.falynsky.smartmarkt.models.dto.BasketDTO(b.id, b.name, b.userId.id) FROM Basket b where b.userId.id = :userId")
    BasketDTO retrieveBasketAsDTObyUserId(@Param("userId") Integer userId);
}
