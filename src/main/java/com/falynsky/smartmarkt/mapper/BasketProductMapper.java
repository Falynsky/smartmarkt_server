package com.falynsky.smartmarkt.mapper;

import com.falynsky.smartmarkt.models.dto.BasketProductDTO;
import com.falynsky.smartmarkt.models.objects.Basket;
import com.falynsky.smartmarkt.models.objects.BasketHistory;
import com.falynsky.smartmarkt.models.objects.BasketProduct;
import com.falynsky.smartmarkt.models.objects.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BasketProductMapper {

    @Mapping(target = "id", source = "basketProductDTO.id")
    @Mapping(target = "quantity", source = "basketProductDTO.quantity")
    @Mapping(target = "quantityType", source = "basketProductDTO.quantityType")
    @Mapping(target = "weight", source = "basketProductDTO.weight")
    @Mapping(target = "purchasedPrice", source = "basketProductDTO.purchasedPrice")
    @Mapping(target = "purchased", source = "basketProductDTO.purchased")
    @Mapping(target = "purchaseDateTime", source = "basketProductDTO.purchaseDateTime")
    @Mapping(target = "closed", source = "basketProductDTO.closed")
    @Mapping(target = "basketId", source = "basket")
    @Mapping(target = "productId", source = "product")
    @Mapping(target = "basketsHistoryId", source = "basketHistory")
    BasketProduct map(BasketProductDTO basketProductDTO, BasketProduct basketProduct, Basket basket, Product product, BasketHistory basketHistory);

    @Mapping(target = "id", source = "basketProduct.id")
    @Mapping(target = "quantity", source = "basketProduct.quantity")
    @Mapping(target = "quantityType", source = "basketProduct.quantityType")
    @Mapping(target = "weight", source = "basketProduct.weight")
    @Mapping(target = "purchasedPrice", source = "basketProduct.purchasedPrice")
    @Mapping(target = "purchased", source = "basketProduct.purchased")
    @Mapping(target = "purchaseDateTime", source = "basketProduct.purchaseDateTime")
    @Mapping(target = "closed", source = "basketProduct.closed")
    @Mapping(target = "basketId", source = "basketProduct.basket.id")
    @Mapping(target = "productId", source = "basketProduct.product.id")
    @Mapping(target = "basketsHistoryId", source = "basketProduct.basketHistory.id")
    BasketProductDTO mapToDto(BasketProduct basketProduct);
}
