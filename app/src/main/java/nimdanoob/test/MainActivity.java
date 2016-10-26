package nimdanoob.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;
import nimdanoob.calendarview.CalendarPicker;
import nimdanoob.calendarview.R;
import nimdanoob.calendarview.SimpleMonthAdapter;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    CalendarPicker calendarPicker= (CalendarPicker) findViewById(R.id.calendarPicker);
    ArrayList<SimpleMonthAdapter.CalendarDay> calendarDays = new ArrayList<SimpleMonthAdapter.CalendarDay>();
    calendarDays.add(new SimpleMonthAdapter.CalendarDay(2000,2,3));
    calendarPicker.getController().setDisableDays(calendarDays);
  }
}
