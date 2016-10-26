package nimdanoob.calendarview;

import java.util.ArrayList;

public interface DatePickerController {
  void setStartDate();
  void setDisableDay(SimpleMonthAdapter.CalendarDay calendarDay);
  void setDisableDays(ArrayList<SimpleMonthAdapter.CalendarDay> calendarDays);
}
