package com.falynsky.smartmarkt.services;

import com.falynsky.smartmarkt.exceptions.NotEnoughQuantity;
import com.falynsky.smartmarkt.models.dto.BasketProductDTO;
import com.falynsky.smartmarkt.models.dto.ProductDTO;
import com.falynsky.smartmarkt.models.dto.SalesDTO;
import com.falynsky.smartmarkt.models.request.BasketObject;
import com.falynsky.smartmarkt.models.objects.*;
import com.falynsky.smartmarkt.repositories.BasketHistoryRepository;
import com.falynsky.smartmarkt.repositories.BasketProductRepository;
import com.falynsky.smartmarkt.repositories.ProductRepository;
import com.falynsky.smartmarkt.repositories.SalesRepository;
import com.falynsky.smartmarkt.utils.PriceUtils;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BasketProductService {

    private final BasketService basketService;
    private final SalesRepository salesRepository;
    private final BasketProductRepository basketProductRepository;
    private final ProductRepository productRepository;
    private final AccountService accountService;
    private final UserService userService;
    private final DocumentService documentService;
    private final BasketHistoryRepository basketHistoryRepository;
    private final SalesService salesService;

    BasketProductService(BasketProductRepository basketProductRepository,
                         ProductRepository productRepository,
                         BasketService basketService, SalesRepository salesRepository,
                         AccountService accountService,
                         UserService userService,
                         DocumentService documentService,
                         BasketHistoryRepository basketHistoryRepository,

                         SalesService salesService) {
        this.basketProductRepository = basketProductRepository;
        this.productRepository = productRepository;
        this.basketService = basketService;
        this.salesRepository = salesRepository;
        this.accountService = accountService;
        this.userService = userService;
        this.documentService = documentService;
        this.basketHistoryRepository = basketHistoryRepository;
        this.salesService = salesService;
    }


    public String updateOrCreateBasketProduct(BasketObject basketObject, String userToken) throws Exception {
        Integer productId = basketObject.productId;
        int quantity = getQuantity(basketObject);
        Product product = getSelectedProduct(productId);

        int productQuantity = product.getQuantity();
        if (productQuantity < quantity) {
            throw new NotEnoughQuantity(product.name);
        }
        return addProductToBasket(quantity, product, userToken);
    }

    @Transactional(rollbackFor = Exception.class)
    String addProductToBasket(int quantity, Product product, String userToken) {
        Basket basket = getUserBasketByUserToken(userToken);
        BasketProduct basketProduct = basketProductRepository.findFirstByProductIdAndBasketIdAndClosedFalse(product, basket);
        if (basketProduct != null) {
            int newQuantity = basketProduct.getQuantity() + quantity;
            basketProduct.setQuantity(newQuantity);
        } else {
            basketProduct = createAndAddBasketProduct(quantity, product, basket);
        }

        int newQuantity = product.getQuantity() - quantity;
        product.setQuantity(newQuantity);
        productRepository.save(product);
        basketProductRepository.save(basketProduct);
        return product.name;
    }

    @SneakyThrows
    public Basket getUserBasketByUserToken(String userToken) {
        User user = getCurrentUser(userToken);
        return basketService.getCurrentUserBasket(user);
    }

    private User getCurrentUser(String userToken) throws Exception {
        Account account = accountService.getCurrentAccount(userToken);
        return userService.getCurrentUserByAccount(account);
    }

    public Product getSelectedProduct(Integer productId) throws Exception {
        Optional<Product> product = productRepository.findById(productId);
        if (!product.isPresent()) {
            throw new Exception("PRODUCT NOT FOUND");
        }
        return product.get();
    }

    public BasketProduct getBasketProduct(BasketObject map, String userToken) throws Exception {
        Integer productId = map.productId;
        Basket basket = getUserBasketByUserToken(userToken);
        Product product = getSelectedProduct(productId);
        return basketProductRepository.findFirstByProductIdAndBasketIdAndClosedFalse(product, basket);
    }

    public String removeProductFromBasket(BasketObject map, String userToken) throws Exception {
        BasketProduct basketProduct = getBasketProduct(map, userToken);
        Integer productId = map.productId;
        Integer quantity = map.quantity;
        Product product = getSelectedProduct(productId);
        updateProductQuantity(basketProduct, product, quantity);
        int basketProductQuantity;
        if (quantity == null) {
            basketProductRepository.delete(basketProduct);
        } else {
            basketProductQuantity = basketProduct.getQuantity() - quantity;
            basketProduct.setQuantity(basketProductQuantity);
            basketProductRepository.save(basketProduct);
        }

        return product.name;
    }

    public void removeAllProductsFromBasket(String userToken) {
        Basket basket = getUserBasketByUserToken(userToken);

        List<BasketProductDTO> basketProductDTOS = basketProductRepository.retrieveBasketProductAsDTObyBasketIdAndNotClosedYet(basket.id);
        for (BasketProductDTO basketProductDTO : basketProductDTOS) {
            BasketProduct basketProduct = basketProductRepository.findById(basketProductDTO.getId());
            if (basketProduct != null) {
                Product product = basketProduct.getProductId();
                updateProductQuantity(basketProduct, product, null);
                basketProductRepository.delete(basketProduct);
            }
        }

    }

    private void updateProductQuantity(BasketProduct basketProduct, Product product, Integer quantity) {
        int basketProductQuantity;
        if (quantity == null) {
            basketProductQuantity = product.getQuantity() + basketProduct.getQuantity();
        } else {
            basketProductQuantity = product.getQuantity() + quantity;
        }
        product.setQuantity(basketProductQuantity);
        productRepository.save(product);
    }

    public BasketProduct createAndAddBasketProduct(int quantity, Product product, Basket basket) {
        int id = getIdForNewBasketProduct(basketProductRepository);

        BasketProduct basketProduct = new BasketProduct();
        basketProduct.setId(id);
        basketProduct.setQuantity(quantity);
        basketProduct.setQuantityType("szt.");
        basketProduct.setPurchased(false);
        basketProduct.setClosed(false);
        basketProduct.setBasketId(basket);
        basketProduct.setProductId(product);
        return basketProduct;
    }

    private int getIdForNewBasketProduct(BasketProductRepository basketProductRepository) {
        BasketProduct lastBasketProduct = basketProductRepository.findFirstByOrderByIdDesc();
        if (lastBasketProduct == null) {
            return 1;
        }
        int lastId = lastBasketProduct.getId();
        return ++lastId;
    }

    @SneakyThrows
    private Integer getQuantity(BasketObject map) {
        Object quantityValue = map.quantity;
        if (quantityValue instanceof Integer) {
            return (Integer) quantityValue;
        } else {
            throw new NotEnoughQuantity();
        }
    }

    public List<Map<String, Object>> getSelectedBasketProductsData(List<BasketProductDTO> basketProductDTOS) {


        return basketProductDTOS.stream()
                .map(this::buildProductData)
                .collect(Collectors.toList());
    }

    private Map<String, Object> buildProductData(BasketProductDTO basketProductDTO) {

        int basketProductDTOId = basketProductDTO.getProductId();
        Optional<ProductDTO> optionalProduct = productRepository.retrieveProductAsDTObyId(basketProductDTOId);

        if (optionalProduct.isPresent()) {
            int id = basketProductDTO.getId();
            ProductDTO productDTO = optionalProduct.get();
            int productId = productDTO.getId();
            String name = productDTO.getName();
            int quantity = basketProductDTO.getQuantity();
            double price = productDTO.getPrice();

            Map<String, Object> productData = new LinkedHashMap<>();
            productData.put("id", id);
            productData.put("productId", productId);
            productData.put("name", name);
            productData.put("quantity", quantity);
            productData.put("price", price);
            double summary = price * quantity;
            double roundSummary = PriceUtils.roundPrice(summary);
            productData.put("summary", roundSummary);
            Double productDiscount = salesService.getProductPriceAfterDiscount(roundSummary, productDTO);
            productData.put("discountPrice", productDiscount != null ? PriceUtils.roundPrice(productDiscount) : null);

            Map<String, Object> documentData = getDocumentData(productDTO);
            productData.putAll(documentData);
            return productData;
        }
        return null;
    }

    private Map<String, Object> getDocumentData(ProductDTO productDTO) {
        Integer documentId = productDTO.documentId;
        return documentService.getDocumentDataByDocumentId(documentId);
    }

    public Map<String, Object> getSelectedBasketSummary(List<BasketProductDTO> basketProductDTOS) {
        Map<String, Object> data = new HashMap<>();
        double summary = 0;
        double summaryAfterDiscount = 0;
        for (BasketProductDTO basketProductDTO : basketProductDTOS) {
            int basketProductDTOId = basketProductDTO.getProductId();
            Optional<ProductDTO> optionalProduct = productRepository.retrieveProductAsDTObyId(basketProductDTOId);
            if (optionalProduct.isPresent()) {
                ProductDTO product = optionalProduct.get();
                double productSummary = getProductSummary(basketProductDTO, product);
                summary += productSummary;
                Double productPriceAfterDiscount = salesService.getProductPriceAfterDiscount(productSummary, product);
                summaryAfterDiscount += productPriceAfterDiscount != null ? productPriceAfterDiscount : productSummary;
            }
        }
        data.put("summary", PriceUtils.roundPrice(summary));
        data.put("summaryAfterDiscount", PriceUtils.roundPrice(summaryAfterDiscount));
        return data;

    }

    private double getProductSummary(BasketProductDTO basketProductDTO, ProductDTO product) {
        double price = product.getPrice();
        int quantity = basketProductDTO.getQuantity();
        return price * quantity;
    }

    public void purchaseAllProductsFromBasket(String userToken) {
        Basket basket = getUserBasketByUserToken(userToken);
        BasketHistory basketHistory = basketHistoryRepository.findByClosedFalse();
        List<SalesDTO> salesDTO = salesRepository.retrieveSalesAsDTO();
        if (basketHistory == null) {
            basketHistory = createAndGetBasketHistory(basket);
        }

        List<BasketProductDTO> basketProductDTOS = basketProductRepository.retrieveBasketProductAsDTObyBasketIdAndNotClosedYet(basket.id);
        Date purchaseDateTime = new Date();
        for (BasketProductDTO basketProductDTO : basketProductDTOS) {
            BasketProduct basketProduct = basketProductRepository.findById(basketProductDTO.getId());
            if (basketProduct != null) {
                modifyAndSaveBasketProduct(basketHistory, salesDTO, purchaseDateTime, basketProduct);
            }
        }

        basketHistory.setPurchased(true);
        basketHistory.setClosed(true);
        basketHistoryRepository.save(basketHistory);
    }

    private BasketHistory createAndGetBasketHistory(Basket basket) {
        BasketHistory basketHistory;
        int newBasketHistoryId = getIdForNewBasketHistory();
        basketHistory = new BasketHistory(newBasketHistoryId, false, false, basket);
        basketHistoryRepository.save(basketHistory);
        return basketHistory;
    }

    private int getIdForNewBasketHistory() {
        BasketHistory BasketHistory = basketHistoryRepository.findFirstByOrderByIdDesc();
        if (BasketHistory == null) {
            return 1;
        }
        int lastId = BasketHistory.getId();
        return ++lastId;
    }

    private void modifyAndSaveBasketProduct(BasketHistory basketHistory, List<SalesDTO> salesDTO, Date purchaseDateTime, BasketProduct basketProduct) {
        basketProduct.setBasketsHistoryId(basketHistory);
        basketProduct.setPurchaseDateTime(purchaseDateTime);
        basketProduct.setPurchased(true);
        basketProduct.setClosed(true);
        Product product = basketProduct.getProductId();
        basketProduct.setWeight(product.getWeight());
        int productId = product.id;
        double purchasedPrice = product.getPrice();
        for (SalesDTO saleDTO : salesDTO) {
            if (saleDTO.getProductId() == productId) {
                purchasedPrice = purchasedPrice * (1 - saleDTO.getDiscount());
                break;
            }
        }
        basketProduct.setPurchasedPrice(PriceUtils.roundPrice(purchasedPrice));
        basketProductRepository.save(basketProduct);
    }

}
