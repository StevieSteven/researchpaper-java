package net.stremo.shopsystem;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import net.stremo.shopsystem.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
public class TestDataSetup {

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    CustomerRepository customerRepository;


    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;


    @Transactional
    public void run() {
        System.out.println("Create TestData");

        Faker faker = new Faker();

        int step = 1;
        int numberOfUser = 20;
        int numberOfProducts = 200;

        System.out.println(step + ". Create Customer. Number: " + numberOfUser);
        for (int i = 0; i < numberOfUser; i++) {
            Name fakerName = faker.name();
            Customer c = new Customer(fakerName.firstName(), fakerName.lastName(), faker.internet().emailAddress());
            customerRepository.save(c);

            for (int j = 0; j < new Random().nextInt(10); j++) {
                com.github.javafaker.Address fakerAddress = faker.address();
                Address a = new Address(fakerAddress.streetName(), fakerAddress.buildingNumber(), fakerAddress.city(), fakerAddress.zipCode());
                addressRepository.save(a);
                c.addAddress(a);
            }

        }

        step++;

        System.out.println(step + ". Create Categories.");

        Category cHome = new Category("Haushalt");
        categoryRepository.save(cHome);
        Category cTechnic = new Category("Technik");
        categoryRepository.save(cTechnic);
        Category cCar = new Category("Auto");
        categoryRepository.save(cCar);
        Category cMovieSeries = new Category("Filme und Serien");
        categoryRepository.save(cMovieSeries);

        Category cMovie = new Category("Filme", cMovieSeries);
        categoryRepository.save(cMovie);

        Category cSerie = new Category("Serie", cMovieSeries);
        categoryRepository.save(cSerie);

        step++;

        System.out.println(step + ". create products. Number of Products: different of each category");

        this.createProducts(800, cHome);
        this.createProducts(4000, cTechnic);
        this.createProducts(500, cCar);
        this.createProducts(200, cMovie);
        this.createProducts(100, cSerie);



    }


    private void createProducts (int number, Category category) {
        Faker faker = new Faker();
        for(int i= 0; i < number; i++) {
            Float price = (float) (new Random().nextInt(100000)) / 100;
            Product p = new Product(faker.beer().name(), price, new Random().nextInt(10)+1, faker.lorem().paragraph());
            p.addCategory(category);
            productRepository.save(p);
        }

    }
}
