package br.com.superaGameStore.model;

import java.math.BigDecimal;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name = "cart_product")
public class CartProduct {

    @EmbeddedId
    @JsonIgnore
    public CartProductKey cpk;

    public int quantity;

    public CartProduct(CartProductKey cpk, int quantity) {
        this.cpk = cpk;
        this.quantity = quantity;
    }

    @JsonIgnore
    @Transient
    public BigDecimal getTotalPrice() {
        return this.getProduct().getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    @Transient
    public Product getProduct() {
        return this.cpk.getProduct();
    }
}
