package uta.cse3310;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UserEvent {

    public enum UserEventType {
        NAME, STAND, HIT, CALL, JOIN, LEAVE;

        private UserEventType() {
        }
    };

    UserEventType event;
    int playerID;
    String name;

    public UserEvent() {
    }

}
