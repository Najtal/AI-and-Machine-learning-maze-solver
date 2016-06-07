package gui.data;

import model.MlModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * Created by jvdur on 13/05/2016.
 */
public class DataCanvas extends JPanel implements ComponentListener {



    private static final int AXIS_BORDER = 30;

    private MlModel mlm;
    private JFrame frame;

    private double spaceBtwPositions;
    private double heightCoefficient;

    public DataCanvas(MlModel mlm, JFrame frame) {
        this.mlm = mlm;
        this.frame = frame;

        this.spaceBtwPositions = Math.max(1, (frame.getContentPane().getSize().getWidth()-AXIS_BORDER*4)/mlm.getNbRuns());
        this.heightCoefficient = Math.max(1, (frame.getContentPane().getSize().getWidth()-2*AXIS_BORDER) / mlm.getMaxNbSteps());
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawLine(AXIS_BORDER, AXIS_BORDER, AXIS_BORDER, this.getSize().height-AXIS_BORDER);
        g.drawLine(AXIS_BORDER, this.getSize().height-AXIS_BORDER, this.getSize().width-AXIS_BORDER, this.getSize().height-AXIS_BORDER);

        g.drawString(0+"", AXIS_BORDER-20, this.getSize().height-AXIS_BORDER);
        g.drawString(0+"", AXIS_BORDER+5, this.getSize().height-AXIS_BORDER+20);

        int nbSteps = 0;
        for (int i=0; i<mlm.getNbRuns(); i++) {
            if (i>1) {
                g.drawLine((int)(AXIS_BORDER * 2 + spaceBtwPositions * i), (int) (nbSteps * heightCoefficient),
                        (int) (AXIS_BORDER * 2 + spaceBtwPositions * i +1), (int)(mlm.getRunSteps(i) * heightCoefficient));
            }
            nbSteps = mlm.getRunSteps(i);
        }

    }

    @Override
    public Dimension getPreferredSize() {
        return frame.getContentPane().getSize();
    }

    @Override
    public void componentResized(ComponentEvent e) {
        this.spaceBtwPositions = Math.max(1, (frame.getContentPane().getSize().getWidth()-AXIS_BORDER*4)/mlm.getNbRuns());
        this.heightCoefficient = Math.max(1, (frame.getContentPane().getSize().getWidth()-2*AXIS_BORDER) / mlm.getMaxNbSteps());
        repaint();
        revalidate();
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}