package net.stremo.shopsystem.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

    private String name;


    @OneToOne(fetch= FetchType.EAGER)
    private Category parent;

//    @ManyToMany //(mappedBy = "products")
//    private final List<Product> products = new ArrayList<>();

    public Category () {

    }


    public Category(String name) {
        this.name = name;
    }

    public Category(String name, Category parent) {
        this.name = name;
        this.parent= parent;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }
}
