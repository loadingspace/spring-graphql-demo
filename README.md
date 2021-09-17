## SpringBoot GraphQL Demo

代码示例：https://github.com/Loading-Life/spring-graphql-demo

> 参考资料  
> [Graphql-Java][Graphql-Java]  
> [Graphql-Java-Kickstart][Graphql-Java-Kickstart]  
> [GraphQL 在微服务架构中的实践][GraphQL 在微服务架构中的实践]  
> [GraphQL Java从入门到实践][GraphQL Java从入门到实践]  
> [Building a GraphQL Server with Spring Boot][Building a GraphQL Server with Spring Boot]

[Graphql-Java]:www.graphql-java.com
[Graphql-Java-Kickstart]:www.graphql-java-kickstart.com
[GraphQL 在微服务架构中的实践]:https://draveness.me/graphql-microservice/
[GraphQL Java从入门到实践]:https://www.jianshu.com/p/4ede220b713e
[Building a GraphQL Server with Spring Boot]:https://www.pluralsight.com/guides/building-a-graphql-server-with-spring-boot

### [GraphQL-Java预览]

[GraphQL-java预览]:https://www.graphql-java.com/documentation/v16/getting-started/

```
public class HelloWorld {

    public static void main(String[] args) {
    
        //1. 定义schema
        String schema = "type Query{hello: String}";

        //2. 解析schema
        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schema);

        //3. 定义hello方法获取数据的方法
        RuntimeWiring runtimeWiring = new RuntimeWiring()
                .type("Query", builder -> builder.dataFetcher("hello", new StaticDataFetcher("world")))
                .build();

        //4. schema与获取数据方法绑定
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);

        //5. 实例化GraphQL
        GraphQL build = GraphQL.newGraphQL(graphQLSchema).build();
        
        //6. 执行{hello}
        ExecutionResult executionResult = build.execute("{hello}");

        System.out.println(executionResult.getData().toString());
        // Prints: {hello=world}
    }
}
```

### [SpringBoot使用GraphQl]

[SpringBoot使用GraphQl]:https://www.graphql-java-kickstart.com/spring-boot/

#### 1. 添加依赖

[添加依赖]:https://www.graphql-java-kickstart.com/spring-boot/


```
    <!-- graphql springboot starter（必须） -->
    <!-- https://github.com/graphql-java-kickstart/graphql-spring-boot -->
    <dependency>
      <groupId>com.graphql-java-kickstart</groupId>
      <artifactId>graphql-spring-boot-starter</artifactId>
      <version>${graphql.version}</version>
    </dependency>
    <dependency>
      <groupId>com.graphql-java-kickstart</groupId>
      <artifactId>graphql-spring-boot-starter-test</artifactId>
      <version>${graphql.version}</version>
    </dependency>
```

#### 2. [定义schema]

[定义schema]:https://www.graphql-java.com/documentation/v16/schema/

springboot启动后扫描配置`graphql.tools.schema-location-pattern`下的文件，解析schema定义，默认值为`**/*.graphqls`

schema针对资源分为两种：

* `query`:数据查询
* `mutation`:数据更改

graphql类型有六种：

* `Scalar`: graphql 类型系统的叶节点称为标量。一旦你到达一个标量类型，你就不能进一步下降到类型层次结构中。标量类型旨在表示不可分割的值。[Scalar参考]
* `Object`: 对象
* `Interface`
* `Union`
* `InputObject`: 输入对象
* `Enum`

> schema.graphqls中的 UserConnection @connection(for: "PersonEntity") 为Relay的一种简写方式，详情可查看链接  
> Relay : https://www.graphql-java-kickstart.com/tools/relay/


[Scalar参考]:https://www.graphql-java.com/documentation/v16/scalars/

```
schema {
    query : Query
    mutation: Mutation
}

type Query {
    persons: [PersonEntity]
    personsPage(first: Int, after: String): UserConnection @connection(for: "PersonEntity")
    person(id: ID!): PersonEntity
    companies: [CompanyEntity]
    company(id: ID!): CompanyEntity
}

type Mutation {
    createPerson(person: createPersonInput): PersonEntity
    createCompany(company: createCompanyInput): CompanyEntity
}

input createPersonInput {
    name: String
    companyId: String
}

input createCompanyInput {
    name: String
    partnerCompanyIds: String
}

interface Node {
    id: ID!
}

type CompanyEntity implements Node {
    id: ID!
    name: String
    partnerCompanyIds: String
    persons: [PersonEntity]
    partners: [CompanyEntity]
}

type PersonEntity implements Node {
    id: ID!
    name: String
    companyId: String
    company: CompanyEntity
}
```

