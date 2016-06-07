package gui.data;

import app.AppContext;
import model.MlModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

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

        XYSeries series = new XYSeries("XYGraph");
        for (int i=0; i<mlm.getNbRuns(); i++) {
            series.add(i, mlm.getRunSteps(i));
        }


        // Add the series to your data set
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        // Generate the graph
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Steps evolution", // Title
                "Maze solver #", // x-axis Label
                "# steps", // y-axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );

        ChartPanel cp = new ChartPanel(chart);

        this.add(cp, BorderLayout.CENTER);
        /*canvas = new DataCanvas(mlm, this);
        JScrollPane jsp = new JScrollPane(canvas, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(jsp, BorderLayout.CENTER);*/
    }

}
