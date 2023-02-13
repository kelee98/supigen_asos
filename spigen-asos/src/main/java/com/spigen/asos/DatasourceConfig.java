package com.spigen.asos;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@MapperScan(basePackages = "com.spigen.asos.service.impl")
@EnableTransactionManagement
public class DatasourceConfig {


	 /*
     * SqlSessionFactory
     */
    @Bean(name = "SqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception{
        
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		//sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath*:/mappers/*.xml"));
		Resource[] res = new PathMatchingResourcePatternResolver().getResources("classpath:mappers/*Mapper.xml");
		sqlSessionFactoryBean.setMapperLocations(res);
		
		return sqlSessionFactoryBean.getObject();
    }
    
    /*
     * SqlSession
     */
    @Bean(name = "SqlSession")
    public SqlSessionTemplate sqlSession(SqlSessionFactory sqlSessionFactory) throws Exception{
    	return new SqlSessionTemplate(sqlSessionFactory);
    }

}
