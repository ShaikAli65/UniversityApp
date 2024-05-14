package app;
import java.util.Objects;

public class Time
{
    public Date date;
    public int start;

    public Time(){
        date=new Date();
        start = 0;
    }

    public Time(int s,int day,int month,int year){
        start=s;
        date=new Date(day,month,year);
    }

    public String toString() {
        return date.toString() + "\t" + start + "Hrs";
    }

    public void addTime(){
        System.out.print("Enter start time (24H) :");
        this.start = University.getIntegerFromInput();
        date.newDate();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Time time = (Time) o;
        return start == time.start && Objects.equals(date, time.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, start);
    }
}
