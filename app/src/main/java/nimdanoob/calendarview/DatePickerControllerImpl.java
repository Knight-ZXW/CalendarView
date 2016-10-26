package nimdanoob.calendarview;

import java.util.ArrayList;

public class DatePickerControllerImpl implements DatePickerController {
  private DatePickerView mDatePickerView;

  public DatePickerControllerImpl(DatePickerView datePickerView) {
    mDatePickerView = datePickerView;
  }

  @Override public void setStartDate() {
  }

  @Override public void setDisableDay(SimpleMonthAdapter.CalendarDay calendarDay) {
    mDatePickerView.getAdapter();
  }

  @Override public void setDisableDays(ArrayList<SimpleMonthAdapter.CalendarDay> calendarDays) {
    ((SimpleMonthAdapter)mDatePickerView.getAdapter()).setDisableDays(calendarDays);
    ((SimpleMonthAdapter)mDatePickerView.getAdapter()).notifyDataSetChanged();
  }
}
