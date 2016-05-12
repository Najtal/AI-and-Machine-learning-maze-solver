/**
 * Main class
 *
 * @author Jean-Vital Durieu
 * @version 0.01
 * Created by jvdur on 09/05/2016.
 */

import app.AppContext;
import app.Pgm;

import javax.swing.*;
import java.util.logging.Level;

public final class Main {

    /**
     * private and empty constructor
     */
    private Main() {
    }

    /**
     * main method
     * @param args boot arguments
     */
    public static void main(final String[] args) {

        try {
            AppContext.INSTANCE.loadAndConfig(AppContext.PRODUCTION);
            new Pgm();
        } catch (Exception e) {
            String errMsg = "Error while loading the app";

            AppContext.INSTANCE.getLogger().log(Level.SEVERE,errMsg + ": "+ e.toString());
            JOptionPane.showMessageDialog(null, errMsg+"\n", "Fatal Error !", JOptionPane.ERROR_MESSAGE);
        }
    }

}
