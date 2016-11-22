# Android 日历控件
<img src="http://7xq84c.com1.z0.glb.clouddn.com/calendar_demo.png" />

## 支持属性
- colorCurrentDay 当天的字体颜色
- colorSelectedDayBackground 选中的背景颜色
- cp_background 日历背景色
- cp_header_background 日历头部 星期 背景
等...

## 方法
- 自定义 日历的 起始 和结束日期
- 设置 不可点击 的天数
- 设置 选中的模式，是 单选 还是 选择区间
 等...

```java
  DatePickerController setFirstDate(int year, int month);
  DatePickerController setLastDate(int year, int month);
  DatePickerController setDisableDays(ArrayList<SimpleMonthAdapter.CalendarDay> calendarDays);
  DatePickerController setDayPickerListener(DatePickerListener listener);
  DatePickerController setSelectMode(int mode, @Nullable int fixSelectDay); //fix selectDay can be 0
  DatePickerController setOnConfirmListener(View.OnClickListener listener);
  DatePickerController setOnYearChangeListener(DatePickerView.OnYearChangedListener listener);
  SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> getSelectedDays();
```

### 添加依赖

1.在项目外层的build.gradle中添加JitPack仓库

```
repositories {
	maven {
		url "https://jitpack.io"
	}
}
```

2.在用到的项目中添加依赖  
>	compile 'com.github.nimdanoob:CalendarView:[Latest release](https://github.com/nimdanoob/CalendarView/releases)(<-click it)'  

**举例：**
```
compile 'com.github.nimdanoob:CalendarView:v0.1.0'
```