package at.htlkaindorf.ahif18.network;

import at.htlkaindorf.ahif18.data.Card;
import at.htlkaindorf.ahif18.data.PlayerInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//Class that buffers the information that was received from the network
//doesnt call any network functions (this class just buffers the thinks
//received from the network)
public class NetworkBuffer {
    public List<PlayerInfo> fetchAllPlayers() {
        ArrayList<PlayerInfo> pls = new ArrayList<>();
        String randomNames[] = new String[] {
                "john", "me", "01234567", "pizza","diablo", "pasta42", "h4ck3r"
        };
        Random r = new Random(42);

        for(int i = 0;i < 20; ++i) {
            pls.add(new PlayerInfo(i, randomNames[r.nextInt(randomNames.length)],r.nextInt(99) + 1));
        }

        return pls;
    }

    public Card getLastPlayedCard() {
        return Card.R0;
    }
}
