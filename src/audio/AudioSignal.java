package audio;

import javax.sound.sampled.*;
import audio.AudioIO;
import java.nio.ByteBuffer;

import static java.lang.Thread.sleep;

/** A container for an audio signal backed by a double buffer so as to allow floating point calculation
 * for signal processing and avoid saturation effects. Samples are 16 bit wide in this implementation. */
public class AudioSignal {

    private double[] sampleBuffer; // floating point representation of audio samples
    private double dBlevel; // current signal level

    /**
     * Construct an AudioSignal that may contain up to "frameSize" samples.
     *
     * @param frameSize the number of samples in one audio frame
     */
    public AudioSignal(int frameSize) {
        this.sampleBuffer = new double[frameSize];
        this.dBlevel = 0;
    }

    /**
     * Sets the content of this signal from another signal.
     *
     * @param other other.length must not be lower than the length of this signal.
     */
    public void setFrom(AudioSignal other) {
        if (other.getFrameSize() >= sampleBuffer.length) {
            for (int i = 0; i < other.getFrameSize(); i++) {
                sampleBuffer[i] = other.getSample(i);
            }
            this.dBlevel=other.getdBlevel();
        }
        else{
            System.out.println("buffer trop court");
        }
    }

    /**
     * Fills the buffer content from the given input. Bytes are converted on the fly to double's.
     *
     * @return false if at end of stream
     */
    public boolean recordFrom(TargetDataLine audioInput) {
        byte[] byteBuffer = new byte[sampleBuffer.length * 2]; // 16 bit samples
        if (audioInput.read(byteBuffer, 0, byteBuffer.length) == -1) return false;
        for (int i = 0; i < sampleBuffer.length; i++) {
            sampleBuffer[i] = ((byteBuffer[2 * i] << 8) + byteBuffer[2 * i + 1]) / 32768.0; // big endian
            dBlevel = 10 * Math.log(sampleBuffer[i]);
        }
        return true;
    }

    /**
     * Plays the buffer content to the given output.
     *
     * @return false if at end of stream
     */
    public boolean playTo(SourceDataLine audioOutput) {
        byte[] byteBuffer = new byte[sampleBuffer.length * 2];

        for (int i = 0; i < sampleBuffer.length / 2; i++) {
            byte[] entier = ByteBuffer.allocate(4).putInt((int) (sampleBuffer[i] * 32768)).array();//on transforme le double en int entre -32767 et 32678
            byteBuffer[2 * i] = entier[2];   //un int codé sur 4 bit -> représente ici dans un tableau de 4 bit
            byteBuffer[2 * i + 1] = entier[3]; //deux derniers bits du tableau pour le buffer.
        }
        if (audioOutput.write(byteBuffer, 0, byteBuffer.length) == 1)
            return (false);
        return (true);
    }

    public double getSample(int i) {
        return sampleBuffer[i];
    }

    public void setSample(int i, double value) {
        sampleBuffer[i] = value;
    }

    public double getdBlevel() {
        return dBlevel;
    }

    public void setdBlevel(double dBlevel) {
        this.dBlevel = dBlevel;
    }

    public double[] getSampleBuffer() {
        return sampleBuffer;
    }

    public int getFrameSize() {return sampleBuffer.length;}


}


