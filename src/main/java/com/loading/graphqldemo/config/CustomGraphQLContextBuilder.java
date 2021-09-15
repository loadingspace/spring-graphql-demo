package com.loading.graphqldemo.config;

import com.loading.graphqldemo.entity.CompanyEntity;
import com.loading.graphqldemo.entity.PersonEntity;
import com.loading.graphqldemo.service.CompanyService;
import com.loading.graphqldemo.service.PersonService;
import graphql.kickstart.execution.context.DefaultGraphQLContext;
import graphql.kickstart.execution.context.GraphQLContext;
import graphql.kickstart.servlet.context.DefaultGraphQLServletContext;
import graphql.kickstart.servlet.context.DefaultGraphQLWebSocketContext;
import graphql.kickstart.servlet.context.GraphQLServletContextBuilder;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import javax.websocket.server.HandshakeRequest;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * desc:
 *
 * @author Lo_ading
 * @version 1.0.0
 * @date 2021/9/15
 */
@Component
public class CustomGraphQLContextBuilder implements GraphQLServletContextBuilder {

  @Autowired
  private CompanyService companyService;

  @Autowired
  private PersonService personService;

  @Override
  public GraphQLContext build(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse) {
    return DefaultGraphQLServletContext
        .createServletContext(buildDataLoaderRegistry(), null)
        .with(httpServletRequest)
        .with(httpServletResponse)
        .build();
  }

  @Override
  public GraphQLContext build(Session session, HandshakeRequest handshakeRequest) {
    return DefaultGraphQLWebSocketContext
        .createWebSocketContext(buildDataLoaderRegistry(), null)
        .with(session)
        .with(handshakeRequest)
        .build();
  }

  @Override
  public GraphQLContext build() {
    return new DefaultGraphQLContext(buildDataLoaderRegistry(), null);
  }

  private DataLoaderRegistry buildDataLoaderRegistry() {

    //companyService.findByIds(companyIds)返回数据的顺序，需要和参数顺序一致
    DataLoader<String, CompanyEntity> companyIdLoader =
        new DataLoader<>(companyIds -> CompletableFuture
            .supplyAsync(() -> companyService.findByIds(companyIds)));

    DataLoader<String, List<PersonEntity>> personCompanyIdLoader =
        new DataLoader<>(
            keys -> CompletableFuture.supplyAsync(() -> personService.findByCompanyIds(keys)));

    DataLoaderRegistry dataLoaderRegistry = new DataLoaderRegistry();
    dataLoaderRegistry.register("companyIdLoader", companyIdLoader);
    dataLoaderRegistry.register("personCompanyIdLoader", personCompanyIdLoader);
    return dataLoaderRegistry;
  }

}
