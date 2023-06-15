package View;

import Events.CrashEvent;
import Events.EatEvent;
import Events.KeyboardEvent;
import Events.TickEvent;
import Interfaces.GameEventListener;
import Interfaces.KeyboardListener;
import Interfaces.Provider;
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

public class BoardView extends JFrame implements GameEventListener {
    private JTable table;
    private Provider provider;
    private ArrayList<KeyboardListener> keyboardListeners;
    private ArrayList<GameEventListener> gameEventListeners;

    public BoardView() {
        super("SNAKE");
        this.keyboardListeners = new ArrayList<>();
        this.gameEventListeners = new ArrayList<>();
    }

    private void init(){
        Board board = provider.getBoard();
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

        table.setShowGrid(true);
        // Disable cell selection
        table.setRowSelectionAllowed(false);
        table.setColumnSelectionAllowed(false);
        table.setFocusable(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(Color.BLACK);

        this.setFocusable(true);
        this.setLayout(new BorderLayout());
        this.add(table, BorderLayout.CENTER);
        this.setSize(1080,1920);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

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

    public void setProvider(Provider provider){
        this.provider = provider;
        init();
    }

    @Override
    public void onTick(TickEvent event) {
        System.out.println("Update view in view");
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        for (Position pos : event.getUpdatedPositions()) {
            tableModel.setValueAt(event.getBoard().getBoardParcel(pos), pos.getY(), pos.getX());
        }
        revalidate();
        repaint();
    }

    @Override
    public void onEat(EatEvent event) {

    }

    @Override
    public void onCrash(CrashEvent event) {

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
