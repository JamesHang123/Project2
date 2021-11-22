package fitness_center_booking_app.entity;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.List;

public class ScheduledFitnessClass implements Serializable {
    private String key;
    private String name;
    private String employeeName;
    private DifficultyLevel difficultyLevel;
    private DayOfWeek day;
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private int capacity;

    public ScheduledFitnessClass() {
    }

    public ScheduledFitnessClass(String key, String name, String employeeName, DifficultyLevel difficultyLevel, DayOfWeek day, int startHour, int startMinute, int endHour, int endMinute, int capacity) {
        this.key = key;
        this.name = name;
        this.employeeName = employeeName;
        this.difficultyLevel = difficultyLevel;
        this.day = day;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.capacity = capacity;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public static boolean scheduledOnTheSameDay(List<ScheduledFitnessClass> scheduledFitnessClassList, String key, String name, DayOfWeek day) {
        for (ScheduledFitnessClass scheduledFitnessClass : scheduledFitnessClassList) {
            if (!scheduledFitnessClass.getKey().equals(key) && scheduledFitnessClass.getName().equals(name) && scheduledFitnessClass.getDay() == day) {
                return true;
            }
        }
        return false;
    }

    public static boolean timeBefore(int hour, int minute, int hour1, int minute1) {
        if (hour == hour1) {
            return minute <= minute1;
        } else if (hour < hour1) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean timeAfter(int hour, int minute, int hour1, int minute1) {
        if (hour == hour1) {
            return minute >= minute1;
        } else if (hour < hour1) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isConflict(List<ScheduledFitnessClass> scheduledFitnessClassList, String key, String employeeName, DayOfWeek day, int startHour, int startMinute, int endHour, int endMinute) {
        for (ScheduledFitnessClass scheduledFitnessClass : scheduledFitnessClassList) {
            if (!scheduledFitnessClass.getKey().equals(key) && scheduledFitnessClass.getEmployeeName().equals(employeeName) && scheduledFitnessClass.getDay() == day) {
                if (!(timeBefore(endHour, endMinute, scheduledFitnessClass.getStartHour(), scheduledFitnessClass.getStartMinute())
                        || timeAfter(startHour, startMinute, scheduledFitnessClass.getEndHour(), scheduledFitnessClass.getEndMinute()))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int getIndex(List<ScheduledFitnessClass> fitnessClassList, String key) {
        for (int i = 0; i < fitnessClassList.size(); i ++) {
            if (fitnessClassList.get(i).getKey().equals(key)) {
                return i;
            }
        }
        return -1;
    }
}
