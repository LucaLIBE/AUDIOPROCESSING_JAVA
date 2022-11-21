package ui;

import audio.AudioSignal;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class VuMeter extends Canvas {
    public VuMeter(double v, double v1) {
        super(v, v1);
    }

    public void updateLevel(AudioSignal signal){
        GraphicsContext gc = this.getGraphicsContext2D();
        if(signal.getdBlevel() == signal.getdBlevel()) {
            gc.setFill(Color.RED);
            gc.clearRect(10, 0, 150, 800);
            gc.fillRect(10, -signal.getdBlevel()*6.5+50, 100, signal.getdBlevel()*6.5+700);
            gc.strokeText(signal.getdBlevel()+" dB",10,20);
        }
    }
}
