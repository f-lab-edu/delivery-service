package me.naming.delieveryservice.controller;

import javax.servlet.http.HttpSession;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.naming.delieveryservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

  @Autowired private OrderService orderService;

  private static ResponseEntity<ResponseResult> CREATE_SUCCESS = new ResponseEntity<>(ResponseResult.SUCCESS, HttpStatus.CREATED);

  @PostMapping("/{id}/req")
  public ResponseEntity<ResponseResult> reqOrder(
      @PathVariable String id, @RequestBody AddressInfo addressInfo, HttpSession httpSession) {

    orderService.reqOrder(
        id,
        addressInfo.getDprtCode(),
        addressInfo.getDprtDetail(),
        addressInfo.getDstinCode(),
        addressInfo.getDstinDetail());
    return CREATE_SUCCESS;
  }

  @Getter
  @RequiredArgsConstructor
  private static class ResponseResult {
    enum ResponseStatus{
      SUCCESS, FAIL, DUPLICATE
    }

    @NonNull
    private ResponseStatus result;

    private static ResponseResult SUCCESS = new ResponseResult(ResponseStatus.SUCCESS);
    private static ResponseResult FAIL = new ResponseResult(ResponseStatus.FAIL);
    private static ResponseResult DUPLICATE = new ResponseResult(ResponseStatus.DUPLICATE);
  }

  @Getter
  @Setter
  private static class AddressInfo {
    @NonNull int dprtCode;
    @NonNull String dprtDetail;
    @NonNull int dstinCode;
    @NonNull String dstinDetail;
  }
}
