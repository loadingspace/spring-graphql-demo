package com.loading.graphqldemo.graphql;

import com.loading.graphqldemo.entity.CompanyEntity;
import com.loading.graphqldemo.entity.PersonEntity;
import com.loading.graphqldemo.service.CompanyService;
import graphql.kickstart.execution.context.GraphQLContext;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
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
public class CompanyResolver implements GraphQLResolver<CompanyEntity> {

  @Autowired
  private CompanyService companyService;

  public CompletableFuture<List<PersonEntity>> persons(CompanyEntity company,
      DataFetchingEnvironment environment) {
    DataLoaderRegistry registry = ((GraphQLContext) environment.getContext())
        .getDataLoaderRegistry();
    DataLoader<String, List<PersonEntity>> dataLoader = registry.getDataLoader("personCompanyIdLoader");
    if (dataLoader != null) {
      return dataLoader.load(company.getId());
    }
    throw new IllegalStateException("No customer data loader found");
  }

  public List<CompanyEntity> partners(CompanyEntity company,
      DataFetchingEnvironment environment) {
    String[] partnerIds = company.getPartnerCompanyIds().split("\\|");
    return companyService.findByIds(partnerIds);
  }

}
