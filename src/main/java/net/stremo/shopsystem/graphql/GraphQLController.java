package net.stremo.shopsystem.graphql;


import com.coxautodev.graphql.tools.SchemaParserBuilder;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import net.stremo.shopsystem.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@EnableAutoConfiguration
public class GraphQLController {

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    RatingReposistory ratingReposistory;

    @Autowired
    ShoppingcardRepository shoppingcardRepository;

    @Autowired
    ShoppingcardElementRepository shoppingcardElementRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    OrderStatusRepository orderStatusRepository;



//    GraphQL graphql = new GraphQL(new MyGraphQLSchema().getSchema());
//    GraphQLSchema graphqlB = new SchemaParserBuilder().file("main.graphqls").build().makeExecutableSchema();
    GraphQL graphql;
//    GraphQL graphql = GraphQL.newGraphQL(graphqlB).build();

    private static final Logger log = LoggerFactory.getLogger(GraphQLController.class);

    GraphQLController() {
        graphql =  GraphQL.newGraphQL(new MyGraphQLSchema(this).getSchema()).build();
    }


    @RequestMapping(value = "/graphql", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object executeOperation(@RequestBody Map body) {

        System.out.println("A little Test: ");
        System.out.println(productRepository.count() + " Produkte vorhanden");

        System.out.println("Order Test: ");
        System.out.println(orderRepository.count());

        String query = (String) body.get("query");
        Map<String, Object> variables = (Map<String, Object>) body.get("variables");
        ExecutionResult executionResult = graphql.execute(query, (Object) null, variables);
        Map<String, Object> result = new LinkedHashMap<>();
        if (executionResult.getErrors().size() > 0) {
            result.put("errors", executionResult.getErrors());
            log.error("Errors: {}", executionResult.getErrors());
        }
        result.put("data", executionResult.getData());
        return result;
    }


}