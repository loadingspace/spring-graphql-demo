package com.loading.graphqldemo.graphql;

import com.loading.graphqldemo.entity.CompanyEntity;
import com.loading.graphqldemo.service.CompanyService;
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
public class CompanyMutationResolver implements GraphQLMutationResolver {

  @Autowired
  private CompanyService companyService;

  public CompanyEntity createCompany(CompanyEntity company) {
    return companyService.save(company);
  }


}
