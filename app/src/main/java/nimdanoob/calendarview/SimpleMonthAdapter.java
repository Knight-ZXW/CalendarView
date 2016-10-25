package nimdanoob.calendarview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class SimpleMonthAdapter extends RecyclerView.Adapter<SimpleMonthAdapter.ViewHolder>
    implements SimpleMonthView.OnDayClickListener {

  static final int MONTHS_IN_YEAR = 12;
  private static final int SELECT_MODE_MULTI = 100;
  private static final int SELECT_MODE_SINGLE = 101;
  private static final int SELECT_MODE_FIX = 102;
  private final TypedArray typedArray;
  private final Context mContext;
  private final DatePickerController mController;
  private final Calendar calendar;
  private final SelectedDays<CalendarDay> selectedDays;
  private final int firstMonth;
  private final int lastMonth;
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
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return null;
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    final SimpleMonthView v = holder.simpleMonthView;
    final HashMap<String, Integer> drawingParams = new HashMap<String, Integer>();
    int month;
    int year;
    month = (firstMonth + (position % MONTHS_IN_YEAR)) % MONTHS_IN_YEAR;
    year = position / MONTHS_IN_YEAR
        + calendar.get(Calendar.YEAR)
        + (firstMonth + (position
        % MONTHS_IN_YEAR)) / MONTHS_IN_YEAR;

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

  @Override public int getItemCount() {
    int itemCount = 2 * MONTHS_IN_YEAR;

    if (firstMonth != -1) {
      itemCount -= firstMonth;
    }

    if (lastMonth != -1) {
      itemCount -= (MONTHS_IN_YEAR - lastMonth) - 1;
    }

    return itemCount;
  }


  private void init() {
    if (typedArray.getBoolean(R.styleable.DayPickerView_currentDaySelected, false)) {
      onDayTapped(new CalendarDay(System.currentTimeMillis()));
    }
  }


  @Override public void onDayClick(SimpleMonthView simpleMonthView,
      CalendarDay calendarDay) {
    if (calendarDay!=null){
      onDayTapped(calendarDay);
    }
  }

  private void onDayTapped(CalendarDay calendarDay) {
    mController.onDayOfMonthSelected(calendarDay.year, calendarDay.month + 1, calendarDay.day);
    setSelectedDay(calendarDay);
  }

  public void setSelectedDay(CalendarDay selectedDay) {
    boolean isReady = false;
    switch (mSelectMode) {
      case SELECT_MODE_SINGLE:
        selectedDays.setFirst(selectedDay);
        selectedDays.setLast(selectedDay);
        isReady = true;
        break;
      case SELECT_MODE_MULTI:
        if (selectedDays.getFirst() != null && selectedDays.getLast() == null) {
          selectedDays.setLast(selectedDay);

          if (selectedDays.getFirst().month < selectedDay.month) {
            for (int i = 0; i < selectedDays.getFirst().month - selectedDay.month - 1; ++i)
              mController.onDayOfMonthSelected(selectedDays.getFirst().year,
                  selectedDays.getFirst().month + i, selectedDays.getFirst().day);
          }
          isReady = true;
          mController.onDateRangeSelected(selectedDays);
        } else if (selectedDays.getLast() != null) {
          selectedDays.setFirst(selectedDay);
          selectedDays.setLast(null);
        } else {
          selectedDays.setFirst(selectedDay);
        }
        break;
      case SELECT_MODE_FIX:
        selectedDays.setFirst(selectedDay.clone());
        Calendar c = Calendar.getInstance();
        c.set(selectedDay.year, selectedDay.month, selectedDay.day);
        c.set(Calendar.DATE, selectedDay.day + mFixSelectDay - 1);
        selectedDay.year = c.get(Calendar.YEAR);
        selectedDay.month = c.get(Calendar.MONTH);
        selectedDay.day = c.get(Calendar.DATE);

        selectedDays.setLast(selectedDay);
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

  public void setSelectMode(int mode,@Nullable int fixSelectDay){
    if (mode != SELECT_MODE_FIX && mode != SELECT_MODE_MULTI && mode != SELECT_MODE_SINGLE) return;
    mSelectMode = mode;
    if (mSelectMode == SELECT_MODE_FIX && fixSelectDay > 1) {
      mFixSelectDay = fixSelectDay;
    }
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

    public ViewHolder(View itemView) {
      super(itemView);
      simpleMonthView = (SimpleMonthView) itemView;

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
}
