package net.stremo.shopsystem.graphql;

import graphql.schema.*;
import net.stremo.shopsystem.entities.*;
import org.springframework.stereotype.Service;

import java.util.*;

import static graphql.Scalars.*;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;
import static graphql.schema.GraphQLSchema.newSchema;

@Service
class MyGraphQLSchema {

    private GraphQLSchema schema;

    private GraphQLController controller;

    MyGraphQLSchema(GraphQLController controller) {
        this.controller = controller;

        GraphQLObjectType customerType = createCustomerType();
        GraphQLObjectType categoryType = createCategoryType();


        GraphQLObjectType QueryRoot = newObject()
                .name("Root")
                .field(newFieldDefinition()
                        .name("Customer")
                        .type(customerType)
                        .argument(GraphQLArgument.newArgument().name("id").type(GraphQLID).build())
                        .dataFetcher(environment -> {
                            Long id = Long.parseLong(environment.getArgument("id"));
                            return controller.customerRepository.findOne(id);
                        })
                        .build())
                .field(newFieldDefinition()
                        .name("me")
                        .type(new GraphQLTypeReference("Customer"))
                        .dataFetcher(environment -> {
                            Long id = 1L;
                            return controller.customerRepository.findOne(id);
                        })
                        .build())
                .field(newFieldDefinition()
                        .name("Category")
                        .type(new GraphQLList(categoryType))
                        .argument(GraphQLArgument.newArgument().name("name").type(GraphQLString).build())
                        .dataFetcher(environment -> {
                            if (environment.containsArgument("name")) {
                                return controller.categoryRepository.findByName(environment.getArgument("name").toString());
                            }
                            return controller.categoryRepository.findAll();
                        })
                        .build())
                .field(newFieldDefinition()
                        .name("Orders")
                        .type(new GraphQLList(new GraphQLTypeReference("Order")))
                        .dataFetcher(environment -> {
                            Long customerId = 1L;
                            Customer c = controller.customerRepository.findOne(customerId);
                            return controller.orderRepository.findByCustomer(c);
                        })
                        .build())
                .build();
        GraphQLObjectType mutationType = newObject()
                .name("Mutation")
                .field(createPutProductInShoppingcardMutation())
                .field(createFinishOrderMutation())
                .field(createAddRatingMutation())
                .field(createAddProductMutation())
                .build();

        schema = newSchema()
                .query(QueryRoot)
                .mutation(mutationType)
                .build();
    }

    private GraphQLFieldDefinition createPutProductInShoppingcardMutation() {
        return newFieldDefinition()
                .name("putProductIntoShoppingCard")
                .type(new GraphQLTypeReference("Shoppingcard"))
                .argument(GraphQLArgument.newArgument().name("productId").type(GraphQLID).build())
                .argument(GraphQLArgument.newArgument().name("count").type(GraphQLInt).build())
                .dataFetcher(environment -> {
                    Long productId = Long.parseLong(environment.getArgument("productId"));
                    int count = environment.getArgument("count");

                    if (count < 1)
                        return null;
                    Product p = controller.productRepository.findOne(productId);
                    if (p == null)
                        return null;
                    Customer c = controller.customerRepository.findOne(1L);
                    Shoppingcard s = c.getShoppingcard();
                    if (s == null) {
                        s = new Shoppingcard();
                        c.setShoppingcard(s);
                    }
                    ShoppingcardElement e = new ShoppingcardElement(count, s, p);
                    controller.shoppingcardElementRepository.save(e);
                    s.addProduct(e);

                    return s;
                })
                .build();
    }

    private GraphQLFieldDefinition createFinishOrderMutation() {
        GraphQLController controller = this.controller;
        return newFieldDefinition()
                .name("finishOrder")
                .type(new GraphQLTypeReference("Order"))
                .argument(GraphQLArgument.newArgument().name("addressId").type(GraphQLID).build())
                .dataFetcher(environment -> {
                    Long addressId = Long.parseLong(environment.getArgument("addressId"));
                    Address address = controller.addressRepository.findOne(addressId);
                    if (address == null)
                        return null;
                    Customer c = controller.customerRepository.findOne(1L);
                    Shoppingcard shoppingcard = c.getShoppingcard();
                    if (shoppingcard == null)
                        return null;

                    OrderStatus orderStatus = controller.orderStatusRepository.findOne(1L);
                    Order order = new Order(address, orderStatus, c);
                    controller.orderRepository.save(order);
                    shoppingcard.getProducts().forEach(shoppingcardElement -> {
                        OrderItem item = new OrderItem(shoppingcardElement.getQuantity(), order, shoppingcardElement.getProduct());
                        controller.orderItemRepository.save(item);
                        order.addOrderItem(item);
                        controller.shoppingcardElementRepository.delete(shoppingcardElement);

                    });
                    controller.shoppingcardRepository.delete(shoppingcard);
                    return order;
                })
                .build();
    }

