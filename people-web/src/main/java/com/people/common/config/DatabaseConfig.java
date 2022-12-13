package com.people.common.config;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jndi.JndiObjectFactoryBean;
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
	@Bean
	public DatabaseProperties databaseProperties() {
		return new DatabaseProperties();
	}
	
	@Bean
	public TomcatServletWebServerFactory tomcatFactory() {
		return new TomcatServletWebServerFactory() {
			@Override
			protected TomcatWebServer getTomcatWebServer(Tomcat tomcat) {
				tomcat.enableNaming();
				return super.getTomcatWebServer(tomcat);
			}

			@Override
			protected void postProcessContext(Context context) {
				ContextResource resource = new ContextResource();

				resource.setType("org.apache.tomcat.jdbc.pool.DataSource");
				resource.setProperty("factory", "org.apache.tomcat.jdbc.pool.DataSourceFactory");

//				resource.setName(databaseProperties().getJndiName());
//				resource.setProperty("driverClassName", databaseProperties().driverClassName);
//				resource.setProperty("url", databaseProperties().url);
//				resource.setProperty("username", databaseProperties().username);
//				resource.setProperty("password", databaseProperties().password);

				resource.setName("jndi/mysql");
				resource.setProperty("driverClassName", "net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
				resource.setProperty("url", "jdbc:log4jdbc:mysql://localhost:3306/people?serverTimezone=UTC&characterEncoding=UTF-8");
				resource.setProperty("username", "people");
				resource.setProperty("password", "~~ksj122400");
				
				
				context.getNamingResources().addResource(resource);
			}
		};
	}

	@Bean
	@Profile("dev")
	public DataSource jndiDataSource() throws IllegalArgumentException, NamingException {
		JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
		bean.setJndiName("java:comp/env/" + "jndi/mysql");
		bean.setProxyInterface(DataSource.class);
		bean.setLookupOnStartup(false);
		bean.afterPropertiesSet();
		return (DataSource) bean.getObject();
	}
	
	
	  @Bean
	  @Profile("local")
	  @ConfigurationProperties(prefix=DatabaseConfig.CONFIG_PROPER_PREFIX)
	  public DataSource peopleDataSource() {
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
