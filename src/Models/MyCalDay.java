package Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class MyCalDay extends GregorianCalendar {
    private ObservableList<Event> events=FXCollections.observableArrayList();
    private boolean empty=false;

    public MyCalDay(int year, int month, int day){
        super(year,month,day);
    }

    public MyCalDay(boolean empty){
        this.empty=empty;
    }


    public ObservableList<Event> getEvents() {
        return events;
    }

    public void setEvents(ObservableList<Event> events) {
        this.events = events;
    }


    public String getDate(){
        if(empty)
            return "";
        else
            return ""+get(DATE);
    }
}
