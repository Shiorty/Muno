package at.htlkaindorf.ahif18.bl;

/**
 * The observer Interface that needs to be implemented to be notified by the NetworkBuffer.<br>
 * Used by the GUI classes.
 * <br><br>
 * Last changed: 2022-05-24
 * @author Jan Mandl
 */
public interface I_Notifiable {
    public void notifyElement();
}