package nimdanoob.calendarview;

import android.support.annotation.Nullable;
import java.util.ArrayList;

public interface DatePickerController {
  DatePickerController setFirstDate(int year,int month);
  DatePickerController setLastDate(int year,int month);
  DatePickerController setDisableDays(ArrayList<SimpleMonthAdapter.CalendarDay> calendarDays);
  DatePickerController setDayPickerListener(DatePickerListener listener);
  DatePickerController setSelectMode(int mode, @Nullable int fixSelectDay);
  SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> getSelectedDays();
  void updateUi();
}
