package me.naming.delieveryservice.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import me.naming.delieveryservice.dto.CoordinatesDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Log4j2
@Component
public class KakaoAPI {

  @Value("${kakao.map.rest-api.key}") String kakaoRestApiKey;
  @Value("${kakao.map.url}") String kakaoMapUrl;

  /**
   * '도로명 주소'를 좌표 값으로 변환
   *  - 카카오 API를 활용해 좌표 값 구하기
   *  - 참고 : https://developers.kakao.com/docs/restapi/local#주소-검색
   * @param address
   * @return
   */
  public CoordinatesDTO getCoordinatesByAddress(String address) {
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders httpHeaders = new HttpHeaders();
    CoordinatesDTO coordinatesDTO = new CoordinatesDTO();

    httpHeaders.add("Authorization", kakaoRestApiKey);
    UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(kakaoMapUrl)
        .queryParam("query", address)
        .build(false);

    ResponseEntity<String> responseEntity = restTemplate.exchange(uriComponents.toUriString(), HttpMethod.GET, new HttpEntity<String>(httpHeaders), String.class);
    JsonObject jsonObject = new Gson().fromJson(responseEntity.getBody(), JsonObject.class);
    JsonArray getDocuments = jsonObject.get("documents").getAsJsonArray();

    double longitude = getDocuments.get(0).getAsJsonObject().get("x").getAsDouble();
    double latitude = getDocuments.get(0).getAsJsonObject().get("y").getAsDouble();

    coordinatesDTO.setLatitude(latitude);
    coordinatesDTO.setLongitude(longitude);

    return coordinatesDTO;
  }

}
