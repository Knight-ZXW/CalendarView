package nimdanoob.calendarview;

public interface DatePickerListener {
  void onDayOfMonthSelected(int year,int month, int day);
  void onDateRangeSelected( final SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> selectedDays);
}
