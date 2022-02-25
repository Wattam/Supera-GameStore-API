package br.com.superaGameStore.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

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
    void shouldReturnCartProductsByCartIdSortedByName() {

        Cart cart = cartService.store();

        Product product = createProduct("B", "A", 1, 1);
        productService.store(product);
        CartProductKey cpk = new CartProductKey(cart, product);
        cartProductRepository.save(new CartProduct(cpk, 1));

        product = createProduct("C", "A", 1, 1);
        productService.store(product);
        cpk = new CartProductKey(cart, product);
        cartProductRepository.save(new CartProduct(cpk, 1));

        product = createProduct("A", "A", 1, 1);
        productService.store(product);
        cpk = new CartProductKey(cart, product);
        cartProductRepository.save(new CartProduct(cpk, 1));

        List<CartProduct> cartProducts = cartProductService.getCartProductsByCartIdSorted(1L, "name");

        assertEquals("A", cartProducts.get(0).getProduct().getName());
        assertEquals("B", cartProducts.get(1).getProduct().getName());
        assertEquals("C", cartProducts.get(2).getProduct().getName());
    }

    @Test
    @DirtiesContext
    void shouldReturnCartProductsByCartIdSortedByPrice() {

        Cart cart = cartService.store();

        Product product = createProduct("A", "A", 1, 2);
        productService.store(product);
        CartProductKey cpk = new CartProductKey(cart, product);
        cartProductRepository.save(new CartProduct(cpk, 1));

        product = createProduct("A", "A", 1, 3);
        productService.store(product);
        cpk = new CartProductKey(cart, product);
        cartProductRepository.save(new CartProduct(cpk, 1));

        product = createProduct("A", "A", 1, 1);
        productService.store(product);
        cpk = new CartProductKey(cart, product);
        cartProductRepository.save(new CartProduct(cpk, 1));

        List<CartProduct> cartProducts = cartProductService.getCartProductsByCartIdSorted(1L, "price");

        assertEquals(BigDecimal.valueOf(1).setScale(2, RoundingMode.CEILING),
                cartProducts.get(0).getProduct().getPrice());
        assertEquals(BigDecimal.valueOf(2).setScale(2, RoundingMode.CEILING),
                cartProducts.get(1).getProduct().getPrice());
        assertEquals(BigDecimal.valueOf(3).setScale(2, RoundingMode.CEILING),
                cartProducts.get(2).getProduct().getPrice());
    }

    @Test
    @DirtiesContext
    void shouldReturnCartProductsByCartIdSortedByScore() {

        Cart cart = cartService.store();

        Product product = createProduct("A", "A", 2, 1);
        productService.store(product);
        CartProductKey cpk = new CartProductKey(cart, product);
        cartProductRepository.save(new CartProduct(cpk, 1));

        product = createProduct("A", "A", 3, 1);
        productService.store(product);
        cpk = new CartProductKey(cart, product);
        cartProductRepository.save(new CartProduct(cpk, 1));

        product = createProduct("A", "A", 1, 1);
        productService.store(product);
        cpk = new CartProductKey(cart, product);
        cartProductRepository.save(new CartProduct(cpk, 1));

        List<CartProduct> cartProducts = cartProductService.getCartProductsByCartIdSorted(1L, "score");

        assertEquals((short) 1, cartProducts.get(0).getProduct().getScore());
        assertEquals((short) 2, cartProducts.get(1).getProduct().getScore());
        assertEquals((short) 3, cartProducts.get(2).getProduct().getScore());
    }

    @Test
    @DirtiesContext
    void shouldNotReturnCartProductsByCartId() {

        List<CartProduct> cartProducts = cartProductService.getCartProductsByCartIdSorted(1L, "");

        assertTrue(cartProducts.isEmpty());
        assertEquals(0, cartProducts.size());
    }

    @Test
    @DirtiesContext
    void shouldShowCartProduct() {

        Cart cart = cartService.store();

        Product product = createProduct("Name", "Image", 1, 1);
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

        Product product = createProduct("Name", "Image", 1, 1);
        product = productService.store(product);

        CartProductKey cpk = new CartProductKey(cart, product);

        assertTrue(cartProductService.show(cpk).isEmpty());
    }

    @Test
    @DirtiesContext
    void shouldStoreCartProduct() {

        Cart cart = cartService.store();

        Product product = createProduct("Name", "Image", 1, 1);
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

        Product product = createProduct("Name", "Image", 1, 1);
        product = productService.store(product);

        CartProductKey cpk = new CartProductKey(cart, product);
        CartProduct cartProduct = new CartProduct(cpk, 1);
        cartProductRepository.save(cartProduct);
        assertTrue(cartProductRepository.findById(cpk).isPresent());

        cartProductService.delete(cpk);

        assertTrue(cartProductRepository.findById(cpk).isEmpty());
    }
}
