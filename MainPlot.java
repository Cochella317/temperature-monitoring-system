import java.util.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;


/**
 *MainPlot class uses Java FX to create a new window with graphed data in real-time.
 */
public class MainPlot extends Application{

    //instance of the live plot
    public static LivePlot my_plotter;

    /**
     *start method is part of JavaFX's Application class. Acts as the entry-point for JavaFX application
     * @param stage creates window in which plot will be added.
     */
    @Override
    public void start (Stage stage){

        my_plotter = new LivePlot();

        //layout manager
        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: #b6ef9c");
        borderPane.setCenter(my_plotter.buildChart());

        //scene is what holds the UI components
        Scene my_scene = new Scene (borderPane,800, 800);
        stage.setTitle("Temp vs. Time Graph");

        //attaches UI layout (scene) to the empty window stage
        stage.setScene(my_scene);
        stage.show();

        //starts to get data
        SerialData.getSerialData();
    }

    //entry point which calls start method
    public static void main(String [] args){launch(args);}

}
