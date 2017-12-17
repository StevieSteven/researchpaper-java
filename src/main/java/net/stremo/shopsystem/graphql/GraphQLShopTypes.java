package net.stremo.shopsystem.graphql;

import graphql.language.EnumTypeDefinition;
import graphql.schema.*;

import static graphql.Scalars.*;
import static graphql.schema.GraphQLEnumType.newEnum;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;

class GraphQLShopTypes {

    static GraphQLEnumType createStatusEnum() {
        return newEnum().name("Status")
                .value("LIEFERBAR")
                .value("AUSVERKAUFT")
                .build();


    }

    static GraphQLObjectType createAddressType() {
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

    static GraphQLObjectType createShoppingCardType() {

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

    private static GraphQLObjectType createProductType() {
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
                        .name("category")
                        .type(new GraphQLTypeReference("Category"))
                        .build())
                .field(newFieldDefinition()
                        .name("status")
                        .type(createStatusEnum())
                        .dataFetcher(environment -> {
                            return "AUSVERKAUFT";
                        })
                        .build())
                .field(newFieldDefinition()
                        .name("ratings")
                        .type(new GraphQLList(new GraphQLTypeReference("Rating")))
                        .build())
                .build();
    }

    static GraphQLObjectType createRatingType() {
        return newObject()
                .name("Rating")
                .field(newFieldDefinition()
                        .name("id")
                        .type(new GraphQLNonNull(GraphQLID))
                        .build())
                .field(newFieldDefinition()
                        .name("stars")
                        .type(GraphQLInt)
                        .build())

                .field(newFieldDefinition()
                        .name("comment")
                        .type(GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("description")
                        .type(GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("customer")
                        .type(new GraphQLTypeReference("Customer"))
                        .build())
                .field(newFieldDefinition()
                        .name("product")
                        .type(new GraphQLTypeReference("Product"))
                        .build())
                .build();
    }

    static GraphQLObjectType createOrderType() {
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
                .field(newFieldDefinition()
                        .name("items")
                        .type(new GraphQLList(createOrderItemType()))
                        .build())
                .field(newFieldDefinition()
                        .name("status")
                        .type(createOrderStatusType())
                        .build())
                .build();
    }


    private static GraphQLObjectType createOrderItemType() {
        return newObject()
                .name("OrderItem")
                .field(newFieldDefinition()
                        .name("id")
                        .type(new GraphQLNonNull(GraphQLID))
                        .build())
                .field(newFieldDefinition()
                        .name("quantity")
                        .type(GraphQLInt)
                        .build())
                .field(newFieldDefinition()
                        .name("product")
                        .type(new GraphQLTypeReference("Product"))
                        .build())
                .build();
    }

    private static GraphQLObjectType createOrderStatusType() {
        return newObject()
                .name("OrderStatus")
                .field(newFieldDefinition()
                        .name("id")
                        .type(new GraphQLNonNull(GraphQLID))
                        .build())
                .field(newFieldDefinition()
                        .name("message")
                        .type(GraphQLString)
                        .build())
                .build();
    }
}
