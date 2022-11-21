package ui;

import math.Complex;
import math.FFT;
import audio.AudioSignal;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class SignalView extends LineChart<Number,Number> {
    public SignalView(Axis<Number> axisX, Axis<Number> axisY) {
        super(axisX, axisY);
        super.setTitle("Graphe de l'audio");
        super.setAnimated(false);
    }

    public void updateData(AudioSignal signal){
        XYChart.Series series = new XYChart.Series();
        for (int i=0; i<signal.getFrameSize(); i++){
            if(signal.getSample(i)>0.01) {
                series.getData().add(new XYChart.Data(i, signal.getSample(i)));
            }
            else{
                series.getData().add(new XYChart.Data(i, 0));
            }
        }
        this.getData().add(series);
    }

    public void updateFFT(AudioSignal signal){
        XYChart.Series series = new XYChart.Series();
        Complex[] x = new Complex[signal.getFrameSize()];

        for (int i=0; i<signal.getFrameSize(); i++){
            x[i] = new Complex(signal.getSample(i), 0);
        }

        Complex[] y = FFT.fft(x);
        for (int i=0; i<signal.getFrameSize(); i++){
            series.getData().add(new XYChart.Data(i*4, y[i].re()));
        }
        this.getData().add(series);
    }
}
