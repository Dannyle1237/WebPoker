package uta.cse3310;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Arrays;

public class Hand
{

    //private transient int i=10;
    // marked transient they will not serialized / deserialized

    public Card[] cards;    
    
    public Hand()
    {
    }

    public boolean is_better_than(Hand H)
    {
        String[] types = {"HIGH CARD", "PAIR", "TWO PAIR", "THREE OF A KIND", "STRAIGHT", "FLUSH", "FULL HOUSE", "FOUR OF A KIND", "STRAIGHT FLUSH", "ROYAL FLUSH"};
        Card[] hand1 = sortSuites(this.cards);
        Card[] hand2 = sortSuites(H.cards);
        hand1 = sortSuites(hand1);
        hand2 = sortSuites(hand2);
        
        String hand1Type = pokerHand(hand1);
        String hand2Type = pokerHand(hand2);
        
        int hand1Value = 0, hand2Value = 0;
        for(int i = 0; i < types.length; i++){
            if(types[i] == hand1Type){
                hand1Value = i;
                break;
            }
        }
        for(int i = 0; i < types.length; i++){
            if(types[i] == hand2Type){
                hand2Value = i;
                break;
            }
        }
        
        if(hand1Value > hand2Value){
            return true;
        }
        else{
            hand1 = sortSuitesHigh(this.cards);
            hand2 = sortSuitesHigh(H.cards);
            if(hand1Type.equals(hand2Type)){
                switch(hand1Type){
                    case "ROYAL FLUSH":
                        return false;
                        
                    case "STRAIGHT FLUSH":
                        //System.out.println("Straight flush");
                        if(convertHigh(hand1[4]) > convertHigh(hand2[4])){
                            return true;
                        }
                        return false;
                        
                   case "FOUR OF A KIND":
                        //System.out.println("FOUR OF A KIND");
                        if(convertHigh(hand1[2]) > convertHigh(hand2[2])){
                            return true;
                        }
                        else if( (convertHigh(hand1[0]) > convertHigh(hand2[0])) || (convertHigh(hand1[4]) > convertHigh(hand2[4])) ){
                             return true;
                        }
                        return false;
                        
                   case "FULL HOUSE":
                        //System.out.println("FULL HOUSE");
                        if(convertHigh(hand1[2]) > convertHigh(hand2[2])){
                           return true;
                        }
                        else if( (convertHigh(hand1[0]) > convertHigh(hand2[0])) || (convertHigh(hand1[4]) > convertHigh(hand2[4])) ){
                           return true;
                        }
                        return false;
                       
                   case "FLUSH":
                        //System.out.println("FLUSH");
                        if(convertHigh(hand1[4]) > convertHigh(hand2[4])){
                           return true;
                        }
                        return false;
                       
                   case "STRAIGHT":
                        //System.out.println("STRAIGHT");
                        if(convertHigh(hand1[4]) > convertHigh(hand2[4])){
                            return true;
                        }
                        return false;
                       
                   case "THREE OF A KIND":
                        //System.out.println("THREE OF A KIND");
                        int max1, max2;
                        if(convertHigh(hand1[2]) > convertHigh(hand2[2])){
                            return true;
                        }
                        else{
                            max1 = convertHigh(hand1[0]);
                            max2 = convertHigh(hand2[0]);
                           
                            for(int i = 1; i < 5; i++){
                                if(max1 < convertHigh(hand1[i])){
                                    max1 = convertHigh(hand1[i]);
                                }
                                if(max2 < convertHigh(hand2[i])){
                                    max2 = convertHigh(hand2[i]);
                                }
                            }
                            if(max1 > max2){
                                return true;
                            }
                                return false;
                        }
                    case "TWO PAIR":
                        //System.out.println("TWO PAIR");
                        int highestPair1 = 0 , highestPair2 = 0;
                        int lowerPair1 = 0, lowerPair2 = 0;
                        int singleCard1 = 0, singleCard2 = 0;
                        for(int i = 0; i < 4; i++){
                            if(convertHigh(hand1[i]) == convertHigh(hand1[i+1])){
                                if(highestPair1 < convertHigh(hand1[i])){
                                    lowerPair1 = highestPair1;
                                    highestPair1 = convertHigh(hand1[i]);
                                }
                            } 
                            if(convertHigh(hand2[i]) == convertHigh(hand2[i+1])){
                                if(highestPair2 < convertHigh(hand2[i])){
                                    lowerPair2 = highestPair2;
                                    highestPair2 = convertHigh(hand2[i]);
                                }
                            }
                        }
                        if(highestPair1 > highestPair2){
                            return true;
                        }
                        else if(highestPair1 == highestPair2){
                            if(lowerPair1 > lowerPair2){
                                return true;
                            }
                            else if(lowerPair1 == lowerPair2){
                                for(int i = 0; i < 5; i++){
                                    if((convertHigh(hand1[i]) != highestPair1 ) || (convertHigh(hand1[i]) != lowerPair1)){
                                        singleCard1 = convertHigh(hand1[i]);
                                    }
                                      if((convertHigh(hand2[i]) != highestPair2 ) || (convertHigh(hand2[i]) != lowerPair2)){
                                        singleCard2 = convertHigh(hand2[i]);
                                    }
                                }
                                if(singleCard1 > singleCard2){
                                    return true;
                                }
                            }
                        }
                        return false;

                    case "PAIR":
                        //System.out.println("PAIR");
                        int pair1 = 0, pair2 = 0, indexOfPair1 = 0, indexOfPair2 = 0,  kicker1 = 0,  kicker2 = 0;
                        for(int i = 0; i < 4; i++){
                            if(convertHigh(hand1[i]) == convertHigh(hand1[i+1])){
                                pair1 = convertHigh(hand1[i]);
                                indexOfPair1 = i+1;
                            }
                            if(convertHigh(hand2[i]) == convertHigh(hand2[i+1])){
                                pair2 = convertHigh(hand2[i]);
                                indexOfPair2 = i+1;
                            }
                        }
                        if(pair1 > pair2){
                            return true;
                        }
                        else if(pair1 == pair2){
                            if(indexOfPair1 == 4){
                                kicker1 = convertHigh(hand1[2]);
                            }
                            else{
                                kicker1 = convertHigh(hand1[4]);
                            }
                            if(indexOfPair2 == 4){
                                kicker2 = convertHigh(hand2[2]);
                            }
                            else{
                                kicker2 = convertHigh(hand2[4]);
                            }
                            if(kicker1 > kicker2){
                                return true;
                            }
                        }
                        return false;
                    case "HIGH CARD":
                        //System.out.println("HIGH CARD");
                        if(convertHigh(hand1[4]) > convertHigh(hand2[4])){
                            return true;
                        }
                        else{
                            return false;
                        }
                }
                return false;
            }
        }
        return false;
    }
    
