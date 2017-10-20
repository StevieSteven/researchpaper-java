package net.stremo.shopsystem.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customer extends BaseEntity{


    private String prename;
    private String surname;
    private String email;

//    @OneToMany(fetch = FetchType.EAGER)
    @OneToMany(mappedBy = "customer")
    private final List<Address> addresses = new ArrayList<>();

    /*
    TODO:
    Addresses[]
    Shoppingcard
    Orders[]
    Ratings[]
     */


    public Customer(String prename, String surname, String email) {
        this.prename = prename;
        this.surname = surname;
        this.email = email;
    }


    public String getPrename() {
        return prename;
    }

    public void setPrename(String prename) {
        this.prename = prename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void addAddress(Address address) {
        addresses.add(address);
        address.setCustomer(this);
    }
}
