package util.valueObject;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateHour {
    // HHMMDD
    // hh mm dd
    String value; 

    public DateHour(String value) {
        this.value = value; 
    }

    public String getValue() {
        return this.value; 
    }
    public void setValue(String value) {
        this.value = value;
    }

    public String getHour() {
        String hour = this.value.substring(0, 2);
        return hour;
    }
    public String getMinutes() {
        String minutes = this.value.substring(2, 4);
        return minutes;
    }

    public String getHourAndMinutes() {
        String hourAndMinutes = this.value.substring(0, 4);
        return hourAndMinutes;
    }

    public String getDays() {
        return this.value.substring(4);
    }
 
    public String[] getDaysArray() {
        String[] separeted = this.value.substring(4).split("");
        return separeted;
    }

    public String[] getDaysArrayName() {
        String separeted = this.getDays();
        String[] days = new String[separeted.length()];

        for(int i = 0; i < separeted.length(); i++) {
            switch (separeted.charAt(i)) {
                case '0':
                    days[i] = "DOM";
                break;
                case '1':
                    days[i] = "SEG";
                break;
                case '2':
                    days[i] = "TER";
                break;
                case '3':
                        days[i] = "QUA";
                    break;
                case '4':
                        days[i] = "QUI";
                    break;
                case '5':
                        days[i] = "SEX";
                    break;
                case '6': 
                    days[i] = "SAB";
                break;
                default:
                    break;
            }
        }
        return days;
    }

    public ParsedSchedule getDateValue() {  

        int hour = Integer.parseInt(this.getHour());
        int minute = Integer.parseInt(this.getMinutes());
        LocalTime time = LocalTime.of(hour, minute);
    
        List<DayOfWeek> days = new ArrayList<>();

        for(char c : this.getDays().toCharArray()) {
            int dayNumber = Character.getNumericValue(c);

            DayOfWeek day;

            if(dayNumber == 0) {
                day = DayOfWeek.SUNDAY;
            } else {
                day = DayOfWeek.of(dayNumber);
            }

            days.add(day);
        }

        return new ParsedSchedule(time, days);
    }

    public static DateHour fromDate(Date date) {
    if (date == null) {
        return null;
    }

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);

    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);

    String hh = String.format("%02d", hour);
    String mm = String.format("%02d", minute);

    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    
    int yourDayFormat = dayOfWeek - 1;

    String finalValue = hh + mm + yourDayFormat;

    return new DateHour(finalValue);
}

}

class ParsedSchedule {
    public LocalTime time;
    public List<DayOfWeek> days;

    public ParsedSchedule(LocalTime time, List<DayOfWeek> days) {
        this.time = time;
        this.days = days;
    }
}