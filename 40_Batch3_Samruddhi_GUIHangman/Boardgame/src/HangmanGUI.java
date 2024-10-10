/**
 *  author: Samruddhi Kadam
 *  roll no: 2441
 *  Title: HANGMANGUI
 *  Start Date: 20 Augest 2024
 *  Modified Date: 31 October 2024
 *  Description: this is Hangman: The word guessing game uses Javafx for its UI.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.HashSet;
import java.util.Random;

public class HangmanGUI extends JFrame {
    private String[] guesses = {
            "student", "teacher", "giraffe", "kingdom", "organic", "resolve", "popular", "network", "freedom"
    };
    private String wordToGuess;
    private char[] currentGuess;
    private HashSet<Character> guessedLetters = new HashSet<>();
    private int triesLeft;
    private int score = 0;
    private int cumulativeScore = 0;
    private boolean wordIsGuessed = false;
    private boolean hintUsed = false;
    private int n = 0;

    private JLabel wordLabel = new JLabel();
    private JLabel triesLeftLabel = new JLabel();
    private JLabel scoreLabel = new JLabel();
    private JTextField inputField = new JTextField(5);
    private JButton guessButton = new JButton("Guess");
    private JButton newGameButton = new JButton("New Game");
    private JLabel hangmanImage = new JLabel();
    private JProgressBar triesProgressBar = new JProgressBar();

    public HangmanGUI() {
        setTitle("Hangman Game");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel for game info (word, tries left, score)
        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.setBackground(Color.LIGHT_GRAY);
        infoPanel.add(wordLabel);
        infoPanel.add(triesLeftLabel);
        infoPanel.add(scoreLabel);

        // Panel for input and buttons
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputField.setFont(new Font("Arial", Font.PLAIN, 24));
        guessButton.setFont(new Font("Arial", Font.BOLD, 18));
        newGameButton.setFont(new Font("Arial", Font.BOLD, 18));
        inputPanel.add(new JLabel("Enter a letter:"));
        inputPanel.add(inputField);
        inputPanel.add(guessButton);
        inputPanel.add(newGameButton);

        // Panel for hangman image and progress bar
        JPanel visualPanel = new JPanel(new BorderLayout());
        triesProgressBar.setMaximum(7); // Max wrong guesses = 7
        hangmanImage.setPreferredSize(new Dimension(300, 300));
        visualPanel.add(hangmanImage, BorderLayout.CENTER);
        visualPanel.add(triesProgressBar, BorderLayout.SOUTH);

        add(infoPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.SOUTH);
        add(visualPanel, BorderLayout.CENTER);

        guessButton.addActionListener(new GuessButtonListener());
        newGameButton.addActionListener(new NewGameButtonListener());

        startNewGame();
    }

    private void startNewGame() {
        Random random = new Random();
        int randomNumber = random.nextInt(guesses.length);
        wordToGuess = guesses[randomNumber];
        triesLeft = wordToGuess.length();
        currentGuess = new char[wordToGuess.length()];
        guessedLetters.clear();
        score = 0;
        wordIsGuessed = false;
        hintUsed = false;
        n = 0;
        for (int i = 0; i < currentGuess.length; i++) {
            currentGuess[i] = '_';
        }
        updateDisplay();
    }

    private void updateDisplay() {
        wordLabel.setText("Word: " + new String(currentGuess));
        wordLabel.setFont(new Font("Serif", Font.BOLD, 30));
        wordLabel.setForeground(Color.BLUE);

        triesLeftLabel.setText("Tries Left: " + triesLeft);
        triesLeftLabel.setFont(new Font("Serif", Font.PLAIN, 20));

        scoreLabel.setText("Score: " + score);
        scoreLabel.setFont(new Font("Serif", Font.PLAIN, 20));

        triesProgressBar.setValue(triesLeft);

        hangmanImage.setIcon(getHangmanImage(n));
    }

    // Modify this method to load images from online URLs
    private ImageIcon getHangmanImage(int n) {
        try {
            URL url;
            switch (n) {
                case 1:
                    url = new URL("https://www.oligalma.com/downloads/images/hangman/hangman/4.jpg");
                    break;
                case 2:
                    url = new URL("https://www.oligalma.com/downloads/images/hangman/hangman/5.jpg");
                    break;
                case 3:
                    url = new URL("https://www.oligalma.com/downloads/images/hangman/hangman/6.jpg");
                    break;
                case 4:
                    url = new URL("https://www.oligalma.com/downloads/images/hangman/hangman/7.jpg");
                    break;
                case 5:
                    url = new URL("https://www.oligalma.com/downloads/images/hangman/hangman/8.jpg");
                    break;
                case 6:
                    url = new URL("https://www.oligalma.com/downloads/images/hangman/hangman/9.jpg");
                    break;
                case 7:
                    url = new URL("https://www.oligalma.com/downloads/images/hangman/hangman/10.jpg");
                    break;
                default:
                    url = new URL("https://www.oligalma.com/downloads/images/hangman/hangman/0.jpg");
                    break;
            }
            return new ImageIcon(url);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void makeGuess(char guess) {
        if (guessedLetters.contains(guess)) {
            JOptionPane.showMessageDialog(this, "You have already guessed that letter. Try again.");
            return;
        }

        guessedLetters.add(guess);

        if (wordToGuess.indexOf(guess) >= 0) {
            for (int i = 0; i < wordToGuess.length(); i++) {
                if (wordToGuess.charAt(i) == guess) {
                    currentGuess[i] = guess;
                }
            }
            score++;
            JOptionPane.showMessageDialog(this, "Good guess! Your current score is: " + score);
        } else {
            triesLeft--;
            n++;
            JOptionPane.showMessageDialog(this, "Wrong guess!");
        }

        updateDisplay();

        if (isWordGuessed()) {
            wordIsGuessed = true;
            cumulativeScore += score;
            JOptionPane.showMessageDialog(this, "Congratulations! You've guessed the word!\nYour final score for this round is: " + score);
            inputField.setEnabled(false);
            guessButton.setEnabled(false);
        }

        if (triesLeft <= 0 && !wordIsGuessed) {
            JOptionPane.showMessageDialog(this, "You ran out of guesses. The word was: " + wordToGuess);
            inputField.setEnabled(false);
            guessButton.setEnabled(false);
        }
    }

    private boolean isWordGuessed() {
        for (char c : currentGuess) {
            if (c == '_') {
                return false;
            }
        }
        return true;
    }

    private class GuessButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String input = inputField.getText().toLowerCase();
            inputField.setText("");

            if (input.length() == 1 && Character.isLetter(input.charAt(0))) {
                makeGuess(input.charAt(0));
            } else {
                JOptionPane.showMessageDialog(HangmanGUI.this, "Invalid input :( Please enter a single alphabetic character.");
            }
        }
    }

    private class NewGameButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            startNewGame();
            inputField.setEnabled(true);
            guessButton.setEnabled(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HangmanGUI gui = new HangmanGUI();
            gui.setVisible(true);
        });
    }
}