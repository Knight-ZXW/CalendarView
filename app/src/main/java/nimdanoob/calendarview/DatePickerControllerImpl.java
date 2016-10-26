package nimdanoob.calendarview;

public class DatePickerControllerImpl implements DatePickerController {
  private DatePickerView mDatePickerView;

  public DatePickerControllerImpl(DatePickerView datePickerView) {
    mDatePickerView = datePickerView;
  }

  @Override public void setStartDate() {
  }

  @Override public void setDayDisable(SimpleMonthAdapter.CalendarDay calendarDay) {
  }
}
