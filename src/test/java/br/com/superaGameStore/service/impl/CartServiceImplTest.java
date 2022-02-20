package br.com.superaGameStore.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.superaGameStore.model.Cart;
import br.com.superaGameStore.model.CartProduct;
import br.com.superaGameStore.model.Product;
import br.com.superaGameStore.repository.CartRepository;
import br.com.superaGameStore.service.CartService;
import br.com.superaGameStore.service.ProductService;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class CartServiceImplTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    private Product createProduct(String name, String image, int score, double price) {

        Product product = new Product();
        product.setName(name);
        product.setImage(image);
        product.setScore((short) score);
        product.setPrice(BigDecimal.valueOf(price).setScale(2, RoundingMode.CEILING));
        return product;
    }

    @Test
    void shouldCreateCart() {

        Cart cartFromService = cartService.createCart();
        Cart cartFromRepository = cartRepository.findById(cartFromService.getId()).get();

        assertEquals(cartFromService.getId(), cartFromRepository.getId());
        assertEquals(cartFromService.getId(), 1L);
        assertEquals(cartFromService.getStatus(), cartFromRepository.getStatus());
        assertEquals(cartFromService.getStatus(), "OPEN");
        assertTrue(cartFromService.getCartProducts().isEmpty());
        assertTrue(cartFromService.getCartProducts().size() == 0);
    }

    @Test
    void shouldReturnAllCarts() {

        cartService.createCart();
        cartService.createCart();

        List<Cart> carts = cartService.getAllCarts();

        assertEquals(2, carts.size());

        assertEquals(1L, carts.get(0).getId());
        assertEquals(2L, carts.get(1).getId());

        assertEquals("OPEN", carts.get(0).getStatus());
        assertEquals("OPEN", carts.get(1).getStatus());

        assertTrue(carts.get(0).getCartProducts().isEmpty());
        assertTrue(carts.get(1).getCartProducts().isEmpty());
    }

    @Test
    void shouldNotReturnAnyCart() {

        List<Cart> carts = cartService.getAllCarts();

        assertEquals(0, carts.size());
    }

    @Test
    @DirtiesContext
    void shouldReturnCart() {

        Cart expected = new Cart();
        expected.setStatus("OPEN");
        expected.setCartProducts(Collections.emptyList());
        expected = cartRepository.save(expected);

        Cart actual = cartService.getCart(expected.getId(), "").get();

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getStatus(), actual.getStatus());
        assertTrue(actual.getCartProducts().isEmpty());
    }

    @Test
    @DirtiesContext
    void shouldReturnCartWithProductsByPrice() {

        Product product1 = createProduct("Name", "Image", 1, 2.0);
        Product product2 = createProduct("Name", "Image", 1, 3.0);
        Product product3 = createProduct("Name", "Image", 1, 1.0);

        product1 = productService.addProduct(product1);
        product2 = productService.addProduct(product2);
        product3 = productService.addProduct(product3);

        long cartId = cartService.createCart().getId();

        cartService.addProduct(cartId, product1.getId(), 1);
        cartService.addProduct(cartId, product2.getId(), 1);
        cartService.addProduct(cartId, product3.getId(), 1);

        List<CartProduct> cartProducts = cartService.getCart(cartId, "price").get().getCartProducts();

        assertEquals(
                BigDecimal.valueOf(1.00).setScale(2, RoundingMode.CEILING),
                cartProducts.get(0).getProduct().getPrice());
        assertEquals(
                BigDecimal.valueOf(2.00).setScale(2, RoundingMode.CEILING),
                cartProducts.get(1).getProduct().getPrice());
        assertEquals(
                BigDecimal.valueOf(3.00).setScale(2, RoundingMode.CEILING),
                cartProducts.get(2).getProduct().getPrice());
    }

    @Test
    @DirtiesContext
    void shouldReturnCartWithProductsByName() {

        Product product1 = createProduct("B", "Image", 1, 1.0);
        Product product2 = createProduct("C", "Image", 1, 1.0);
        Product product3 = createProduct("A", "Image", 1, 1.0);

        product1 = productService.addProduct(product1);
        product2 = productService.addProduct(product2);
        product3 = productService.addProduct(product3);

        long cartId = cartService.createCart().getId();

        cartService.addProduct(cartId, product1.getId(), 1);
        cartService.addProduct(cartId, product2.getId(), 1);
        cartService.addProduct(cartId, product3.getId(), 1);

        List<CartProduct> cartProducts = cartService.getCart(cartId, "name").get().getCartProducts();

        assertEquals("A", cartProducts.get(0).getProduct().getName());
        assertEquals("B", cartProducts.get(1).getProduct().getName());
        assertEquals("C", cartProducts.get(2).getProduct().getName());
    }

    @Test
    @DirtiesContext
    void shouldReturnCartWithProductsByScore() {

        Product product1 = createProduct("Name", "Image", 2, 1.0);
        Product product2 = createProduct("Name", "Image", 1, 1.0);
        Product product3 = createProduct("Name", "Image", 3, 1.0);

        product1 = productService.addProduct(product1);
        product2 = productService.addProduct(product2);
        product3 = productService.addProduct(product3);

        long cartId = cartService.createCart().getId();

        cartService.addProduct(cartId, product1.getId(), 1);
        cartService.addProduct(cartId, product2.getId(), 1);
        cartService.addProduct(cartId, product3.getId(), 1);

        List<CartProduct> cartProducts = cartService.getCart(cartId, "score").get().getCartProducts();

        assertEquals((short) 3, cartProducts.get(0).getProduct().getScore());
        assertEquals((short) 2, cartProducts.get(1).getProduct().getScore());
        assertEquals((short) 1, cartProducts.get(2).getProduct().getScore());
    }

    @Test
    @DirtiesContext
    void shouldAddProduct() {

        Product product = createProduct("Name", "Image", 1, 1.0);

        product = productService.addProduct(product);

        long cartId = cartService.createCart().getId();

        cartService.addProduct(cartId, product.getId(), 1);

        List<CartProduct> cartProducts = cartRepository.findById(cartId).get().getCartProducts();

        assertEquals(1, cartProducts.get(0).getQuantity());
        assertEquals(1L, cartProducts.get(0).getProduct().getId());
        assertEquals("Name", cartProducts.get(0).getProduct().getName());
        assertEquals("Image", cartProducts.get(0).getProduct().getImage());
        assertEquals((short) 1, cartProducts.get(0).getProduct().getScore());
        assertEquals(
                BigDecimal.valueOf(1).setScale(2, RoundingMode.CEILING),
                cartProducts.get(0).getProduct().getPrice());
    }

    @Test
    @DirtiesContext
    void shouldIncreaseProductQuantity() {

        Product product = createProduct("Name", "Image", 1, 1.0);

        product = productService.addProduct(product);

        long cartId = cartService.createCart().getId();

        cartService.addProduct(cartId, product.getId(), 1);
        cartService.addProduct(cartId, product.getId(), 1);

        List<CartProduct> cartProducts = cartRepository.findById(cartId).get().getCartProducts();

        assertEquals(1L, cartProducts.get(0).getProduct().getId());
        assertEquals(2, cartProducts.get(0).getQuantity());
    }

    @Test
    @DirtiesContext
    void shouldRemoveProduct() {

        Product product = createProduct("Name", "Image", 1, 1.0);

        product = productService.addProduct(product);

        long cartId = cartService.createCart().getId();
        cartService.addProduct(cartId, product.getId(), 1);

        assertEquals(1, cartRepository.findById(cartId).get().getCartProducts().size());

        cartService.removeProduct(cartId, product.getId(), 1);

        assertEquals(0, cartRepository.findById(cartId).get().getCartProducts().size());
    }

    @Test
    @DirtiesContext
    void shouldDecreaseProductQuantity() {

        Product product = createProduct("Name", "Image", 1, 1.0);

        product = productService.addProduct(product);

        long cartId = cartService.createCart().getId();
        cartService.addProduct(cartId, product.getId(), 5);

        assertEquals(product.getId(),
                cartRepository.findById(cartId).get().getCartProducts().get(0).getProduct().getId());
        assertEquals(5, cartRepository.findById(cartId).get().getCartProducts().get(0).getQuantity());

        cartService.removeProduct(cartId, product.getId(), 1);

        assertEquals(4, cartRepository.findById(cartId).get().getCartProducts().get(0).getQuantity());
    }

    @Test
    void shouldCheckOutCart() {

        long cartId = cartService.createCart().getId();
        assertEquals("OPEN", cartRepository.findById(cartId).get().getStatus());

        Cart closedCart = cartService.cartCheckOut(cartId);

        assertEquals("CLOSED", closedCart.getStatus());
        assertEquals("CLOSED", cartRepository.findById(cartId).get().getStatus());
    }

    @Test
    void shouldDeleteCart() {

        long cartId = cartService.createCart().getId();
        List<Cart> carts = cartService.getAllCarts();

        assertEquals(1, carts.size());

        cartService.deleteCart(cartId);

        carts = cartService.getAllCarts();
        assertEquals(0, carts.size());
    }
}