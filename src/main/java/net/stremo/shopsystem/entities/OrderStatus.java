package net.stremo.shopsystem.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "order-status")
public class OrderStatus extends BaseEntity {

    private String message;


    public OrderStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
