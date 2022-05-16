package at.htlkaindorf.ahif18.network;

import at.htlkaindorf.ahif18.data.Card;
import at.htlkaindorf.ahif18.data.PlayerInfo;
import lombok.Data;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Provides Static methods to create and receive different Message Types
 *
 * Last Changed: 2022-05-16
 * @author Andreas Kurz
 */
public class RequestParser {

    public enum RequestType {
        INIT,

        CARD_PLAYED
    }

    public static RequestType getRequestTpe(InputStream stream) throws IOException
    {
        String headerString = ByteDealer.receiveString(stream);
        return RequestType.valueOf(headerString);
    }

    public static void sendInit(OutputStream stream, Card[] cards) throws IOException
    {
        String[] cardNames = Arrays.stream(cards).map(Card::name).toArray(String[]::new);
        ByteDealer.sendStringArray(stream, cardNames);
    }

    public static Card[] receiveInit(InputStream stream) throws IOException
    {
        return Arrays.stream(ByteDealer.receiveStringArray(stream)).map(Card::valueOf).toArray(Card[]::new);
    }
}
