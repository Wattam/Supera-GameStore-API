package br.com.superaGameStore.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.superaGameStore.model.Cart;
import br.com.superaGameStore.model.CartProduct;
import br.com.superaGameStore.model.CartProductKey;
import br.com.superaGameStore.repository.CartRepository;
import br.com.superaGameStore.service.CartProductService;
import br.com.superaGameStore.service.CartService;
import br.com.superaGameStore.service.ProductService;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartProductService cartProductService;

    @Autowired
    private ProductService productService;

    @Override
    public Cart store() {

        Cart cart = new Cart();
        cart.setStatus("OPEN");
        return cartRepository.save(cart);
    }

    @Override
    public List<Cart> index() {

        return cartRepository
                .findAll()
                .stream()
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Cart> show(long id, String sort_by) {

        Optional<Cart> cart = cartRepository.findById(id);

        if (cart.isEmpty() || sort_by == null) {
            return cart;
        }

        List<CartProduct> cartProducts = cart.get().getCartProducts();

        if (sort_by.equals("price")) {
            Collections.sort(cartProducts, CartProduct.PriceComparator);
            cart.get().setCartProducts(cartProducts);
        }
        if (sort_by.equals("name")) {
            Collections.sort(cartProducts, CartProduct.NameComparator);
            cart.get().setCartProducts(cartProducts);
        }
        if (sort_by.equals("score")) {
            Collections.sort(cartProducts, CartProduct.ScoreComparator);
            cart.get().setCartProducts(cartProducts);
        }

        return cart;
    }

    @Override
    public Cart addProduct(long cartId, long productId, int quantity) {

        CartProductKey cpk = new CartProductKey(
                cartRepository.findById(cartId).get(),
                productService.show(productId).get());

        if (cartProductService.show(cpk).isPresent()) {

            CartProduct cp = cartProductService.show(cpk).get();
            cp.setQuantity(cp.getQuantity() + quantity);
            cartProductService.store(cp);
            return cartRepository.findById(cartId).get();
        }

        CartProduct cp = new CartProduct(cpk, quantity);
        cartProductService.store(cp);

        return cartRepository.findById(cartId).get();
    }

    @Override
    public Cart removeProduct(long cartId, long productId, int quantity) {

        CartProductKey cpk = new CartProductKey(
                cartRepository.findById(cartId).get(),
                productService.show(productId).get());

        if (cartProductService.show(cpk).isPresent()) {

            CartProduct cp = cartProductService.show(cpk).get();

            if ((cp.getQuantity() - quantity) <= 0) {
                cartProductService.delete(cpk);
            } else {
                cp.setQuantity(cp.getQuantity() - quantity);
                cartProductService.store(cp);
            }
        }

        return cartRepository.findById(cartId).get();
    }

    @Override
    public Cart checkout(long id) {

        Cart cart = cartRepository.findById(id).get();
        cart.setStatus("CLOSED");
        return cartRepository.save(cart);
    }

    @Override
    public void delete(long id) {

        Cart cart = cartRepository.findById(id).get();

        for (CartProduct cp : cart.getCartProducts()) {
            cartProductService.delete(cp.getCpk());
        }

        cartRepository.deleteById(id);
    }
}
