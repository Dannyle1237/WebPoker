package uta.cse3310;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Random;

import uta.cse3310.UserEvent.UserEventType;

public class Game {
    int maxPlayers = 5;
    char quotesChar = '"';
    ArrayList<Player> players = new ArrayList<Player>();
    ArrayList<Card> deck = new ArrayList<Card>();
    String[] suites = {"HEARTS", "CLUBS", "DIAMONDS", "SPADES"};
    String[] values = {"ACE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE","TEN", "JACK", "QUEEN", "KING"};
    int winner = 0;

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

    String exportPlayerRequestedDeck(String msg){
        GsonBuilder builder = new GsonBuilder();
        Gson gson_builder = builder.create();
        UserEvent event = gson_builder.fromJson(msg, UserEvent.class);
        Gson gson = new Gson();
        int ID = event.playerID;
        String s = "{\"your_hand\":" + gson.toJson(players.get(ID-1).Cards) + "}";
        System.out.println(s);
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
                System.out.println("Game will not start: Ready button pressed by:" + players.get(i).Id);
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

    public UserEvent.UserEventType processMessage(String msg) {

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
        if(event.event == UserEventType.SEND_HAND){

        }
        return event.event;
    }

    public void startGame(){
        deck = initalizeDeck();
        printDeck();
        deal();
        printDeck();
    }

    public ArrayList<Card> initalizeDeck(){
        ArrayList<Card> sorted = new ArrayList<Card>();
        Card temp;
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 13; j++){
                temp = new Card();
                temp.suite = Card.Suite.valueOf(suites[i]);
                temp.value = Card.Value.valueOf(values[j]);
                sorted.add(temp);
                System.out.println("Card added: " + temp.suite + " " + temp.value);
            }
        }
        return sorted;
    }

    //Debug method to print out the Deck and see if our values are right
    public void printDeck(){
        for(int i = 0; i < deck.size(); i++){
            System.out.println("Deck: Card " + i + ": " + deck.get(i).suite + " " + deck.get(i).value);
        }
    }

    public void deal(){
        for(int i = 0; i < players.size(); i++){
            for(int j = 0; j < 5; j++){
                Random rand = new Random();
                int random = rand.nextInt(deck.size());
                players.get(i).Cards[j] = deck.get(random);
                deck.remove(random);
            }
        }
    }
    
    public int bestHand(){ //Returns number ID of winner
        //We initalize the best hand to automatically be player 1's
        Player bestPlayer = players.get(0);
        Hand bestHand = new Hand();
        bestHand.cards = bestPlayer.Cards;
        Player compPlayer;
        Hand compHand = new Hand();

        //Loop through every single player besides the first
        for(int i = 1; i < players.size(); i++){
            compPlayer = players.get(i);
            compHand.cards = compPlayer.Cards;

            /*If compPlayer's hand is better, we set bestHand to theirs. 
            No need to change compPlayer, since it will change at 
            Beginning of loop anyways*/
            if(compHand.is_better_than(bestHand)){
                bestPlayer = compPlayer;
                bestHand.cards = compPlayer.Cards;
            }
        }
        winner = bestPlayer.Id;
        return winner;
    }
    public Game() {
        System.out.println("creating a Game Object");
    }

}
