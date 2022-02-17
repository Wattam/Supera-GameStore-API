package br.com.superaGameStore.model;

import java.math.BigDecimal;
import java.util.Comparator;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
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

    public static Comparator<CartProduct> NameComparator = new Comparator<CartProduct>() {

        public int compare(CartProduct cp1, CartProduct cp2) {

            String cpName1 = cp1.getProduct().getName().toUpperCase();
            String cpName2 = cp2.getProduct().getName().toUpperCase();

            return cpName1.compareTo(cpName2);
        }
    };

    public static Comparator<CartProduct> ScoreComparator = new Comparator<CartProduct>() {

        public int compare(CartProduct cp1, CartProduct cp2) {

            short cpScore1 = cp1.getProduct().getScore();
            short cpScore2 = cp2.getProduct().getScore();

            return cpScore2 - cpScore1;
        }
    };

    public static Comparator<CartProduct> PriceComparator = new Comparator<CartProduct>() {

        public int compare(CartProduct cp1, CartProduct cp2) {

            BigDecimal cpScore1 = cp1.getProduct().getPrice();
            BigDecimal cpScore2 = cp2.getProduct().getPrice();

            return cpScore1.compareTo(cpScore2);
        }
    };
}
