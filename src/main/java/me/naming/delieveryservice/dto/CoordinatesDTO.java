package me.naming.delieveryservice.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 카카오 API로부터 전달받은 좌표 값을 전송하기 위한 DTO
 */
@Getter
@Setter
public class CoordinatesDTO {
  private double latitude;
  private double longitude;
}