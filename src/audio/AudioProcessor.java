/** The main audio processing class, implemented as a Runnable so
 * as to be run in a separated execution Thread. */
package audio;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class AudioProcessor implements Runnable {
    public AudioSignal inputSignal, outputSignal;
    private TargetDataLine audioInput;
    private SourceDataLine audioOutput;
    private boolean isThreadRunning; // makes it possible to "terminate" thread

    /** Creates an AudioProcessor that takes input from the given TargetDataLine, and plays back
     * to the given SourceDataLine.
     * @param frameSize the size of the audio buffer. The shorter, the lower the latency. */
    public AudioProcessor(TargetDataLine audioInput, SourceDataLine audioOutput, int frameSize) {
        this.audioInput = audioInput;
        this.audioOutput = audioOutput;
        this.inputSignal = new AudioSignal(frameSize);
        this.outputSignal = new AudioSignal(frameSize);
    }

    /** Audio processing thread code. Basically an infinite loop that continuously fills the sample
     * buffer with audio data fed by a TargetDataLine and then applies some audio effect, if any,
     * and finally copies data back to a SourceDataLine.*/
    @Override
    public void run() {
        isThreadRunning = true;
        while (isThreadRunning) {
            inputSignal.recordFrom(audioInput);
            //mettre effet sur signal
            outputSignal.setFrom(inputSignal);
            //outputSignal.playTo(audioOutput);
        }
    }

    /** Tells the thread loop to break as soon as possible. This is an asynchronous process. */
    public void terminateAudioThread() {
        isThreadRunning = false;
    }

    public AudioSignal getInputSignal() {
        return inputSignal;
    }

    public TargetDataLine getAudioInput() {
        return audioInput;
    }

    public AudioSignal getOutputSignal() {
        return outputSignal;
    }

    public SourceDataLine getAudioOutput() {
        return audioOutput;
    }

    public boolean isThreadRunning() {
        return isThreadRunning;
    }

    public void setThreadRunning(boolean threadRunning) {
        isThreadRunning = threadRunning;
    }

    /* an example of a possible test code */
    public static void main(String[] args) throws LineUnavailableException {
        TargetDataLine inLine = AudioIO.obtainAudioInput("External Mic (Realtek(R) Audio)", 16000);
        SourceDataLine outLine = AudioIO.obtainAudioOutput("Casque (Realtek(R) Audio)", 16000);
        inLine.open(); inLine.start(); outLine.open(); outLine.start();
        AudioProcessor as = new AudioProcessor(inLine,outLine,1024);
        Thread thread = new Thread(as);
        thread.start();
        System.out.println("A new thread has been created!");
    }
}
