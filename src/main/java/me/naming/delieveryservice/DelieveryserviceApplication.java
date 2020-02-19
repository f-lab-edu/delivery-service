package me.naming.delieveryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @EnableCaching 현위치에 작성한 이유
 * 기본설정으로만 사용할 것이기 때문에 CacheConfig 클래스를 따로 생성하지 않고 어노테이션을 사용했습니다.
 * 아무런 설정을 하지 않을 경우 SimpleCacheConfiguration이 동작하게되며,
 * CacheManager는 ConcurrentMapCacheManager가 빈으로 등록됩니다.
 * Cache 종류에는 Redis, Ehcache, caffeine 등이 존재하지만, JSR-107 기준으로 설계된 것이라면,
 * Spring에서 추상화된 CacheManager를 통해 사용 할 수 있습니다.
 *
 * Cache를 사용한 이유는
 *  - DB에서 동일하게 반복적으로 갖고오는 요금 정보를 Cache에 저장함으로써 불필요한 리소스 낭비를 줄여주기 위해서 입니다.
 *
 * 참고
 * 스프링 공식 문서 : https://docs.spring.io/spring/docs/4.1.x/spring-framework-reference/html/cache.html
 */
@EnableAspectJAutoProxy
@EnableCaching
@SpringBootApplication
public class DelieveryserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DelieveryserviceApplication.class, args);
    }
}