#### 3. [定义Resolver]和数据类

[定义Resolver]:https://www.graphql-java-kickstart.com/tools/schema-definition/

接下来需在对应的Resolver中实现具体的方法。graphql会根据pojo规范自动的将对象与schema对象映射

例如: 

* schema中`persons: [PersonEntity]`方法，该方法无参数，返回`PersonEntity数组`, 则在`GraphQLQueryResolver`中实现persons方法。

```
@Component
public class PersonQueryResolver implements GraphQLQueryResolver {

  @Autowired
  private PersonService personService;

  public List<PersonEntity> persons() {
    return personService.all();
  }

  ...

}
```

* schema中`company(id: ID!): CompanyEntity`方法，该方法需要一个`id`参数，返回`CompanyEntity`, 则在`GraphQLQueryResolver`中实现persons方法。

```
@Component
public class CompanyQueryResolver implements GraphQLQueryResolver {

  @Autowired
  private CompanyService companyService;

  public CompanyEntity company(String id) {
    return companyService.findById(id);
  }
  
  ...

}
```

往往在实际业务中，我们常常会遇到有关联的数据。

例如: 实例中的`公司company`和`员工person`. 

* 一个`公司`会有多个`员工`
* 一个`员工`只能在一家`公司`工作
* 一个`公司`可能存在多个`合作公司`

此时我们的这种 数据关系 可以在schame中定义(java实体中不需要定义此字段)，在 `GraphQLResolver<T>` 中添加实现即可

例如：一个`公司`可能存在多个`合作公司`场景

```
@Data
@Proxy(lazy = false)
@Entity
public class CompanyEntity {

  @Id
  private String id;

  private String name;

  private String partnerCompanyIds;

  public CompanyEntity(){}

  public CompanyEntity(String id, String name, String partnerCompanyIds){
    this.id = id;
    this.name = name;
    this.partnerCompanyIds = partnerCompanyIds;
  }
}
```

```
@Component
public class CompanyResolver implements GraphQLResolver<CompanyEntity> {

  @Autowired
  private CompanyService companyService;

  public List<CompanyEntity> partners(CompanyEntity company,
      DataFetchingEnvironment environment) {
    String[] partnerIds = company.getPartnerCompanyIds().split("\\|");
    return companyService.findByIds(partnerIds);
  }
  
  ...

}
```

在关联对象中会存在`N+1`问题，该问题graph提供了`BatchLoader`批量加载数据方式解决。当获取到所有带查询的关联数据后，在进行一次查询。

例如: 一个`公司`会有多个`员工`

* 首先需要定义需要在请求上下文中定义`personCompanyIdLoader`

```
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
```

* 在GraphQLResolver中获取`personCompanyIdLoader`并查询数据

```
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

  ...

}
```
### 调试工具GraphIQL

添加依赖后，访问`${address}/graphiql`

```
    <!-- 查询调试工具(可选) -->
    <!-- https://github.com/graphql/graphiql -->
    <dependency>
      <groupId>com.graphql-java-kickstart</groupId>
      <artifactId>graphiql-spring-boot-starter</artifactId>
      <version>${graphql.version}</version>
    </dependency>
```
![graphiql.png](https://github.com/Loading-Life/spring-graphql-demo/edit/master/resource/graphiql.png)

### 调试工具Voyager

添加依赖后，访问`${address}/voyager`

```
    <!-- graphql资源图形展示工具(可选) -->
    <!-- https://github.com/APIs-guru/graphql-voyager -->
    <dependency>
      <groupId>com.graphql-java-kickstart</groupId>
      <artifactId>voyager-spring-boot-starter</artifactId>
      <version>${graphql.version}</version>
    </dependency>
```

![voyager.png](https://github.com/Loading-Life/spring-graphql-demo/edit/master/resource/voyager.png)


















