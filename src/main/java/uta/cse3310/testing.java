package uta.cse3310;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import uta.cse3310.UserEvent.UserEventType;

public class testing {

    public static void main(String[] args){
        ArrayList<Player> players = new ArrayList<Player>();

        Player player = new Player(5);
        player.setName("Dan");
        players.add(player);
        player = new Player(1);
        player.setName("Bob");
        players.add(player);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String s = "{\"numPlayers\":" + players.size() +"}" + gson.toJson(players) ;
        System.out.println(s);


    }

}
