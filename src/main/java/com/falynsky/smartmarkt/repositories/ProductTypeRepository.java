package com.falynsky.smartmarkt.repositories;

import com.falynsky.smartmarkt.models.DTO.ProductTypeDTO;
import com.falynsky.smartmarkt.models.Product;
import com.falynsky.smartmarkt.models.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductTypeRepository extends JpaRepository<Product, Integer> {


    ProductType findFirstByOrderByIdDesc();

    @Query("SELECT new com.falynsky.smartmarkt.models.DTO.ProductTypeDTO(pt.id, pt.name) FROM ProductType pt")
    List<ProductTypeDTO> retrieveProductTypesAsDTO();

    @Query("SELECT new com.falynsky.smartmarkt.models.DTO.ProductTypeDTO(pt.id, pt.name) FROM ProductType pt where pt.id = :productTypeId")
    ProductTypeDTO retrieveProductTypeAsDTObyId(@Param("productTypeId") Integer productTypeId);
}
