package uta.cse3310;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import uta.cse3310.UserEvent.UserEventType;

public class Game {
    int maxPlayers = 5;
    char quotesChar = '"';
    ArrayList<Player> players = new ArrayList<Player>();
    ArrayList<Card> deck = new ArrayList<Card>();
    boolean started = false;
    int turn; // player ID that has the current turn

    String exportStateAsJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    String exportNumPlayersAsJSON(){
        Gson gson = new Gson();
        String s = "{\"numPlayers\":" + players.size() +"}" ;
        JsonObject jsonObject = JsonParser.parseString(s).getAsJsonObject();
        return gson.toJson(jsonObject);
    }
    
    String exportPlayerNamesAsJson(){
        Gson gson = new Gson();
        String s = "{\"playerNames\":[\"" + players.get(0).Name + "\"";
        for(int i = 1; i < players.size(); i++){
            s += ",\"" + players.get(i).Name + "\"";
        }
        s += "]}";
        JsonObject jsonObject = JsonParser.parseString(s).getAsJsonObject();
        return gson.toJson(jsonObject);
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

    public boolean allPlayersReady(){
        boolean start = true;
        if(players.size() < 2){
            start = false;
        }
        for(int i = 0; i < players.size(); i++){
            if(players.get(i).ready == false){
                System.out.println("Game will not start: " + players.get(i).Id);
                start = false;
            }
        }
        
        if(start){
            System.out.println("Game starting...");
            started = true;
            startGame();
        }
        return start;
    }

    public void processMessage(String msg) {

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        // take the string we just received, and turn it into a user event
        UserEvent event = gson.fromJson(msg, UserEvent.class);

        if (event.event == UserEventType.NAME) {
            players.get(event.playerID).setName(event.name);
        }

        if(event.event == UserEventType.LEAVE) {
            System.out.println("Removed Player");
            removePlayer(event.playerID);
        }

        if(event.event == UserEventType.JOIN) {
            System.out.println("Changed player" + event.playerID);
            players.get(event.playerID-1).Name = event.name;
        }

        if(event.event == UserEventType.READY){
            System.out.println("player"+event.playerID+" is " + event.status);
            players.get(event.playerID-1).ready = event.status;
        }
    }

    public void startGame(){

    }
    public Game() {
        System.out.println("creating a Game Object");
    }

}
