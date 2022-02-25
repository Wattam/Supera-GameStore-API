package br.com.superaGameStore.service;

import java.util.Optional;

import br.com.superaGameStore.model.CartProduct;
import br.com.superaGameStore.model.CartProductKey;

public interface CartProductService {

    public Optional<CartProduct> show(CartProductKey cpk);

    public void store(CartProduct cp);

    public void delete(CartProductKey cpk);
}
