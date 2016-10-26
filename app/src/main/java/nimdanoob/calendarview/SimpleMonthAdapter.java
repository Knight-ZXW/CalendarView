/***********************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Robin Chutaux
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ***********************************************************************************/

package nimdanoob.calendarview;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class SimpleMonthAdapter extends RecyclerView.Adapter<SimpleMonthAdapter.ViewHolder>
    implements SimpleMonthView.OnDayClickListener {
  protected static final int MONTHS_IN_YEAR = 12;
  private static final int SELECT_MODE_MULTI = 100;
  private static final int SELECT_MODE_SINGLE = 101;
  private static final int SELECT_MODE_FIX = 102;
  private final TypedArray typedArray;
  private final Context mContext;
  private final DatePickerController mController;
  private final Calendar calendar;
  private final SelectedDays<CalendarDay> selectedDays;
  private final Integer firstMonth;
  private final Integer lastMonth;
  private int mSelectMode = SELECT_MODE_MULTI;
  private int mFixSelectDay = 7;

  private OnSelectStateChangeListener mOnSelectStateChangeListener;

  public SimpleMonthAdapter(Context context, DatePickerController datePickerController,
      TypedArray typedArray) {
    this.typedArray = typedArray;
    calendar = Calendar.getInstance();
    firstMonth =
        typedArray.getInt(R.styleable.DayPickerView_firstMonth, calendar.get(Calendar.MONTH));
    lastMonth = typedArray.getInt(R.styleable.DayPickerView_lastMonth,
        (calendar.get(Calendar.MONTH) - 1) % MONTHS_IN_YEAR);
    selectedDays = new SelectedDays<>();
    mContext = context;
    mController = datePickerController;
    init();
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    final SimpleMonthView simpleMonthView = new SimpleMonthView(mContext, typedArray);
    return new ViewHolder(simpleMonthView, this);
  }

  @Override
  public void onBindViewHolder(ViewHolder viewHolder, int position) {
    final SimpleMonthView v = viewHolder.simpleMonthView;
    final HashMap<String, Integer> drawingParams = new HashMap<String, Integer>();
    int month;
    int year;

    month = (firstMonth + (position % MONTHS_IN_YEAR)) % MONTHS_IN_YEAR;
    year = position / MONTHS_IN_YEAR + calendar.get(Calendar.YEAR) + ((firstMonth + (position
        % MONTHS_IN_YEAR)) / MONTHS_IN_YEAR);

    int selectedFirstDay = -1;
    int selectedLastDay = -1;
    int selectedFirstMonth = -1;
    int selectedLastMonth = -1;
    int selectedFirstYear = -1;
    int selectedLastYear = -1;

    if (selectedDays.getFirst() != null
        && selectedDays.getLast() != null
        && selectedDays.getFirst().compareTo(selectedDays.getLast()) > 0) {
      CalendarDay c = selectedDays.getFirst();
      selectedDays.setFirst(selectedDays.getLast());
      selectedDays.setLast(c);
    }
    if (selectedDays.getFirst() != null) {
      selectedFirstDay = selectedDays.getFirst().day;
      selectedFirstMonth = selectedDays.getFirst().month;
      selectedFirstYear = selectedDays.getFirst().year;
    }

    if (selectedDays.getLast() != null) {
      selectedLastDay = selectedDays.getLast().day;
      selectedLastMonth = selectedDays.getLast().month;
      selectedLastYear = selectedDays.getLast().year;
    }

    v.reuse();

    drawingParams.put(SimpleMonthView.VIEW_PARAMS_SELECTED_BEGIN_YEAR, selectedFirstYear);
    drawingParams.put(SimpleMonthView.VIEW_PARAMS_SELECTED_LAST_YEAR, selectedLastYear);
    drawingParams.put(SimpleMonthView.VIEW_PARAMS_SELECTED_BEGIN_MONTH, selectedFirstMonth);
    drawingParams.put(SimpleMonthView.VIEW_PARAMS_SELECTED_LAST_MONTH, selectedLastMonth);
    drawingParams.put(SimpleMonthView.VIEW_PARAMS_SELECTED_BEGIN_DAY, selectedFirstDay);
    drawingParams.put(SimpleMonthView.VIEW_PARAMS_SELECTED_LAST_DAY, selectedLastDay);
    drawingParams.put(SimpleMonthView.VIEW_PARAMS_YEAR, year);
    drawingParams.put(SimpleMonthView.VIEW_PARAMS_MONTH, month);
    drawingParams.put(SimpleMonthView.VIEW_PARAMS_WEEK_START, calendar.getFirstDayOfWeek());
    v.setMonthParams(drawingParams);
    v.invalidate();
  }

  public long getItemId(int position) {
    return position;
  }

  @Override
  public int getItemCount() {
    // 设置月数
    //int itemCount =
    //    (((mController.getMaxYear() - calendar.get(Calendar.YEAR)) + 1) * MONTHS_IN_YEAR);

    int itemCount = 2 * MONTHS_IN_YEAR;

    if (firstMonth != -1) {
      itemCount -= firstMonth;
    }

    if (lastMonth != -1) {
      itemCount -= (MONTHS_IN_YEAR - lastMonth) - 1;
    }

    return itemCount;
  }

  protected void init() {
    if (typedArray.getBoolean(R.styleable.DayPickerView_currentDaySelected, false)) {
      onDayTapped(new CalendarDay(System.currentTimeMillis()));
    }
  }

  public void onDayClick(SimpleMonthView simpleMonthView, CalendarDay calendarDay) {
    if (calendarDay != null) {
      onDayTapped(calendarDay);
    }
  }

  protected void onDayTapped(CalendarDay calendarDay) {
    mController.onDayOfMonthSelected(calendarDay.year, calendarDay.month + 1, calendarDay.day);
    setSelectedDay(calendarDay);
  }

  public void setSelectedDay(CalendarDay calendarDay) {
    boolean isReady = false;
    switch (mSelectMode) {
      case SELECT_MODE_SINGLE:
        selectedDays.setFirst(calendarDay);
        selectedDays.setLast(calendarDay);
        isReady = true;
        break;
      case SELECT_MODE_MULTI:
        if (selectedDays.getFirst() != null && selectedDays.getLast() == null) {
          selectedDays.setLast(calendarDay);

          if (selectedDays.getFirst().month < calendarDay.month) {
            for (int i = 0; i < selectedDays.getFirst().month - calendarDay.month - 1; ++i)
              mController.onDayOfMonthSelected(selectedDays.getFirst().year,
                  selectedDays.getFirst().month + i, selectedDays.getFirst().day);
          }
          isReady = true;
          mController.onDateRangeSelected(selectedDays);
        } else if (selectedDays.getLast() != null) {
          selectedDays.setFirst(calendarDay);
          selectedDays.setLast(null);
        } else {
          selectedDays.setFirst(calendarDay);
        }
        break;
      case SELECT_MODE_FIX:
        selectedDays.setFirst(calendarDay.clone());
        Calendar c = Calendar.getInstance();
        c.set(calendarDay.year, calendarDay.month, calendarDay.day);
        c.set(Calendar.DATE, calendarDay.day + mFixSelectDay - 1);
        calendarDay.year = c.get(Calendar.YEAR);
        calendarDay.month = c.get(Calendar.MONTH);
        calendarDay.day = c.get(Calendar.DATE);

        selectedDays.setLast(calendarDay);
        isReady = true;
        break;
    }
    notifySelectedStateChange(isReady);
    notifyDataSetChanged();
  }

  private void notifySelectedStateChange(boolean isReady) {
    if (mOnSelectStateChangeListener == null) return;
    mOnSelectStateChangeListener.onSelectStateChange(isReady);
  }

  public void setSelectMode(int mode, @Nullable int fixSelectDay) {
    if (mode != SELECT_MODE_FIX && mode != SELECT_MODE_MULTI && mode != SELECT_MODE_SINGLE) return;
    mSelectMode = mode;
    if (mSelectMode == SELECT_MODE_FIX && fixSelectDay > 1) {
      mFixSelectDay = fixSelectDay;
    }
  }

  public void setOnSelectStateChangeListener(OnSelectStateChangeListener listener) {
    this.mOnSelectStateChangeListener = listener;
  }

  public SelectedDays<CalendarDay> getSelectedDays() {
    if (selectedDays.getFirst() == null || selectedDays.getLast() == null) return null;
    SelectedDays<CalendarDay> s = new SelectedDays<>();
    s.setFirst(selectedDays.getFirst().clone());
    s.setLast(selectedDays.getLast().clone());
    s.getFirst().month += 1;
    s.getLast().month += 1;
    switch (mSelectMode) {
      case SELECT_MODE_SINGLE:
        if (s.getFirst().equals(s.getLast())) {
          return s;
        } else {
          return null;
        }
      case SELECT_MODE_MULTI:
        if (s.getFirst().compareTo(s.getLast()) > 0) {
          CalendarDay c = s.getFirst();
          s.setFirst(selectedDays.getLast());
          s.setLast(c);
        }
        return s;
      case SELECT_MODE_FIX:
        return s;
      default:
        return null;
    }
  }

  public interface OnSelectStateChangeListener {
    void onSelectStateChange(boolean isEnable);
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    final SimpleMonthView simpleMonthView;

    public ViewHolder(View itemView, SimpleMonthView.OnDayClickListener onDayClickListener) {
      super(itemView);
      simpleMonthView = (SimpleMonthView) itemView;
      simpleMonthView.setLayoutParams(
          new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.MATCH_PARENT));
      simpleMonthView.setClickable(true);
      simpleMonthView.setOnDayClickListener(onDayClickListener);
    }
  }

  public static class CalendarDay implements Serializable, Cloneable, Comparable<CalendarDay> {
    private static final long serialVersionUID = -5456695978688356202L;
    public int day;
    public int month;
    public int year;
    private Calendar calendar;

    public CalendarDay() {
      setTime(System.currentTimeMillis());
    }

    public CalendarDay(int year, int month, int day) {
      setDay(year, month, day);
    }

    public CalendarDay(long timeInMillis) {
      setTime(timeInMillis);
    }

    public CalendarDay(Calendar calendar) {
      year = calendar.get(Calendar.YEAR);
      month = calendar.get(Calendar.MONTH);
      day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    private void setTime(long timeInMillis) {
      if (calendar == null) {
        calendar = Calendar.getInstance();
      }
      calendar.setTimeInMillis(timeInMillis);
      month = this.calendar.get(Calendar.MONTH);
      year = this.calendar.get(Calendar.YEAR);
      day = this.calendar.get(Calendar.DAY_OF_MONTH);
    }

    public void set(CalendarDay calendarDay) {
      year = calendarDay.year;
      month = calendarDay.month;
      day = calendarDay.day;
    }

    public void setDay(int year, int month, int day) {
      this.year = year;
      this.month = month;
      this.day = day;
    }

    public Date getDate() {
      if (calendar == null) {
        calendar = Calendar.getInstance();
      }
      calendar.set(year, month - 1, day);
      return calendar.getTime();
    }

    @Override public boolean equals(Object obj) {
      if (this == obj) return true;
      if (!(obj instanceof CalendarDay)) return false;
      CalendarDay c = (CalendarDay) obj;
      return this.year == c.year && this.month == c.month && this.day == c.day;
    }

    @Override
    public String toString() {
      final StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("{ year: ");
      stringBuilder.append(year);
      stringBuilder.append(", month: ");
      stringBuilder.append(month);
      stringBuilder.append(", day: ");
      stringBuilder.append(day);
      stringBuilder.append(" }");

      return stringBuilder.toString();
    }

    public CalendarDay clone() {
      CalendarDay o = null;
      try {
        o = (CalendarDay) super.clone();
      } catch (CloneNotSupportedException e) {
        e.printStackTrace();
      }
      return o;
    }

    @Override public int compareTo(@NonNull CalendarDay calendarDay) {
      if (this.year > calendarDay.year) return 1;
      if (this.year < calendarDay.year) return -1;
      if (this.month > calendarDay.month) return 1;
      if (this.month < calendarDay.month) return -1;
      if (this.day > calendarDay.day) return 1;
      if (this.day < calendarDay.day) return -1;
      return 0;
    }
  }

  public static class SelectedDays<K> implements Serializable {
    private static final long serialVersionUID = 3942549765282708376L;
    private K first;
    private K last;

    public K getFirst() {
      return first;
    }

    public void setFirst(K first) {
      this.first = first;
    }

    public K getLast() {
      return last;
    }

    public void setLast(K last) {
      this.last = last;
    }
  }
}