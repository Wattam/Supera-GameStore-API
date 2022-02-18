package br.com.superaGameStore.service;

import br.com.superaGameStore.model.Cart;

public interface CartService {

    public Cart createCart();

    public Iterable<Cart> getAllCarts();

    public Cart getCartProductsByPrice(long id);

    public Cart getCartProductsByName(long id);

    public Cart getCartProductsByScore(long id);

    public Cart addProduct(long cartId, long productId, int quantity);

    public Cart removeProduct(long cartId, long productId, int quantity);

    public Cart cartCheckOut(long id);

    public void deleteCart(long id);

    public void cartNotFound(long id);
}
