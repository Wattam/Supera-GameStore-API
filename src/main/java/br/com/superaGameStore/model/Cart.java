package br.com.superaGameStore.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Data
@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public String status;

    @JsonManagedReference
    @OneToMany(mappedBy = "cpk.cart", fetch = FetchType.EAGER)
    @Valid
    public List<CartProduct> cartProducts = new ArrayList<>();

    @Transient
    public BigDecimal getShippingFee() {

        if (this.getSubTotalPrice().compareTo(BigDecimal.valueOf(250)) >= 0) {
            return BigDecimal.valueOf(0.00);
        }

        int quantity = 0;

        for (CartProduct cp : this.cartProducts) {
            quantity += cp.getQuantity();
        }
        return BigDecimal.valueOf(10.00).multiply(BigDecimal.valueOf(quantity));
    }

    @Transient
    public BigDecimal getSubTotalPrice() {

        BigDecimal subTotalPrice = BigDecimal.ZERO;

        for (CartProduct cp : this.cartProducts) {
            subTotalPrice = subTotalPrice.add(cp.getTotalPrice());
        }
        return subTotalPrice;
    }

    @Transient
    public BigDecimal getTotalPrice() {

        return this.getShippingFee().add(this.getSubTotalPrice());
    }
}
