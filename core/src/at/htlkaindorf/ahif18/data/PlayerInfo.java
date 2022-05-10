package at.htlkaindorf.ahif18.data;

import at.htlkaindorf.ahif18.network.ByteConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerInfo {
    private int playerID;
    private String playerName;
    private int cardAmount;
}
