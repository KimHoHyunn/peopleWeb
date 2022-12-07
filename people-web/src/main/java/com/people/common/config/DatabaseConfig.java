package com.people.common.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

public abstract class DatabaseConfig {
	public static final String PACKAGE_PREFIX     				= "com.people.*";
	public static final String CONFIG_PROPER_PREFIX  			= "spring.datasource";
	public static final String BASE_MAPPER_LOCATIONS_PATH   	= "classpath*:mapper/**/*.xml";
}

@Configuration
@MapperScan(basePackages =DatabaseConfig.PACKAGE_PREFIX, sqlSessionFactoryRef="sqlSessionFactory")
@EnableTransactionManagement
class PeopleDatabaseConfig extends DatabaseConfig {
	
	@Bean(destroyMethod = "close")
	@ConfigurationProperties(prefix=DatabaseConfig.CONFIG_PROPER_PREFIX)
	public DataSource wDataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);

		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		sessionFactory.setMapperLocations(resolver.getResources(BASE_MAPPER_LOCATIONS_PATH));
		return sessionFactory.getObject();
	}

	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception {
		final SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
		return sqlSessionTemplate;
	}
	
	@Bean
	public PlatformTransactionManager transactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
}
