package at.htlkaindorf.ahif18.network;

import at.htlkaindorf.ahif18.bl.I_Notifiable;
import at.htlkaindorf.ahif18.data.Card;
import at.htlkaindorf.ahif18.data.PlayerInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Networkbuffer is a class that saves(buffers) what comes from the network<br>
 * and provides it to the other parts of the program.<br>
 * <br>
 * Last changed: 2022-06-03
 * @author Jan Mandl
 */
public class NetworkBuffer {

    /**
     * A list with the players that participate in the Muno-Session.
     * Null if there are no other players.
     */
    private List<PlayerInfo> playerList;
    /**
     * The card that was last played by a player
     */
    private int currentPlayerID;
    private Card lastPlayedCard;
    private List<Card> cards;
    private List<I_Notifiable> observers;

    public NetworkBuffer(I_Notifiable observer) {
        //Debug//
        cards = new ArrayList<>();
        observers = new ArrayList<>();
        playerList = new ArrayList<>();
        /*
        String randomNames[] = new String[] {
                "john", "me", "01234567", "pizza","diablo", "pasta42", "h4ck3r"
        };
        Random r = new Random(42);

        for(int i = 0;i < 20; ++i) {
            playerList.add(new PlayerInfo(i, randomNames[r.nextInt(randomNames.length)],r.nextInt(99) + 1));
        }
        */

        lastPlayedCard = Card.CARD_BACK;
        //Debug//

        this.observers.add(observer);
    }

    /**
     * Find the index of the given player
     * @param id the id of the player
     * @return the index of the player; -1 if the ID is not present
     */
    private int findIndexOfPlayer(int id){
        for(int i = 0; i < playerList.size(); i++)
        {
            if(playerList.get(i).getPlayerID() == id)
            {
                return i;
            }
        }

        return -1;
    }

    public void setCurrentPlayerID(int playerID)
    {
        this.currentPlayerID = playerID;
        notifyObservers();
    }

    public int getCurrentPlayerID()
    {
        return this.currentPlayerID;
    }

    /**
     * Returns a list with all the players the client knows of.
     * <br><br>
     * @return list with players and null if there are none
     */
    public List<PlayerInfo> fetchAllPlayers() {
        return playerList;
    }
	
    public List<Card> fetchAllCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
        notifyObservers();
    }

    public void removeCard(Card card){
        this.cards.remove(card);
        notifyObservers();
    }

    public void addCard(Card card){
        this.cards.add(card);
        notifyObservers();
    }

    public void setLastPlayedCard(Card lastPlayedCard){
        this.lastPlayedCard = lastPlayedCard;
        notifyObservers();
    }
  
    public void setPlayers(List<PlayerInfo> players) {
        this.playerList = players;
        notifyObservers();
    }

    /**
     * Used to add players to the current Muno-Session
     * <br><br>
     * @param playerInfo the new player in the session
     */
    public void addPlayer(PlayerInfo playerInfo) {
        if(!playerList.contains(playerInfo)) {
            playerList.add(playerInfo);
        }
        notifyObservers();
    }

    /**
     * Used to remove players from the current Muno-Session
     * <br><br>
     * @param playerInfo the player that should be removed from the session
     */
    public void removePlayer(PlayerInfo playerInfo) {
        System.err.printf("removePlayer is not implemented yet");
    }

    /**
     * @return the last played card
     */
    public Card fetchLastPlayedCard() {
        return lastPlayedCard;
    }

    /**
     * Updates the Information on a given Player.
     */
    public void playerUpdate(PlayerInfo playerInfo) {
        int index = findIndexOfPlayer(playerInfo.getPlayerID());
        playerList.set(index, playerInfo);

        notifyObservers();
    }

    public void addObserver(I_Notifiable other) {
        this.observers.add(other);
    }

    public void notifyObservers() {
        for(I_Notifiable u : observers) {
            u.notifyElement();
        }
    }
}
