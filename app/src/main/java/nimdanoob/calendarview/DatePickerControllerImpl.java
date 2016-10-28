package nimdanoob.calendarview;

import android.support.annotation.Nullable;
import java.util.ArrayList;

public class DatePickerControllerImpl implements DatePickerController {
  private DatePickerView mDatePickerView;

  public DatePickerControllerImpl(DatePickerView datePickerView) {
    mDatePickerView = datePickerView;
  }
  public DatePickerController setEndDate(int year,int month){
    mDatePickerView.getSimpleMonthAdapter().setLastDate(year,month);
    return this;
  }
  @Override public DatePickerController setFirstDate(int year,int month) {
    mDatePickerView.getSimpleMonthAdapter().setFirstDate(year,month);
    return this;
  }

  @Override public DatePickerController setDisableDay(SimpleMonthAdapter.CalendarDay calendarDay) {
    return this;
  }

  @Override public DatePickerController setDisableDays(
      ArrayList<SimpleMonthAdapter.CalendarDay> calendarDays) {
    mDatePickerView.getSimpleMonthAdapter().setDisableDays(calendarDays);
    mDatePickerView.getAdapter().notifyDataSetChanged();
    return this;
  }

  @Override public DatePickerController setDayPickerListener(DatePickerListener listener) {
    mDatePickerView.getSimpleMonthAdapter().setDatePickerListener(listener);
    return this;
  }

  @Override public DatePickerController setSelectMode(int mode, @Nullable int fixSelectDay) {
    mDatePickerView.getSimpleMonthAdapter().setSelectMode(mode,fixSelectDay);
    return this;
  }

  @Override
  public SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> getSelectedDays() {
    return mDatePickerView.getSimpleMonthAdapter().getSelectedDays();
  }

  @Override public void updateUi() {
    mDatePickerView.getSimpleMonthAdapter().notifyDataSetChanged();
  }

}
