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

import br.com.superaGameStore.controller.exception.RecordNotFoundException;
import br.com.superaGameStore.model.Cart;
import br.com.superaGameStore.model.CartProduct;
import br.com.superaGameStore.model.CartProductKey;
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
    void shouldCreateCart() throws Exception {

        Cart cart = createCart(1L);

        when(cartService.createCart()).thenReturn(cart);

        mockMvc.perform(post("/carts/create").accept(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is("OPEN")))
                .andExpect(jsonPath("$.cartProducts", is(Collections.emptyList())))
                .andExpect(jsonPath("$.totalPrice", is(0)))
                .andExpect(jsonPath("$.subTotalPrice", is(0)))
                .andExpect(jsonPath("$.freight", is(0)));
    }

    @Test
    void shouldGetAllCarts() throws Exception {

        Cart cart1 = createCart(1L);
        Cart cart2 = createCart(2L);
        List<Cart> carts = ImmutableList
                .<Cart>builder()
                .add(cart1)
                .add(cart2)
                .build();

        when((List<Cart>) cartService.getAllCarts()).thenReturn(carts);

        mockMvc.perform(get("/carts/get").accept(APPLICATION_JSON))
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
    void shouldNotGetAllCarts() throws Exception {

        when((List<Cart>) cartService.getAllCarts()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/carts/get").accept(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof RecordNotFoundException))
                .andExpect(result -> assertEquals("no cart found",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void shouldGetCartByPrice() throws Exception {

        Cart cart = createCart(1);

        CartProduct cartProduct1 = new CartProduct(
                new CartProductKey(cart, createProduct("1", "1", 1, 1)),
                1);
        CartProduct cartProduct2 = new CartProduct(
                new CartProductKey(cart, createProduct("2", "2", 2, 2)),
                1);
        CartProduct cartProduct3 = new CartProduct(
                new CartProductKey(cart, createProduct("3", "3", 3, 3)),
                1);

        List<CartProduct> cartProducts = ImmutableList
                .<CartProduct>builder()
                .add(cartProduct1)
                .add(cartProduct2)
                .add(cartProduct3)
                .build();

        cart.setCartProducts(cartProducts);

        when(cartService.getCartProductsByPrice(1)).thenReturn(cart);

        mockMvc.perform(get("/carts/1/cartByPrice").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartProducts.length()").value(cart.getCartProducts().size()))
                .andExpect(jsonPath("$.cartProducts[:1].product.price").value(1.0))
                .andExpect(jsonPath("$.cartProducts[1:2].product.price").value(2.0))
                .andExpect(jsonPath("$.cartProducts[2:3].product.price").value(3.0));
    }

    @Test
    void shouldNotGetCartByPrice() throws Exception {

        Mockito.doThrow(new RecordNotFoundException("no cart found")).when(cartService).cartNotFound(1L);

        mockMvc.perform(get("/carts/1/cartByPrice").accept(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof RecordNotFoundException))
                .andExpect(result -> assertEquals("no cart found",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void shouldGetCartByName() throws Exception {

        Cart cart = createCart(1);

        CartProduct cartProduct1 = new CartProduct(
                new CartProductKey(cart, createProduct("1", "1", 1, 1)),
                1);
        CartProduct cartProduct2 = new CartProduct(
                new CartProductKey(cart, createProduct("2", "2", 2, 2)),
                1);
        CartProduct cartProduct3 = new CartProduct(
                new CartProductKey(cart, createProduct("3", "3", 3, 3)),
                1);

        List<CartProduct> cartProducts = ImmutableList
                .<CartProduct>builder()
                .add(cartProduct1)
                .add(cartProduct2)
                .add(cartProduct3)
                .build();

        cart.setCartProducts(cartProducts);

        when(cartService.getCartProductsByName(1)).thenReturn(cart);

        mockMvc.perform(get("/carts/1/cartByName").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartProducts.length()").value(cart.getCartProducts().size()))
                .andExpect(jsonPath("$.cartProducts[:1].product.name").value("1"))
                .andExpect(jsonPath("$.cartProducts[1:2].product.name").value("2"))
                .andExpect(jsonPath("$.cartProducts[2:3].product.name").value("3"));
    }

    @Test
    void shouldNotGetCartByName() throws Exception {

        Mockito.doThrow(new RecordNotFoundException("no cart found")).when(cartService).cartNotFound(1L);

        mockMvc.perform(get("/carts/1/cartByName").accept(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof RecordNotFoundException))
                .andExpect(result -> assertEquals("no cart found",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void shouldGetCartByScore() throws Exception {

        Cart cart = createCart(1);

        CartProduct cartProduct1 = new CartProduct(
                new CartProductKey(cart, createProduct("1", "1", 1, 1)),
                1);
        CartProduct cartProduct2 = new CartProduct(
                new CartProductKey(cart, createProduct("2", "2", 2, 2)),
                1);
        CartProduct cartProduct3 = new CartProduct(
                new CartProductKey(cart, createProduct("3", "3", 3, 3)),
                1);

        List<CartProduct> cartProducts = ImmutableList
                .<CartProduct>builder()
                .add(cartProduct1)
                .add(cartProduct2)
                .add(cartProduct3)
                .build();

        cart.setCartProducts(cartProducts);

        when(cartService.getCartProductsByScore(1)).thenReturn(cart);

        mockMvc.perform(get("/carts/1/cartByScore").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartProducts.length()").value(cart.getCartProducts().size()))
                .andExpect(jsonPath("$.cartProducts[:1].product.score").value(1))
                .andExpect(jsonPath("$.cartProducts[1:2].product.score").value(2))
                .andExpect(jsonPath("$.cartProducts[2:3].product.score").value(3));
    }

    @Test
    void shouldNotGetCartByScore() throws Exception {

        Mockito.doThrow(new RecordNotFoundException("no cart found")).when(cartService).cartNotFound(1L);

        mockMvc.perform(get("/carts/1/cartByScore").accept(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof RecordNotFoundException))
                .andExpect(result -> assertEquals("no cart found",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void shouldAddProduct() throws Exception {

        Cart cart = createCart(1);

        Product product = createProduct("Name", "Image", 1, 1);

        when(cartService.addProduct(cart.getId(), product.getId(), 1)).thenReturn(cart);
        when(productService.getProduct(1L)).thenReturn(Optional.of(product));

        String json = "{\"cartId\": \"1\",\"productId\": \"1\",\"quantity\": 1}";

        mockMvc.perform(put("/carts/add").accept(APPLICATION_JSON).contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotAddProductCauseCartNotFound() throws Exception {

        Mockito.doThrow(new RecordNotFoundException("no cart found")).when(cartService).cartNotFound(1L);

        String json = "{\"cartId\": \"1\",\"productId\": \"1\",\"quantity\": 1}";

        mockMvc.perform(put("/carts/add").accept(APPLICATION_JSON).contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof RecordNotFoundException))
                .andExpect(result -> assertEquals("no cart found",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void shouldNotAddProductCauseProductNotFound() throws Exception {

        String json = "{\"cartId\": \"1\",\"productId\": \"1\",\"quantity\": 1}";

        mockMvc.perform(put("/carts/add").accept(APPLICATION_JSON).contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof RecordNotFoundException))
                .andExpect(result -> assertEquals("no product with the ID: 1",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void shouldRemoveProduct() throws Exception {

        Cart cart = createCart(1);

        Product product = createProduct("Name", "Image", 1, 1);

        when(cartService.removeProduct(cart.getId(), product.getId(), 1)).thenReturn(cart);
        when(productService.getProduct(1L)).thenReturn(Optional.of(product));

        String json = "{\"cartId\": \"1\",\"productId\": \"1\",\"quantity\": 1}";

        mockMvc.perform(put("/carts/remove").accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotRemoveProductCauseCartNotFound() throws Exception {

        Mockito.doThrow(new RecordNotFoundException("no cart found")).when(cartService).cartNotFound(1L);

        String json = "{\"cartId\": \"1\",\"productId\": \"1\",\"quantity\": 1}";

        mockMvc.perform(put("/carts/remove").accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof RecordNotFoundException))
                .andExpect(result -> assertEquals("no cart found",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void shouldNotRemoveProductCauseProductNotFound() throws Exception {

        String json = "{\"cartId\": \"1\",\"productId\": \"1\",\"quantity\": 1}";

        mockMvc.perform(put("/carts/remove").accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof RecordNotFoundException))
                .andExpect(result -> assertEquals("no product with the ID: 1",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void shouldCheckOutCart() throws Exception {

        Cart cart = createCart(1);
        cart.setStatus("CLOSED");

        when(cartService.cartCheckOut(cart.getId())).thenReturn(cart);

        mockMvc.perform(put("/carts/checkout/1").accept(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotCheckOutCart() throws Exception {

        Mockito.doThrow(new RecordNotFoundException("no cart found")).when(cartService).cartNotFound(1L);

        mockMvc.perform(put("/carts/checkout/1").accept(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteCart() throws Exception {

        mockMvc.perform(delete("/carts/1").accept(APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldNotDeleteCart() throws Exception {

        Mockito.doThrow(new RecordNotFoundException("no cart found")).when(cartService).cartNotFound(1L);

        mockMvc.perform(delete("/carts/1").accept(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