    private GraphQLFieldDefinition createAddRatingMutation() {
        return newFieldDefinition()
                .name("addRating")
                .type(new GraphQLTypeReference("Rating"))
                .argument(GraphQLArgument.newArgument().name("productId").type(GraphQLID).build())
                .argument(GraphQLArgument.newArgument().name("stars").type(GraphQLInt).build())
                .argument(GraphQLArgument.newArgument().name("comment").type(GraphQLString).build())
                .dataFetcher(environment -> {
                    Long productId = Long.parseLong(environment.getArgument("productId"));
                    int stars = environment.getArgument("stars");
                    String comment = environment.getArgument("comment");

                    if (stars < 1)
                        return null;

                    Product p = controller.productRepository.findOne(productId);
                    if (p == null)
                        return null;
                    Customer c = controller.customerRepository.findOne(1L);

                    Rating rating = new Rating(stars, c, p);
                    if(comment.length() > 0)
                        rating.setComment(comment);
                    controller.ratingReposistory.save(rating);
                    c.addRating(rating);
                    p.addRating(rating);

                    return rating;
                })
                .build();
    }



    private GraphQLFieldDefinition createAddProductMutation() {
        return newFieldDefinition()
                .name("addProduct")
                .type(new GraphQLTypeReference("Product"))
                .argument(GraphQLArgument.newArgument().name("name").type(GraphQLString).build())
                .argument(GraphQLArgument.newArgument().name("price").type(GraphQLFloat).build())
                .argument(GraphQLArgument.newArgument().name("deliveryTime").type(GraphQLInt).build())
                .argument(GraphQLArgument.newArgument().name("description").type(GraphQLString).build())
                .dataFetcher(environment -> {
                    int deliveryTime = environment.getArgument("deliveryTime");
                    String description = environment.getArgument("description");
                    String name = environment.getArgument("name");
                    Float price = Float.parseFloat(environment.getArgument("price") + "");

                    if (deliveryTime < 1)
                        return null;

                    if(name == null){
                        return null;
                    }
                    if(description == null)
                        description = "";

                    Product p = new Product(name, price, deliveryTime, description);

                    controller.productRepository.save(p);

                    return p;
                })
                .build();
    }

    private GraphQLObjectType createCustomerType() {
        GraphQLController controller = this.controller;
        return newObject()
                .name("Customer")
                .field(newFieldDefinition()
                        .name("id")
                        .type(new GraphQLNonNull(GraphQLID))
                        .build())
                .field(newFieldDefinition()
                        .name("prename")
                        .type(GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("surname")
                        .type(GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("email")
                        .type(GraphQLString)
                        .build())

                .field(newFieldDefinition()
                        .name("address")
                        .type(new GraphQLList(GraphQLShopTypes.createAddressType()))
                        .dataFetcher((DataFetchingEnvironment environment) -> {
                            Customer customer = environment.getSource();
                            return customer.getAddresses();
                        })
                        .build())
                .field(newFieldDefinition()
                        .name("shoppingcard")
                        .type(GraphQLShopTypes.createShoppingCardType())
                        .dataFetcher((DataFetchingEnvironment environment) -> {
                            Customer customer = environment.getSource();
                            return customer.getShoppingcard();
                        })
                        .build())
                .field(newFieldDefinition()
                        .name("orders")
                        .type(new GraphQLList(GraphQLShopTypes.createOrderType()))
                        .dataFetcher((DataFetchingEnvironment environment) -> {
                            Customer customer = environment.getSource();

                            List<Order> orders = controller.orderRepository.findByCustomer(customer);
                            if (orders != null)
                                System.out.println("ordersize: " + orders.size());
                            return orders;
                        })
                        .build())
                .field(newFieldDefinition()
                        .name("ratings")
                        .type(new GraphQLList(GraphQLShopTypes.createRatingType()))
                        .dataFetcher((DataFetchingEnvironment environment) -> {
                            Customer customer = environment.getSource();
                            return customer.getRatings();
                        })
                        .build())
                .build();
    }


    private GraphQLObjectType createCategoryType() {
        return newObject()
                .name("Category")
                .field(newFieldDefinition()
                        .name("id")
                        .type(new GraphQLNonNull(GraphQLID))
                        .build())
                .field(newFieldDefinition()
                        .name("name")
                        .type(GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("parent")
                        .type(new GraphQLTypeReference("Category"))
                        .build())
                .field(newFieldDefinition()
                        .name("products")
                        .type(new GraphQLList(new GraphQLTypeReference("Product")))
                        .dataFetcher((DataFetchingEnvironment environment) -> {
                            Category category = environment.getSource();
                            return category.getProducts();
                        })
                        .build())

                .build();
    }

    GraphQLSchema getSchema() {
        return schema;
    }
}