package nimdanoob.calendarview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

public class DatePickerView extends RecyclerView {
  protected Context mContext;
  protected SimpleMonthAdapter mAdapter;
  protected int mCurrentScrollState = 0;
  protected long mPreviousScrollPosition;
  protected int mPreviousScrollState = 0;
  private DatePickerListener mDatePickerListener;
  private TypedArray typedArray;
  private OnScrollListener onScrollListener;
  private LinearLayoutManager mLinearLayoutManager;
  private int mCurrentYear;
  private OnYearChangedListener mOnYearChangedListener;
  private DatePickerController mDatePickerController;
  public DatePickerView(Context context) {
    this(context, null);
  }

  public DatePickerView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public DatePickerView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    if (!isInEditMode()) {
      typedArray = context.obtainStyledAttributes(attrs, R.styleable.DatePickerView);
      setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
      init(context);
    }
  }

  public void init(Context paramContext) {
    mLinearLayoutManager = new LinearLayoutManager(paramContext);
    setLayoutManager(mLinearLayoutManager);
    mContext = paramContext;
    onScrollListener = new OnScrollListener() {
      @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
      }

      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        final SimpleMonthView child = (SimpleMonthView) recyclerView.getChildAt(0);
        if (child == null) {
          return;
        }
        SimpleMonthView simpleMonthView =
            (SimpleMonthView) mLinearLayoutManager.findViewByPosition(
                mLinearLayoutManager.findLastVisibleItemPosition());
        if (mCurrentYear != simpleMonthView.getYear()) {
          // 调用 listener
          mCurrentYear = simpleMonthView.getYear();
          if (mOnYearChangedListener != null) {
            mOnYearChangedListener.onYearChange(mCurrentYear);
          }
        }
        mPreviousScrollPosition = dy;
        mPreviousScrollState = mCurrentScrollState;
      }
    };
    setupListView();
    setupAdapter();
  }

  protected void setupAdapter() {
    if (mAdapter == null) {
      mAdapter = new SimpleMonthAdapter(getContext(), typedArray);
    }
    setAdapter(mAdapter);
    mAdapter.notifyDataSetChanged();
  }

  protected void setupListView() {
    setVerticalScrollBarEnabled(false);
    addOnScrollListener(onScrollListener);
    setFadingEdgeLength(0);
  }

  @Override protected void onDetachedFromWindow() {
    removeOnScrollListener(onScrollListener);
    super.onDetachedFromWindow();
  }

  public SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> getSelectedDays() {
    return mAdapter.getSelectedDays();
  }

  public void setOnYearChangedListener(OnYearChangedListener listener) {
    mOnYearChangedListener = listener;
  }

  protected DatePickerListener getDatePickerListener() {
    return mDatePickerListener;
  }

  public void setDatePickerListener(DatePickerListener datePickerListener) {
    this.mDatePickerListener = datePickerListener;
    Log.e("zxw","设置mDatePickerListener"+mDatePickerListener);
  }

  protected TypedArray getTypedArray() {
    return typedArray;
  }

  protected SimpleMonthAdapter getSimpleMonthAdapter() {
    return mAdapter;
  }

  public interface OnYearChangedListener {
    void onYearChange(int year);
  }

  public void setDisable(){

  }
}
