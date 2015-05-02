package Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class MyCalCalendar {
    private HashMap<String,ObservableList<Event>> eventsDateMap=new HashMap<>();
    private int id;
    public MyCalCalendar(int id){
        this.id=id;
    }
    public void update(){
        eventsDateMap.clear();
        ArrayList<Event> events=ClientSocket.getClientSocket().getEvents(id);
        for (Event event:events) {
            if (eventsDateMap.get(event.getDateBegin()) == null)
                eventsDateMap.put(event.getDateBegin(), FXCollections.observableArrayList());
            eventsDateMap.get(event.getDateBegin()).add(event);
        }
    }

    public ArrayList<ObservableList<MyCalDay>> getMonth(int month,int year){
        ArrayList<ObservableList<MyCalDay>> monthList=new ArrayList<>(7);
        for(int i=0;i<7;i++)
            monthList.add(FXCollections.observableArrayList());
        GregorianCalendar requestedMonth=new GregorianCalendar(year,month,1);
        int maxDay=requestedMonth.getActualMaximum(Calendar.DATE);
        for(int i=1;i<=maxDay;i++){
            MyCalDay myCalDay =new MyCalDay(year,month,i);
            if(eventsDateMap.get(dateFormat(myCalDay)) != null)
                myCalDay.setEvents(eventsDateMap.get(dateFormat(myCalDay)));
            if(myCalDay.get(Calendar.WEEK_OF_MONTH)==2 && myCalDay.get(Calendar.DATE)<=7)
                monthList.get(myCalDay.get(Calendar.DAY_OF_WEEK)-1).add(new MyCalDay(true));
            monthList.get(myCalDay.get(Calendar.DAY_OF_WEEK)-1).add(myCalDay);
        }
        return monthList;
    }

    private String dateFormat(GregorianCalendar gregorianCalendar){
        String format="";
        int day=gregorianCalendar.get(Calendar.DAY_OF_MONTH);
        int month=gregorianCalendar.get(Calendar.MONTH)+1;
        int year=gregorianCalendar.get(Calendar.YEAR);
        format+=year+"-";
        if(month<10)
            format+="0"+month;
        else
            format+=month;
        format+="-";
        if(day<10)
            format+="0"+day;
        else
            format+=day;
        return format;
    }
}
