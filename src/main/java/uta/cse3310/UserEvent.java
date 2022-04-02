package uta.cse3310;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;

public class UserEvent {

    public enum UserEventType {
        NAME, JOIN, LEAVE, READY, SEND_HAND, MOVE, SWAP;

        private UserEventType() {
        }
    };

    UserEventType event;
    int playerID;
    String name, move;
    boolean status;
    int raise;
    boolean swapCard1, swapCard2, swapCard3, swapCard4, swapCard5;
    public UserEvent() {
    }

}
