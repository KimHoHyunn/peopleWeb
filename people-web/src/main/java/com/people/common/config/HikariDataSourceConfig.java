package com.people.common.config;

public abstract class HikariDataSourceConfig {
	public static final String PEOPLE_PACKAGE_PREFIX     	= "com.people.*.dao";
	public static final String PEOPLE_CONFIG_PROPER_PREFIX  	= "spring.datasource.hikari";
}
//@Configuration
//@MapperScan(basePackages =HikariDataSourceConfig.PEOPLE_PACKAGE_PREFIX, sqlSessionFactoryRef="peopleSqlSessionFactory")
//@EnableTransactionManagement
//class peopleDatabaseConfig extends HikariDataSourceConfig {
//	// application.yml의 설정 정보를 토대로 HikariCP 설정
//	@Bean
//	@ConfigurationProperties(prefix = "spring.datasource.hikari") // 읽어올 설정 정보의 prefix 지정
//	public HikariConfig peopleDatasourceConfig() {
//		return new HikariConfig();
//	}
//	
//	// Connection Pool을 관리하는 DataSource 인터페이스 객체 선언
//	@Bean
//	public DataSource peopleDataSource() {
//		return new HikariDataSource(peopleDatasourceConfig());
//	}
//	
//	// SqlSessionTemplate에서 사용할 SqlSession을 생성하는 Factory
//	@Bean
//	public SqlSessionFactory peopleSqlSessionFactory(DataSource dataSource) throws Exception {
//		/*
//		 * MyBatis는 JdbcTemplate 대신 Connection 객체를 통한 질의를 위해서 SqlSession을 사용한다.
//		 * 내부적으로 SqlSessionTemplate가 SqlSession을 구현한다.
//		 * Thread-Safe하고 여러 개의 Mapper에서 공유할 수 있다.
//		 */
//		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
//		bean.setDataSource(dataSource);
//		
//		// MyBatis Mapper Source
//		// MyBatis의 SqlSession에서 불러올 쿼리 정보
//		Resource[] res = new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/**/*.xml");
//		bean.setMapperLocations(res);
//		
//		// MyBatis Config Setting
//		// MyBatis 설정 파일
//		Resource myBatisConfig = new PathMatchingResourcePatternResolver().getResource("classpath:config/mybatis-config.xml");
//		bean.setConfigLocation(myBatisConfig);
//		
//		return bean.getObject();
//	}
//	
//	// DataSource에서 Transaction 관리를 위한 Manager 클래스 등록
//	@Bean
//	public DataSourceTransactionManager peopleTransactionManager(DataSource dataSource) {
//		return new DataSourceTransactionManager(dataSource);
//	}
//}
