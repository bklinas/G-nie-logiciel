package apprentissage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsMenu extends JFrame {
    private SoundManager soundManager;
    private JCheckBox musicCheckBox;
    private JCheckBox soundEffectsCheckBox;

    public SettingsMenu(SoundManager soundManager) {
        super("Game Settings");

        this.soundManager = soundManager;

        setLayout(new GridLayout(4, 2));

        // Music settings
        add(new JLabel("Background Music:"));
        musicCheckBox = new JCheckBox("Enable");
        musicCheckBox.setSelected(soundManager.isMusicEnabled());
        add(musicCheckBox);

        // Sound effects settings
        add(new JLabel("Sound Effects:"));
        soundEffectsCheckBox = new JCheckBox("Enable");
        soundEffectsCheckBox.setSelected(soundManager.isSoundEffectsEnabled());
        add(soundEffectsCheckBox);

        // Apply button
        JButton applyButton = new JButton("Apply Settings");
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applySettings();
                dispose(); // Fermer la fenêtre après application des réglages
            }
        });
        add(applyButton);

        setSize(300, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Permet de fermer uniquement la fenêtre actuelle
        setVisible(true);
    }

    private void applySettings() {
        soundManager.setMusicEnabled(musicCheckBox.isSelected());
        soundManager.setSoundEffectsEnabled(soundEffectsCheckBox.isSelected());
        soundManager.updateAudioSettings();
        JOptionPane.showMessageDialog(this, "Settings Applied!");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Instantiate SoundManager
                SoundManager soundManager = new SoundManager();

                // Pass the SoundManager instance to SettingsMenu constructor
                new SettingsMenu(soundManager);
            }
        });
    }
}