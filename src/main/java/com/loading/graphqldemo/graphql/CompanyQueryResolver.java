package com.loading.graphqldemo.graphql;

import com.loading.graphqldemo.entity.CompanyEntity;
import com.loading.graphqldemo.service.CompanyService;
import graphql.kickstart.tools.GraphQLQueryResolver;
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
public class CompanyQueryResolver implements GraphQLQueryResolver {

  @Autowired
  private CompanyService companyService;

  public List<CompanyEntity> companies() {
    return companyService.all();
  }

  public CompanyEntity company(String id) {
    return companyService.findById(id);
  }

}
