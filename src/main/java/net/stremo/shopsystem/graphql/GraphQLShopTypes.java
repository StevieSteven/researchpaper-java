package net.stremo.shopsystem.graphql;

import graphql.schema.*;

import static graphql.Scalars.*;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;

public class GraphQLShopTypes {

    public static GraphQLObjectType createCustomerType() {
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
                        .type(new GraphQLList(createAddressType()))
//                        .dataFetcher(environment -> {
//                            System.out.println("Address Envirement");
//                            System.out.println(environment);
//                            return new Object();
//                        })
                        .build())
                .build();
    }

    public static GraphQLObjectType createAddressType() {
        return newObject()
                .name("Address")
                .field(newFieldDefinition()
                        .name("id")
                        .type(new GraphQLNonNull(GraphQLID))
                        .build())
                .field(newFieldDefinition()
                        .name("street")
                        .type(GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("number")
                        .type(GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("city")
                        .type(GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("postalCode")
                        .type(GraphQLString)
                        .build())
                .build();
    }

    public static GraphQLObjectType createShoppingCardType() {

        return newObject()
                .name("Shoppingcard")
                .field(newFieldDefinition()
                        .name("id")
                        .type(new GraphQLNonNull(GraphQLID))
                        .build())
                .field(newFieldDefinition()
                        .name("customer")
                        .type(new GraphQLTypeReference("Customer"))
                        .build())
                .field(newFieldDefinition()
                        .name("products")
                        .type(new GraphQLList(GraphQLShopTypes.createShoppingCardElementType()))
                        .build())
                .build();
    }

    private static GraphQLType createShoppingCardElementType() {
        return newObject().name("ShoppingcardElement")
                .field(newFieldDefinition()
                        .name("id")
                        .type(new GraphQLNonNull(GraphQLID))
                        .build())
                .field(newFieldDefinition()
                        .name("quantity")
                        .type(new GraphQLNonNull(GraphQLLong))
                        .build())
                .field(newFieldDefinition()
                        .name("product")
                        .type(GraphQLShopTypes.createProductType())
                        .build())
                .build();
    }

    protected static GraphQLObjectType createProductType() {
        return newObject()
                .name("Product")
                .field(newFieldDefinition()
                        .name("id")
                        .type(new GraphQLNonNull(GraphQLID))
                        .build())
                .field(newFieldDefinition()
                        .name("name")
                        .type(GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("price")
                        .type(GraphQLFloat)
                        .build())
                .field(newFieldDefinition()
                        .name("deliveryTime")
                        .type(GraphQLLong)
                        .build())
                .field(newFieldDefinition()
                        .name("description")
                        .type(GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("categories")
                        .type(new GraphQLList(new GraphQLTypeReference("Category")))
                        .build())
                .build();
    }

    public static GraphQLObjectType createOrderType() {
        return newObject()
                .name("Order")
                .field(newFieldDefinition()
                        .name("id")
                        .type(new GraphQLNonNull(GraphQLID))
                        .build())
                .field(newFieldDefinition()
                        .name("date")
                        .type(GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("customer")
                        .type(new GraphQLTypeReference("Customer"))
                        .build())
                .field(newFieldDefinition()
                        .name("address")
                        .type(new GraphQLTypeReference("Address"))
                        .build())
                .build();
    }
}
