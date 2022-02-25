package br.com.superaGameStore.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import br.com.superaGameStore.model.Cart;
import br.com.superaGameStore.model.CartProduct;
import br.com.superaGameStore.model.CartProductKey;
import br.com.superaGameStore.model.Product;
import br.com.superaGameStore.repository.CartProductRepository;
import br.com.superaGameStore.service.CartProductService;
import br.com.superaGameStore.service.CartService;
import br.com.superaGameStore.service.ProductService;

@SpringBootTest
public class CartProductServiceImplTest {

    @Autowired
    private CartProductService cartProductService;

    @Autowired
    private CartProductRepository cartProductRepository;

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
    @DirtiesContext
    void shouldShowCartProduct() {

        Cart cart = cartService.store();

        Product product = createProduct("Name", "Image", 1, 1.0);
        product = productService.store(product);

        CartProductKey cpk = new CartProductKey(cart, product);
        CartProduct expected = new CartProduct(cpk, 1);
        cartProductRepository.save(expected);

        CartProduct actual = cartProductService.show(cpk).get();

        assertEquals(expected, actual);
        assertEquals(expected.getProduct(), actual.getProduct());
        assertEquals(expected.getQuantity(), actual.getQuantity());
        assertEquals(expected.getCpk(), actual.getCpk());
        assertEquals(expected.getTotalPrice(), actual.getTotalPrice());
    }

    @Test
    @DirtiesContext
    void shouldNotShowCartProduct() {

        Cart cart = cartService.store();

        Product product = createProduct("Name", "Image", 1, 1.0);
        product = productService.store(product);

        CartProductKey cpk = new CartProductKey(cart, product);

        assertTrue(cartProductService.show(cpk).isEmpty());
    }

    @Test
    @DirtiesContext
    void shouldStoreCartProduct() {

        Cart cart = cartService.store();

        Product product = createProduct("Name", "Image", 1, 1.0);
        product = productService.store(product);

        CartProductKey cpk = new CartProductKey(cart, product);
        CartProduct expected = new CartProduct(cpk, 1);

        cartProductService.store(expected);

        CartProduct actual = cartProductRepository.findById(cpk).get();

        assertEquals(expected, actual);
        assertEquals(expected.getProduct(), actual.getProduct());
        assertEquals(expected.getQuantity(), actual.getQuantity());
        assertEquals(expected.getCpk(), actual.getCpk());
        assertEquals(expected.getTotalPrice(), actual.getTotalPrice());
    }

    @Test
    @DirtiesContext
    void shouldDeleteCartProduct() {

        Cart cart = cartService.store();

        Product product = createProduct("Name", "Image", 1, 1.0);
        product = productService.store(product);

        CartProductKey cpk = new CartProductKey(cart, product);
        CartProduct cartProduct = new CartProduct(cpk, 1);
        cartProductRepository.save(cartProduct);
        assertTrue(cartProductRepository.findById(cpk).isPresent());

        cartProductService.delete(cpk);

        assertTrue(cartProductRepository.findById(cpk).isEmpty());
    }
}
