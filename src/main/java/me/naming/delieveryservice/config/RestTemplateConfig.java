package me.naming.delieveryservice.config;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;
import java.io.IOException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate을 Bean으로 생성하는 이유
 * Spring 프레임워크의 핵심 기능인 IoC를 활용해 객체를 관리해주기 위해 생성했다.
 * RestTemplate을 Bean으로 설정해도 괜찮은 이유는 RestTemplate은 스레드 세이프하기 때문에 여러 스레드가 동시에 접근해도
 * 원하는 결과 값을 출력할 수 있다.
 */
@Configuration
public class RestTemplateConfig {

  private class RestTemplateErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
      return (httpResponse.getStatusCode().series() == CLIENT_ERROR
          || httpResponse.getStatusCode().series() == SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {

      // handle SERVER_ERROR
      if (httpResponse.getStatusCode().series() == SERVER_ERROR) {
        throw new RuntimeException("API측 서버 에러 발생");

      // handle CLIENT_ERROR
      } else if (httpResponse.getStatusCode().series() == CLIENT_ERROR) {

        if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND)
          throw new RuntimeException("API URL 주소가 존재하지 않습니다");

        if (httpResponse.getStatusCode() == HttpStatus.UNAUTHORIZED)
          throw new RuntimeException("클라이언트측에서 헤더에 AppKey를 추가하지 않았습니다");

        if (httpResponse.getStatusCode() == HttpStatus.BAD_REQUEST)
          throw new RuntimeException("클라이언트측에서 쿼리 파라미터가 존재하지 않습니다");
      }
    }
  }

  @Bean
  public RestTemplate restTemplate() {
    RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
    RestTemplate restTemplate = restTemplateBuilder.errorHandler(new RestTemplateErrorHandler()).build();

    return restTemplate;
  }
}
