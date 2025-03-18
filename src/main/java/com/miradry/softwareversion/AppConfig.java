package com.miradry.softwareversion;

import com.microsoft.azure.storage.CloudStorageAccount;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;


@Configuration
@ComponentScan
//@EnableTransactionManagement
public class AppConfig {
	
	@Autowired
	private Environment env;
	@Bean
	public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer(){
	        return new PropertySourcesPlaceholderConfigurer();
	 }
	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource){
	        return new JdbcTemplate(dataSource);
	    }
	
	
	@Bean
	public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataSource) {
	    DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
	    dataSourceTransactionManager.setDataSource(dataSource);

	    return dataSourceTransactionManager;
	}
	@Bean
	public CloudStorageAccount cloudStorageAccount() throws Exception {
		return CloudStorageAccount.parse(env.getProperty("azure.storage.connection-string"));
	}
	
	@Bean
	public DataSource dataSource(){
	        BasicDataSource dataSource = new BasicDataSource();
	        dataSource.setDriverClassName(env.getProperty("spring.datasource.driverClassName"));
	        dataSource.setUrl(env.getProperty("spring.datasource.url"));
	        dataSource.setUsername(env.getProperty("jdbc.username"));
	        dataSource.setPassword(env.getProperty("jdbc.password"));
	
	        return dataSource;
	    }


}
