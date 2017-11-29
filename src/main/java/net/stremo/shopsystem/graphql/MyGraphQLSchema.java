package net.stremo.shopsystem.graphql;


import graphql.GraphQL;
import graphql.Scalars;
import graphql.relay.Connection;
import graphql.relay.Relay;
import graphql.relay.SimpleListConnection;
import graphql.schema.*;
import net.stremo.shopsystem.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static graphql.Scalars.GraphQLID;
import static graphql.Scalars.GraphQLInt;
import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;
import static graphql.schema.GraphQLSchema.newSchema;

@Service
public class MyGraphQLSchema {

    private GraphQLSchema schema;

    private GraphQLObjectType todoType;

    private GraphQLObjectType customerType = createCustomerType();
    private GraphQLObjectType categoryType = createCategoryType();
    private GraphQLObjectType addressType = GraphQLShopTypes.createAddressType();

    private GraphQLObjectType connectionFromUserToTodos;
    private GraphQLInterfaceType nodeInterface;

    private GraphQLObjectType todosEdge;


    private User theOnlyUser = new User();
    private List<Todo> todos = new ArrayList<>();


    private SimpleListConnection simpleConnection;


    private Relay relay = new Relay();

    private int nextTodoId = 0;


    GraphQLController controller;

    public MyGraphQLSchema(GraphQLController controller) {

        this.controller = controller;
//        System.out.println("Zweiter Test: " +controller.productRepository.count());

        TypeResolverProxy typeResolverProxy = new TypeResolverProxy();
        nodeInterface = relay.nodeInterface(typeResolverProxy);
        simpleConnection = new SimpleListConnection(todos);

//        createTodoType();
//        createConnectionFromUserToTodos();


        typeResolverProxy.setTypeResolver(object -> {
            return null;
        });


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
                        .name("Category")
                        .type(new GraphQLList(categoryType))
                        .argument(GraphQLArgument.newArgument().name("name").type(GraphQLString).build())
                        .dataFetcher(environment -> {

                            if(environment.containsArgument("name")) {
                                List<Category> categories = controller.categoryRepository.findByName(environment.getArgument("name").toString());
                                return categories;
                            }
                            return controller.categoryRepository.findAll();
                        })
                        .build())
                .build();

//        GraphQLSchemaMutation todoSchemaMutations = new GraphQLSchemaMutation(this);

//        GraphQLObjectType mutationType = newObject()
//                .name("Mutation")
//                .fields(todoSchemaMutations.getFields())
//                .build();

        schema = newSchema()
                .query(QueryRoot)
//                .mutation(mutationType)
                .build();
    }


//    private void createTodoType() {
//        todoType = newObject()
//                .name("Todo")
//                .field(newFieldDefinition()
//                        .name("id")
//                        .type(new GraphQLNonNull(GraphQLID))
//                        .dataFetcher(environment -> {
//                                    Todo todo = (Todo) environment.getSource();
//                                    return relay.toGlobalId("Todo", todo.getId());
//                                }
//                        )
//                        .build())
//                .field(newFieldDefinition()
//                        .name("text")
//                        .type(Scalars.GraphQLString)
//                        .build())
//                .field(newFieldDefinition()
//                        .name("complete")
//                        .type(Scalars.GraphQLBoolean)
//                        .build())
//                .withInterface(nodeInterface)
//                .build();
//    }

//    private void createConnectionFromUserToTodos() {
//        todosEdge = relay.edgeType("Todo", todoType, nodeInterface, Collections.<GraphQLFieldDefinition>emptyList());
//        GraphQLFieldDefinition totalCount = newFieldDefinition()
//                .name("totalCount")
//                .type(GraphQLInt)
//                .dataFetcher(environment -> {
//                    Connection connection = (Connection) environment.getSource();
//                    return connection.getEdges().size();
//                })
//                .build();
//
//        GraphQLFieldDefinition completedCount = newFieldDefinition()
//                .name("completedCount")
//                .type(GraphQLInt)
//                .dataFetcher(environment -> {
//                    Connection connection = (Connection) environment.getSource();
//                    return -1;
////                    return (int) connection.getEdges().stream().filter(edge -> ((Todo) edge.getNode()).isComplete()).count();
//                })
//                .build();
//        connectionFromUserToTodos = relay.connectionType("Todo", todosEdge, Arrays.asList(totalCount, completedCount));
//    }

    public User getTheOnlyUser() {
        return theOnlyUser;
    }


    public SimpleListConnection getSimpleConnection() {
        return simpleConnection;
    }


    public GraphQLObjectType getCustomerType() {
        return customerType;
    }

    public GraphQLObjectType getTodoType() {
        return todoType;
    }

    public GraphQLObjectType getTodosEdge() {
        return todosEdge;
    }


    public Relay getRelay() {
        return relay;
    }


    public String addTodo(String text) {
        Todo newTodo = new Todo();
        newTodo.setId(Integer.toString(nextTodoId++));
        newTodo.setText(text);
        todos.add(newTodo);
        return newTodo.getId();
    }


    public void removeTodo(String id) {
        Todo del = todos.stream().filter(todo -> todo.getId().equals(id)).findFirst().get();
        todos.remove(del);
    }

    public void renameTodo(String id, String text) {
        Todo matchedTodo = todos.stream().filter(todo -> todo.getId().equals(id)).findFirst().get();
        matchedTodo.setText(text);
    }


    public List<String> removeCompletedTodos() {
        List<String> toDelete = todos.stream().filter(Todo::isComplete).map(Todo::getId).collect(Collectors.toList());
        todos.removeIf(todo -> toDelete.contains(todo.getId()));
        return toDelete;
    }

    public List<String> markAllTodos(boolean complete) {
        List<String> changed = new ArrayList<>();
        todos.stream().filter(todo -> complete != todo.isComplete()).forEach(todo -> {
            changed.add(todo.getId());
            todo.setComplete(complete);
        });

        return changed;
    }

    public void changeTodoStatus(String id, boolean complete) {
        Todo todo = getTodo(id);
        todo.setComplete(complete);
    }


    public Todo getTodo(String id) {
        return todos.stream().filter(todo -> todo.getId().equals(id)).findFirst().get();
    }

    public List<Todo> getTodos(List<String> ids) {
        return todos.stream().filter(todo -> ids.contains(todo.getId())).collect(Collectors.toList());
    }


    public GraphQLObjectType createCustomerType() {
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

                            if (customer.getOrder() != null)
                                System.out.println("ordersize: " + customer.getOrder().getId());
                            return customer.getOrder();
                        })
                        .build())
                .build();
    }


    public GraphQLObjectType createCategoryType() {
        GraphQLController controller = this.controller;
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


    public GraphQLSchema getSchema() {
        return schema;
    }
}