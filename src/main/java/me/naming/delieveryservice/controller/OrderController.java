package me.naming.delieveryservice.controller;

import java.net.URI;
import lombok.Getter;
import lombok.NonNull;
import me.naming.delieveryservice.aop.CheckSessionUserId;
import me.naming.delieveryservice.aop.UserInterface;
import me.naming.delieveryservice.dto.AddressDTO;
import me.naming.delieveryservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
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
  @UserInterface
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
  public ResponseEntity deliveryProduct(@RequestBody ProductInfo productInfo) {

    orderService.deliveryProduct(
        productInfo.getCategory(),
        productInfo.getBrandName(),
        productInfo.getProductName(),
        productInfo.getComment(),
        productInfo.getOrderNum());

    URI uri = ControllerLinkBuilder.linkTo(OrderController.class).slash("/delivery/product").toUri();

    return ResponseEntity.created(uri).build();
  }

  @Getter
  private static class ProductInfo {
    @NonNull String category;
    @NonNull String brandName;
    @NonNull String productName;
    String comment;
    @NonNull int orderNum;
  }
}