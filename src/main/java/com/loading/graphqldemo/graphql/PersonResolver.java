package com.loading.graphqldemo.graphql;

import com.loading.graphqldemo.entity.CompanyEntity;
import com.loading.graphqldemo.entity.PersonEntity;
import graphql.kickstart.execution.context.GraphQLContext;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import java.util.concurrent.CompletableFuture;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.stereotype.Component;

/**
 * desc:
 *
 * @author Lo_ading
 * @version 1.0.0
 * @date 2021/9/14
 */
@Component
public class PersonResolver implements GraphQLResolver<PersonEntity> {


  public CompletableFuture<CompanyEntity> company(PersonEntity person,
      DataFetchingEnvironment environment) {

    DataLoaderRegistry registry = ((GraphQLContext) environment.getContext())
        .getDataLoaderRegistry();
    DataLoader<String, CompanyEntity> dataLoader = registry.getDataLoader("companyIdLoader");
    if (dataLoader != null) {
      return dataLoader.load(person.getCompanyId());
    }
    throw new IllegalStateException("No customer data loader found");
  }

}
