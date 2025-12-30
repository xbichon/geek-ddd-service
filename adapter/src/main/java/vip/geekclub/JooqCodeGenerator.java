package vip.geekclub;

import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.*;
import org.jooq.meta.jaxb.Strategy;
import org.jooq.meta.jaxb.Matchers;
import org.jooq.meta.jaxb.MatcherRule;
import org.jooq.meta.jaxb.MatcherRule.*;
import org.jooq.meta.jaxb.MatcherTransformType;


public class JooqCodeGenerator {
    public static void main(String[] args) throws Exception {
        // 直接在代码中设置数据库配置
        Configuration configuration = new org.jooq.meta.jaxb.Configuration();

        // 设置数据库连接
        configuration.setJdbc(new Jdbc()
                .withDriver("com.mysql.cj.jdbc.Driver")
                .withUrl("jdbc:mysql://localhost:3306/test?useSSL=true&serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8")
                .withUser("root")
                .withPassword("12345678")
        );

        // 设置生成器配置（这部分可以从XML加载，也可以硬编码）
        configuration.setGenerator(new Generator()
                .withDatabase(new Database()
                        .withName("org.jooq.meta.mysql.MySQLDatabase")
                        .withInputSchema("test")
                        .withIncludes(".*")
                )
                .withGenerate(new Generate()
                        .withRecords(false)
                        .withInterfaces(true)
                        .withPojos(true)
                        .withFluentSetters(true)
                        .withJavaTimeTypes(true)
                        .withImmutablePojos(true)
                        .withSerializablePojos(true)
                )
                .withTarget(new Target()
                        .withPackageName("vip.geekclub.security.query.dsl")
                        .withDirectory("adapter/src/main/java")
                )
                // 保留您的策略配置
//                .withStrategy(new Strategy()
//                        .withMatchers(new Matchers()
//                                .withTables(new MatchersTableType()
//                                        .withTableClass(new MatcherRule()
//                                                .withExpression(".*?_(.*)$")
//                                                .withTransform(MatcherTransformType.PASCAL)
//                                                .withExpression("$1_Table")
//                                        )
//                                        .withTableIdentifier(new MatcherRule()
//                                                .withExpression(".*?_(.*)$")
//                                                .withTransform(MatcherTransformType.PASCAL)
//                                                .withExpression("$1")
//                                        )
//                                        .withPojoClass(new MatcherRule()
//                                                .withExpression(".*?_(.*)$")
//                                                .withTransform(MatcherTransformType.PASCAL)
//                                                .withExpression("$1_Dto")
//                                        )
//                                )
//                        )
//                )
        );

        GenerationTool.generate(configuration);
    }
}