## SpringBoot GraphQL Demo

> 参考资料  
> [graphql-java][graphql-java]  
> [graphql-java-kickstart][graphql-java-kickstart]  
> [GraphQL 在微服务架构中的实践][GraphQL 在微服务架构中的实践]  
> [GraphQL Java从入门到实践][GraphQL Java从入门到实践]

[graphql-java]:www.graphql-java.com
[graphql-java-kickstart]:www.graphql-java-kickstart.com
[GraphQL 在微服务架构中的实践]:https://draveness.me/graphql-microservice/
[GraphQL Java从入门到实践]:https://www.jianshu.com/p/4ede220b713e

### [GraphQL-Java预览]

[GraphQL-java预览]:https://www.graphql-java.com/documentation/v16/getting-started/

```
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.StaticDataFetcher;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;

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
    
    <!-- 查询调试工具(可选) -->
    <!-- https://github.com/graphql/graphiql -->
    <dependency>
      <groupId>com.graphql-java-kickstart</groupId>
      <artifactId>graphiql-spring-boot-starter</artifactId>
      <version>${graphql.version}</version>
    </dependency>
    
    <!-- graphql资源图形展示工具(可选) -->
    <!-- https://github.com/APIs-guru/graphql-voyager -->
    <dependency>
      <groupId>com.graphql-java-kickstart</groupId>
      <artifactId>voyager-spring-boot-starter</artifactId>
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

#### [定义Resolver]和数据类

[定义Resolver]:https://www.graphql-java-kickstart.com/tools/schema-definition/

GraphQL Java tools会自动将schema定义的对象映射到java对象的方法和属性上。因此只需在对应的Resolver中实现Query和Mutation中相关的方法提供数据即可。












