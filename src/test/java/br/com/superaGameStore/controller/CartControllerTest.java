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
import java.util.ArrayList;
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

import br.com.superaGameStore.controller.exception.MethodNotAllowedException;
import br.com.superaGameStore.controller.exception.RecordNotFoundException;
import br.com.superaGameStore.model.Cart;
import br.com.superaGameStore.model.CartProduct;
import br.com.superaGameStore.model.Product;
import br.com.superaGameStore.service.CartService;
import br.com.superaGameStore.service.ProductService;

@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private CartService cartService;

        @MockBean
        private ProductService productService;

        private Cart createCart(long id) {

                Cart cart = new Cart();
                cart.setId(id);
                cart.setStatus("OPEN");
                List<CartProduct> cartProducts = new ArrayList<>();
                cart.setCartProducts(cartProducts);
                return cart;
        }

        public Product createProduct(String name, String image, int score, double price) {

                Product product = new Product();
                product.setName(name);
                product.setImage(image);
                product.setScore((short) score);
                product.setPrice(BigDecimal.valueOf(price).setScale(2, RoundingMode.CEILING));
                return product;
        }

        @BeforeEach
        public void setUp() {
                Mockito.reset(cartService);
        }

        @Test
        void shouldStoreCart() throws Exception {

                Cart cart = createCart(1L);

                when(cartService.store()).thenReturn(cart);

                mockMvc.perform(post("/carts").accept(APPLICATION_JSON))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id", is(1)))
                                .andExpect(jsonPath("$.status", is("OPEN")))
                                .andExpect(jsonPath("$.cartProducts", is(Collections.emptyList())))
                                .andExpect(jsonPath("$.totalPrice").value(0.00))
                                .andExpect(jsonPath("$.subTotalPrice").value(0.00))
                                .andExpect(jsonPath("$.shippingFee").value(0.00));
        }

        @Test
        void shouldIndexCarts() throws Exception {

                Cart cart1 = createCart(1L);
                Cart cart2 = createCart(2L);
                List<Cart> carts = ImmutableList
                                .<Cart>builder()
                                .add(cart1)
                                .add(cart2)
                                .build();

                when(cartService.index()).thenReturn(carts);

                mockMvc.perform(get("/carts").accept(APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("length()").value(carts.size()))
                                .andExpect(jsonPath("$[0].id", is(1)))
                                .andExpect(jsonPath("$[0].status", is("OPEN")))
                                .andExpect(jsonPath("$[0].cartProducts", is(Collections.emptyList())))
                                .andExpect(jsonPath("$[1].id", is(2)))
                                .andExpect(jsonPath("$[1].status", is("OPEN")))
                                .andExpect(jsonPath("$[1].cartProducts", is(Collections.emptyList())));
        }

        @Test
        void shouldNotIndexCarts() throws Exception {

                when((List<Cart>) cartService.index()).thenReturn(Collections.emptyList());

                mockMvc.perform(get("/carts").accept(APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(result -> assertTrue(
                                                result.getResolvedException() instanceof RecordNotFoundException))
                                .andExpect(result -> assertEquals("no cart found",
                                                result.getResolvedException().getMessage()));
        }

        @Test
        void shouldShowCart() throws Exception {

                Cart cart = createCart(1);

                when(cartService.show(1L, null)).thenReturn(Optional.of(cart));

                mockMvc.perform(get("/carts/1").accept(APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.cartProducts.length()").value(cart.getCartProducts().size()));
        }

        @Test
        void shouldNotShowCart() throws Exception {

                mockMvc.perform(get("/carts/1").accept(APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(result -> assertTrue(
                                                result.getResolvedException() instanceof RecordNotFoundException))
                                .andExpect(result -> assertEquals("no cart with the ID: 1",
                                                result.getResolvedException().getMessage()));
        }

        @Test
        void shouldAddProduct() throws Exception {

                Cart cart = createCart(1);

                Product product = createProduct("Name", "Image", 1, 1);

                when(cartService.show(1L, "")).thenReturn(Optional.of(cart));
                when(productService.show(1L)).thenReturn(Optional.of(product));
                when(cartService.addProduct(cart.getId(), product.getId(), 1)).thenReturn(cart);

                String json = "{\"cartId\": \"1\",\"productId\": \"1\",\"quantity\": 1}";

                mockMvc.perform(put("/carts/addProduct").accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                                .content(json))
                                .andExpect(status().isOk());
        }

        @Test
        void shouldNotAddProductCauseCartNotFound() throws Exception {

                String json = "{\"cartId\": \"1\",\"productId\": \"1\",\"quantity\": 1}";

                mockMvc.perform(put("/carts/addProduct").accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                                .content(json))
                                .andExpect(status().isNotFound())
                                .andExpect(result -> assertTrue(
                                                result.getResolvedException() instanceof RecordNotFoundException))
                                .andExpect(result -> assertEquals("no cart with the ID: 1",
                                                result.getResolvedException().getMessage()));
        }

        @Test
        void shouldNotAddProductCauseProductNotFound() throws Exception {

                Cart cart = createCart(1);

                when(cartService.show(1L, "")).thenReturn(Optional.of(cart));

                String json = "{\"cartId\": \"1\",\"productId\": \"1\",\"quantity\": 1}";

                mockMvc.perform(put("/carts/addProduct").accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                                .content(json))
                                .andExpect(status().isNotFound())
                                .andExpect(result -> assertTrue(
                                                result.getResolvedException() instanceof RecordNotFoundException))
                                .andExpect(result -> assertEquals("no product with the ID: 1",
                                                result.getResolvedException().getMessage()));
        }

        @Test
        void shouldNotAddProductCauseCartIsClosed() throws Exception {

                Cart cart = createCart(1);
                cart.setStatus("CLOSED");

                when(cartService.show(1L, "")).thenReturn(Optional.of(cart));

                String json = "{\"cartId\": \"1\",\"productId\": \"1\",\"quantity\": 1}";

                mockMvc.perform(put("/carts/addProduct").accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                                .content(json))
                                .andExpect(status().isMethodNotAllowed())
                                .andExpect(result -> assertTrue(
                                                result.getResolvedException() instanceof MethodNotAllowedException))
                                .andExpect(result -> assertEquals("the cart 1 is closed",
                                                result.getResolvedException().getMessage()));
        }

        @Test
        void shouldRemoveProduct() throws Exception {

                Cart cart = createCart(1);

                Product product = createProduct("Name", "Image", 1, 1);

                when(cartService.show(1L, "")).thenReturn(Optional.of(cart));
                when(productService.show(1L)).thenReturn(Optional.of(product));
                when(cartService.removeProduct(cart.getId(), product.getId(), 1)).thenReturn(cart);

                String json = "{\"cartId\": \"1\",\"productId\": \"1\",\"quantity\": 1}";

                mockMvc.perform(put("/carts/removeProduct").accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                                .content(json))
                                .andExpect(status().isOk());
        }

        @Test
        void shouldNotRemoveProductCauseCartNotFound() throws Exception {

                String json = "{\"cartId\": \"1\",\"productId\": \"1\",\"quantity\": 1}";

                mockMvc.perform(put("/carts/removeProduct").accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                                .content(json))
                                .andExpect(status().isNotFound())
                                .andExpect(result -> assertTrue(
                                                result.getResolvedException() instanceof RecordNotFoundException))
                                .andExpect(result -> assertEquals("no cart with the ID: 1",
                                                result.getResolvedException().getMessage()));
        }

        @Test
        void shouldNotRemoveProductCauseProductNotFound() throws Exception {

                Cart cart = createCart(1);

                when(cartService.show(1L, "")).thenReturn(Optional.of(cart));

                String json = "{\"cartId\": \"1\",\"productId\": \"1\",\"quantity\": 1}";

                mockMvc.perform(put("/carts/removeProduct").accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                                .content(json))
                                .andExpect(status().isNotFound())
                                .andExpect(result -> assertTrue(
                                                result.getResolvedException() instanceof RecordNotFoundException))
                                .andExpect(result -> assertEquals("no product with the ID: 1",
                                                result.getResolvedException().getMessage()));
        }

        @Test
        void shouldNotRemoveProductCauseCartIsClosed() throws Exception {

                Cart cart = createCart(1);
                cart.setStatus("CLOSED");

                when(cartService.show(1L, "")).thenReturn(Optional.of(cart));

                String json = "{\"cartId\": \"1\",\"productId\": \"1\",\"quantity\": 1}";

                mockMvc.perform(put("/carts/removeProduct").accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                                .content(json))
                                .andExpect(status().isMethodNotAllowed())
                                .andExpect(result -> assertTrue(
                                                result.getResolvedException() instanceof MethodNotAllowedException))
                                .andExpect(result -> assertEquals("the cart 1 is closed",
                                                result.getResolvedException().getMessage()));
        }

        @Test
        void shouldCheckoutCart() throws Exception {

                Cart cart = createCart(1);

                when(cartService.show(1L, "")).thenReturn(Optional.of(cart));
                when(cartService.checkout(1L)).thenReturn(cart);

                mockMvc.perform(put("/carts/checkout/1").accept(APPLICATION_JSON))
                                .andExpect(status().isOk());
        }

        @Test
        void shouldNotCheckoutCartCauseCartNotFound() throws Exception {

                mockMvc.perform(put("/carts/checkout/1").accept(APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(result -> assertTrue(
                                                result.getResolvedException() instanceof RecordNotFoundException))
                                .andExpect(result -> assertEquals("no cart with the ID: 1",
                                                result.getResolvedException().getMessage()));
        }

        @Test
        void shouldNotCheckoutCartCauseCartIsClosed() throws Exception {

                Cart cart = createCart(1);
                cart.setStatus("CLOSED");

                when(cartService.show(1L, "")).thenReturn(Optional.of(cart));

                mockMvc.perform(put("/carts/checkout/1").accept(APPLICATION_JSON))
                                .andExpect(status().isMethodNotAllowed())
                                .andExpect(result -> assertTrue(
                                                result.getResolvedException() instanceof MethodNotAllowedException))
                                .andExpect(result -> assertEquals("the cart 1 is closed",
                                                result.getResolvedException().getMessage()));
        }

        @Test
        void shouldDeleteCart() throws Exception {

                Cart cart = createCart(1);
                when(cartService.show(1L, "")).thenReturn(Optional.of(cart));

                mockMvc.perform(delete("/carts/1").accept(APPLICATION_JSON))
                                .andExpect(status().isNoContent());
        }

        @Test
        void shouldNotDeleteCart() throws Exception {

                mockMvc.perform(delete("/carts/1").accept(APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(result -> assertTrue(
                                                result.getResolvedException() instanceof RecordNotFoundException))
                                .andExpect(result -> assertEquals("no cart with the ID: 1",
                                                result.getResolvedException().getMessage()));
        }
}
