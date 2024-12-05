package apprentissage;
import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class SoundManager {
    private Clip backgroundMusic;
    private Clip soundEffect;
    
    private boolean isMusicEnabled;
    private int musicVolume;
    private boolean isSoundEffectsEnabled;
    private int soundEffectsVolume;

    public SoundManager() {
        // Load background music
        loadBackgroundMusic("src/sound/Funny-background-music.wav");

        // Load sound effect
        loadSoundEffect("src/sound/soundeffect.wav");

        // Set default values for sound settings
        isMusicEnabled = true;
        musicVolume = 50;
        isSoundEffectsEnabled = true;
        soundEffectsVolume = 50;
    }

    private void loadBackgroundMusic(String filePath) {
        try {
            File file = new File(filePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void loadSoundEffect(String filePath) {
        try {
            File file = new File(filePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            soundEffect = AudioSystem.getClip();
            soundEffect.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void playBackgroundMusic() {
        if (isMusicEnabled && backgroundMusic != null) {
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
    }

    public void playSoundEffect() {
        if (isSoundEffectsEnabled && soundEffect != null) {
            soundEffect.setFramePosition(0);
            soundEffect.start();
        }
    }

    // Add the following getter and setter methods
    public boolean isMusicEnabled() {
        return isMusicEnabled;
    }

    public void setMusicEnabled(boolean musicEnabled) {
        this.isMusicEnabled = musicEnabled;
    }

    public int getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(int musicVolume) {
        this.musicVolume = musicVolume;
    }

    public boolean isSoundEffectsEnabled() {
        return isSoundEffectsEnabled;
    }

    public void setSoundEffectsEnabled(boolean soundEffectsEnabled) {
        this.isSoundEffectsEnabled = soundEffectsEnabled;
    }

    public int getSoundEffectsVolume() {
        return soundEffectsVolume;
    }

    public void setSoundEffectsVolume(int soundEffectsVolume) {
        this.soundEffectsVolume = soundEffectsVolume;
    }
    public void updateAudioSettings() {
        // Adjust the volume of background music
        if (isMusicEnabled && backgroundMusic != null) {
            adjustVolume(backgroundMusic, musicVolume);
        }

        // Adjust the volume of sound effects
        if (isSoundEffectsEnabled && soundEffect != null) {
            adjustVolume(soundEffect, soundEffectsVolume);
        }
    }

    private void adjustVolume(Clip clip, int volume) {
        if (clip != null && clip.isOpen()) {
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float calculatedVolume = calculateVolumeLevel(volume);
            volumeControl.setValue(calculatedVolume);
        }
    }

    private float calculateVolumeLevel(int volume) {
        // Convert the volume range (0-100) to the expected range for FloatControl (-80.0 to 6.0206)
        return (volume / 100f) * 86.0206f - 80.0f;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SoundManager soundManager = new SoundManager();

            JFrame frame = new JFrame("Sound Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(300, 200);
            frame.setVisible(true);

            JButton playBackgroundButton = new JButton("Play Background Music");
            playBackgroundButton.addActionListener(e -> soundManager.playBackgroundMusic());
            frame.add(playBackgroundButton);

            JButton stopBackgroundButton = new JButton("Stop Background Music");
            stopBackgroundButton.addActionListener(e -> soundManager.stopBackgroundMusic());
            frame.add(stopBackgroundButton);

            JButton playSoundEffectButton = new JButton("Play Sound Effect");
            playSoundEffectButton.addActionListener(e -> soundManager.playSoundEffect());
            frame.add(playSoundEffectButton);
        });
    }
}