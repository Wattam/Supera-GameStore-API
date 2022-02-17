package br.com.superaGameStore.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Embeddable
public class CartProductKey implements Serializable {

    @ManyToOne(optional = false)
    @JoinColumn(name = "cart_id")
    public Cart cart;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    public Product product;

    public CartProductKey(Cart cart, Product product) {
        this.cart = cart;
        this.product = product;
    }

    public CartProductKey(Cart cart) {
        this.cart = cart;
    }
}
