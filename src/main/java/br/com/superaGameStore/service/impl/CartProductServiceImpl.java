package br.com.superaGameStore.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
    public List<CartProduct> getCartProductsByCartIdSorted(long id, String sort_by) {

        return cartProductRepository.findByCpkCartId(
                1L,
                Sort.by(Sort.Direction.ASC, String.format("cpk.product.%s", sort_by)));
    }

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
