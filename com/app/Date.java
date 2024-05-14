package app;

import java.util.Objects;
import java.util.Scanner;

public class Date
{
    final Scanner sc=new Scanner(System.in);
    private int day=0;
    private int month=0;
    private int year=0;

    public Date(){

    }

    Date(int day, int month, int year)
    {
        this.day=day; 
        this.month=month;
        this.year=year;
    }

    public void newDate(){
        System.out.print("Enter DD/MM/YYYY\t:");
        String x = sc.next();
        String[] ar = x.split("/");
        try {
            day=Integer.parseInt(ar[0]);
            month=Integer.parseInt(ar[1]);
            year=Integer.parseInt(ar[2]);
        }
        catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            day = month = year = 0;
        }
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

    @Override
    public int hashCode() {
        return Objects.hash(day, month, year);
    }
}