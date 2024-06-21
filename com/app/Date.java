package app;

import java.util.Objects;

public class Date implements java.io.Serializable, Comparable<Date>
{
    private int day=0;
    private int month=0;
    private int year=0;

    public Date(){}
    public Date(String s) {

    }
    public Date(int day, int month, int year)
    {
        this.day=day; 
        this.month=month;
        this.year=year;
    }
    public void setDate(String x) {
        String[] ar = x.split("/");
        try {
            day=Integer.parseInt(ar[0]);
            month=Integer.parseInt(ar[1]);
            year=Integer.parseInt(ar[2]);
        }
        catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            UniversityApp.getError(16);
            day = 1;
            month = 1;
            year = 2000;
        }
    }
    public void getNewDateFromStdIn(){
        System.out.print("Enter DD/MM/YYYY:");
        String x = University.getStringFromInput(true);
        setDate(x);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();

        if(day < 10) sb.append("0");
        sb.append(day).append("-");

        if(month < 10) sb.append("0");
        sb.append(month).append("-").append(year);
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Date date = (Date) o;
        return day == date.day && month == date.month && year == date.year;
    }
    public int compareTo(Date other) {
        if (this.year != other.year) {
            return Integer.compare(this.year, other.year);
        }
        if (this.month != other.month) {
            return Integer.compare(this.month, other.month);
        }
        return Integer.compare(this.day, other.day);
    }
    @Override
    public int hashCode() {
        return Objects.hash(day, month, year);
    }
}