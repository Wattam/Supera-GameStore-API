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
    private MockMvc mockMvc;

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
    void shouldIndexProducts() throws Exception {

        Product product1 = createProduct("Name1", "Image1", 1, 1);
        Product product2 = createProduct("Name2", "Image2", 2, 2);
        List<Product> products = ImmutableList
                .<Product>builder()
                .add(product1)
                .add(product2)
                .build();

        when(productService.index()).thenReturn(products);

        mockMvc.perform(get("/products").accept(APPLICATION_JSON))
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
    void shouldNotIndexProducts() throws Exception {

        when(productService.index()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/products").accept(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RecordNotFoundException))
                .andExpect(result -> assertEquals("no product found", result.getResolvedException().getMessage()));
    }

    @Test
    void shouldShowProduct() throws Exception {

        Product product = createProduct("Name", "Image", 1, 1);
        product.setId(1L);

        when(productService.show(1L)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/products/1").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Name")))
                .andExpect(jsonPath("$.image", is("Image")))
                .andExpect(jsonPath("$.score", is(1)))
                .andExpect(jsonPath("$.price", is(1.00)));
    }

    @Test
    void shouldNotShowProduct() throws Exception {

        mockMvc.perform(get("/products/1").accept(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RecordNotFoundException))
                .andExpect(result -> assertEquals("no product with the ID: 1",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void shouldStoreProduct() throws Exception {

        String json = "{\"name\": \"Name\",\"image\": \"Image\",\"score\": 1, \"price\": 1}";

        mockMvc.perform(post("/products").accept(APPLICATION_JSON).contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldUpdateProduct() throws Exception {

        String json = "{\"name\": \"Name\",\"image\": \"Image\",\"score\": 1, \"price\": 1}";

        Product product = new Product();

        when(productService.show(1L)).thenReturn(Optional.of(product));

        mockMvc.perform(put("/products/1").accept(APPLICATION_JSON).contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotUpdateProduct() throws Exception {

        String json = "{\"name\": \"Name\",\"image\": \"Image\",\"score\": 1, \"price\": 1}";

        mockMvc.perform(put("/products/1").accept(APPLICATION_JSON).contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RecordNotFoundException))
                .andExpect(result -> assertEquals("no product with the ID: 1",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void shouldDeleteProduct() throws Exception {

        Product product = new Product();

        when(productService.show(1L)).thenReturn(Optional.of(product));

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