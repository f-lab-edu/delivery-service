package me.naming.delieveryservice.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
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

  /**
   * 주문정보 등록
   *  - 배달정보(출발지, 도착지)와 물품정보를 등록한다.
   * @param userId
   * @param orderInfoDTO
   * @return
   */
  @PostMapping("/users/{userId}")
  public ResponseEntity<Map<String, Integer>> orderInfo(@PathVariable String userId, @RequestBody OrderInfoDTO orderInfoDTO) {

    orderInfoDTO.setUserId(userId);
    int orderNum = orderService.orderInfo(orderInfoDTO);
    Map <String, Integer> map = new HashMap<>();
    map.put("orderNum", orderNum);

    return ResponseEntity.status(HttpStatus.CREATED).body(map);
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

}