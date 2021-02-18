package com.falynsky.smartmarkt.services;

import com.falynsky.smartmarkt.models.dto.ProductTypeDTO;
import com.falynsky.smartmarkt.repositories.ProductTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductTypeService {

    private final ProductTypeRepository productTypeRepository;

    public ProductTypeService(ProductTypeRepository productTypeRepository) {
        this.productTypeRepository = productTypeRepository;
    }

    public ProductTypeDTO getProductTypeDTOById(Integer productTypeId) throws Exception {
        ProductTypeDTO productTypeDTO = productTypeRepository.retrieveProductTypeAsDTObyId(productTypeId);
        if(productTypeDTO == null){
            throw new Exception("USER NOT FOUND");
        }
        return productTypeDTO;
    }

}
