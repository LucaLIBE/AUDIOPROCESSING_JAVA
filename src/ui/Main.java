package ui;

import math.Complex;
import math.FFT;
import audio.AudioIO;
import audio.AudioProcessor;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import math.Complex;
import sun.misc.Signal;

import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class Main extends Application {
        public boolean isRunning=false;
    public void start(Stage primaryStage) throws Exception {

        BorderPane root = new BorderPane();
        root.setTop(createToolbar());
        /** récupérer l'audio et le renvoyer */
        TargetDataLine inLine = AudioIO.obtainAudioInput("Pilote de capture audio principal", 16000);
        SourceDataLine outLine = AudioIO.obtainAudioOutput("Périphérique audio principal", 16000);
        AudioProcessor as = new AudioProcessor(inLine, outLine, 1024);
        inLine.open();
        inLine.start();
        outLine.open();
        outLine.start();
        new Thread(as).start();

        /** crée les différents trucs à afficher */


        final NumberAxis xAxis = new NumberAxis("n",0,as.getInputSignal().getFrameSize(),256);
        final NumberAxis yAxis = new NumberAxis("x[n]",-1,1,0.1);
        final NumberAxis xAxisfft = new NumberAxis("Fréquence",0,4000,800);
        final NumberAxis yAxisfft = new NumberAxis("fft",0,-40,5);
        SignalView view = new SignalView(xAxis, yAxis);
        SignalView view2 = new SignalView(xAxisfft, yAxisfft);

        VuMeter canvas = new VuMeter(200, 800);

        root.setLeft(canvas);
        root.setCenter(view);
        root.setRight(view2);

        Scene scene = new Scene(root, 1500, 800);

        new AnimationTimer() {
            @Override //handle method
            public void handle(long now) {
                if(isRunning==true) {
                    view.getData().clear();
                    view2.getData().clear();
                    view.updateData(as.getInputSignal());
                    view2.updateFFT(as.getInputSignal());
                    canvas.updateLevel(as.getInputSignal());
                }
            }
        }.start();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Java FX Audio Interface");
        primaryStage.show();

    }

    private Node createToolbar() {
        Button button = new Button("Start");
        Button button2 = new Button("Stop");
        ToolBar tb = new ToolBar(button,button2);
        button.setOnAction(event -> isRunning=true);
        button2.setOnAction(event -> isRunning=false);

        return tb;
    }
}

