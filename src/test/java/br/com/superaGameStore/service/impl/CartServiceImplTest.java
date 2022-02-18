package br.com.superaGameStore.service.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.superaGameStore.controller.exception.MethodNotAllowedException;
import br.com.superaGameStore.controller.exception.RecordNotFoundException;
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

        Iterable<Cart> carts = cartService.getAllCarts();
        List<Cart> list = (List<Cart>) carts;

        assertEquals(2, list.size());

        assertEquals(1L, list.get(0).getId());
        assertEquals(2L, list.get(1).getId());

        assertEquals("OPEN", list.get(0).getStatus());
        assertEquals("OPEN", list.get(1).getStatus());

        assertTrue(list.get(0).getCartProducts().isEmpty());
        assertTrue(list.get(1).getCartProducts().isEmpty());
    }

    @Test
    void shouldNotReturnAnyCart() {

        Iterable<Cart> carts = cartService.getAllCarts();
        List<Cart> list = (List<Cart>) carts;

        assertEquals(0, list.size());
    }

    @Test
    void shouldReturnCartProductsByPrice() {

        Product product1 = new Product();
        product1.setPrice(BigDecimal.valueOf(2));
        product1.setName(".");
        product1.setImage(".");
        Product product2 = new Product();
        product2.setPrice(BigDecimal.valueOf(3));
        product2.setName(".");
        product2.setImage(".");
        Product product3 = new Product();
        product3.setPrice(BigDecimal.valueOf(1));
        product3.setName(".");
        product3.setImage(".");

        product1 = productService.addProduct(product1);
        product2 = productService.addProduct(product2);
        product3 = productService.addProduct(product3);

        long cartId = cartService.createCart().getId();

        cartService.addProduct(cartId, product1.getId(), 1);
        cartService.addProduct(cartId, product2.getId(), 1);
        cartService.addProduct(cartId, product3.getId(), 1);

        List<CartProduct> cartProducts = cartService.getCartProductsByPrice(cartId).getCartProducts();

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
    void shouldReturnCartProductsByName() {

        Product product1 = new Product();
        product1.setName("B");
        product1.setImage(".");
        Product product2 = new Product();
        product2.setName("C");
        product2.setImage(".");
        Product product3 = new Product();
        product3.setName("A");
        product3.setImage(".");

        product1 = productService.addProduct(product1);
        product2 = productService.addProduct(product2);
        product3 = productService.addProduct(product3);

        long cartId = cartService.createCart().getId();

        cartService.addProduct(cartId, product1.getId(), 1);
        cartService.addProduct(cartId, product2.getId(), 1);
        cartService.addProduct(cartId, product3.getId(), 1);

        List<CartProduct> cartProducts = cartService.getCartProductsByName(cartId).getCartProducts();

        assertEquals("A", cartProducts.get(0).getProduct().getName());
        assertEquals("B", cartProducts.get(1).getProduct().getName());
        assertEquals("C", cartProducts.get(2).getProduct().getName());
    }

    @Test
    void shouldReturnCartProductsByScore() {

        Product product1 = new Product();
        product1.setScore((short) 2);
        product1.setName(".");
        product1.setImage(".");
        Product product2 = new Product();
        product2.setScore((short) 1);
        product2.setName(".");
        product2.setImage(".");
        Product product3 = new Product();
        product3.setScore((short) 3);
        product3.setName(".");
        product3.setImage(".");

        product1 = productService.addProduct(product1);
        product2 = productService.addProduct(product2);
        product3 = productService.addProduct(product3);

        long cartId = cartService.createCart().getId();

        cartService.addProduct(cartId, product1.getId(), 1);
        cartService.addProduct(cartId, product2.getId(), 1);
        cartService.addProduct(cartId, product3.getId(), 1);

        List<CartProduct> cartProducts = cartService.getCartProductsByScore(cartId).getCartProducts();

        assertEquals((short) 3, cartProducts.get(0).getProduct().getScore());
        assertEquals((short) 2, cartProducts.get(1).getProduct().getScore());
        assertEquals((short) 1, cartProducts.get(2).getProduct().getScore());
    }

    @Test
    void shouldAddProduct() {

        Product product = new Product();
        product.setName("Name");
        product.setImage("Image");
        product.setScore((short) 1);
        product.setPrice(BigDecimal.valueOf(1));

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
    void shouldIncreaseProductQuantity() {

        Product product = new Product();
        product.setName(".");
        product.setImage(".");

        product = productService.addProduct(product);

        long cartId = cartService.createCart().getId();

        cartService.addProduct(cartId, product.getId(), 1);
        cartService.addProduct(cartId, product.getId(), 1);

        List<CartProduct> cartProducts = cartRepository.findById(cartId).get().getCartProducts();

        assertEquals(1L, cartProducts.get(0).getProduct().getId());
        assertEquals(2, cartProducts.get(0).getQuantity());
    }

    @Test
    void shouldNotAddProduct() {

        Product product = new Product();
        product.setName(".");
        product.setImage(".");

        product = productService.addProduct(product);

        long cartId = cartService.createCart().getId();
        cartService.cartCheckOut(cartId);

        MethodNotAllowedException thrown = assertThrows(
                MethodNotAllowedException.class,
                () -> {
                    cartService.addProduct(cartId, 1L, 1);
                });

        assertEquals("the cart " + cartId + " is closed", thrown.getMessage());
    }

    @Test
    void shouldRemoveProduct() {

        Product product = new Product();
        product.setName(".");
        product.setImage(".");

        product = productService.addProduct(product);

        long cartId = cartService.createCart().getId();
        cartService.addProduct(cartId, product.getId(), 1);

        assertEquals(1, cartRepository.findById(cartId).get().getCartProducts().size());

        cartService.removeProduct(cartId, product.getId(), 1);

        assertEquals(0, cartRepository.findById(cartId).get().getCartProducts().size());
    }

    @Test
    void shouldDecreaseProductQuantity() {

        Product product = new Product();
        product.setName(".");
        product.setImage(".");

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
    void shouldNotRemoveProduct() {

        Product product = new Product();
        product.setName(".");
        product.setImage(".");

        product = productService.addProduct(product);

        long cartId = cartService.createCart().getId();
        cartService.cartCheckOut(cartId);

        MethodNotAllowedException thrown = assertThrows(
                MethodNotAllowedException.class,
                () -> {
                    cartService.removeProduct(cartId, 1L, 1);
                });

        assertEquals("the cart " + cartId + " is closed", thrown.getMessage());
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
    void shouldNotCheckOutCart() {

        long cartId = cartService.createCart().getId();
        cartService.cartCheckOut(cartId);

        MethodNotAllowedException thrown = assertThrows(
                MethodNotAllowedException.class,
                () -> {
                    cartService.cartCheckOut(cartId);
                });

        assertEquals("the cart " + cartId + " it's already closed", thrown.getMessage());
    }

    @Test
    void shouldDeleteCart() {

        long cartId = cartService.createCart().getId();
        Iterable<Cart> carts = cartService.getAllCarts();
        List<Cart> list = (List<Cart>) carts;

        assertEquals(1, list.size());

        cartService.deleteCart(cartId);

        list = (List<Cart>) cartService.getAllCarts();
        assertEquals(0, list.size());
    }

    @Test
    void shouldThrowCartNotFound() {

        RecordNotFoundException thrown = assertThrows(
                RecordNotFoundException.class,
                () -> {
                    cartService.cartNotFound(1L);
                });

        assertEquals("no cart with the ID: " + 1L + "", thrown.getMessage());
    }

    @Test
    void shouldNotThrowCartNotFound() {

        cartService.createCart();
        assertDoesNotThrow(
                () -> {
                    cartService.cartNotFound(1L);
                },
                "no cart with the ID: " + 1L + "");
    }
}