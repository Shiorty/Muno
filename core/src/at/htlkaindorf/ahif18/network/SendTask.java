package at.htlkaindorf.ahif18.network;

import java.io.IOException;

/**
 * Represents a single task that needs to be fulfilled by a send-thread
 *
 * <br><br>
 * Last changed: 2022-06-03
 * @author Andreas Kurz
 */
public interface SendTask {
    /**
     * task that needs to be fulfilled
     * @throws IOException when a network error occurs
     */
    void execute() throws IOException;
}
