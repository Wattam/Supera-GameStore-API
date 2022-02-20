package br.com.superaGameStore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.superaGameStore.controller.exception.MethodNotAllowedException;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cart store() {

        return cartService.createCart();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Cart> index() {

        List<Cart> carts = cartService.getAllCarts();
        if (carts == null || carts.isEmpty()) {
            throw new RecordNotFoundException("no cart found");
        }

        return carts;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Cart show(@PathVariable long id, @RequestParam(required = false, name = "sort_by") String sort_by) {

        return cartService.getCart(id, sort_by)
                .orElseThrow(() -> new RecordNotFoundException("no cart with the ID: " + id));
    }

    @PutMapping("/addProduct")
    @ResponseStatus(HttpStatus.OK)
    public Cart addProduct(@RequestBody CartForm cartForm) {

        cartService.getCart(cartForm.getCartId(), "")
                .orElseThrow(() -> new RecordNotFoundException("no cart with the ID: " + cartForm.getCartId()));

        productService.getProduct(cartForm.getProductId())
                .orElseThrow(() -> new RecordNotFoundException("no product with the ID: " + cartForm.getProductId()));

        if (cartService.getCart(cartForm.getCartId(), "").get().getStatus().equals("CLOSED")) {
            throw new MethodNotAllowedException("the cart " + cartForm.getCartId() + " is closed");
        }

        return cartService.addProduct(
                cartForm.getCartId(),
                cartForm.getProductId(),
                cartForm.getQuantity());
    }

    @PutMapping("/removeProduct")
    @ResponseStatus(HttpStatus.OK)
    public Cart removeProduct(@RequestBody CartForm cartForm) {

        cartService.getCart(cartForm.getCartId(), "")
                .orElseThrow(() -> new RecordNotFoundException("no cart with the ID: " + cartForm.getCartId()));

        productService.getProduct(cartForm.getProductId())
                .orElseThrow(() -> new RecordNotFoundException("no product with the ID: " + cartForm.getProductId()));

        if (cartService.getCart(cartForm.getCartId(), "").get().getStatus().equals("CLOSED")) {
            throw new MethodNotAllowedException("the cart " + cartForm.getCartId() + " is closed");
        }

        return cartService.removeProduct(
                cartForm.getCartId(),
                cartForm.getProductId(),
                cartForm.getQuantity());
    }

    @PutMapping("/checkout/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Cart checkout(@PathVariable long id) {

        cartService.getCart(id, "")
                .orElseThrow(() -> new RecordNotFoundException("no cart with the ID: " + id));

        if (cartService.getCart(id, "").get().getStatus().equals("CLOSED")) {
            throw new MethodNotAllowedException("the cart " + id + " is closed");
        }

        return cartService.cartCheckOut(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {

        cartService.getCart(id, "")
                .orElseThrow(() -> new RecordNotFoundException("no cart with the ID: " + id));

        cartService.deleteCart(id);
    }

    @Data
    public static class CartForm {

        private long cartId;
        private long productId;
        private int quantity;
    }
}
