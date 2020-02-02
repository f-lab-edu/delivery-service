package me.naming.delieveryservice.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")       // application.properties에 작성된 'spring.datasource.hikari' 설정
    public HikariConfig hikariConfig() {
        return new HikariConfig();
    }

    @Bean
    public DataSource dataSource(HikariConfig hikariConfig) {
        DataSource dataSource = new HikariDataSource(hikariConfig);     // HikariConfig 빈 객체 주입
        return dataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);                                    // 위에서 생성된 datasource 빈 객체 주입
        sqlSessionFactoryBean.setVfs(SpringBootVFS.class);                                  // 스프링부트 가상파일 시스템을 설정
        sqlSessionFactoryBean.setTypeAliasesPackage("me.naming.delieveryservice.dto");       // alias 대상 클래스가 위치한 패키지 경로 설정
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mapper/*.xml"));        // Mapper 파일 위치 셋팅
        SqlSessionFactory sqlFactory = sqlSessionFactoryBean.getObject();                   // SqlSessionFactoryBean을 활용해 Mybatis 구성 파일 없이 SqlSessionFactory 빌드
        sqlFactory.getConfiguration().setMapUnderscoreToCamelCase(true);

        return sqlFactory;
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {     // 'SqlSessionFactory' 빈 객체 주입
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
