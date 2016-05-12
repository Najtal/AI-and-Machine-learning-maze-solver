package gui.swing;

import app.AppContext;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by jvdur on 09/05/2016.
 */
public class Frame extends JFrame implements ChangeListener {


    // CONTAINERS
    // Main container
    private JPanel mainContainer;

    // Side option pane
    private JPanel sideOptionPane;
    private JPanel menuPane;


    /**
     * Main constructer
     */
    public Frame() {

        initFrame();

        initComponentStructure();

        // TODO
        // initJMenuBar();

        // TODO
        initSideOptionPane();

        // TODO
        // initVisualisationPane();

        // TODO
        // initCliOutputPane();

        this.setVisible(true);
    }


    /**
     * Init all settings for main frame
     */
    private void initFrame() {
        this.setTitle(AppContext.INSTANCE.getProperty("guiTitle"));
        this.setSize(
                Integer.parseInt(AppContext.INSTANCE.getProperty("guiSizeWidth")),
                Integer.parseInt(AppContext.INSTANCE.getProperty("guiSizeHeight")));
        this.setMinimumSize(new Dimension(
                Integer.parseInt(AppContext.INSTANCE.getProperty("guiSizeMinimumWidth")),
                Integer.parseInt(AppContext.INSTANCE.getProperty("guiSizeMinimumHeight"))));

        /* TODO add icon image
        ImageIcon icon = new ImageIcon(getClass().getResource("/icon.png"));
        this.setIconImage(icon.getImage());
        */
        this.setLocationRelativeTo(null);

        // Close operations
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                AppContext.INSTANCE.closeApplication();
            }
        });
    }


    private void initComponentStructure() {

        /*
            RootPane
                > MainContainer : center
                    > OptionPane : West
                    > VisualisationPane : Center
                    > CliPane : South
        */

        // Main container
        mainContainer = new JPanel(new BorderLayout());
        this.add(mainContainer);
    }


    private void initSideOptionPane() {

        sideOptionPane = new JPanel(new BorderLayout());
        mainContainer.add(sideOptionPane, BorderLayout.WEST);

        menuPane = new JPanel(new GridLayout(4, 1));

        JButton jbOpAddAnchor = new JButton("Add anchor");
        JButton jbOpSetGateway = new JButton("Set gateway");
        JButton jbOpConfig = new JButton("Configure");
        JButton jbOpStartStop = new JButton("Start");

        menuPane.add(jbOpAddAnchor);
        menuPane.add(jbOpSetGateway);
        menuPane.add(jbOpConfig);
        menuPane.add(jbOpStartStop);

        sideOptionPane.add(menuPane, BorderLayout.NORTH);

    }


    @Override
    public void stateChanged(ChangeEvent e) {
        repaint();
    }

}
