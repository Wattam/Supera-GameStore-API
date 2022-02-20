package br.com.superaGameStore.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    public Cart createCart() {

        Cart cart = new Cart();
        cart.setStatus("OPEN");
        return cartRepository.save(cart);
    }

    public List<Cart> getAllCarts() {

        return cartRepository.findAll();
    }

    @Override
    public Optional<Cart> getCart(long id, String sort_by) {

        Cart cart = cartRepository.findById(id).get();

        List<CartProduct> cartProducts = cart.getCartProducts();

        if (sort_by == null) {
            sort_by = "";
        }
        if (sort_by.equals("price")) {
            Collections.sort(cartProducts, CartProduct.PriceComparator);
        }
        if (sort_by.equals("name")) {
            Collections.sort(cartProducts, CartProduct.NameComparator);
        }
        if (sort_by.equals("score")) {
            Collections.sort(cartProducts, CartProduct.ScoreComparator);
        }

        cart.setCartProducts(cartProducts);

        return Optional.of(cart);
    }

    @Override
    public Cart addProduct(long cartId, long productId, int quantity) {

        Cart cart = cartRepository.findById(cartId).get();
        CartProductKey cpk = new CartProductKey(cart, productService.getProduct(productId).get());

        if (cartProductService.getCartProduct(cpk).isPresent()) {

            CartProduct cp = cartProductService.getCartProduct(cpk).get();
            cp.setQuantity(cp.getQuantity() + quantity);
            cartProductService.addCartProduct(cp);
            return cartRepository.findById(cartId).get();
        }

        CartProduct cp = new CartProduct(cpk, quantity);
        cartProductService.addCartProduct(cp);

        return cartRepository.findById(cartId).get();
    }

    @Override
    public Cart removeProduct(long cartId, long productId, int quantity) {

        CartProductKey cpk = new CartProductKey(
                cartRepository.findById(cartId).get(),
                productService.getProduct(productId).get());

        if (cartProductService.getCartProduct(cpk).isPresent()) {

            CartProduct cp = cartProductService.getCartProduct(cpk).get();

            if ((cp.getQuantity() - quantity) <= 0) {
                cartProductService.deleteCartProduct(cpk);
            } else {
                cp.setQuantity(cp.getQuantity() - quantity);
                cartProductService.addCartProduct(cp);
            }
        }

        return cartRepository.findById(cartId).get();
    }

    public Cart cartCheckOut(long id) {

        Cart cart = cartRepository.findById(id).get();
        cart.setStatus("CLOSED");
        return cartRepository.save(cart);
    }

    public void deleteCart(long id) {

        Cart cart = cartRepository.findById(id).get();

        for (CartProduct cp : cart.getCartProducts()) {
            cartProductService.deleteCartProduct(cp.getCpk());
        }

        cartRepository.deleteById(id);
    }
}
