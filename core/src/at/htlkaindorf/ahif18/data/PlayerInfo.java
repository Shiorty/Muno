package at.htlkaindorf.ahif18.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * Saves the information about the players that are<br>
 * needed for the client to display the list of players in<br>
 * the game screen.
 *
 * Last changed: 2022-06-13
 * @author Jan Mandl
 */
public class PlayerInfo {
    private int playerID;
    private String playerName;
    private int cardAmount;
}
