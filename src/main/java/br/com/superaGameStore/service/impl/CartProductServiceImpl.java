package br.com.superaGameStore.service.impl;

import java.util.Optional;

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
    public Optional<CartProduct> show(CartProductKey cpk) {

        return cartProductRepository.findById(cpk);
    }

    @Override
    public void store(CartProduct cp) {

        cartProductRepository.save(cp);
    }

    @Override
    public void delete(CartProductKey cpk) {

        cartProductRepository.deleteById(cpk);
    }
}
