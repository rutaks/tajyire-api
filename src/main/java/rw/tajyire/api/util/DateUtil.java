package rw.tajyire.api.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
  public static Date addHoursToJavaUtilDate(Date date, int hours) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.HOUR_OF_DAY, hours);
    return calendar.getTime();
  }
}
