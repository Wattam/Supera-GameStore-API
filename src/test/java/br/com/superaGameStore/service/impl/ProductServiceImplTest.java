package br.com.superaGameStore.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import br.com.superaGameStore.model.Product;
import br.com.superaGameStore.repository.ProductRepository;
import br.com.superaGameStore.service.ProductService;

@SpringBootTest
public class ProductServiceImplTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    private Product createProduct(long id, String name, String image, int score, double price) {

        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setImage(image);
        product.setScore((short) score);
        product.setPrice(BigDecimal.valueOf(price).setScale(2, RoundingMode.CEILING));
        return product;
    }

    @Test
    @DirtiesContext
    void shouldIndexProducts() {

        productRepository.save(createProduct(1L, "Name1", "Image1", 1, 1));
        productRepository.save(createProduct(2L, "Name2", "Image2", 2, 2));

        List<Product> products = productService.index();

        assertEquals(2, products.size());

        assertEquals(1L, products.get(0).getId());
        assertEquals("Name1", products.get(0).getName());
        assertEquals("Image1", products.get(0).getImage());
        assertEquals((short) 1, products.get(0).getScore());
        assertEquals(BigDecimal.valueOf(1).setScale(2, RoundingMode.CEILING),
                products.get(0).getPrice());

        assertEquals(2L, products.get(1).getId());
        assertEquals("Name2", products.get(1).getName());
        assertEquals("Image2", products.get(1).getImage());
        assertEquals((short) 2, products.get(1).getScore());
        assertEquals(BigDecimal.valueOf(2).setScale(2, RoundingMode.CEILING),
                products.get(1).getPrice());
    }

    @Test
    void shouldNotIndexProducts() {

        List<Product> products = productService.index();

        assertEquals(0, products.size());
    }

    @Test
    @DirtiesContext
    void shouldShowProduct() {

        Product expected = productRepository.save(createProduct(1L, "Name", "Image", 1, 1));

        Product actual = productService.show(1L).get();

        assertEquals(expected, actual);
        assertEquals(1L, actual.getId());
        assertEquals("Name", actual.getName());
        assertEquals("Image", actual.getImage());
        assertEquals((short) 1, actual.getScore());
        assertEquals(BigDecimal.valueOf(1).setScale(2, RoundingMode.CEILING), actual.getPrice());
    }

    @Test
    @DirtiesContext
    void shouldNotShowProduct() {

        assertTrue(productService.show(1L).isEmpty());
    }

    @Test
    @DirtiesContext
    void shouldStoreProduct() {

        Product expected = new Product();
        expected.setName("Name");
        expected.setImage("Image");
        expected.setScore((short) 1);
        expected.setPrice(BigDecimal.valueOf(1).setScale(2, RoundingMode.CEILING));

        Product addedProduct = productService.store(expected);
        assertNotNull(addedProduct);

        Product actual = productRepository.findById(addedProduct.getId()).get();

        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getImage(), actual.getImage());
        assertEquals(expected.getScore(), actual.getScore());
        assertEquals(expected.getPrice(), actual.getPrice());
    }

    @Test
    @DirtiesContext
    void shouldUpdateProduct() {

        Product product = new Product();
        product.setName("Name");
        product.setImage("Image");
        product.setScore((short) 1);
        product.setPrice(BigDecimal.valueOf(1).setScale(2, RoundingMode.CEILING));
        product = productRepository.save(product);

        Product expected = new Product();
        expected.setName("NameUpdated");
        expected.setImage("ImageUpdated");
        expected.setScore((short) 2);
        expected.setPrice(BigDecimal.valueOf(2).setScale(2, RoundingMode.CEILING));

        Product updatedProduct = productService.update(expected, product.getId());
        assertNotNull(updatedProduct);

        Product actual = productRepository.findById(updatedProduct.getId()).get();

        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getImage(), actual.getImage());
        assertEquals(expected.getScore(), actual.getScore());
        assertEquals(expected.getPrice(), actual.getPrice());
    }

    @Test
    @DirtiesContext
    void shouldDeleteProduct() {

        productRepository.save(createProduct(1L, "Name", "Image", 1, 1));
        assertNotNull(productService.show(1L).get());

        productService.delete(1L);

        assertTrue(productService.show(1L).isEmpty());
    }
}