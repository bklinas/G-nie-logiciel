package apprentissage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuu extends JFrame {
    private JPanel mainPanel;
    private JPanel buttonPanel;
    private SoundManager soundManager;

    public MainMenuu() {
        super("Cat Adventures");
        soundManager = new SoundManager();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 500);  // Menu window size
        setLocationRelativeTo(null);


        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon("src/images/caty.jpg");
                java.awt.Image image = icon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        add(mainPanel);


        JLabel titleLabel = new JLabel("Cat Adventures");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        titleLabel.setForeground(Color.BLACK); 
        mainPanel.add(titleLabel);


        buttonPanel = new ButtonPanel();
        buttonPanel.setOpaque(false); 
        mainPanel.add(buttonPanel);


        createButton("Play");
        createButton("Instructions");
        createButton("Settings");
        createButton("Credits");
        createButton("Quit");


        mainPanel.add(Box.createVerticalStrut(10));
    }
    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton buttonClicked = (JButton) e.getSource();
            String buttonText = buttonClicked.getText();

            switch (buttonText) {
                case "Play":
                    dispose(); 
                    startGame();
                    break;
                case "Instructions":
                    showInstructions();
                    break;
                case "Settings":
                    showSettings();
                    break;
                case "Credits":
                    showCredits();
                    break;
                case "Quit":
                    quitGame();
                    break;
            }
        }

        private void showSettings() {
            new SettingsMenu(soundManager).setVisible(true);
        }
    }
    private void createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(255, 255, 255));
        button.setForeground(Color.BLACK); 
        button.setFocusPainted(false);
        button.addActionListener(new ButtonClickListener());
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(button);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); 
    }

    private class ButtonPanel extends JPanel {
        public ButtonPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
        }
    }

    
    private void startGame() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new MainGui("Cat Adventures", soundManager);
            }
        });
    }

    private void showInstructions() {

        JOptionPane.showMessageDialog(this, "Showing instructions...");
    }

    private void showSettings() {

        JOptionPane.showMessageDialog(this, "Showing settings...");
    }

    private void showCredits() {

        JOptionPane.showMessageDialog(this, "Showing credits...");
    }

    private void quitGame() {

        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Quit", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainMenuu().setVisible(true);
            }
        });
    }
}