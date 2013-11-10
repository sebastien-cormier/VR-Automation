package fr.cormier.vra.web.services.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages="fr.cormier.vra.web.services.controller, fr.cormier.vra.service.impl")
public class WebServicesConfig {

	   @Bean
	    public InternalResourceViewResolver viewResolver() {
	        InternalResourceViewResolver resolver = 
	                    new InternalResourceViewResolver();
	        resolver.setPrefix("/WEB-INF/view/");
	        resolver.setSuffix(".jsp");
	        return resolver;
	    }
	   
}
