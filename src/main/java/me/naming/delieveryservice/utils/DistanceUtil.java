package me.naming.delieveryservice.utils;

public class DistanceUtil {

  /**
   * 좌표 값을 활용해 직선거리를 구하기 위한 메소드.
   * 직선거리는 km 단위이다.
   * @param departureLat
   * @param departureLon
   * @param destinationLat
   * @param destinationLon
   * @return
   */
  public static double kmDistanceByCoordinates(double departureLat, double departureLon, double destinationLat, double destinationLon) {

    double theta = departureLon - destinationLon;
    double dist = Math.sin(deg2rad(departureLat)) * Math.sin(deg2rad(destinationLat)) + Math.cos(deg2rad(departureLat)) * Math.cos(deg2rad(destinationLat)) * Math.cos(deg2rad(theta));

    dist = Math.acos(dist);
    dist = rad2deg(dist);
    dist = dist * 60 * 1.1515 * 1.609344;

    return Math.ceil(dist*100)/100.0;   // 소수점 2자리까지 올림 형태로 변환한다.
  }

  private static double deg2rad(double deg) {
    return (deg * Math.PI / 180.0);
  }

  private static double rad2deg(double rad) {
    return (rad * 180 / Math.PI);
  }

}
