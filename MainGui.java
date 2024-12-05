package apprentissage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


/**
 * 
 * 
 * @author karinemoussaoui9@gmail.com
 *
 */
public class MainGui extends JFrame implements Runnable {
    private boolean gameStarted = false;
    private boolean paused = false;
    private SoundManager soundManager;
    private JButton startButton;
    private JButton pauseButton;
    private JButton exitButton;
    

    private static final long serialVersionUID = 1L;

    private Map map;
    private final static Dimension preferredSize = new Dimension(GameConfiguration.WINDOW_WIDTH, GameConfiguration.WINDOW_HEIGHT);
    private MobileElementManager manager;
    private GameDisplay dashboard;

    public MainGui(String title,SoundManager soundManager) {
    	
        super(title);
        this.soundManager = soundManager;
        init();
        setFocusable(true);
    }

    private void init() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(118, 135, 155)); // Blue background

        map = GameBuilder.buildMap();
        manager = GameBuilder.buildInitMobile(map);
        dashboard = new GameDisplay(map, manager);
        dashboard.setPreferredSize(preferredSize);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        contentPane.add(dashboard, BorderLayout.CENTER);

        startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.BOLD, 14));
        startButton.setForeground(Color.BLACK); // Black text color
        startButton.setFocusPainted(false); 
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        pauseButton = new JButton("Resume");
        pauseButton.setFont(new Font("Arial", Font.BOLD, 14));
        pauseButton.setForeground(Color.BLACK); // Black text color
        pauseButton.setFocusPainted(false); 
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                togglePause();
            }
        });

        exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));
        exitButton.setForeground(Color.BLACK); // Black text color
        exitButton.setFocusPainted(false); 
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopGame();
            }
        });

        buttonPanel.add(startButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(exitButton);

        contentPane.add(buttonPanel, BorderLayout.NORTH);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        setPreferredSize(preferredSize);
        setResizable(false);
    }

    public void run() {
        while (true) {
            while (!gameStarted || paused) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }

            try {
                Thread.sleep(GameConfiguration.GAME_SPEED);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }

            manager.moveCatAutonomously();
            manager.nextRound();
            dashboard.repaint();

            if (manager.isAllFoodEaten()) {
                gameOver();
                break; 
            }
        }
    }
    private void startGame() {
        map = GameBuilder.buildMap();
        manager = GameBuilder.buildInitMobile(map);
        dashboard.setMap(map);
        dashboard.setManager(manager);
        dashboard.repaint();

        gameStarted = true;
        Thread gameThread = new Thread(this);
        gameThread.start();

        startButton.setEnabled(false);
        updatePauseButtonText();


        soundManager.playBackgroundMusic();
    }
    private void gameOver() {
        gameStarted = false;


        String message = ("<html> <font size='4'>Try Again?</font><br><br>" +
                "<img src='file:src/images/gameover.png' width='250' height='230'><br></center></html>");

        String[] options = {"Try Again", "Quit"};


        int option = JOptionPane.showOptionDialog(this, message, "Game Over",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                options, options[0]);

        // Process the user's choice
        if (option == JOptionPane.YES_OPTION) {
            startGame();
        } else {
            stopGame();
        }
    }

    private void togglePause() {
        paused = !paused;
        updatePauseButtonText();  
    }

    private void updatePauseButtonText() {
        if (paused) {
            pauseButton.setText("Resume");
        } else {
            pauseButton.setText("Pause");
        }
    }

    private void stopGame() {
        gameStarted = false;
        System.exit(0);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                SoundManager soundManager = new SoundManager();


                new MainGui("Game GUI", soundManager);
            }
        });
    }

   
  

}