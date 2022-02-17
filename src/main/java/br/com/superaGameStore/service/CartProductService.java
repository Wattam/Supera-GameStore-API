package br.com.superaGameStore.service;

import br.com.superaGameStore.model.CartProduct;
import br.com.superaGameStore.model.CartProductKey;

public interface CartProductService {

    public CartProduct getCartProduct(CartProductKey cpk);

    public boolean cartProductIsPresent(CartProductKey cpk);

    public void addCartProduct(CartProduct cp);

    public void deleteCartProduct(CartProductKey cpk);
}
