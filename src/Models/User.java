package Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class User {
    private IntegerProperty id;
    private StringProperty firstName;
    private StringProperty lastName;
    private StringProperty birthday;
    private IntegerProperty N_friends;
    private IntegerProperty N_events;
    private IntegerProperty N_going;
    private StringProperty photo;


    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getBirthday() {
        return birthday.get();
    }

    public StringProperty birthdayProperty() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday.set(birthday);
    }

    public int getN_friends() {
        return N_friends.get();
    }

    public IntegerProperty n_friendsProperty() {
        return N_friends;
    }

    public void setN_friends(int n_friends) {
        this.N_friends.set(n_friends);
    }

    public int getN_events() {
        return N_events.get();
    }

    public IntegerProperty n_eventsProperty() {
        return N_events;
    }

    public void setN_events(int n_events) {
        this.N_events.set(n_events);
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

    public String getPhoto() {
        return photo.get();
    }

    public StringProperty photoProperty() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo.set(photo);
    }
}
