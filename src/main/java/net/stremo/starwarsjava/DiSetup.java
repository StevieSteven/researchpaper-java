package net.stremo.starwarsjava;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import javax.persistence.EntityManager;

@Configuration
public class DiSetup {

//    @Bean
//    public SpelAwareProxyProjectionFactory projectionFactory() {
//        return new SpelAwareProxyProjectionFactory();
//    }

//    @Bean
//    public GraphQLExecutor graphQLExecutor(EntityManager entityManager, MutationBuilder mutation) {
//        return new GraphQLExecutor(entityManager, mutation::addMutation);
//    }
}
