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
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.extern.slf4j.Slf4j;

public abstract class DatabaseConfig_back {
	public static final String PACKAGE_PREFIX     				= "com.people.*";
	public static final String CONFIG_PROPER_PREFIX  			= "spring.datasource.hikari";
	public static final String JNDICONFIG_PROPER_PREFIX  		= "spring.datasource";
	public static final String BASE_MAPPER_LOCATIONS_PATH   	= "classpath*:mapper/**/*.xml";
}

//@Slf4j
//@Configuration
//@MapperScan(basePackages =DatabaseConfig_back.PACKAGE_PREFIX, sqlSessionFactoryRef="sqlSessionFactory")
//@EnableTransactionManagement
//class PeopleDatabaseConfig extends DatabaseConfig_back {
//	
//	  @Bean
//	  @ConfigurationProperties(prefix =DatabaseConfig_back.JNDICONFIG_PROPER_PREFIX) 
//	  public JndiPropertyHolder getJndiPropertyHolder() {
//	    return new JndiPropertyHolder();
//	  }
//	 
//	  @Primary
//	  @Bean("peopleDataSource")
//	  @Profile("local")
//	  public DataSource jndiDatasource() throws IllegalArgumentException, NamingException {
//	    String jndiName = getJndiPropertyHolder().getJndiName();
//	    log.info("### jndiName = {}",jndiName);
//	    return (DataSource) new JndiDataSourceLookup().getDataSource(jndiName);
//	  }
//	
//	
//	  @Bean(destroyMethod = "close")
//	  @Profile("dev")
//	  @ConfigurationProperties(prefix=DatabaseConfig_back.CONFIG_PROPER_PREFIX)
//	  public DataSource peopleDataSource() {
//		return DataSourceBuilder.create().build();
//	  }
//	
//	  @Bean
//	  public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
//		final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
//		sessionFactory.setDataSource(dataSource);
//
//		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//		sessionFactory.setMapperLocations(resolver.getResources(BASE_MAPPER_LOCATIONS_PATH));
//		return sessionFactory.getObject();
//	  }
//
//	  @Bean
//	  public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception {
//		final SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
//		return sqlSessionTemplate;
//	  }
//	
//	  @Bean
//	  public PlatformTransactionManager transactionManager(DataSource dataSource) {
//		return new DataSourceTransactionManager(dataSource);
//	  }
//	
//	  class JndiPropertyHolder {
//		  private String jndiName;
//	 
//		  public String getJndiName() {
//			  return jndiName;
//		  }
//	 
//		  public void setJndiName(String jndiName) {
//			  this.jndiName = jndiName;
//		  }
//	  }
//}
