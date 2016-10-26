package nimdanoob.calendarview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.view.MotionEvent;
import android.view.View;
import java.security.InvalidParameterException;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SimpleMonthView extends View {

  public static final String VIEW_PARAMS_HEIGHT = "height";
  public static final String VIEW_PARAMS_MONTH = "month";
  public static final String VIEW_PARAMS_YEAR = "year";
  public static final String VIEW_PARAMS_SELECTED_BEGIN_DAY = "selected_begin_day";
  public static final String VIEW_PARAMS_SELECTED_LAST_DAY = "selected_last_day";
  public static final String VIEW_PARAMS_SELECTED_BEGIN_MONTH = "selected_begin_month";
  public static final String VIEW_PARAMS_SELECTED_LAST_MONTH = "selected_last_month";
  public static final String VIEW_PARAMS_SELECTED_BEGIN_YEAR = "selected_begin_year";
  public static final String VIEW_PARAMS_SELECTED_LAST_YEAR = "selected_last_year";
  public static final String VIEW_PARAMS_WEEK_START = "week_start";
  protected static final int DEFAULT_NUM_ROWS = 6;
  private static final int DRAW_RECT_WITH_CURVE_ON_LEFT = 1001;
  private static final int DRAW_RECT_WITH_CURVE_ON_RIGHT = 1002;
  private static final int SELECTED_CIRCLE_ALPHA = 128;
  protected static int DEFAULT_HEIGHT = 32;
  protected static int DAY_SELECTED_CIRCLE_SIZE;
  protected static int DAY_SEPARATOR_WIDTH = 1;
  protected static int MINI_DAY_NUMBER_TEXT_SIZE;
  protected static int MIN_HEIGHT = 10;
  protected static int MONTH_DAY_LABEL_TEXT_SIZE;
  protected static int MONTH_HEADER_SIZE;
  protected static int MONTH_LABEL_TEXT_SIZE;
  final Time today;
  private final StringBuilder mStringBuilder;
  private final Calendar mCalendar;
  private final Calendar mDayLabelCalendar;
  private final Boolean isPrevDayEnabled;
  protected int mPadding = 0;
  protected Paint mMonthDayLabelPaint;
  protected Paint mMonthNumPaint;
  protected Paint mMonthDisEnablePaint;
  protected Paint mMonthTitleBGPaint;
  protected Paint mMonthTitlePaint;
  protected Paint mSelectedCirclePaint;
  protected Paint mSelectedRectPaint;
  protected Paint mSeperatorPaint;
  protected float mSeperatorWidth;
  protected int mCurrentDayTextColor;
  protected int mMonthTextColor;
  protected int mDayTextColor;
  protected int mDayNumColor;
  protected int mDisableDayNumColor;
  protected int mMonthTitleBGColor;
  protected int mFirstEqualsLastBGColor;
  protected int mPreviousDayColor;
  protected int mSelectedDaysColor;
  protected int mSelectedCircleColor;
  protected int mSelectedRectColor;
  protected int mSeperatorColor;
  protected boolean mHasToday = false;
  protected boolean mIsPrev = false;
  protected int mSelectedBeginDay = -1;
  protected int mSelectedLastDay = -1;
  protected int mSelectedBeginMonth = -1;
  protected int mSelectedLastMonth = -1;
  protected int mSelectedBeginYear = -1;
  protected int mSelectedLastYear = -1;
  protected int mToday = -1;
  protected int mWeekStart = 0;
  protected int mNumDays = 7;
  protected int mNumCells = mNumDays;
  protected int mMonth;
  protected Boolean mDrawRect;
  protected int mRowHeight = DEFAULT_HEIGHT;
  protected int mWidth;
  protected int mYear;
  private String mDayOfWeekTypeface;
  private String mMonthTitleTypeface;
  private int mDayOfWeekStart = 0;
  private int mNumRows = DEFAULT_NUM_ROWS;
  private String mMonthName = "";
  private DateFormatSymbols mDateFormatSymbols = new DateFormatSymbols();
  private OnDayClickListener mOnDayClickListener;
  //todo 不可用的天数
  private Set<Integer> mDisableDays;

  public SimpleMonthView(Context context, TypedArray typedArray) {
    super(context);
    Resources resources = getResources();
    mDisableDays = new HashSet<>();
    mDayLabelCalendar = Calendar.getInstance();
    mCalendar = Calendar.getInstance();
    today = new Time(Time.getCurrentTimezone());
    today.setToNow();
    //todo what is this
    mDayOfWeekTypeface = resources.getString(R.string.sans_serif);
    mMonthTitleTypeface = resources.getString(R.string.sans_serif);
    mCurrentDayTextColor = typedArray.getColor(R.styleable.DatePickerView_colorCurrentDay,
        resources.getColor(R.color.calendarNormalDay));
    mMonthTextColor = typedArray.getColor(R.styleable.DatePickerView_colorMonthName,
        resources.getColor(R.color.calendarNormalDay));
    mDayTextColor = typedArray.getColor(R.styleable.DatePickerView_colorDayName,
        resources.getColor(R.color.calendarNormalDay));
    mDisableDayNumColor = resources.getColor(R.color.calendarDisableDayTextColor);
    mDayNumColor = typedArray.getColor(R.styleable.DatePickerView_colorNormalDay,
        resources.getColor(R.color.calendarNormalDay));
    mPreviousDayColor = typedArray.getColor(R.styleable.DatePickerView_colorPreviousDay,
        resources.getColor(R.color.calendarNormalDay));
    mSelectedDaysColor = typedArray.getColor(R.styleable.DatePickerView_colorSelectedDayBackground,
        resources.getColor(R.color.calendarFirstEqualsLastBackground));
    mSelectedCircleColor = typedArray.getColor(R.styleable.DatePickerView_colorCircleBackground,
        resources.getColor(R.color.calendarFirstEqualsLastBackground));
    mSelectedRectColor = typedArray.getColor(R.styleable.DatePickerView_colorRectBackground,
        resources.getColor(R.color.calendarFirstEqualsLastBackground));
    mMonthTitleBGColor = typedArray.getColor(R.styleable.DatePickerView_colorSelectedDayText,
        resources.getColor(R.color.calendarSelectedDayText));
    mFirstEqualsLastBGColor = resources.getColor(R.color.calendarFirstEqualsLastBackground);
    mSeperatorColor = resources.getColor(R.color.calendarSeperatorColor);
    mSeperatorWidth = resources.getDimension(R.dimen.calendar_seperator_width);
    mDrawRect = typedArray.getBoolean(R.styleable.DatePickerView_drawRoundRect, false);

    mStringBuilder = new StringBuilder(50);

    MINI_DAY_NUMBER_TEXT_SIZE =
        typedArray.getDimensionPixelSize(R.styleable.DatePickerView_textSizeDay,
            resources.getDimensionPixelSize(R.dimen.calendar_text_size_day));
    MONTH_LABEL_TEXT_SIZE =
        typedArray.getDimensionPixelSize(R.styleable.DatePickerView_textSizeMonth,
            resources.getDimensionPixelSize(R.dimen.calendar_text_size_month));
    MONTH_DAY_LABEL_TEXT_SIZE =
        typedArray.getDimensionPixelSize(R.styleable.DatePickerView_textSizeDayName,
            resources.getDimensionPixelSize(R.dimen.calendar_text_size_day_name));
    MONTH_HEADER_SIZE =
        typedArray.getDimensionPixelOffset(R.styleable.DatePickerView_headerMonthHeight,
            resources.getDimensionPixelOffset(R.dimen.calendar_header_month_height));
    DAY_SELECTED_CIRCLE_SIZE =
        typedArray.getDimensionPixelSize(R.styleable.DatePickerView_selectedDayRadius,
            resources.getDimensionPixelOffset(R.dimen.calendar_selected_day_radius));

    mRowHeight = ((typedArray.getDimensionPixelSize(R.styleable.DatePickerView_calendarHeight,
        resources.getDimensionPixelOffset(R.dimen.calendar_height))) / 6);

    isPrevDayEnabled = typedArray.getBoolean(R.styleable.DatePickerView_enablePreviousDay, true);
    initView();
  }

  public void setDisableDay(int day, boolean enable) {
    if (enable) {
      mDisableDays.add(day);
    } else {
      mDisableDays.remove(day);
    }
  }
  public void setDisableDays(Set disableDays){
    mDisableDays = disableDays;
  }


  private void initView() {
    mMonthTitlePaint = new Paint();
    mMonthTitlePaint.setFakeBoldText(true);
    mMonthTitlePaint.setAntiAlias(true);
    mMonthTitlePaint.setTextSize(MONTH_LABEL_TEXT_SIZE);
    mMonthTitlePaint.setTypeface(Typeface.create(mMonthTitleTypeface, Typeface.BOLD));
    mMonthTitlePaint.setColor(mMonthTextColor);
    mMonthTitlePaint.setTextAlign(Paint.Align.CENTER);
    mMonthTitlePaint.setStyle(Paint.Style.FILL);

    mMonthTitleBGPaint = new Paint();
    mMonthTitleBGPaint.setFakeBoldText(true);
    mMonthTitleBGPaint.setAntiAlias(true);
    mMonthTitleBGPaint.setColor(mMonthTitleBGColor);
    mMonthTitleBGPaint.setTextAlign(Paint.Align.CENTER);
    mMonthTitleBGPaint.setStyle(Paint.Style.FILL);

    mSelectedCirclePaint = new Paint();
    mSelectedCirclePaint.setFakeBoldText(true);
    mSelectedCirclePaint.setAntiAlias(true);
    mSelectedCirclePaint.setColor(mSelectedCircleColor);
    mSelectedCirclePaint.setTextAlign(Paint.Align.CENTER);
    mSelectedCirclePaint.setStyle(Paint.Style.FILL);
    //mSelectedCirclePaint.setAlpha(SELECTED_CIRCLE_ALPHA);

    mSelectedRectPaint = new Paint();
    mSelectedRectPaint.setFakeBoldText(true);
    mSelectedRectPaint.setAntiAlias(true);
    mSelectedRectPaint.setColor(mSelectedRectColor);
    mSelectedRectPaint.setTextAlign(Paint.Align.CENTER);
    mSelectedRectPaint.setStyle(Paint.Style.FILL);

    mMonthDayLabelPaint = new Paint();
    mMonthDayLabelPaint.setAntiAlias(true);
    mMonthDayLabelPaint.setTextSize(MONTH_DAY_LABEL_TEXT_SIZE);
    mMonthDayLabelPaint.setColor(mDayTextColor);
    mMonthDayLabelPaint.setTypeface(Typeface.create(mDayOfWeekTypeface, Typeface.NORMAL));
    mMonthDayLabelPaint.setStyle(Paint.Style.FILL);
    mMonthDayLabelPaint.setTextAlign(Paint.Align.CENTER);
    mMonthDayLabelPaint.setFakeBoldText(true);

    mMonthNumPaint = new Paint();
    mMonthNumPaint.setAntiAlias(true);
    mMonthNumPaint.setTextSize(MINI_DAY_NUMBER_TEXT_SIZE);
    mMonthNumPaint.setStyle(Paint.Style.FILL);
    mMonthNumPaint.setTextAlign(Paint.Align.CENTER);
    mMonthNumPaint.setFakeBoldText(false);

    mSeperatorPaint = new Paint();
    mSeperatorPaint.setColor(mSeperatorColor);
    mSeperatorPaint.setStrokeWidth(mSeperatorWidth);
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    drawMonthDayLabels(canvas);
    drawMonthNums(canvas);
  }

  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
        mRowHeight * mNumRows + MONTH_HEADER_SIZE);
  }

  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    mWidth = w;
  }

  public boolean onTouchEvent(MotionEvent event) {
    if (event.getAction() == MotionEvent.ACTION_UP) {
      SimpleMonthAdapter.CalendarDay calendarDay = getDayFromLocation(event.getX(), event.getY());
      if (calendarDay != null) {
        onDayClick(calendarDay);
      }
    }
    return true;
  }

  private void drawMonthDayLabels(Canvas canvas) {

  }

  private void drawMonthNums(Canvas canvas) {
    int y =
        (MINI_DAY_NUMBER_TEXT_SIZE) / 2 + MONTH_HEADER_SIZE + mRowHeight / 2 - DAY_SEPARATOR_WIDTH;
    int paddingDay = (mWidth - 2 * mPadding) / (2 * mNumDays);
    int rectWidth = (mWidth - 2 * mPadding) / (2 * mNumDays);
    int dayOffset = findDayOffset();
    int day = 1;
    SimpleMonthAdapter.CalendarDay firstCalendar =
        new SimpleMonthAdapter.CalendarDay(mSelectedBeginYear, mSelectedBeginMonth,
            mSelectedBeginDay);
    SimpleMonthAdapter.CalendarDay lastCalendar =
        new SimpleMonthAdapter.CalendarDay(mSelectedLastYear, mSelectedLastMonth, mSelectedLastDay);
    SimpleMonthAdapter.CalendarDay c = new SimpleMonthAdapter.CalendarDay(mYear, mMonth, day);
    //draw 月份标题
    drawMonthTitle(canvas, paddingDay * (1 + dayOffset * 2) + mPadding, rectWidth * 2, dayOffset);
    int firstX = paddingDay * (dayOffset * 2) + mPadding;
    if (mMonthTitlePaint.measureText(mMonthName) > (paddingDay * 2)) {
      firstX = (int) (firstX - (mMonthTitlePaint.measureText(mMonthName) / 2) + paddingDay);
    }

    if (dayOffset == 6) {
      firstX = (int) (determineMaxTextSize(mMonthName, rectWidth * 2,
          paddingDay * (1 + dayOffset * 2) + mPadding, dayOffset)
          - mMonthTitlePaint.measureText(mMonthName) / 2);
    }

    while (day <= mNumCells) {
      c.day = day;
      int x = paddingDay * (1 + dayOffset * 2) + mPadding;
      if ((mMonth == mSelectedBeginMonth && mSelectedBeginDay == day && mSelectedBeginYear == mYear)
          || (mMonth == mSelectedLastMonth
          && mSelectedLastDay == day
          && mSelectedLastYear == mYear)) {
        if (mDrawRect) {
          RectF rectF = new RectF(x - DAY_SELECTED_CIRCLE_SIZE,
              (y - MINI_DAY_NUMBER_TEXT_SIZE / 3) - DAY_SELECTED_CIRCLE_SIZE,
              x + DAY_SELECTED_CIRCLE_SIZE,
              (y - MINI_DAY_NUMBER_TEXT_SIZE / 3) + DAY_SELECTED_CIRCLE_SIZE);
          canvas.drawRoundRect(rectF, 10.0f, 10.0f, mSelectedCirclePaint);
        } else {
          // 为选中的 Day 画背景圆
          canvas.drawCircle(x, y - MINI_DAY_NUMBER_TEXT_SIZE / 3, DAY_SELECTED_CIRCLE_SIZE,
              mSelectedCirclePaint);
        }
      }
      if (mHasToday && (mToday == day)) {
        // 如果是今天
        mMonthNumPaint.setColor(mCurrentDayTextColor);
        mMonthNumPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
      } else if (mDisableDays.contains(day)) {
        mMonthNumPaint.setColor(mDisableDayNumColor);
      } else {
        mMonthNumPaint.setColor(mDayNumColor);
        mMonthNumPaint.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
      }

      if ((mMonth == mSelectedBeginMonth && mSelectedBeginDay == day && mSelectedBeginYear == mYear)
          || (mMonth == mSelectedLastMonth
          && mSelectedLastDay == day
          && mSelectedLastYear == mYear)) {
        //如果是首尾
        mMonthNumPaint.setColor(mMonthTitleBGColor);
      }

      if ((mSelectedBeginDay != -1
          && mSelectedLastDay != -1
          && mSelectedBeginYear == mSelectedLastYear
          &&
          mSelectedBeginMonth == mSelectedLastMonth
          &&
          mSelectedBeginDay == mSelectedLastDay
          &&
          day == mSelectedBeginDay
          &&
          mMonth == mSelectedBeginMonth
          &&
          mYear == mSelectedBeginYear)) {
        // First 和 Last 相同
        mMonthNumPaint.setColor(mFirstEqualsLastBGColor);
      }
      if (mSelectedBeginDay != -1 && mSelectedLastDay != -1) {
        if (firstCalendar.compareTo(lastCalendar) != 0 && c.compareTo(firstCalendar) == 0 && (
            dayOffset == 6
                || day == mNumCells)) {
          canvas.drawCircle(x, y - MINI_DAY_NUMBER_TEXT_SIZE / 3, DAY_SELECTED_CIRCLE_SIZE,
              mSelectedCirclePaint);
        } else if (firstCalendar.compareTo(lastCalendar) != 0 && c.compareTo(firstCalendar) == 0) {
          drawArcRect(canvas, DRAW_RECT_WITH_CURVE_ON_LEFT, x, y, rectWidth, mSelectedCirclePaint);
        }
        if (firstCalendar.compareTo(lastCalendar) != 0 && c.compareTo(lastCalendar) == 0 && (
            dayOffset == 0
                || day == 1)) {
          canvas.drawCircle(x, y - MINI_DAY_NUMBER_TEXT_SIZE / 3, DAY_SELECTED_CIRCLE_SIZE,
              mSelectedCirclePaint);
        } else if (firstCalendar.compareTo(lastCalendar) != 0 && c.compareTo(lastCalendar) == 0) {
          drawArcRect(canvas, DRAW_RECT_WITH_CURVE_ON_RIGHT, x, y, rectWidth, mSelectedCirclePaint);
        }
      }

      if (mSelectedBeginDay != -1
          && mSelectedLastDay != -1
          && (c.compareTo(firstCalendar) > 0 && c.compareTo(lastCalendar) < 0)
          ) {
        // 首尾之间
        mMonthNumPaint.setColor(mSelectedDaysColor);
        //if (dayOffset == 6 && (day == mSelectedBeginDay || day == mSelectedLastDay)) {
        //  canvas.drawCircle(x, y - MINI_DAY_NUMBER_TEXT_SIZE / 3, DAY_SELECTED_CIRCLE_SIZE,
        //      mSelectedCirclePaint);
        //}
        if (dayOffset == 6 && day == 1) {
          canvas.drawCircle(x, y - MINI_DAY_NUMBER_TEXT_SIZE / 3, DAY_SELECTED_CIRCLE_SIZE,
              mSelectedRectPaint);
        } else if (dayOffset == 6 || day == mNumCells) {
          drawArcRect(canvas, DRAW_RECT_WITH_CURVE_ON_RIGHT, x, y, rectWidth, mSelectedRectPaint);
        } else if (dayOffset == 0 || day == 1) {
          drawArcRect(canvas, DRAW_RECT_WITH_CURVE_ON_LEFT, x, y, rectWidth, mSelectedRectPaint);
        } else {
          RectF rectF = new RectF(x - rectWidth,
              (y - MINI_DAY_NUMBER_TEXT_SIZE / 3) - DAY_SELECTED_CIRCLE_SIZE,
              x + rectWidth,
              (y - MINI_DAY_NUMBER_TEXT_SIZE / 3) + DAY_SELECTED_CIRCLE_SIZE);
          canvas.drawRect(rectF, mSelectedRectPaint);
        }
      }

      if (!isPrevDayEnabled
          && prevDay(day, today)
          && today.month == mMonth
          && today.year == mYear) {
        mMonthNumPaint.setColor(mPreviousDayColor);
        mMonthNumPaint.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
      }

      canvas.drawText(String.format("%d", day), x, y, mMonthNumPaint);
      dayOffset++;
      if (dayOffset == mNumDays) {
        dayOffset = 0;
        if (day > 7) {
          canvas.drawLine(mPadding, y - MINI_DAY_NUMBER_TEXT_SIZE / 3 - mRowHeight / 2, getWidth(),
              y - MINI_DAY_NUMBER_TEXT_SIZE / 3 - mRowHeight / 2, mSeperatorPaint);
        } else {
          canvas.drawLine(firstX, y - MINI_DAY_NUMBER_TEXT_SIZE / 3 - mRowHeight / 2, getWidth(),
              y - MINI_DAY_NUMBER_TEXT_SIZE / 3 - mRowHeight / 2, mSeperatorPaint);
        }
        y += mRowHeight;
      }
      day++;
    }
    canvas.drawLine(0, y - MINI_DAY_NUMBER_TEXT_SIZE / 3 - mRowHeight / 2, getWidth(),
        y - MINI_DAY_NUMBER_TEXT_SIZE / 3 - mRowHeight / 2, mSeperatorPaint);
  }

  private void drawMonthTitle(Canvas canvas, int x, int width, int dayOffset) {
    int y = MONTH_HEADER_SIZE / 2 + MONTH_LABEL_TEXT_SIZE / 2;
    StringBuilder stringBuilder = new StringBuilder(getMonthAndYearString().toLowerCase());
    stringBuilder.setCharAt(0, Character.toUpperCase(stringBuilder.charAt(0)));
    mMonthName = stringBuilder.toString();
    canvas.drawText(stringBuilder.toString(),
        determineMaxTextSize(stringBuilder.toString(), width, x, dayOffset), y, mMonthTitlePaint);
  }

  private void drawArcRect(Canvas canvas, int mode, int x, int y, int rectWidth,
      Paint circlePaint) {
    switch (mode) {
      case DRAW_RECT_WITH_CURVE_ON_LEFT:

        RectF rectF1 = new RectF(x,
            (y - MINI_DAY_NUMBER_TEXT_SIZE / 3) - DAY_SELECTED_CIRCLE_SIZE, x + rectWidth,
            (y - MINI_DAY_NUMBER_TEXT_SIZE / 3) + DAY_SELECTED_CIRCLE_SIZE);
        RectF rectF2 = new RectF(x - DAY_SELECTED_CIRCLE_SIZE,
            y - MINI_DAY_NUMBER_TEXT_SIZE / 3 - DAY_SELECTED_CIRCLE_SIZE,
            x + DAY_SELECTED_CIRCLE_SIZE,
            y - MINI_DAY_NUMBER_TEXT_SIZE / 3 + DAY_SELECTED_CIRCLE_SIZE);
        canvas.drawRect(rectF1, mSelectedRectPaint);
        canvas.drawArc(rectF2, 90, 180, true, circlePaint);
        //canvas.drawCircle(x, y - MINI_DAY_NUMBER_TEXT_SIZE / 3, DAY_SELECTED_CIRCLE_SIZE,
        //   mSelectedRectPaint);
        break;
      case DRAW_RECT_WITH_CURVE_ON_RIGHT:
        RectF rectF3 = new RectF(x - rectWidth,
            (y - MINI_DAY_NUMBER_TEXT_SIZE / 3) - DAY_SELECTED_CIRCLE_SIZE, x,
            (y - MINI_DAY_NUMBER_TEXT_SIZE / 3) + DAY_SELECTED_CIRCLE_SIZE);
        RectF rectF4 = new RectF(x - DAY_SELECTED_CIRCLE_SIZE,
            y - MINI_DAY_NUMBER_TEXT_SIZE / 3 - DAY_SELECTED_CIRCLE_SIZE,
            x + DAY_SELECTED_CIRCLE_SIZE,
            y - MINI_DAY_NUMBER_TEXT_SIZE / 3 + DAY_SELECTED_CIRCLE_SIZE);
        canvas.drawRect(rectF3, mSelectedRectPaint);
        canvas.drawArc(rectF4, 270, 180, true, circlePaint);

        break;
    }
  }

  //判断 字体 x位置
  private float determineMaxTextSize(String s, int maxWidth, int x, int dayOffset) {
    int offset = 0;
    mMonthTitlePaint.setTextSize(MONTH_LABEL_TEXT_SIZE);
    if (mMonthTitlePaint.measureText(s) < maxWidth) return x;
    offset = (int) (mMonthTitlePaint.measureText(s) - maxWidth);
    if (dayOffset == 6) return x - offset;
    if (dayOffset == 0) return x + offset;
    return x;
  }

  private boolean prevDay(int monthDay, Time time) {
    return ((mYear < time.year)) || (mYear == time.year && mMonth < time.month) || (mMonth
        == time.month && monthDay < time.monthDay);
  }

  public SimpleMonthAdapter.CalendarDay getDayFromLocation(float x, float y) {
    int padding = mPadding;
    if ((x < padding) || (x > mWidth - mPadding)) {
      return null;
    }

    int yDay = (int) (y - MONTH_HEADER_SIZE) / mRowHeight;
    int day = 1
        + ((int) ((x - padding) * mNumDays / (mWidth - padding - mPadding)) - findDayOffset())
        + yDay * mNumDays;

    if (mMonth > 11 || mMonth < 0 || CalendarUtils.getDaysInMonth(mMonth, mYear) < day || day < 1) {
      return null;
    }

    return new SimpleMonthAdapter.CalendarDay(mYear, mMonth, day);
  }

  public void reuse() {
    mNumRows = DEFAULT_NUM_ROWS;
    requestLayout();
  }

  private int findDayOffset() {
    return (mDayOfWeekStart < mWeekStart ? (mDayOfWeekStart + mNumDays) : mDayOfWeekStart)
        - mWeekStart;
  }

  private String getMonthAndYearString() {
    int flags =
        DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NO_YEAR | DateUtils.FORMAT_NO_MONTH_DAY;
    mStringBuilder.setLength(0);
    long millis = mCalendar.getTimeInMillis();
    return DateUtils.formatDateRange(getContext(), millis, millis, flags);
  }

  public void setMonthParams(HashMap<String, Integer> params) {
    //todo 这里应该是 ||
    if (!params.containsKey(VIEW_PARAMS_MONTH) && !params.containsKey(VIEW_PARAMS_YEAR)) {
      throw new InvalidParameterException("You must specify month and year for this view");
    }
    setTag(params);
    if (params.containsKey(VIEW_PARAMS_HEIGHT)) {
      mRowHeight = params.get(VIEW_PARAMS_HEIGHT);
      if (mRowHeight < MIN_HEIGHT) {
        mRowHeight = MIN_HEIGHT;
      }
    }
    if (params.containsKey(VIEW_PARAMS_SELECTED_BEGIN_DAY)) {
      mSelectedBeginDay = params.get(VIEW_PARAMS_SELECTED_BEGIN_DAY);
    }
    if (params.containsKey(VIEW_PARAMS_SELECTED_LAST_DAY)) {
      mSelectedLastDay = params.get(VIEW_PARAMS_SELECTED_LAST_DAY);
    }
    if (params.containsKey(VIEW_PARAMS_SELECTED_BEGIN_MONTH)) {
      mSelectedBeginMonth = params.get(VIEW_PARAMS_SELECTED_BEGIN_MONTH);
    }
    if (params.containsKey(VIEW_PARAMS_SELECTED_LAST_MONTH)) {
      mSelectedLastMonth = params.get(VIEW_PARAMS_SELECTED_LAST_MONTH);
    }
    if (params.containsKey(VIEW_PARAMS_SELECTED_BEGIN_YEAR)) {
      mSelectedBeginYear = params.get(VIEW_PARAMS_SELECTED_BEGIN_YEAR);
    }
    if (params.containsKey(VIEW_PARAMS_SELECTED_LAST_YEAR)) {
      mSelectedLastYear = params.get(VIEW_PARAMS_SELECTED_LAST_YEAR);
    }

    mMonth = params.get(VIEW_PARAMS_MONTH);
    mYear = params.get(VIEW_PARAMS_YEAR);

    mHasToday = false;
    mToday = -1;

    mCalendar.set(Calendar.MONTH, mMonth);
    mCalendar.set(Calendar.YEAR, mYear);
    mCalendar.set(Calendar.DAY_OF_MONTH, 1);
    mDayOfWeekStart = mCalendar.get(Calendar.DAY_OF_WEEK);

    if (params.containsKey(VIEW_PARAMS_WEEK_START)) {
      mWeekStart = params.get(VIEW_PARAMS_WEEK_START);
    } else {
      mWeekStart = mCalendar.getFirstDayOfWeek();
    }

    mNumCells = CalendarUtils.getDaysInMonth(mMonth, mYear);
    for (int i = 0; i < mNumCells; i++) {
      final int day = i + 1;
      if (sameDay(day, today)) {
        mHasToday = true;
        mToday = day;
      }

      mIsPrev = prevDay(day, today);
    }

    mNumRows = calculateNumRows();
  }

  private boolean sameDay(int monthDay, Time time) {
    return (mYear == time.year) && (mMonth == time.month) && (monthDay == time.monthDay);
  }

  private int calculateNumRows() {
    int offset = findDayOffset();
    int dividend = (offset + mNumCells) / mNumDays;
    int remainder = (offset + mNumCells) % mNumDays;
    return (dividend + (remainder > 0 ? 1 : 0));
  }

  private void onDayClick(SimpleMonthAdapter.CalendarDay calendarDay) {
    int day = calendarDay.day;
    //如果是不可用直接退出
    if (mDisableDays.contains(day)) return;
    if (mOnDayClickListener != null && (isPrevDayEnabled || !((calendarDay.month == today.month)
        && (calendarDay.year == today.year)
        && calendarDay.day < today.monthDay))) {
      mOnDayClickListener.onDayClick(this, calendarDay);
    }
  }

  public void setOnDayClickListener(OnDayClickListener onDayClickListener) {
    mOnDayClickListener = onDayClickListener;
  }

  public int getYear() {
    return mYear;
  }

  public interface OnDayClickListener {
    void onDayClick(SimpleMonthView simpleMonthView,
        SimpleMonthAdapter.CalendarDay calendarDay);
  }
}
