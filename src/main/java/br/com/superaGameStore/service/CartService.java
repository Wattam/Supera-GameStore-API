package br.com.superaGameStore.service;

import java.util.List;
import java.util.Optional;

import br.com.superaGameStore.model.Cart;

public interface CartService {

    public Cart createCart();

    public List<Cart> getAllCarts();

    public Optional<Cart> getCart(long id);

    public Optional<Cart> getCartProductsByPrice(long id);

    public Optional<Cart> getCartProductsByName(long id);

    public Optional<Cart> getCartProductsByScore(long id);

    public Cart addProduct(long cartId, long productId, int quantity);

    public Cart removeProduct(long cartId, long productId, int quantity);

    public Cart cartCheckOut(long id);

    public void deleteCart(long id);

    public void cartNotFound(long id);
}
