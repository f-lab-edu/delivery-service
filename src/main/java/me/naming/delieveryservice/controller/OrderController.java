package me.naming.delieveryservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.naming.delieveryservice.dto.OrderInfoDTO;
import me.naming.delieveryservice.dto.UserOrderListDTO;
import me.naming.delieveryservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

  @Autowired OrderService orderService;
  @Autowired ObjectMapper objectMapper;

  /**
   * 주문정보 등록
   *  - 배달정보(출발지, 도착지)와 물품정보를 등록한다.
   * @param userId
   * @param orderInfoRequest
   * @return
   */
  @PostMapping("/users/{userId}")
  public ResponseEntity<OrderNum> orderInfo(@PathVariable String userId, @RequestBody OrderInfoRequest orderInfoRequest) {

    OrderInfoDTO orderInfoDTO = OrderInfoDTO.builder()
        .userId(userId)
        .departureCode(orderInfoRequest.getDepartureCode())
        .departureDetail(orderInfoRequest.getDepartureDetail())
        .destinationCode(orderInfoRequest.getDestinationCode())
        .destinationDetail(orderInfoRequest.getDestinationDetail())
        .category(orderInfoRequest.getCategory())
        .brandName(orderInfoRequest.getBrandName())
        .productName(orderInfoRequest.getProductName())
        .comment(orderInfoRequest.getComment())
        .build();

    int orderNum = orderService.orderInfo(orderInfoDTO);

    return ResponseEntity.status(HttpStatus.CREATED).body(new OrderNum(orderNum));
  }

  /**
   * 사용자 주문정보 조회
   * @param userId
   * @return
   */
  @GetMapping("/users/{userId}")
  public List<UserOrderListDTO> userOrderList(@PathVariable String userId) {
    List<UserOrderListDTO> orderList = orderService.userOrderList(userId);

    return orderList;
  }

  // --------------- Body로 Request 받을 데이터 지정 ---------------
  @Getter
  private static class OrderInfoRequest {
    // 배달(출발지, 도착지)주소
    @NonNull private int departureCode;
    @NonNull private String departureDetail;
    @NonNull private int destinationCode;
    @NonNull private String destinationDetail;

    // 상품정보
    @NonNull private String category;
    @NonNull private String brandName;
    @NonNull private String productName;
    private String comment;
  }

  // ---------- 주문번호를 리턴하기 위한 클래스
  @RequiredArgsConstructor
  @Data
  private class OrderNum{
    @NonNull private int orderNum;
  }
}