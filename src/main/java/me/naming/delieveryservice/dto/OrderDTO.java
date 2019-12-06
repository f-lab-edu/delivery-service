package me.naming.delieveryservice.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;
import org.springframework.hateoas.ResourceSupport;

@Getter
@Setter
@Alias("OrderDTO")
public class OrderDTO extends ResourceSupport {
  private int orderNum;
  private Date reqDate;
  private String orderStatus;
  private Date arrTime;
  private Date exArrTime;

  private String userId;
  private String deliveryId;
  private int departureCode;
  private String departureDetail;
  private int destinationCode;
  private String destinationDetail;
}
