package br.com.superaGameStore.controller;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.google.common.collect.ImmutableList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import br.com.superaGameStore.controller.exception.RecordNotFoundException;
import br.com.superaGameStore.model.Product;
import br.com.superaGameStore.service.ProductService;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private Product createProduct(String name, String image, int score, double price) {

        Product product = new Product();
        product.setName(name);
        product.setImage(image);
        product.setScore((short) score);
        product.setPrice(BigDecimal.valueOf(price).setScale(2, RoundingMode.CEILING));
        return product;
    }

    @BeforeEach
    public void setUp() {
        Mockito.reset(productService);
    }

    @Test
    void shouldGetAllProducts() throws Exception {

        Product product1 = createProduct("Name1", "Image1", 1, 1);
        Product product2 = createProduct("Name2", "Image2", 2, 2);
        List<Product> products = ImmutableList
                .<Product>builder()
                .add(product1)
                .add(product2)
                .build();

        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/products/get").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(products.size()))
                .andExpect(jsonPath("$[0].name", is("Name1")))
                .andExpect(jsonPath("$[0].image", is("Image1")))
                .andExpect(jsonPath("$[0].score", is(1)))
                .andExpect(jsonPath("$[0].price", is(1.00)))
                .andExpect(jsonPath("$[1].name", is("Name2")))
                .andExpect(jsonPath("$[1].image", is("Image2")))
                .andExpect(jsonPath("$[1].score", is(2)))
                .andExpect(jsonPath("$[1].price", is(2.00)));
    }

    @Test
    void shouldNotGetAllProducts() throws Exception {

        when(productService.getAllProducts()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/products/get").accept(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RecordNotFoundException))
                .andExpect(result -> assertEquals("no product found", result.getResolvedException().getMessage()));
    }

    @Test
    void shouldGetProduct() throws Exception {

        Product product = createProduct("Name", "Image", 1, 1);
        product.setId(1L);

        when(productService.getProduct(1L)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/products/1").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Name")))
                .andExpect(jsonPath("$.image", is("Image")))
                .andExpect(jsonPath("$.score", is(1)))
                .andExpect(jsonPath("$.price", is(1.00)));
    }

    @Test
    void shouldNotGetProduct() throws Exception {

        when(productService.getAllProducts()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/products/1").accept(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RecordNotFoundException))
                .andExpect(result -> assertEquals("no product with the ID: 1",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void shouldPostProduct() throws Exception {

        String json = "{\"name\": \"Name\",\"image\": \"Image\",\"score\": 1, \"price\": 1}";

        mockMvc.perform(post("/products/post").accept(APPLICATION_JSON).contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldPutProduct() throws Exception {

        String json = "{\"id\": \"1\",\"name\": \"Name\",\"image\": \"Image\",\"score\": 1, \"price\": 1}";

        Product product = new Product();

        when(productService.getProduct(1L)).thenReturn(Optional.of(product));

        mockMvc.perform(put("/products/put").accept(APPLICATION_JSON).contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotPutProduct() throws Exception {

        String json = "{\"id\": \"1\",\"name\": \"Name\",\"image\": \"Image\",\"score\": 1, \"price\": 1}";

        mockMvc.perform(put("/products/put").accept(APPLICATION_JSON).contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RecordNotFoundException))
                .andExpect(result -> assertEquals("no product with the ID: 1",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void shouldDeleteProduct() throws Exception {

        Product product = new Product();

        when(productService.getProduct(1L)).thenReturn(Optional.of(product));

        mockMvc.perform(delete("/products/1").accept(APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldNotDeleteProduct() throws Exception {

        mockMvc.perform(delete("/products/1").accept(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RecordNotFoundException))
                .andExpect(result -> assertEquals("no product with the ID: 1",
                        result.getResolvedException().getMessage()));
    }
}