package com.loading.graphqldemo.graphql;

import com.loading.graphqldemo.entity.PersonEntity;
import com.loading.graphqldemo.service.PersonService;
import graphql.kickstart.tools.GraphQLMutationResolver;
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
public class PersonMutationResolver implements GraphQLMutationResolver {

  @Autowired
  private PersonService personService;

  public PersonEntity createPerson(PersonEntity company) {
    return personService.save(company);
  }

}
