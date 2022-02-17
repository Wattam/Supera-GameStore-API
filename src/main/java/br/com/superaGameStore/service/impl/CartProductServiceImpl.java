package br.com.superaGameStore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.superaGameStore.model.CartProduct;
import br.com.superaGameStore.model.CartProductKey;
import br.com.superaGameStore.repository.CartProductRepository;
import br.com.superaGameStore.service.CartProductService;

@Service
public class CartProductServiceImpl implements CartProductService {

    @Autowired
    public CartProductRepository cartProductRepository;

    @Override
    public CartProduct getCartProduct(CartProductKey cpk) {

        return cartProductRepository.findById(cpk).get();
    }

    @Override
    public boolean cartProductIsPresent(CartProductKey cpk) {

        if (cartProductRepository.findById(cpk).isPresent()) {
            return true;
        }
        return false;
    }

    @Override
    public void addCartProduct(CartProduct cp) {

        cartProductRepository.save(cp);
    }

    @Override
    public void deleteCartProduct(CartProductKey cpk) {

        cartProductRepository.deleteById(cpk);
    }
}
