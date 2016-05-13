package gui.swing;

import app.AppContext;
import ucc.MazeDTO;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by jvdur on 09/05/2016.
 */
public class MazeFrame extends JFrame implements ChangeListener {

    // Global frame variables
    private final String name;

    // CONTAINERS
    private JPanel canvas;


    /**
     * Main constructer
     */
    public MazeFrame(MazeDTO maze, String name) {
        this.name = name;

        initFrame();

        initCanvas(maze);

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

    private void initCanvas(MazeDTO maze) {
        canvas = new MazeCanvas(maze, this);
        this.add(canvas, BorderLayout.CENTER);
    }





    @Override
    public void stateChanged(ChangeEvent e) {
        repaint();
    }

}
