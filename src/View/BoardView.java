package View;

import Events.KeyboardEvent;
import Interfaces.KeyboardListener;
import Interfaces.ViewUpdateListener;
import Model.Board;
import Model.Position;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class BoardView extends JFrame implements ViewUpdateListener<Board> {
    private JTable table;
    private JPanel panel;
    private int score;

    private ArrayList<KeyboardListener> keyboardListeners;

    public BoardView() {
        super("SNAKE");

        this.keyboardListeners = new ArrayList<>();
        this.setSize(1080,1920);
        this.setLayout(new BorderLayout());

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        DefaultTableModel model = new DefaultTableModel(25, 16) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.table = new JTable(model);


        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setMaxWidth(20); // Max cell width
            table.getColumnModel().getColumn(i).setMinWidth(20); // Min cell width
            table.getColumnModel().getColumn(i).setPreferredWidth(20); // Preferred cell width
        }

        this.panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawString("Score: " + score, 10, 20);
            }
        };

        // Set table cell render
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                int cellValue = (int) value;

                if(cellValue == 1){
                    setBackground(Color.GREEN);
                }else if(cellValue == 2){
                    setBackground(Color.RED);
                }else{
                    setBackground(Color.BLACK);
                }
                setForeground(getBackground());
                setText("");
                return this;
            }
        });

        // Disabling cell grid
        table.setShowGrid(false);
        // Disable cell selection
        table.setRowSelectionAllowed(false);
        table.setColumnSelectionAllowed(false);
        table.setFocusable(false);
        // Remove cell margins
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(Color.BLACK);

        this.setFocusable(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        System.out.println("fire up");
                        fireEvent(KeyboardEvent.UP);
                        break;
                    case KeyEvent.VK_DOWN:
                        System.out.println("fire down");
                        fireEvent(KeyboardEvent.DOWN);
                        break;
                    case KeyEvent.VK_LEFT:
                        System.out.println("fire left");
                        fireEvent(KeyboardEvent.LEFT);
                        break;
                    case KeyEvent.VK_RIGHT:
                        System.out.println("fire right");
                        fireEvent(KeyboardEvent.RIGHT);
                        break;
                }
            }
        });
    }


    @Override
    public void updateView(Board item) {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

        for(int i = 0; i < 25; i++){
            for(int j = 0; j < 16; j++){
                tableModel.setValueAt(item.getBoardParcel(new Position(j, i)),i, j);
            }
        }
        add(panel);
        add(table);
        revalidate();
        repaint();

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

    // Method to remove keyboard listeners
    public void removeKeyboardListener(KeyboardListener listener) {
        keyboardListeners.remove(listener);
    }
    public JTable getTable() {
        return table;
    }

    public JPanel getPanel() {
        return panel;
    }
}
