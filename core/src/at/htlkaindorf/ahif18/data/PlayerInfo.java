package at.htlkaindorf.ahif18.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * Saves the information about the players that are<br>
 * needed for the client to display the list of players in<br>
 * the game screen.
 *
 * Last changed: 2022-06-13
 * @author Jan Mandl
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerInfo {
    private int playerID;
    private String playerName;
    private int cardAmount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerInfo that = (PlayerInfo) o;
        return playerID == that.playerID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerID);
    }
}
