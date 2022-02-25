package br.com.superaGameStore.service;

import java.util.List;
import java.util.Optional;

import br.com.superaGameStore.model.Cart;

public interface CartService {

    public Cart store();

    public List<Cart> index();

    public Optional<Cart> show(long id, String sort_by);

    public Cart addProduct(long cartId, long productId, int quantity);

    public Cart removeProduct(long cartId, long productId, int quantity);

    public Cart checkout(long id);

    public void delete(long id);
}
