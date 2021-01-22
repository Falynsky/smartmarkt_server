package com.falynsky.smartmarkt.services;

import com.falynsky.smartmarkt.JWT.JwtTokenUtil;
import com.falynsky.smartmarkt.exceptions.NotEnoughQuantity;
import com.falynsky.smartmarkt.models.*;
import com.falynsky.smartmarkt.models.DTO.BasketProductDTO;
import com.falynsky.smartmarkt.models.DTO.ProductDTO;
import com.falynsky.smartmarkt.models.DTO.SalesDTO;
import com.falynsky.smartmarkt.repositories.*;
import com.falynsky.smartmarkt.utils.PriceUtils;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BasketProductService {

    private final BasketRepository basketRepository;
    private final SalesRepository salesRepository;
    private final BasketProductRepository basketProductRepository;
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final DocumentRepository documentRepository;
    private final BasketHistoryRepository basketHistoryRepository;
    private final JwtTokenUtil jwtTokenUtil;

    BasketProductService(BasketProductRepository basketProductRepository,
                         ProductRepository productRepository,
                         BasketRepository basketRepository,
                         SalesRepository salesRepository, AccountRepository accountRepository,
                         UserRepository userRepository,
                         DocumentRepository documentRepository, BasketHistoryRepository basketHistoryRepository, JwtTokenUtil jwtTokenUtil) {
        this.basketProductRepository = basketProductRepository;
        this.productRepository = productRepository;
        this.basketRepository = basketRepository;
        this.salesRepository = salesRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.documentRepository = documentRepository;
        this.basketHistoryRepository = basketHistoryRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }


    public String updateOrCreateBasketProduct(Map<String, Object> map, String userToken) throws Exception {
        Integer productId = (Integer) map.get("productId");
        int quantity = getQuantity(map);
        Product product = getSelectedProduct(productId);

        int productQuantity = product.getQuantity();
        if (productQuantity < quantity) {
            throw new NotEnoughQuantity(product.name);
        }

        return addProductToBasket(map, product, userToken);

    }

    @Transactional(rollbackFor = Exception.class)
    String addProductToBasket(Map<String, Object> map, Product product, String userToken) throws Exception {
        int quantity = getQuantity(map);
        Basket basket = getUserBasketByUserToken(userToken);
        Optional<BasketProduct> optionalBasketProduct = basketProductRepository.findFirstByProductIdAndBasketIdAndClosedFalse(product, basket);
        BasketProduct basketProduct;
        if (optionalBasketProduct.isPresent()) {
            basketProduct = optionalBasketProduct.get();
            int oldQuantity = basketProduct.getQuantity() + quantity;
            basketProduct.setQuantity(oldQuantity);
        } else {
            basketProduct = createAndAddBasketProduct(map, basket);
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
        return getCurrentUserBasket(user);
    }

    private User getCurrentUser(String userToken) throws Exception {
        Account account = getCurrentAccount(userToken);
        return getCurrentUserByAccount(account);
    }

    private Account getCurrentAccount(String userToken) throws Exception {
        String currentUserUsername = jwtTokenUtil.getUsernameFromToken(userToken.substring(5));

        Optional<Account> optionalAccount = accountRepository.findByUsername(currentUserUsername);
        if (!optionalAccount.isPresent()) {
            throw new Exception("ACCOUNT NOT FOUND");
        }
        return optionalAccount.get();
    }

    private User getCurrentUserByAccount(Account account) throws Exception {
        Optional<User> optionalUser = userRepository.findFirstByAccountId(account);
        if (!optionalUser.isPresent()) {
            throw new Exception("USER NOT FOUND");
        }
        return optionalUser.get();
    }


    private Basket getCurrentUserBasket(User user) throws Exception {
        Optional<Basket> optionalBasket = basketRepository.findByUserId(user);
        if (optionalBasket.isPresent()) {
            return optionalBasket.get();
        } else {
            throw new Exception("BASKET NOT FOUND");
        }
    }

    public Product getSelectedProduct(Integer productId) throws Exception {
        Optional<Product> product = productRepository.findById(productId);
        if (!product.isPresent()) {
            throw new Exception("PRODUCT NOT FOUND");
        }
        return product.get();
    }

    public BasketProduct getBasketProduct(Map<String, Object> map, String userToken) throws Exception {
        Integer productId = (Integer) map.get("productId");
        Basket basket = getUserBasketByUserToken(userToken);
        Product product = getSelectedProduct(productId);
        Optional<BasketProduct> optionalBasketProduct = basketProductRepository.findFirstByProductIdAndBasketIdAndClosedFalse(product, basket);
        return optionalBasketProduct.orElse(null);
    }

    public void removeProductFromBasket(Map<String, Object> map, String userToken) throws Exception {
        BasketProduct basketProduct = getBasketProduct(map, userToken);
        Integer productId = (Integer) map.get("productId");
        Product product = getSelectedProduct(productId);
        updateProductQuantity(basketProduct, product);
        basketProductRepository.delete(basketProduct);
    }

    public void removeAllProductsFromBasket(String userToken) {
        Basket basket = getUserBasketByUserToken(userToken);

        List<BasketProductDTO> basketProductDTOS = basketProductRepository.retrieveBasketProductAsDTObyBasketIdAndNotClosedYet(basket.id);
        for (BasketProductDTO basketProductDTO : basketProductDTOS) {
            Optional<BasketProduct> optionalBasketProduct = basketProductRepository.findById(basketProductDTO.getId());
            if (optionalBasketProduct.isPresent()) {
                BasketProduct basketProduct = optionalBasketProduct.get();
                Product product = basketProduct.getProductId();
                updateProductQuantity(basketProduct, product);
                basketProductRepository.delete(basketProduct);
            }
        }

    }

    private void updateProductQuantity(BasketProduct basketProduct, Product product) {
        int basketProductQuantity = product.getQuantity() + basketProduct.getQuantity();
        product.setQuantity(basketProductQuantity);
        productRepository.save(product);
    }


    public BasketProduct createAndAddBasketProduct(Map<String, Object> map, Basket basket) {
        BasketProduct basketProduct = new BasketProduct();
        int id = getIdForNewBasketProduct(basketProductRepository);
        basketProduct.setId(id);
        Integer quantity = getQuantity(map);
        basketProduct.setQuantity(quantity);
        Product product = getProduct(map);
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
    private Integer getQuantity(Map<String, Object> map) {
        Object quantityValue = map.get("quantity");
        if (quantityValue instanceof Integer) {
            return (Integer) quantityValue;
        } else {
            throw new NotEnoughQuantity();
        }
    }

    private Product getProduct(Map<String, Object> map) {
        Integer productId = (Integer) map.get("productId");
        return productRepository.getOne(productId);
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
            Double price = productDTO.getPrice();

            Map<String, Object> productData = new LinkedHashMap<>();
            productData.put("id", id);
            productData.put("productId", productId);
            productData.put("name", name);
            productData.put("quantity", quantity);
            productData.put("price", price);
            double summary = price * quantity;
            double roundSummary = PriceUtils.roundPrice(summary);
            productData.put("summary", roundSummary);
            Double productDiscount = getProductPriceAfterDiscount(roundSummary, productDTO);
            productData.put("discountPrice", productDiscount != null ? PriceUtils.roundPrice(productDiscount) : null);

            Integer documentId = productDTO.documentId;
            Optional<Document> optionalDocument = documentRepository.findById(documentId);
            if (optionalDocument.isPresent()) {
                Document document = optionalDocument.get();
                productData.put("documentId", documentId);
                productData.put("documentName", document.getDocName());
                productData.put("documentType", document.getDocType());
            }
            return productData;
        }
        return null;
    }

    public void addOneToBasketProduct(Map<String, Object> map, String userToken) throws Exception {
        BasketProduct basketProduct = getBasketProduct(map, userToken);
        if (basketProduct == null) {
            throw new Exception("No element found");
        }
        int newQuantity = basketProduct.getQuantity() + 1;
        basketProduct.setQuantity(newQuantity);
        basketProductRepository.save(basketProduct);
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
                Double productPriceAfterDiscount = getProductPriceAfterDiscount(productSummary, product);
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

    private Double getProductPriceAfterDiscount(double productSummary, ProductDTO product) {
        SalesDTO salesDTO = salesRepository.retrieveSaleAsDTOByProductId(product.id);
        if (salesDTO != null) {
            double discount = salesDTO.discount;
            return productSummary * (1 - discount);
        }
        return null;
    }


    public void purchaseAllProductsFromBasket(String userToken) {
        Basket basket = getUserBasketByUserToken(userToken);
        BasketHistory basketHistory = basketHistoryRepository.findByClosedFalse();
        List<SalesDTO> salesDTO = salesRepository.retrieveSalesAsDTO();
        if (basketHistory == null) {
            int newBasketHistoryId = getIdForNewBasketHistory();
            basketHistory = new BasketHistory(newBasketHistoryId, false, false, basket);
            basketHistoryRepository.save(basketHistory);
        }

        List<BasketProductDTO> basketProductDTOS = basketProductRepository.retrieveBasketProductAsDTObyBasketIdAndNotClosedYet(basket.id);
        Date purchaseDateTime = new Date();
        for (BasketProductDTO basketProductDTO : basketProductDTOS) {
            Optional<BasketProduct> optionalBasketProduct = basketProductRepository.findById(basketProductDTO.getId());
            if (optionalBasketProduct.isPresent()) {
                BasketProduct basketProduct = optionalBasketProduct.get();
                basketProduct = prepareBasketProductToSave(basketHistory, salesDTO, purchaseDateTime, basketProduct);
                basketProductRepository.save(basketProduct);
            }
        }

        basketHistory.setPurchased(true);
        basketHistory.setClosed(true);
        basketHistoryRepository.save(basketHistory);
    }

    private BasketProduct prepareBasketProductToSave(BasketHistory basketHistory, List<SalesDTO> salesDTO, Date purchaseDateTime, BasketProduct basketProduct) {
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
        return basketProduct;
    }


    private int getIdForNewBasketHistory() {
        BasketHistory BasketHistory = basketHistoryRepository.findFirstByOrderByIdDesc();
        if (BasketHistory == null) {
            return 1;
        }
        int lastId = BasketHistory.getId();
        return ++lastId;
    }

}
