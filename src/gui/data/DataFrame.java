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


    /**
     * Main constructer
     */
    public DataFrame(MlModel mlm) {
        initFrame();

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Steps", stepChart(mlm));
        tabbedPane.addTab("Grab key", grabKey(mlm));
        tabbedPane.addTab("Open door", door(mlm));
        tabbedPane.addTab("Discovery path", discoveryPathChart(mlm));
        tabbedPane.addTab("Reach Goal", goal(mlm));
        tabbedPane.addTab("All together", all(mlm));

        this.add(tabbedPane);

        this.setVisible(true);
    }


    private JPanel discoveryPathChart(MlModel mlm) {
        XYSeries seriesGK = new XYSeries("Grab Key");
        for (int i=0; i<mlm.getNbRuns(); i++) {
            seriesGK.add(i, mlm.getRunGoals(i).getLoadGrabKey());
        }

        XYSeries seriesLA = new XYSeries("Action");
        for (int i=0; i<mlm.getNbRuns(); i++) {
            seriesLA.add(i, mlm.getRunGoals(i).getLoadAction());
        }

        XYSeries seriesLD = new XYSeries("discovery path");
        for (int i=0; i<mlm.getNbRuns(); i++) {
            seriesLD.add(i, mlm.getRunGoals(i).getLoadDiscoverPath());
        }

        // Add the series to your data set
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(seriesGK);
        dataset.addSeries(seriesLA);
        dataset.addSeries(seriesLD);

        // Generate the graph
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Discovery path", // Title
                "Maze solver #", // x-axis Label
                "# steps", // y-axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );

        return new ChartPanel(chart);
    }

    private JPanel grabKey(MlModel mlm) {
        XYSeries seriesGK = new XYSeries("Grab Key");
        for (int i=0; i<mlm.getNbRuns(); i++) {
            seriesGK.add(i, mlm.getRunGoals(i).getLoadGrabKey());
        }

        // Add the series to your data set
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(seriesGK);

        // Generate the graph
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Grab key", // Title
                "Maze solver #", // x-axis Label
                "Key goal", // y-axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );

        return new ChartPanel(chart);
    }

    private JPanel all(MlModel mlm) {
        XYSeries seriesGK = new XYSeries("Grab Key");
        for (int i=0; i<mlm.getNbRuns(); i++) {
            seriesGK.add(i, mlm.getRunGoals(i).getLoadGrabKey());
        }

        XYSeries seriesLA = new XYSeries("Action");
        for (int i=0; i<mlm.getNbRuns(); i++) {
            seriesLA.add(i, mlm.getRunGoals(i).getLoadAction());
        }

        XYSeries seriesGoal = new XYSeries("Goal");
        for (int i=0; i<mlm.getNbRuns(); i++) {
            seriesGoal.add(i, mlm.getRunGoals(i).getLoadReachGoal());
        }

        XYSeries seriesDoor = new XYSeries("Door");
        for (int i=0; i<mlm.getNbRuns(); i++) {
            seriesDoor.add(i, mlm.getRunGoals(i).getLoadOpenDoor());
        }

        XYSeries seriesLD = new XYSeries("discovery path");
        for (int i=0; i<mlm.getNbRuns(); i++) {
            seriesLD.add(i, mlm.getRunGoals(i).getLoadDiscoverPath());
        }

        // Add the series to your data set
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(seriesGK);
        dataset.addSeries(seriesLA);
        dataset.addSeries(seriesLD);
        dataset.addSeries(seriesDoor);
        dataset.addSeries(seriesGoal);

        // Generate the graph
        JFreeChart chart = ChartFactory.createXYLineChart(
                "All datas", // Title
                "Maze solver #", // x-axis Label
                "*", // y-axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );

        return new ChartPanel(chart);
    }



    private JPanel goal(MlModel mlm) {

        XYSeries seriesGoal = new XYSeries("Goal");
        for (int i=0; i<mlm.getNbRuns(); i++) {
            seriesGoal.add(i, mlm.getRunGoals(i).getLoadReachGoal());
        }
        // Add the series to your data set
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(seriesGoal);

        // Generate the graph
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Goal load", // Title
                "Maze solver #", // x-axis Label
                "Goal load", // y-axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );

        return new ChartPanel(chart);
    }


    private JPanel door(MlModel mlm) {

        XYSeries seriesDoor = new XYSeries("Door");
        for (int i=0; i<mlm.getNbRuns(); i++) {
            seriesDoor.add(i, mlm.getRunGoals(i).getLoadOpenDoor());
        }


        // Add the series to your data set
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(seriesDoor);

        // Generate the graph
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Door goal", // Title
                "Maze solver #", // x-axis Label
                "Door goalsF", // y-axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );

        return new ChartPanel(chart);
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

    private JPanel stepChart(MlModel mlm) {

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

        return new ChartPanel(chart);
    }

}
