package audio;

import javax.sound.sampled.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** A collection of static utilities related to the audio system. */
 public class AudioIO {

    /**
     * Displays every audio mixer available on the current system.
     */
    public static String[] printAudioMixers() {
//        Arrays.stream(AudioSystem.getMixerInfo())
//                .forEach(e -> System.out.println("- name=\"" + e.getName()
//                        + "\" description=\"" + e.getDescription() + " by " + e.getVendor() + "\""));
        String[] liste = new String[0];
        List<String> arrliste = new ArrayList(Arrays.asList(liste));
        Arrays.stream(AudioSystem.getMixerInfo()).forEach(e->arrliste.add(e.getName()));
        liste = arrliste.toArray(liste);
        return liste;
    }

    /**
     * @return a Mixer.Info whose name matches the given string.
     * Example of use: getMixerInfo("Macbook default output")
     */
    public static Mixer.Info getMixerInfo(String mixerName) {
        // see how the use of streams is much more compact than for() loops!
        return Arrays.stream(AudioSystem.getMixerInfo()).filter(e -> e.getName().equalsIgnoreCase(mixerName)).findFirst().get();
    }

    /** Return a line that's appropriate for recording sound from a microphone.
     * Example of use:
     * TargetDataLine line = obtainInputLine("USB Audio Device", 8000);
     * @param mixerName a string that matches one of the available mixers.
    //* @see AudioSystem.getMixerInfo() which provides a list of all mixers on your system.
     */
    public static TargetDataLine obtainAudioInput(String mixerName, int sampleRate) throws LineUnavailableException {
        AudioFormat audioformat = new AudioFormat(sampleRate, 16, 1, true, true);
        if (mixerName == "Default Audio Device") {
            return AudioSystem.getTargetDataLine(audioformat);
        } else {
            Mixer.Info info = getMixerInfo(mixerName);
            return AudioSystem.getTargetDataLine(audioformat, info);
        }
    }
    /**
     * Return a line that's appropriate for playing sound to a loudspeaker.
     */
    public static SourceDataLine obtainAudioOutput(String mixerName, int sampleRate) throws LineUnavailableException {
        Mixer.Info mixerinfo = getMixerInfo(mixerName);
        AudioFormat audioformat = new AudioFormat(sampleRate, 16, 1, true, true);
        return AudioSystem.getSourceDataLine(audioformat, mixerinfo);
    }

    public static void main(String[] args) throws LineUnavailableException {
        TargetDataLine inLine = AudioIO.obtainAudioInput("External Mic (Realtek(R) Audio)", 16000);
        SourceDataLine outLine = AudioIO.obtainAudioOutput("Casque (Realtek(R) Audio)", 16000);

    }
}