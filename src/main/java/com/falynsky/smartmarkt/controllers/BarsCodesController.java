package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.DTO.ProductDTO;
import com.falynsky.smartmarkt.models.DTO.ProductTypeDTO;
import com.falynsky.smartmarkt.services.DocumentService;
import com.falynsky.smartmarkt.services.ProductService;
import com.falynsky.smartmarkt.services.ProductTypeService;
import com.falynsky.smartmarkt.services.SalesService;
import com.falynsky.smartmarkt.utils.PriceUtils;
import com.falynsky.smartmarkt.utils.ResponseMapUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/barsCodes")
public class BarsCodesController {

    private final ProductService productService;
    private final ProductTypeService productTypeService;
    private final SalesService salesService;
    private final DocumentService documentService;

    public BarsCodesController(
            ProductService productService,
            ProductTypeService productTypeService,
            SalesService salesService,
            DocumentService documentService) {
        this.productService = productService;
        this.productTypeService = productTypeService;
        this.salesService = salesService;
        this.documentService = documentService;
    }

    @GetMapping("/{code}")
    public Map<String, Object> getProductDataUsingBarsCode(@PathVariable("code") Integer barsCodeCode) throws Exception {

        final ProductDTO productDTO = productService.getProductDTOByBarsCode(barsCodeCode);
        if (productDTO == null) {
            return new HashMap<>();
        }

        Map<String, Object> productData = new HashMap<>();
        double productPrice = productDTO.price;

        productData.put("id", productDTO.id);
        productData.put("name", productDTO.name);
        productData.put("price", productPrice);
        productData.put("quantity", productDTO.quantity);

        Double priceAfterDiscount = salesService.getProductPriceAfterDiscount(productPrice, productDTO);
        Double afterDiscount = getAfterDiscount(priceAfterDiscount);
        productData.put("afterDiscount", afterDiscount);

        Map<String, Object> documentData = getDocumentData(productDTO);
        productData.putAll(documentData);

        int productTypeId = productDTO.productTypeId;
        final ProductTypeDTO productTypeDTO = productTypeService.getProductTypeDTOById(productTypeId);

        productData.put("productTypeId", productTypeDTO.id);
        productData.put("productType", productTypeDTO.name);
        return ResponseMapUtils.buildResponse(productData, true);
    }

    private Map<String, Object> getDocumentData(ProductDTO productDTO) {
        Integer documentId = productDTO.documentId;
        return documentService.getDocumentDataByDocumentId(documentId);
    }

    private Double getAfterDiscount(Double priceAfterDiscount) {
        return priceAfterDiscount != null ? PriceUtils.roundPrice(priceAfterDiscount) : null;
    }


}
