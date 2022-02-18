package br.com.superaGameStore.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
@Entity(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @NotBlank
    public String name;

    @NotBlank
    public String image;

    public short score;

    public BigDecimal price;
}
