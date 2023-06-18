package View;

import Events.DialogEvent;
import Events.NicknameProvidedEvent;
import Interfaces.DialogEventListener;
import Interfaces.GameResetListener;
import Interfaces.NicknameProvidedEventListener;
import Model.Player;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class GameOverView extends JPanel implements DialogEventListener {
    private JButton resetButton;
    private JTable highScoresTable;

    private ArrayList<NicknameProvidedEventListener> nicknameProvidedEventListeners;

    private ArrayList<GameResetListener> gameResetListeners;

    public GameOverView() {
        this.nicknameProvidedEventListeners = new ArrayList<>();
        this.gameResetListeners = new ArrayList<>();
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        highScoresTable = new JTable();
        resetButton = new JButton("Reset Game");
        resetButton.addActionListener(e -> {
            fireGameResetEvent();
            System.out.println("Game reset");
        });
        this.setBackground(Color.BLACK);
        add(resetButton, BorderLayout.SOUTH);
    }

    public void addNicknameProvidedEventListener(NicknameProvidedEventListener listener){
        nicknameProvidedEventListeners.add(listener);
    }

    public void addGameResetListener(GameResetListener listener) {
        this.gameResetListeners.add(listener);
    }

    public void fireNicknameEvent(String nickname){
        NicknameProvidedEvent event = new NicknameProvidedEvent(this, nickname);
        for(NicknameProvidedEventListener listener : nicknameProvidedEventListeners){
            listener.onNicknameProvided(event);
        }
    }

    private void fireGameResetEvent() {
        for (GameResetListener listener : gameResetListeners) {
            listener.onReset();
        }
    }

    public void setList(ArrayList<Player> scores){
        String[] columnNames = {"Player", "Score"};
        Object[][] data = new Object[scores.size()][2];
        for (int i = 0; i < scores.size(); i++) {
            data[i][0] = scores.get(i).getName();
            data[i][1] = scores.get(i).getScore();
        }
        highScoresTable.setModel(new DefaultTableModel(data, columnNames){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        highScoresTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(Color.GREEN);
                setFocusable(false);
                return this;
            }
        });

        this.removeAll();

        JLabel gameOverLabel = new JLabel("Game Over", SwingConstants.CENTER);
        gameOverLabel.setForeground(Color.RED);
        gameOverLabel.setFont(new Font("TimesRoman", Font.BOLD, 24));
        gameOverLabel.setBackground(Color.BLACK);

        this.add(gameOverLabel, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(highScoresTable);
        scrollPane.setBackground(Color.BLACK);
        scrollPane.getViewport().setBackground(Color.BLACK);
        this.add(scrollPane, BorderLayout.CENTER);

        this.add(resetButton, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }



    @Override
    public void onDialog(DialogEvent dialogEvent) {
        String nickname = (String)JOptionPane.showInputDialog(
                null,
                "You've got a high score of " + dialogEvent.getScore() + "! Please enter your nickname:",
                "GREAT SCORE!",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                null);

        if (nickname != null && !nickname.trim().isEmpty()) {
            fireNicknameEvent(nickname);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        String gameOverText = "Game Over";
        g.setColor(Color.RED);
        g.setFont(new Font("TimesRoman", Font.BOLD, 24));
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        int x = (this.getWidth() - metrics.stringWidth(gameOverText)) / 2;
        int y = ((this.getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
        g.drawString(gameOverText, x, y);
    }
}
