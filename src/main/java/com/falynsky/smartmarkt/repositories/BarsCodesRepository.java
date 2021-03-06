package com.falynsky.smartmarkt.repositories;

import com.falynsky.smartmarkt.models.BarsCodes;
import com.falynsky.smartmarkt.models.DTO.BarsCodesDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BarsCodesRepository extends JpaRepository<BarsCodes, Integer> {

    BarsCodes findFirstByOrderByIdDesc();

    @Query("SELECT new com.falynsky.smartmarkt.models.DTO.BarsCodesDTO(b.id, b.code, b.productId) FROM BarsCodes b where b.code = :barsCodeCode")
    Optional<BarsCodesDTO> retrieveProductAsDTObyCode(@Param("barsCodeCode") Integer barsCodeCode);
}
