package nimdanoob.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import nimdanoob.calendarview.R;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    //CalendarPicker calendarPicker = (CalendarPicker) findViewById(R.id.calendarPicker);
    //ArrayList<SimpleMonthAdapter.CalendarDay> calendarDays =
    //    new ArrayList<>();
    //calendarDays.add(new SimpleMonthAdapter.CalendarDay(2000, 2, 3));
    //calendarPicker.getController().setDisableDays(calendarDays);
    //calendarPicker.getController().setDayPickerListener(new DatePickerListener() {
    //  @Override public void onDayOfMonthSelected(int year, int month, int day) {
    //    Log.e("zxw", "year is " + year + ":" + month + ":" + day);
    //  }
    //
    //  @Override public void onDateRangeSelected(
    //      SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> selectedDays) {
    //    Log.e("zxw", selectedDays.getFirst() + ":" + selectedDays.getLast());
    //  }
    //});
    //calendarPicker.getController().setEndDate(2002,1);
    //calendarPicker.getDayPickerView().getAdapter().notifyDataSetChanged();
  }


}
