package me.naming.delieveryservice.dto;

import java.util.Date;
import lombok.Getter;

@Getter
public class UserOrderListDTO {
  private int orderNum;
  private Date reqDate;
  private String orderStatus;
  private Date arrTime;
  private Date expectArrTime;

  private int departureCode;
  private String departureDetail;
  private int destinationCode;
  private String destinationDetail;
  private String productName;
}
