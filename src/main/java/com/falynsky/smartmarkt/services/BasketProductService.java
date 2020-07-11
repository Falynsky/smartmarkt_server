package com.falynsky.smartmarkt.services;

import com.falynsky.smartmarkt.JWT.JwtTokenUtil;
import com.falynsky.smartmarkt.models.*;
import com.falynsky.smartmarkt.models.DTO.BasketProductDTO;
import com.falynsky.smartmarkt.models.DTO.ProductDTO;
import com.falynsky.smartmarkt.repositories.*;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    public BasketProduct getOrCreateBasketProduct(Map<String, Object> map, String userToken) throws Exception {
        Integer productId = (Integer) map.get("productId");
        Integer quantity = getQuantity(map);
        Basket basket = getUserBasketByUserToken(userToken);
        Product product = getSelectedProduct(productId);
        return updateOrCreateBasketProduct(map, quantity, basket, product);
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
        if (optionalAccount.isEmpty()) {
            throw new Exception("ACCOUNT NOT FOUND");
        }
        return optionalAccount.get();
    }

    private User getCurrentUserByAccount(Account account) throws Exception {
        Optional<User> optionalUser = userRepository.findFirstByAccountId(account);
        if (optionalUser.isEmpty()) {
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
        if (product.isEmpty()) {
            throw new Exception("PRODUCT NOT FOUND");
        }
        return product.get();
    }

    private BasketProduct updateOrCreateBasketProduct(Map<String, Object> map,
                                                      Integer quantity,
                                                      Basket basket,
                                                      Product product) {
        Optional<BasketProduct> optionalBasketProduct = basketProductRepository.findFirstByProductIdAndBasketId(product, basket);
        BasketProduct basketProduct;
        if (optionalBasketProduct.isPresent()) {
            basketProduct = optionalBasketProduct.get();
            Integer oldQuantity = basketProduct.getQuantity() + quantity;
            basketProduct.setQuantity(oldQuantity);
        } else {
            basketProduct = createAndAddBasketProduct(map, basket);
        }
        return basketProduct;
    }

    public BasketProduct createAndAddBasketProduct(Map<String, Object> map, Basket basket) {
        BasketProduct basketProduct = new BasketProduct();
        Integer id = getIdForNewBasketProduct(basketProductRepository);
        basketProduct.setId(id);
        Integer quantity = getQuantity(map);
        basketProduct.setQuantity(quantity);
        Product product = getProduct(map);
        basketProduct.setProductId(product);
        basketProduct.setBasketId(basket);
        basketProduct.setQuantityType("szt.");

        return basketProduct;
    }

    private Integer getIdForNewBasketProduct(BasketProductRepository basketProductRepository) {
        BasketProduct lastBasketProduct = basketProductRepository.findFirstByOrderByIdDesc();
        if (lastBasketProduct == null) {
            return 1;
        }
        Integer lastId = lastBasketProduct.getId();
        return ++lastId;
    }

    @SneakyThrows
    private Integer getQuantity(Map<String, Object> map) {
        Object quantityValue = map.get("quantity");
        if (quantityValue instanceof Integer) {
            return (Integer) quantityValue;
        } else {
            throw new Exception("NO QUANTITY");
        }
    }

    private Product getProduct(Map<String, Object> map) {
        Integer productId = (Integer) map.get("productId");
        return productRepository.getOne(productId);
    }

    public List<Map<String, Object>> getSelectedBasketProductsData(Basket basket) {

        List<BasketProductDTO> basketProductDTOS =
                basketProductRepository.retrieveBasketProductAsDTObyBasketId(basket.getId());

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

}
