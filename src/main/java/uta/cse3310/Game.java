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
    int pot = 0;
    int ante = 5;
    int call = 0;
    int round = 0; //Round id (o = 1st betting, 1 = drawing, 2 = final betting)
    int[] playersMoney;
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

    String exportPlayersMoney(){
        Gson gson = new Gson();
        playersMoney = new int[players.size()];
        for(int i = 0; i < players.size(); i++){
            playersMoney[i] = players.get(i).money;
        }
        String s = "{\"players_money\":" + gson.toJson(playersMoney) + "}";
        JsonObject jsonObject = JsonParser.parseString(s).getAsJsonObject();
        return gson.toJson(jsonObject);
    }

    String exportTurn(){
        Gson gson = new Gson();
        String s = "{\"round\":" + gson.toJson(round) + ",\"turn\":" + gson.toJson(turn) + ",\"pot\":" + pot + ",\"calls\":[" + players.get(0).call;
        for(int i = 1; i < players.size(); i++){
            s += "," + players.get(i).call;
        }
        s += "]}";
        JsonObject jsonObject = JsonParser.parseString(s).getAsJsonObject();
        return gson.toJson(jsonObject);
    }

    String exportRound(){
        Gson gson = new Gson();
        String s = "{\"round\":" + gson.toJson(round) + "}";
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

    public boolean allPlayersSwapped(){
        boolean yes = true;
        for(int i = 0; i < players.size(); i++){
            if(players.get(i).swapping == true){
                yes = false;
            }
        }
        return yes;
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
        if(event.event == UserEventType.MOVE){
            System.out.println("Player " + event.playerID + " is doing a move " + event.move);
            if(event.move.equals("CHECK")){
            }
            if(event.move.equals("FOLD")){
                players.get(event.playerID-1).folded = true;
            }
            if(event.move.equals("CALL")){
                players.get(event.playerID-1).money -= players.get(event.playerID-1).call;
                pot += players.get(event.playerID-1).call;
                for(int i = 1; i <= players.size(); i++){
                    if(i!= event.playerID){
                        players.get(i-1).call += players.get(event.playerID-1).call;
                    }
                }
                players.get(event.playerID-1).call = 0;
            }
            if(event.move.equals("RAISE")){
                pot += event.raise;
                players.get(event.playerID-1).money -= event.raise;
                for(int i = 1; i <= players.size(); i++){
                    if(i!= event.playerID){
                        players.get(i-1).call += event.raise;
                    }
                    else{
                        players.get(event.playerID-1).call -= event.raise;
                    }
                }
            }
            if(turn < players.size()){
                turn ++;
            }
            else{
                for(int i = 0; i < players.size(); i++){
                    players.get(i).swapping = true;
                }
                round = 1;
                turn = 1;
            }
            System.out.println("Game now on turn: " + turn);
        }
        if(event.event == UserEventType.SWAP){
            System.out.println("Changing out player" + event.playerID + "hand");
            if(event.swapCard1){
                players.get(event.playerID-1).Cards[0] = draw();
            }
            if(event.swapCard2){
                players.get(event.playerID-1).Cards[1] = draw();
            }
            if(event.swapCard3){
                players.get(event.playerID-1).Cards[2] = draw();
            }
            if(event.swapCard4){
                players.get(event.playerID-1).Cards[3] = draw();
            }
            if(event.swapCard5){
                players.get(event.playerID-1).Cards[4] = draw();
            }
            players.get(event.playerID-1).swapping = false;
        }
        return event.event;
    }

    public void startGame(){
        turn = 1;
        call = 0;
        for(int i = 0; i < players.size(); i++){
            players.get(i).call = 0;
            players.get(i).betted = 0;
            players.get(i).money -= ante;
            pot += ante;
            players.get(i).folded = false;
        }
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
    
    public Card draw(){
        Random rand = new Random();
        int random = rand.nextInt(deck.size());
        Card randomCard = deck.get(random);
        deck.remove(random);
        return randomCard;
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
