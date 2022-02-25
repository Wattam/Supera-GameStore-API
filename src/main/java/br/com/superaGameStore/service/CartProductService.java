package br.com.superaGameStore.service;

import java.util.List;
import java.util.Optional;

import br.com.superaGameStore.model.CartProduct;
import br.com.superaGameStore.model.CartProductKey;

public interface CartProductService {

    public List<CartProduct> getCartProductsByCartIdSorted(long id, String sort_by);

    public Optional<CartProduct> show(CartProductKey cpk);

    public void store(CartProduct cp);

    public void delete(CartProductKey cpk);
}
