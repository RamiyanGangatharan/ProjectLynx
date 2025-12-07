package projectLynx.client.view;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

public class SoundPlayer {

    private Clip clip;
    private FloatControl volumeControl;

    public void loadAndPlay(String soundFileName) {
        try {
            File file = new File(soundFileName);
            AudioInputStream sound = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(sound);
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            }
            clip.start(); // Play the sound
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    public void setVolume(float level) {
        if (volumeControl != null) {
            float min = volumeControl.getMinimum(); // minimum dB
            float max = volumeControl.getMaximum(); // maximum dB
            float dB = min + (max - min) * level;   // linear interpolation
            volumeControl.setValue(dB);
        }
    }

    public void stopSound() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }
}