    //Function to convert card's value into an integer (with ace as lowest)
    public static int convert(Card card){
            switch (card.value){
                case ACE:     return 1;     
                case TWO:     return 2;      
                case THREE:   return 3;  
                case FOUR:    return 4;
                case FIVE:    return 5; 
                case SIX:     return 6;      
                case SEVEN:   return 7;     
                case EIGHT:   return 8;    
                case NINE:    return 9;      
                case TEN:     return 10;       
                case JACK:    return 11;      
                case QUEEN:   return 12;
                case KING:    return 13;
                
                default: return 0;                
            }    

    }
    
    //Same function as convert (Ace as highest instead)
    public static int convertHigh(Card card){
            switch (card.value){
                case ACE:     return 14;     
                case TWO:     return 2;      
                case THREE:   return 3;  
                case FOUR:    return 4;
                case FIVE:    return 5; 
                case SIX:     return 6;      
                case SEVEN:   return 7;     
                case EIGHT:   return 8;    
                case NINE:    return 9;      
                case TEN:     return 10;       
                case JACK:    return 11;      
                case QUEEN:   return 12;
                case KING:    return 13;
                
                default: return 0;                
            }    

    }
    
    //Function to sort the a hand in the order of suites
    public static Card[] sortSuites(Card[] hand){
        for(int i = 0; i < hand.length - 1; i++){
            for(int j = 0; j < (hand.length -i -1); j++){
                if( convert(hand[j]) > convert(hand[j+1]) ){
                    //System.out.println("Swapping: " + hand[j].value + " " + hand[j+1].value);
                    Card temp = hand[j];
                    hand[j] = hand[j+1];
                    hand[j+1] = temp;
                }
            }
        }
        return hand;
    }
    
