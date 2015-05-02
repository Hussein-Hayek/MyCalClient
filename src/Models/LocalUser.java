package Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class LocalUser extends User {
    private IntegerProperty N_requests=new SimpleIntegerProperty();

    public int getN_requests() {
        return N_requests.get();
    }

    public IntegerProperty n_requestsProperty() {
        return N_requests;
    }

    public void setN_requests(int n_requests) {
        this.N_requests.set(n_requests);
    }
}
