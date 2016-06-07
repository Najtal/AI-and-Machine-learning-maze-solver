package gui.data;

import app.AppContext;
import model.MlModel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by jvdur on 09/05/2016.
 */
public class DataFrame extends JFrame {

    // CONTAINERS
    private DataCanvas canvas;


    /**
     * Main constructer
     */
    public DataFrame(MlModel mlm) {
        initFrame();
        initCanvas(mlm);
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

        this.setLocationRelativeTo(null);
    }

    private void initCanvas(MlModel mlm) {
        canvas = new DataCanvas(mlm, this);
        JScrollPane jsp = new JScrollPane(canvas, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(jsp, BorderLayout.CENTER);
    }

}
