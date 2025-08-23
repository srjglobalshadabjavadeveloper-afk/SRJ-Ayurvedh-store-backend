package Ayurvedh.ayurvedh.entity;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sub_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    private Date createdAt;

    private Date updatedAt;

    // Bidirectional relationship with Products
    @OneToMany(mappedBy = "subCategory", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Products> products = new ArrayList<>();

    // Utility methods for managing bidirectional relationships
    public void addProduct(Products product) {
        products.add(product);
        product.setSubCategory(this);
    }

    public void removeProduct(Products product) {
        products.remove(product);
        product.setSubCategory(null);
    }
}
