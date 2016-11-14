package nimdanoob.calendarview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import java.util.ArrayList;

public class CalendarPicker extends LinearLayout
    implements DatePickerController {

  private DatePickerView mDatePickerView;
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
    mDatePickerView = (DatePickerView) findViewById(R.id.daypicker);
    if (mSelectMode != 0) {
      mDatePickerView.getSimpleMonthAdapter().setSelectMode(mSelectMode, mFixDayLength);
    }
  }

  public DatePickerController getController() {
    return this;
  }

  private void populateAttributes(Context context, AttributeSet attrs) {
    TypedArray ta =
        context.getTheme().obtainStyledAttributes(attrs, R.styleable.CalendarPicker, 0, 0);
    mSelectMode = ta.getInt(R.styleable.CalendarPicker_cp_selectMode, 0);
    mFixDayLength = ta.getInt(R.styleable.CalendarPicker_cp_fixDayLength, 0);
    ta.recycle();
  }

  public DatePickerView getDayPickerView() {
    return mDatePickerView;
  }

  public SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> getSelectedDays() {
    return mDatePickerView.getSimpleMonthAdapter().getSelectedDays();
  }

  @Override public void updateUi() {
    mDatePickerView.invalidate();
  }

  @Override public DatePickerController setFirstDate(int year, int month) {
    mDatePickerView.getSimpleMonthAdapter().setFirstDate(year, month);
    return this;
  }

  @Override public DatePickerController setLastDate(int year, int month) {
    mDatePickerView.getSimpleMonthAdapter().setLastDate(year, month);
    return this;
  }

  @Override public DatePickerController setDisableDays(
      ArrayList<SimpleMonthAdapter.CalendarDay> calendarDays) {
    mDatePickerView.getSimpleMonthAdapter().setDisableDays(calendarDays);
    mDatePickerView.getAdapter().notifyDataSetChanged();
    return this;
  }

  public DatePickerController setDayPickerListener(DatePickerListener listener) {
    mDatePickerView.getSimpleMonthAdapter().setDatePickerListener(listener);
    return this;
  }

  @Override public DatePickerController setSelectMode(int mode, @Nullable int fixSelectDay) {
    mDatePickerView.getSimpleMonthAdapter().setSelectMode(mode, fixSelectDay);
    return this;
  }

  public void scrollerToPosition(int position) {
    getDayPickerView().scrollToPosition(position);
  }

  // 当滑动  年份变化时回调
  public void setOnYearChangeListener(DatePickerView.OnYearChangedListener listener) {
    mDatePickerView.setOnYearChangedListener(listener);
  }

  //当 用户选中天数 满足 SelectState时 回到，如 Multi 则选中一个返回，Single 则选中一天
  public void setOnSelectStateChangeListener(
      SimpleMonthAdapter.OnSelectStateChangeListener listener) {
    mDatePickerView.getSimpleMonthAdapter().setOnSelectStateChangeListener(listener);
  }
}
