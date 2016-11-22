package nimdanoob.calendarview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;
import nimdanoob.calenderpickerview.CalendarPicker;
import nimdanoob.calenderpickerview.SimpleMonthAdapter;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    CalendarPicker calendarPicker = (CalendarPicker) findViewById(R.id.calendarPicker);
    calendarPicker.setFirstDate(2011,1);
    calendarPicker.setLastDate(2015,2);
    calendarPicker.setDisableDays(new ArrayList<SimpleMonthAdapter.CalendarDay>());
  }
}
