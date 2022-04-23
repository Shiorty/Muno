package at.htlkaindorf.ahif18.data;

public class PlayerInfo {
    private int playerID;
    private String playerName;
    private int cardAmount;

    public PlayerInfo(int playerID, String name, int cardAmount) {
        this.playerID = playerID;
        this.playerName = name;
        this.cardAmount = cardAmount;
    }

    public int getPlayerID() {
        return playerID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getCardAmount() {
        return cardAmount;
    }
}
