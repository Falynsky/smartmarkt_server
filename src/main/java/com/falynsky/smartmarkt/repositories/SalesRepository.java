package com.falynsky.smartmarkt.repositories;

import com.falynsky.smartmarkt.models.DTO.SalesDTO;
import com.falynsky.smartmarkt.models.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Integer> {

    Sales findFirstByOrderByIdDesc();

    @Query("SELECT new com.falynsky.smartmarkt.models.DTO.SalesDTO(s.id, s.title, s.description) FROM Sales s")
    List<SalesDTO> retrieveSalesAsDTO();
}
