package br.com.superaGameStore.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.superaGameStore.model.Cart;
import br.com.superaGameStore.model.CartProduct;
import br.com.superaGameStore.model.CartProductKey;
import br.com.superaGameStore.model.Product;
import br.com.superaGameStore.repository.CartProductRepository;
import br.com.superaGameStore.service.CartProductService;
import br.com.superaGameStore.service.CartService;
import br.com.superaGameStore.service.ProductService;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class CartProductServiceImplTest {

    @Autowired
    private CartProductService cartProductService;

    @Autowired
    private CartProductRepository cartProductRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Test
    void shouldReturnCartProduct() {

        Cart cart = cartService.createCart();

        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(1.00));
        product.setName(".");
        product.setImage(".");
        product = productService.addProduct(product);

        CartProductKey cpk = new CartProductKey(cart, product);
        CartProduct expected = new CartProduct(cpk, 1);
        cartProductRepository.save(expected);

        CartProduct actual = cartProductService.getCartProduct(cpk).get();

        assertEquals(expected, actual);
        assertEquals(expected.getProduct(), actual.getProduct());
        assertEquals(expected.getQuantity(), actual.getQuantity());
        assertEquals(expected.getCpk(), actual.getCpk());
        assertEquals(expected.getTotalPrice(), actual.getTotalPrice());
    }

    @Test
    void shouldNotReturnCartProduct() {

        Cart cart = cartService.createCart();

        Product product = new Product();
        product.setName(".");
        product.setImage(".");
        product = productService.addProduct(product);

        CartProductKey cpk = new CartProductKey(cart, product);

        assertTrue(cartProductService.getCartProduct(cpk).isEmpty());
    }

    @Test
    void shouldAddCartProduct() {

        Cart cart = cartService.createCart();

        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(1.00));
        product.setName(".");
        product.setImage(".");
        product = productService.addProduct(product);

        CartProductKey cpk = new CartProductKey(cart, product);
        CartProduct expected = new CartProduct(cpk, 1);

        cartProductService.addCartProduct(expected);

        CartProduct actual = cartProductRepository.findById(cpk).get();

        assertEquals(expected, actual);
        assertEquals(expected.getProduct(), actual.getProduct());
        assertEquals(expected.getQuantity(), actual.getQuantity());
        assertEquals(expected.getCpk(), actual.getCpk());
        assertEquals(expected.getTotalPrice(), actual.getTotalPrice());
    }

    @Test
    void shouldDeleteCartProduct() {

        Cart cart = cartService.createCart();

        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(1.00));
        product.setName(".");
        product.setImage(".");
        product = productService.addProduct(product);

        CartProductKey cpk = new CartProductKey(cart, product);
        CartProduct cartProduct = new CartProduct(cpk, 1);
        cartProductRepository.save(cartProduct);
        assertTrue(cartProductRepository.findById(cpk).isPresent());

        cartProductService.deleteCartProduct(cpk);

        assertTrue(cartProductRepository.findById(cpk).isEmpty());
    }
}
