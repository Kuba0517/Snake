package View;

import Events.CrashEvent;
import Events.KeyboardEvent;
import Events.TickEvent;
import Interfaces.*;
import Model.Board;
import Model.Position;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class BoardView extends JFrame implements TickEventListener, CrashEventListener, GameResetListener {
    private JTable table;
    private JPanel score;
    private BoardProvider boardProvider;
    private GameOverView gameOverView;
    private ArrayList<KeyboardListener> keyboardListeners;
    private ArrayList<TickEventListener> tickEventListeners;

    public BoardView(ScorePanelView score, GameOverView gameOverView) {
        super("SNAKE");
        this.keyboardListeners = new ArrayList<>();
        this.tickEventListeners = new ArrayList<>();
        this.gameOverView = gameOverView;
        this.score = score;
        this.setBackground(Color.BLACK);
    }

    private void init(){
        Board board = boardProvider.getBoard();
        DefaultTableModel model = new DefaultTableModel(25, 16) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.table = new JTable(model);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setMaxWidth(20);
            table.getColumnModel().getColumn(i).setMinWidth(20);
            table.getColumnModel().getColumn(i).setPreferredWidth(20);
        }

        for (int i = 0; i < table.getColumnCount(); i++){
            for(int j = 0; j < table.getRowCount(); j++){
                table.setValueAt(board.getBoard()[j][i], j, i);
            }
        }

        Border border = BorderFactory.createLineBorder(Color.CYAN, 1);
        table.setBorder(border);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            private int cellValue;
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                cellValue = (int) value;
               if (cellValue == 0){
                    setBackground(Color.BLACK);
                }

                setForeground(getBackground());
                setText("");
                return this;
            }

            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(1, 1, getWidth()-2, getHeight()-2);

                if (cellValue == 3) {
                    g.setColor(Color.BLUE);
                    g.fillOval(1, 1, getWidth()-2, getHeight()-2);
                }
                else if (cellValue == 1) {
                    g.setColor(Color.YELLOW);
                    int xPoints[] = {0, getWidth()/2, getWidth(), getWidth()/2, 0};
                    int yPoints[] = {getHeight()/2, 0, getHeight()/2, getHeight(), getHeight()/2};
                    g.fillPolygon(xPoints, yPoints, 5);
                }
                else if (cellValue == 2){
                    g.setColor(Color.RED);
                    g.fillOval(1,1,getWidth()-2,getHeight()-2);
                }
            }
        });


        table.setShowGrid(false);
        table.setRowSelectionAllowed(false);
        table.setColumnSelectionAllowed(false);
        table.setFocusable(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(Color.BLACK);

        this.setFocusable(true);
        this.setLayout(new BorderLayout());
        this.add(score, BorderLayout.NORTH);
        this.add(table, BorderLayout.CENTER);
        this.setSize(338,472);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLocationRelativeTo(null);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        fireEvent(KeyboardEvent.UP);
                        break;
                    case KeyEvent.VK_DOWN:
                        fireEvent(KeyboardEvent.DOWN);
                        break;
                    case KeyEvent.VK_LEFT:
                        fireEvent(KeyboardEvent.LEFT);
                        break;
                    case KeyEvent.VK_RIGHT:
                        fireEvent(KeyboardEvent.RIGHT);
                        break;
                }
            }
        });
    }

    public void setProvider(BoardProvider boardProvider){
        this.boardProvider = boardProvider;
        init();
    }

    @Override
    public void onCrash(CrashEvent event) {
        this.remove(score);
        this.remove(table);

        gameOverView.setList(event.getScores());

        this.add(gameOverView, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    @Override
    public void onReset() {
        this.remove(gameOverView);
        init();
        revalidate();
        repaint();
    }

    @Override
    public void onTick(TickEvent event) {
            if(table != null) {
                DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
                for (Position pos : event.getUpdatedPositions()) {
                    tableModel.setValueAt(event.getBoard().getBoardParcel(pos), pos.getY(), pos.getX());
                }
                revalidate();
                repaint();
            }
    }



    private void fireEvent(int direction) {
        KeyboardEvent event = new KeyboardEvent(this, direction);
        for (KeyboardListener listener : keyboardListeners) {
            listener.directionChanged(event);
        }
    }

    public void addKeyboardListener(KeyboardListener listener) {
        keyboardListeners.add(listener);
    }

}
