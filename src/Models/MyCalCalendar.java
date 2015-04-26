package Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class MyCalCalendar extends GregorianCalendar {
    private ObservableList<Event> events=FXCollections.observableArrayList();
    private static HashMap<String,ObservableList<Event>> eventsDateMap=new HashMap<>();
    private boolean empty=false;
    private static final GregorianCalendar currentDate=new GregorianCalendar();
    public static void update(){
        eventsDateMap.clear();
        ArrayList<Event> events=ClientSocket.getClientSocket().getEvents();
        for (Event event:events) {
            if (eventsDateMap.get(event.getDateBegin()) == null)
                eventsDateMap.put(event.getDateBegin(), FXCollections.observableArrayList());
            eventsDateMap.get(event.getDateBegin()).add(event);
        }
    }

    public MyCalCalendar(int year,int month,int day){
        super(year,month,day);
    }

    public MyCalCalendar(boolean empty){
        this.empty=empty;
    }

    public static ArrayList<ObservableList<MyCalCalendar>> getMonth(int month,int year){
        ArrayList<ObservableList<MyCalCalendar>> monthList=new ArrayList<>(7);
        for(int i=0;i<7;i++)
            monthList.add(FXCollections.observableArrayList());
        GregorianCalendar requestedMonth=new GregorianCalendar(year,month,1);
        int maxDay=requestedMonth.getActualMaximum(DATE);
        for(int i=1;i<=maxDay;i++){
            MyCalCalendar myCalCalendar=new MyCalCalendar(year,month,i);
            if(eventsDateMap.get(dateFormat(myCalCalendar)) != null)
                myCalCalendar.setEvents(eventsDateMap.get(dateFormat(myCalCalendar)));
            if(myCalCalendar.get(WEEK_OF_MONTH)==2 && myCalCalendar.get(DATE)<=7)
                monthList.get(myCalCalendar.get(DAY_OF_WEEK)-1).add(new MyCalCalendar(true));
            monthList.get(myCalCalendar.get(DAY_OF_WEEK)-1).add(myCalCalendar);
        }
        return monthList;
    }

    public ObservableList<Event> getEvents() {
        return events;
    }

    public void setEvents(ObservableList<Event> events) {
        this.events = events;
    }

    private static String dateFormat(GregorianCalendar gregorianCalendar){
        String format="";
        int day=gregorianCalendar.get(DAY_OF_MONTH);
        int month=gregorianCalendar.get(MONTH)+1;
        int year=gregorianCalendar.get(YEAR);
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

    public String getDate(){
        if(empty)
            return "";
        else
            return ""+get(DATE);
    }
}
