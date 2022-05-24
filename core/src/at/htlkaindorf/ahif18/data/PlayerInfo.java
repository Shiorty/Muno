package at.htlkaindorf.ahif18.data;

import at.htlkaindorf.ahif18.network.ByteConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * Saves the informations about the players that are<br>
 * needed for the client to display the list of players in<br>
 * the game screen.
 */
public class PlayerInfo {
    private int playerID;
    private String playerName;
    private int cardAmount;
}
