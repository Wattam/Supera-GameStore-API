package br.com.superaGameStore.controller;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.superaGameStore.controller.exception.RecordNotFoundException;
import br.com.superaGameStore.model.Cart;
import br.com.superaGameStore.service.CartService;
import br.com.superaGameStore.service.ProductService;
import lombok.Data;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Cart create() {

        return cartService.createCart();
    }

    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public @NotNull List<Cart> getAllCarts() {

        List<Cart> carts = (List<Cart>) cartService.getAllCarts();
        if (carts == null || carts.isEmpty()) {
            throw new RecordNotFoundException("no cart found");
        }

        return carts;
    }

    @GetMapping("/{id}/cartByPrice")
    @ResponseStatus(HttpStatus.OK)
    public Cart getCartByPrice(@PathVariable long id) {

        cartService.cartNotFound(id);

        return cartService.getCartProductsByPrice(id);
    }

    @GetMapping("/{id}/cartByName")
    @ResponseStatus(HttpStatus.OK)
    public Cart getCartByName(@PathVariable long id) {

        cartService.cartNotFound(id);

        return cartService.getCartProductsByName(id);
    }

    @GetMapping("/{id}/cartByScore")
    @ResponseStatus(HttpStatus.OK)
    public Cart getCartByScore(@PathVariable long id) {

        cartService.cartNotFound(id);

        return cartService.getCartProductsByScore(id);
    }

    @PutMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public Cart addProduct(@RequestBody CartForm cartForm) {

        cartService.cartNotFound(cartForm.getCartId());

        productService.getProduct(cartForm.getProductId())
                .orElseThrow(() -> new RecordNotFoundException("no product with the ID: " + cartForm.getProductId()));

        return cartService.addProduct(
                cartForm.getCartId(),
                cartForm.getProductId(),
                cartForm.getQuantity());
    }

    @PutMapping("/remove")
    @ResponseStatus(HttpStatus.OK)
    public Cart removeProduct(@RequestBody CartForm cartForm) {

        cartService.cartNotFound(cartForm.getCartId());

        productService.getProduct(cartForm.getProductId())
                .orElseThrow(() -> new RecordNotFoundException("no product with the ID: " + cartForm.getProductId()));

        return cartService.removeProduct(
                cartForm.getCartId(),
                cartForm.getProductId(),
                cartForm.getQuantity());
    }

    @PutMapping("/checkout/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Cart cartCheckOut(@PathVariable long id) {

        cartService.cartNotFound(id);

        return cartService.cartCheckOut(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCart(@PathVariable long id) {

        cartService.cartNotFound(id);

        cartService.deleteCart(id);
    }

    @Data
    public static class CartForm {

        private long cartId;
        private long productId;
        private int quantity;
    }
}
