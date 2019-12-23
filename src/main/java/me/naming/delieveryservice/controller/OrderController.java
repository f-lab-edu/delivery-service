package me.naming.delieveryservice.controller;

import java.net.URI;
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

  @UserInterface
  @PostMapping("/delivery/address")
  public ResponseEntity deliveryAddress(String userId, @RequestBody AddressDTO addressDTO) {

    orderService.deliveryAddress(
        userId,
        addressDTO.getDepartureCode(),
        addressDTO.getDepartureDetail(),
        addressDTO.getDestinationCode(),
        addressDTO.getDestinationDetail());

    URI uri = ControllerLinkBuilder.linkTo(OrderController.class).slash("/delivery/address").toUri();
    return ResponseEntity.created(uri).build();
  }

}