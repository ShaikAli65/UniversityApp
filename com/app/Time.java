package app;
import java.io.Serializable;
import java.util.Objects;

public class Time implements Serializable, Comparable<Time>
{
    final public Date date;
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
        return date + " " + String.format("%-3s", start + "H");
    }

    public void addTime(){
        System.out.print("Enter start time (24H) :");
        this.start = University.getIntegerFromInput();
        date.getNewDateFromStdIn();
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

    public int compareTo(Time other) {
        int i = this.date.compareTo(other.date);
        if (i == 0){
            return Integer.compareUnsigned(start, other.start);
        }
        return i;
    }
}
