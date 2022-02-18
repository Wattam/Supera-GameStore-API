package br.com.superaGameStore.service;

import java.util.Optional;

import br.com.superaGameStore.model.CartProduct;
import br.com.superaGameStore.model.CartProductKey;

public interface CartProductService {

    public Optional<CartProduct> getCartProduct(CartProductKey cpk);

    public void addCartProduct(CartProduct cp);

    public void deleteCartProduct(CartProductKey cpk);
}
