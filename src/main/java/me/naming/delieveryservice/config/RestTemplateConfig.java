package me.naming.delieveryservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate을 Bean으로 생성하는 이유 : Spring 프레임워크의 핵심 기능인 IoC를 활용해 객체를 관리해주기 위해 생성했다.
 * RestTemplate을 Bean으로 설정해도 괜찮은 이유는 RestTemplate은 스레드 세이프하기 때문에 여러 스레드가 동시에 접근해도 원하는 결과 값을 출력할 수 있다.
 * 참고
 *  - https://spring.io/blog/2009/03/27/rest-in-spring-3-resttemplate
 */
@Configuration
public class RestTemplateConfig {

  @Bean
  public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    return restTemplate;
  }
}
