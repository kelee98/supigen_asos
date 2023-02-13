package com.spigen.asos;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpigenWebApplication {


	public static void main(String[] args) {
		SpringApplication.run(SpigenWebApplication.class, args);
	}

    /*
     * JSON
    */
    @Bean
    public MappingJackson2JsonView jsonView(){
        return new MappingJackson2JsonView();
    }
    
    @Bean
    public MultipartConfigElement multipartConfigElement() {
      MultipartConfigFactory factory = new MultipartConfigFactory();

      //factory.setMaxFileSize(uploadMaxFileSize);
      //factory.setMaxRequestSize(uploadMaxRequestSize);

      return factory.createMultipartConfig();
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
    	CommonsMultipartResolver  mr = new CommonsMultipartResolver ();
    	mr.setMaxUploadSize(2097152000);
    	mr.setMaxInMemorySize(1048576);
    	mr.setDefaultEncoding("UTF-8");
      return mr;
    }
    
    @Bean(name = "pdfViewer")
    public ViewResolver beanNameViewResolver() {
        BeanNameViewResolver resolver = new BeanNameViewResolver();
        return resolver;
    }

    @Bean
    public SpringSecurityDialect springSecurityDialect(){
        return new SpringSecurityDialect();
    }
}
