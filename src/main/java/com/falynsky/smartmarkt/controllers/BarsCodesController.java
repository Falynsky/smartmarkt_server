package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.DTO.ProductDTO;
import com.falynsky.smartmarkt.models.DTO.ProductTypeDTO;
import com.falynsky.smartmarkt.models.Document;
import com.falynsky.smartmarkt.repositories.DocumentRepository;
import com.falynsky.smartmarkt.services.ProductService;
import com.falynsky.smartmarkt.services.ProductTypeService;
import com.falynsky.smartmarkt.services.SalesService;
import com.falynsky.smartmarkt.utils.PriceUtils;
import com.falynsky.smartmarkt.utils.ResponseMapBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/barsCodes")
public class BarsCodesController {

    private final ProductService productService;
    private final ProductTypeService productTypeService;
    private final SalesService salesService;
    private final DocumentRepository documentRepository;

    public BarsCodesController(ProductService productService, ProductTypeService productTypeService, SalesService salesService, DocumentRepository documentRepository) {
        this.productService = productService;
        this.productTypeService = productTypeService;
        this.salesService = salesService;
        this.documentRepository = documentRepository;
    }

    @GetMapping("/{code}")
    public Map<String, Object> getProductDataUsingBarsCode(@PathVariable("code") Integer barsCodeCode) {

        final ProductDTO productDTO = productService.getProductDTOByBarsCode(barsCodeCode);
        if (productDTO == null) {
            return new HashMap<>();
        }

        Map<String, Object> productData = new HashMap<>();
        productData.put("id", productDTO.id);
        productData.put("name", productDTO.name);
        double productPrice = productDTO.price;
        productData.put("price", productPrice);
        productData.put("quantity", productDTO.quantity);
        Double priceAfterDiscount = salesService.getProductPriceAfterDiscount(productPrice, productDTO);
        productData.put("afterDiscount", priceAfterDiscount != null ? PriceUtils.roundPrice(priceAfterDiscount) : null);

        Integer documentId = productDTO.documentId;
        Optional<Document> optionalDocument = documentRepository.findById(documentId);
        if (optionalDocument.isPresent()) {
            Document document = optionalDocument.get();
            productData.put("documentId", documentId);
            productData.put("documentName", document.getDocName());
            productData.put("documentType", document.getDocType());
        }
        int productTypeId = productDTO.productTypeId;
        final ProductTypeDTO productTypeDTO = productTypeService.getProductTypeDTOById(productTypeId);

        productData.put("productTypeId", productTypeDTO.id);
        productData.put("productType", productTypeDTO.name);
        return ResponseMapBuilder.buildResponse(productData, true);
    }


}
