package nimdanoob.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import java.util.ArrayList;
import nimdanoob.calendarview.CalendarPicker;
import nimdanoob.calendarview.DatePickerListener;
import nimdanoob.calendarview.R;
import nimdanoob.calendarview.SimpleMonthAdapter;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    CalendarPicker calendarPicker = (CalendarPicker) findViewById(R.id.calendarPicker);
    ArrayList<SimpleMonthAdapter.CalendarDay> calendarDays =
        new ArrayList<>();
    calendarDays.add(new SimpleMonthAdapter.CalendarDay(2000, 2, 3));
    calendarPicker.getController()
        .setDisableDays(calendarDays)
        .setDayPickerListener(new DatePickerListener() {
          @Override public void onDayOfMonthSelected(int year, int month, int day) {
            Log.e("zxw", "onDayOfMonthSelected"+"year is " + year + ":" + month + ":" + day);
          }

          @Override public void onDateRangeSelected(
              SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> selectedDays) {
            Log.e("zxw", "onDateRangeSelected"+selectedDays.getFirst() + ":" + selectedDays.getLast());
          }
        }).setEndDate(2002, 1)
        .updateUi();

    calendarPicker.setOnConfirmListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Log.e("zxw", "confirm");
      }
    });
  }
}
