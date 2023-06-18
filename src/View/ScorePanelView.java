package View;

import Events.DialogEvent;
import Events.EatEvent;
import Interfaces.DialogEventListener;
import Interfaces.EatEventListener;

import javax.swing.*;
import java.awt.*;

public class ScorePanelView extends JPanel implements EatEventListener {
    private int score;

    public ScorePanelView() {
        this.score = 0;
        this.setPreferredSize(new Dimension(0, 30));
        this.setBackground(Color.BLACK);
    }

    @Override
    public void onEat(EatEvent event) {
        score = event.getScore();
        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        String scoreText = "Score: " + score;
        g.setColor(Color.CYAN);
        g.setFont(new Font("TimesRoman", Font.BOLD, 24));
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        int x = (this.getWidth() - metrics.stringWidth(scoreText)) / 2;
        int y = ((this.getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
        g.drawString(scoreText, x, y);
    }
}