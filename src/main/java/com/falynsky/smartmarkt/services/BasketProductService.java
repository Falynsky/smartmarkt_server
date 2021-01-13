package com.falynsky.smartmarkt.services;

import com.falynsky.smartmarkt.JWT.JwtTokenUtil;
import com.falynsky.smartmarkt.exceptions.NotEnoughQuantity;
import com.falynsky.smartmarkt.models.*;
import com.falynsky.smartmarkt.models.DTO.BasketProductDTO;
import com.falynsky.smartmarkt.models.DTO.ProductDTO;
import com.falynsky.smartmarkt.repositories.*;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BasketProductService {

    BasketRepository basketRepository;
    BasketProductRepository basketProductRepository;
    ProductRepository productRepository;
    AccountRepository accountRepository;
    UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;

    BasketProductService(BasketProductRepository basketProductRepository,
                         ProductRepository productRepository,
                         BasketRepository basketRepository,
                         AccountRepository accountRepository,
                         UserRepository userRepository,
                         JwtTokenUtil jwtTokenUtil) {
        this.basketProductRepository = basketProductRepository;
        this.productRepository = productRepository;
        this.basketRepository = basketRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
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
    private String addProductToBasket(Map<String, Object> map, Product product, String userToken) throws Exception {
        int quantity = getQuantity(map);
        Basket basket = getUserBasketByUserToken(userToken);
        Optional<BasketProduct> optionalBasketProduct = basketProductRepository.findFirstByProductIdAndBasketId(product, basket);
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
    public Basket getUserBasketByUserToken(String userToken) throws Exception {
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
        Optional<BasketProduct> optionalBasketProduct = basketProductRepository.findFirstByProductIdAndBasketId(product, basket);
        return optionalBasketProduct.orElse(null);
    }

    public void removeProductFromBasket(Map<String, Object> map, String userToken) throws Exception {
        BasketProduct basketProduct = getBasketProduct(map, userToken);
        Integer productId = (Integer) map.get("productId");
        Product product = getSelectedProduct(productId);
        int basketProductQuantity = product.getQuantity() + basketProduct.getQuantity();
        product.setQuantity(basketProductQuantity);
        productRepository.save(product);
        basketProductRepository.delete(basketProduct);
    }

    public BasketProduct createAndAddBasketProduct(Map<String, Object> map, Basket basket) throws Exception {
        BasketProduct basketProduct = new BasketProduct();
        int id = getIdForNewBasketProduct(basketProductRepository);
        basketProduct.setId(id);
        Integer quantity = getQuantity(map);
        basketProduct.setQuantity(quantity);
        Product product = getProduct(map);
        basketProduct.setProductId(product);
        basketProduct.setBasketId(basket);
        basketProduct.setQuantityType("szt.");

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
    private Integer getQuantity(Map<String, Object> map) throws Exception {
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
            ProductDTO product = optionalProduct.get();
            int productId = product.getId();
            String name = product.getName();
            int quantity = basketProductDTO.getQuantity();
            Float price = product.getPrice();
            String currency = product.getCurrency();

            Map<String, Object> productData = new LinkedHashMap<>();
            productData.put("id", id);
            productData.put("productId", productId);
            productData.put("name", name);
            productData.put("quantity", quantity);
            productData.put("price", price);
            productData.put("currency", currency);
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
        float summaryPrice = 0;
        for (BasketProductDTO basketProductDTO : basketProductDTOS) {
            int basketProductDTOId = basketProductDTO.getProductId();
            Optional<ProductDTO> optionalProduct = productRepository.retrieveProductAsDTObyId(basketProductDTOId);
            if (optionalProduct.isPresent()) {
                ProductDTO product = optionalProduct.get();
                float price = product.getPrice();
                summaryPrice += price * basketProductDTO.getQuantity();
            }
        }
        data.put("summary", summaryPrice);
        return data;

    }


}
