package uta.cse3310;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import uta.cse3310.UserEvent.UserEventType;

public class Game {
    int maxPlayers = 5;
    ArrayList<Player> players = new ArrayList<Player>();
    ArrayList<Card> deck = new ArrayList<Card>();
    
    int turn; // player ID that has the current turn

    String exportStateAsJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public boolean addPlayer(Player p) {
        if(players.size() > maxPlayers){
            System.out.println("Too many players already");
            return false;
        }
        else{
            players.add(p);
            return true;
        }
    }

    public void removePlayer(int playerID){
        players.remove(playerID);
        for(int i = playerID; i < maxPlayers; i++){
            players.get(i).Id -= 1;
        }
        System.out.println("Current players " + players);
    }

    public void processMessage(String msg) {

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        // take the string we just received, and turn it into a user event
        UserEvent event = gson.fromJson(msg, UserEvent.class);

        if (event.event == UserEventType.NAME) {
            players.get(event.playerID).SetName(event.name);
        }

        if(event.event == UserEventType.LEAVE) {
            System.out.println("Removed Player");
            removePlayer(event.playerID);
        }

        if(event.event == UserEventType.JOIN) {
            players.get(event.playerID).Name = event.name;
        }
    }

    public Game() {
        System.out.println("creating a Game Object");
    }

}
