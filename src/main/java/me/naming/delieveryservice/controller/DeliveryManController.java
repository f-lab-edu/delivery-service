package me.naming.delieveryservice.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import me.naming.delieveryservice.dto.DeliveryManDTO;
import me.naming.delieveryservice.service.DeliveryManService;
import me.naming.delieveryservice.utils.EnumKeyUtil;
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
@RequestMapping("/deliverymen")
public class DeliveryManController {

  @Autowired DeliveryManService deliveryManService;

  @PostMapping(value = "/signup")
  public ResponseEntity signUpDeliveryManInfo(@RequestBody @Valid DeliveryManDTO deliveryManDTO) {
    deliveryManService.addDeliveryManInfo(deliveryManDTO);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping(value = "/{id}/exists")
  public ResponseEntity idExistsCheck(@PathVariable String id) {
    if (deliveryManService.checkIdDuplicate(id)) return ResponseEntity.status(HttpStatus.CONFLICT).build();
    return ResponseEntity.ok().build();
  }

  @PostMapping(value = "/login")
  public ResponseEntity loginDeliveryMan(@RequestBody DeliveryManLoginRequest deliveryManLoginRequest, HttpSession httpSession) {
    DeliveryManDTO deliveryManDTO = deliveryManService.getDeliveryManInfo(deliveryManLoginRequest.getId(), deliveryManLoginRequest.getPassword());
    httpSession.setAttribute(EnumKeyUtil.DELIVERY_MAN_SESSION_KEY.toString(), deliveryManDTO);
    return ResponseEntity.ok().build();
  }

  @Getter
  private static class DeliveryManLoginRequest {
    @NotNull String id;
    @NotNull String password;
  }
}
