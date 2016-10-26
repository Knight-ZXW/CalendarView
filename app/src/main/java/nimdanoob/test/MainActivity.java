package nimdanoob.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import nimdanoob.calendarview.CalendarPicker;
import nimdanoob.calendarview.R;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    CalendarPicker calendarPicker= (CalendarPicker) findViewById(R.id.calendarPicker);
  }
}
