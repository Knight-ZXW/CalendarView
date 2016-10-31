package nimdanoob.calendarview;

import android.support.annotation.Nullable;
import android.view.View;
import java.util.ArrayList;

public interface DatePickerController {
  DatePickerController setFirstDate(int year,int month);
  DatePickerController setEndDate(int year,int month);
  DatePickerController setDisableDay(SimpleMonthAdapter.CalendarDay calendarDay);
  DatePickerController setDisableDays(ArrayList<SimpleMonthAdapter.CalendarDay> calendarDays);
  DatePickerController setDayPickerListener(DatePickerListener listener);
  DatePickerController setSelectMode(int mode, @Nullable int fixSelectDay);
  DatePickerController setOnConfirmListener(View.OnClickListener listener);
  SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> getSelectedDays();
  void updateUi();
}
