import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.application.Platform;


/**
 * LivePlot class is used to create the plot
 */
public class LivePlot {
    //series is used as we are creating a "line" of data and adding points to it as we go
    private XYChart.Series<Number,Number> my_series;

    /**
     * method buildChart create the chart with defined data, labeled axes, title, etc
     * @return sends my_lineChart back to MainPlot class
     */
    public LineChart<Number, Number> buildChart(){
        //creates axes
        var xAxis = new NumberAxis("Time (s)", 0.0,100, 10);
        var yAxis = new NumberAxis("Temperature (°C)", 0.0,50, 10);

        //LineChart is used so that there are lines between data points
        LineChart <Number, Number> my_lineChart  = new LineChart<>(xAxis,yAxis);

        my_lineChart.setTitle("Live Temperature vs. Time Plot");
        my_lineChart.setLegendVisible(false);
        my_lineChart.setAnimated(false);

        //initializes the date series
        my_series = new XYChart.Series<>();

        //tells the lineChart object what data to display
        my_lineChart.getData().add(my_series);

        return my_lineChart;
    }

    /**
     * method updateSeries is called in Serial.java and is used to add a new point to plot
     * @param time on the x-axis
     * @param temperature on the y-axis
     */
    public void updateSeries(double time, double temperature){
        //Platform.runLater schedules code to be run on the JavaFX thread
        Platform.runLater(() -> { my_series.getData().add(new XYChart.Data<>(time, temperature));
    });

    }

}
