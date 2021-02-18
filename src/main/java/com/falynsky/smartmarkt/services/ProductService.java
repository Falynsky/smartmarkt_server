package com.falynsky.smartmarkt.services;

import com.falynsky.smartmarkt.models.dto.BarsCodesDTO;
import com.falynsky.smartmarkt.models.dto.ProductDTO;
import com.falynsky.smartmarkt.repositories.BarsCodesRepository;
import com.falynsky.smartmarkt.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final BarsCodesRepository barsCodesRepository;

    public ProductService(
            ProductRepository productRepository,
            BarsCodesRepository barsCodesRepository) {
        this.productRepository = productRepository;
        this.barsCodesRepository = barsCodesRepository;
    }

    public ProductDTO getProductDTOByBarsCode(Integer barsCodeCode) {
        Optional<BarsCodesDTO> basket = barsCodesRepository.retrieveProductAsDTObyCode(barsCodeCode);
        if (basket.isPresent()) {
            final int productId = basket.get().productId;
            Optional<ProductDTO> productDTO = productRepository.retrieveProductAsDTObyId(productId);
            return productDTO.orElse(null);
        }
        return null;
    }

}
