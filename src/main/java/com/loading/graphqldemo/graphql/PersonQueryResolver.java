package com.loading.graphqldemo.graphql;

import com.loading.graphqldemo.entity.PersonEntity;
import com.loading.graphqldemo.service.PersonService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.relay.Connection;
import graphql.relay.SimpleListConnection;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * desc:
 *
 * @author Lo_ading
 * @version 1.0.0
 * @date 2021/9/14
 */
@Component
public class PersonQueryResolver implements GraphQLQueryResolver {

  @Autowired
  private PersonService personService;

  public PersonEntity save(PersonEntity person){
    return personService.save(person);
  }

  public List<PersonEntity> persons() {
    return personService.all();
  }

  public Connection<PersonEntity> personsPage(int first, String after, DataFetchingEnvironment env) {
    return new SimpleListConnection<>(personService.all()).get(env);
  }

  public PersonEntity person(String id) {
    return personService.findById(id);
  }

}
