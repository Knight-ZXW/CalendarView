package nimdanoob.calendarview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CalendarPicker extends LinearLayout
    implements DatePickerView.OnYearChangedListener, DatePickerListener,
    SimpleMonthAdapter.OnSelectStateChangeListener {

  private TextView mTxtYear;
  private Button mBtnConfirm;
  private DatePickerView mDayPickerView;
  private DatePickerController mDatePickerController;
  private int mSelectMode;
  private int mFixDayLength;

  public CalendarPicker(Context context) {
    this(context, null);
  }

  public CalendarPicker(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CalendarPicker(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    populateAttributes(context, attrs);
    initializeViews(context);
  }

  private void initializeViews(Context context) {
    setOrientation(VERTICAL);
    LayoutInflater.from(context).inflate(R.layout.calendar_list, this);
    mTxtYear = (TextView) findViewById(R.id.text_year);
    mBtnConfirm = (Button) findViewById(R.id.btn_confirm_date);
    mDayPickerView = (DatePickerView) findViewById(R.id.daypicker);
    mDatePickerController = new DatePickerControllerImpl(mDayPickerView);
    mDayPickerView.setOnYearChangedListener(this);
  }

  public DatePickerController getController(){
    return mDatePickerController;
  }
  private void populateAttributes(Context context, AttributeSet attrs) {
    TypedArray ta =
        context.getTheme().obtainStyledAttributes(attrs, R.styleable.CalendarPicker, 0, 0);
    mSelectMode = ta.getInt(R.styleable.CalendarPicker_cp_selectMode, 0);
    mFixDayLength = ta.getInt(R.styleable.CalendarPicker_cp_fixDayLength, 0);
    ta.recycle();
  }

  @Override public void onDayOfMonthSelected(int year, int month, int day) {

  }

  @Override public void onDateRangeSelected(
      SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> selectedDays) {
  }

  public DatePickerView getDayPickerView(){
    return mDayPickerView;
  }

  @Override public void onYearChange(int year) {
    mTxtYear.setText(String.valueOf(year));
  }

  @Override public void onSelectStateChange(boolean isEnable) {
    mBtnConfirm.setEnabled(isEnable);
  }

  public SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> getSelectedDays(){
    return mDayPickerView.getSimpleMonthAdapter().getSelectedDays();
  }

  public void setOnConfirmListener(OnClickListener listener){
    mBtnConfirm.setOnClickListener(listener);
  }

  public void setController(CalendarPicker controller) {
    mDayPickerView.setController(controller);
    if (mSelectMode != 0) {
      mDayPickerView.getSimpleMonthAdapter().setSelectMode(mSelectMode, mFixDayLength);
    }

    mDayPickerView.getSimpleMonthAdapter().setOnSelectStateChangeListener(this);
  }

  public void scrollerTo(){
    getDayPickerView().scrollToPosition(5);
  }

}