    //Same as sort function, but with Ace as the highest value card  `
    public static Card[] sortSuitesHigh(Card[] hand){
        for(int i = 0; i < hand.length - 1; i++){
            for(int j = 0; j < (hand.length -i -1); j++){
                if( convertHigh(hand[j]) > convertHigh(hand[j+1]) ){
                    //System.out.println("Swapping: " + hand[j].value + " " + hand[j+1].value);
                    Card temp = hand[j];
                    hand[j] = hand[j+1];
                    hand[j+1] = temp;
                }
            }
        }
        return hand; 
    }
    //Method to find what kind of hand 
    public static String pokerHand(Card[] cards)
    {    
        //test for flushs
        String suite = cards[0].suite.toString();
        boolean sameSuite = true;
        for(int i = 1; i < 5; i++)
        {
            if(cards[i].suite != Card.Suite.valueOf(suite))
            {
                sameSuite = false;
            }
        }
        
        //test for Straight
        boolean straight = true;
        //tests normally by values assigned by convert
        for(int i = 0; i < 5 - 1; i++)
        {
            if( (convert(cards[i]) + 1) != (convert(cards[i+1])) )
            {
                straight = false;
            }
        }

        //tests for royal straight , with ACE being the highest value
        String[] royalValue = {"ACE", "TEN", "JACK", "QUEEN", "KING"};
        boolean royal = true;
        for(int i = 0; i < 5-1; i++)
        {
            if( cards[i].value.toString() != royalValue[i] )
            {
                royal = false;
            }
        }
        if(royal && sameSuite){
            return "ROYAL FLUSH";
        }
        if(straight && sameSuite){
            return "STRAIGHT FLUSH";
        }
        if(sameSuite){
            return "FLUSH";
        }
        if(straight || royal){
            return "STRAIGHT";
        }
        
        //Tests for how many of each value
        int[] valuesCounter = countValues(cards);
        
        //Check for Four-of-a-kind
        for(int i = 0; i < valuesCounter.length; i++){
            if(valuesCounter[i] == 4){
                return "FOUR OF A KIND";
            } 
        }
        
        //variables to keep track if there is a 3 of a kind or pair
        boolean three = false;
        boolean two = false;
        //Check for full house
        for(int i = 0; i < valuesCounter.length ; i++){
            if(valuesCounter[i] == 3){
                three = true;
            }
            if(valuesCounter[i] == 2){
                two = true;
            }
            if(three && two){
                return "FULL HOUSE";
            }    
        }
        if(three){
            return "THREE OF A KIND";
        }
        
        //Check for 2 pair
        int count = 0;
        if(two){
            for(int i = 0; i < valuesCounter.length; i++){
                if(valuesCounter[i] == 2){
                    count ++;   
                }
            }
            if(count == 2){
                return "TWO PAIR";
            }
            else{
                return "PAIR";
            }
        }
        
            
        
        return "HIGH CARD";
    }
    
    //count number of each suite in hand
    public static int[] countValues(Card[] hand){
        //Array to keep track of each values with index 0-12 being ACE, TWO, THREE... QUEEN, KING
        int[] values = new int[13];
        for(int i = 0; i < 5; i++){
            switch (hand[i].value){
                case ACE:     values[0] += 1;
                                break;     
                case TWO:     values[1] += 1;     
                                break; 
                case THREE:   values[2] += 1;
                                break;   
                case FOUR:    values[3] += 1;
                                break; 
                case FIVE:    values[4] += 1;
                                break;  
                case SIX:     values[5] += 1;
                                break;       
                case SEVEN:   values[6] += 1;
                                break;      
                case EIGHT:   values[7] += 1;
                                break;     
                case NINE:    values[8] += 1;
                                break;       
                case TEN:     values[9] += 1;
                                break;        
                case JACK:    values[10] += 1;
                                break;       
                case QUEEN:   values[11] += 1;
                                break; 
                case KING:    values[12] += 1;
                                break;  
                }
        }
        return values;
    }
}
