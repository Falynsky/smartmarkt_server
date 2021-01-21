package com.falynsky.smartmarkt.repositories;

import com.falynsky.smartmarkt.models.DTO.SalesDTO;
import com.falynsky.smartmarkt.models.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Integer> {

    Sales findFirstByOrderByIdDesc();

    @Query("SELECT new com.falynsky.smartmarkt.models.DTO.SalesDTO(s.id, s.productId.name, s.description, s.discount, s.productId.price, s.productId.id) FROM Sales s")
    List<SalesDTO> retrieveSalesAsDTO();

    @Query("SELECT new com.falynsky.smartmarkt.models.DTO.SalesDTO(s.id, s.productId.name, s.description, s.discount, s.productId.price, s.productId.id) FROM Sales s WHERE s.productId.id = :productId")
    SalesDTO retrieveSaleAsDTOByProductId(@Param("productId") Integer productId);
}
