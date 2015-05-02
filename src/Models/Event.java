package Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Event {
    private IntegerProperty id=new SimpleIntegerProperty();
    private IntegerProperty creator_id=new SimpleIntegerProperty();
    private StringProperty name=new SimpleStringProperty();
    private StringProperty location=new SimpleStringProperty();
    private StringProperty dateBegin=new SimpleStringProperty();
    private StringProperty dateEnd=new SimpleStringProperty();
    private StringProperty timeBegin=new SimpleStringProperty();
    private StringProperty timeEnd=new SimpleStringProperty();
    private StringProperty timeZone=new SimpleStringProperty();
    private IntegerProperty N_going=new SimpleIntegerProperty();
    private IntegerProperty N_invited=new SimpleIntegerProperty();
    private StringProperty mapUrl =new SimpleStringProperty();

    public Event(){

    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getCreator_id() {
        return creator_id.get();
    }

    public IntegerProperty creator_idProperty() {
        return creator_id;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id.set(creator_id);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getLocation() {
        return location.get();
    }

    public StringProperty locationProperty() {
        return location;
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public String getDateBegin() {
        return dateBegin.get();
    }

    public StringProperty dateBeginProperty() {
        return dateBegin;
    }

    public void setDateBegin(String dateBegin) {
        this.dateBegin.set(dateBegin);
    }

    public String getDateEnd() {
        return dateEnd.get();
    }

    public StringProperty dateEndProperty() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd.set(dateEnd);
    }

    public String getTimeBegin() {
        return timeBegin.get();
    }

    public StringProperty timeBeginProperty() {
        return timeBegin;
    }

    public void setTimeBegin(String timeBegin) {
        this.timeBegin.set(timeBegin);
    }

    public String getTimeEnd() {
        return timeEnd.get();
    }

    public StringProperty timeEndProperty() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd.set(timeEnd);
    }

    public String getTimeZone() {
        return timeZone.get();
    }

    public StringProperty timeZoneProperty() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone.set(timeZone);
    }

    public int getN_going() {
        return N_going.get();
    }

    public IntegerProperty n_goingProperty() {
        return N_going;
    }

    public void setN_going(int n_going) {
        this.N_going.set(n_going);
    }

    public int getN_invited() {
        return N_invited.get();
    }

    public IntegerProperty n_invitedProperty() {
        return N_invited;
    }

    public void setN_invited(int n_invited) {
        this.N_invited.set(n_invited);
    }

    public String getMapUrl() {
        return mapUrl.get();
    }

    public StringProperty mapUrlProperty() {
        return mapUrl;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl.set(mapUrl);
    }
}
