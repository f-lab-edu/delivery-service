package me.naming.delieveryservice.controller;

import java.net.URI;
import java.util.List;
import me.naming.delieveryservice.aop.CheckSessionUserId;
import me.naming.delieveryservice.aop.UserIdObjParam;
import me.naming.delieveryservice.dto.AddressDTO;
import me.naming.delieveryservice.dto.ProductInfoDTO;
import me.naming.delieveryservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
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
   * 주문 주소지 등록
   * insert 후 등록된 pk값 전달하기 위해 Body에 addressDTO 리턴
   * @param userId
   * @param addressDTO
   * @return
   */
  @UserIdObjParam
  @PostMapping("/address")
  public ResponseEntity deliveryAddress(String userId, @RequestBody AddressDTO addressDTO) {

    addressDTO.setUserId(userId);
    orderService.deliveryAddress(addressDTO);

    return ResponseEntity.ok(addressDTO);
  }

  /**
   * 주문 상품 등록
   * @param productInfo
   * @return
   */
  @CheckSessionUserId
  @PostMapping("/product")
  public ResponseEntity deliveryProduct(@RequestBody ProductInfoDTO productInfo) {

    orderService.deliveryProduct(
        productInfo.getCategory(),
        productInfo.getBrandName(),
        productInfo.getProductName(),
        productInfo.getComment(),
        productInfo.getOrderNum());

    URI uri = ControllerLinkBuilder.linkTo(OrderController.class).slash("/delivery/product").toUri();

    return ResponseEntity.created(uri).build();
  }

  @CheckSessionUserId
  @GetMapping("/product/{orderNum}")
  public ResponseEntity productInfo(@PathVariable int orderNum){

    List<ProductInfoDTO> productList = orderService.productInfoDetail(orderNum);
    return ResponseEntity.ok(productList);
  }
}