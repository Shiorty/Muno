package at.htlkaindorf.ahif18.data;

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
                "john", "me", "xxx__I_am_the_best_Gamer__xxx", "pizza","diablo", "cabonara_pasta42", "h4ck3r"
        };
        Random r = new Random(42);

        for(int i = 0;i < 20; ++i) {
            pls.add(new PlayerInfo(i, randomNames[r.nextInt(randomNames.length)],23840));
        }

        return pls;
    }
}
