package com.app.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;



@Configuration
@EnableCaching
public class EhCacheConfig {

	@Bean
	public CacheManager cacheManager(){
		return new EhCacheCacheManager(ehCacheManagerFactory().getObject());
	}

	@Bean
	public EhCacheManagerFactoryBean ehCacheManagerFactory(){
		EhCacheManagerFactoryBean ehCacheBean = new EhCacheManagerFactoryBean();
		ehCacheBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
		ehCacheBean.setShared(true);
		return ehCacheBean;
	}

}
