package Models;

import java.util.GregorianCalendar;

public class MyCalCalendar extends GregorianCalendar {
    Event event=null;

    public MyCalCalendar(int year, int month, int day){
        this.set(year,month,day);
    }

    public MyCalCalendar(int year, int month, int day, Event event){
        this.set(year,month,day);
        this.event=event;
    }
}
