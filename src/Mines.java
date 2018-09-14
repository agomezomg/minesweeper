
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author lana
 */
public class Mines implements MouseListener {

    JFrame frame = new JFrame("Minesweeper");
    JButton reset = new JButton("Reset");
    boolean won;
    JButton[][] front;
    int[][] bgCount;
    int[][] flagCount;
    Container grid;
    private static final int HEIGHT = 420;
    private static final int WIDTH = 510;
    private int MINES;
    int xVal, yVal;
    boolean lost;

    public Mines() {
        won = false;
        lost = false;
        this.xVal = 8;
        this.yVal = 8;
        MINES = 10;
        front = new JButton[8][8];
        bgCount = new int[8][8];
        flagCount = new int[8][8];
        grid = new Container();

        for (int[] flagCount1 : flagCount) {
            for (int j = 0; j < flagCount[0].length; j++) {
                flagCount1[j] = 0;
            }
        }

        frame.setSize(HEIGHT, WIDTH);
        //centers frame layout
        frame.setLayout(new BorderLayout());
        frame.add(reset, BorderLayout.NORTH);
        reset.addMouseListener(this);

        createMines(xVal, yVal);

        for (JButton[] campo1 : front) {
            for (int b = 0; b < front[0].length; b++) {
                campo1[b] = new JButton();
                campo1[b].setBackground(Color.getHSBColor(255, 112, 4));
                campo1[b].addMouseListener(this);
                grid.add(campo1[b]);
            } //implementa el campo de JButton en el JFrame
            frame.add(grid, BorderLayout.CENTER);//centra los botones en el JFrame
        }
        grid.setLayout(new GridLayout(8, 8));

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public Mines(int xVal, int yVal, int mines) {
        won = false;
        MINES = mines;
        this.xVal = xVal;
        this.yVal = yVal;
        front = new JButton[this.xVal][this.yVal];
        bgCount = new int[this.xVal][this.yVal];
        flagCount = new int[this.xVal][this.yVal];

        for (int i = 0; i < yVal; i++) {
            for (int j = 0; j < xVal; j++) {
                flagCount[i][j] = 0;
            }
        }

        frame.setLayout(new BorderLayout());
        frame.add(reset, BorderLayout.NORTH);
        reset.addMouseListener(this);

        grid = new Container();

        for (JButton[] campo1 : front) {
            for (int b = 0; b < front[0].length; b++) {
                campo1[b] = new JButton();
                campo1[b].addMouseListener(this);
                grid.add(campo1[b]);
            } //implementa el campo de JButton en el JFrame
            frame.add(grid, BorderLayout.CENTER);//centra los botones en el JFrame
        }
        grid.setLayout(new GridLayout(xVal, yVal));

        createMines(xVal, yVal);

        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void createMines(int xVal, int yVal) {
        ArrayList<Integer> stackSimulation = new ArrayList<>();
        /*stacks simulation to do backtracking*/

        for (int x = 0; x < bgCount.length; x++) {
            for (int y = 0; y < bgCount[0].length; y++) {
                stackSimulation.add((x * 10) + y);
            }
        }
        //picks 30 bombs and resets counts

        bgCount = new int[xVal][yVal];
        for (int i = 0; i < MINES; i++) {
            int mine = (int) (Math.random() * stackSimulation.size());
            bgCount[stackSimulation.get(mine / 10)][stackSimulation.get(mine) % 10] = MINES;
            stackSimulation.remove(mine);
        }

        //inicializo los contadores de las minas        
        for (int x = 0; x < bgCount.length; x++) {
            for (int y = 0; y < bgCount[0].length; y++) {
                if (bgCount[x][y] != MINES) {
                    int mineCount = 0;
                    if (x > 0 && bgCount[x - 1][y] == MINES) {
                        mineCount++;
                    }
                    if (x < bgCount.length - 1 && bgCount[x + 1][y] == MINES) {
                        mineCount++;
                    }
                    if (y > 0 && bgCount[x][y - 1] == MINES) {
                        mineCount++;
                    }
                    if (y < bgCount[0].length - 1 && bgCount[x][y + 1] == MINES) {
                        mineCount++;
                    }
                    if (x > 0 && y > 0 && bgCount[x - 1][y - 1] == MINES) {
                        mineCount++;
                    }
                    if (x < bgCount.length - 1 && y > 0 && bgCount[x + 1][y - 1] == MINES) {
                        mineCount++;
                    }
                    if (x > 0 && y < bgCount[0].length - 1 && bgCount[x - 1][y + 1] == MINES) {
                        mineCount++;
                    }
                    if (x < bgCount.length - 1 && y < bgCount[0].length - 1 && bgCount[x + 1][y + 1] == MINES) {
                        mineCount++;
                    }
                    bgCount[x][y] = mineCount;
                }
            }
        }
    }

    public boolean checkWin() {
        for (int x = 0; x < bgCount.length; x++) {
            for (int y = 0; y < bgCount[0].length; y++) {
                if (bgCount[x][y] != MINES && front[x][y].isEnabled() == true) {
                    return false;
                }
            }
        }
        won = true;
        lost = false;
        return true;
    }

    public void YouLose() {
        for (int x = 0; x < front.length; x++) {
            for (int y = 0; y < front[0].length; y++) {
                if (front[x][y].isEnabled()) {
                    if (bgCount[x][y] != MINES) {
                        front[x][y].setText(bgCount[x][y] + "");
                        front[x][y].setEnabled(false);
                    } else {
                        front[x][y].setText("X");
                        front[x][y].setEnabled(false);
                    }
                }
            }
        }
        won = false;
        lost = true;
        JOptionPane.showMessageDialog(null, "YOU LOSE!",
                "YOU LOSE", JOptionPane.PLAIN_MESSAGE);
    }

    public void Zeros(ArrayList<Integer> recursiveClear) {
        if (recursiveClear.isEmpty()) {
            return;
        } else {
            int x = recursiveClear.get(0) / 10;
            int y = recursiveClear.get(0) % 10;
            recursiveClear.remove(0);
            if (x > 0 && y > 0 && front[x - 1][y - 1].isEnabled()) {//arriba y a la izquierda
                front[x - 1][y - 1].setText(bgCount[x - 1][y - 1] + "");
                front[x - 1][y - 1].setEnabled(false);
                if (bgCount[x - 1][y - 1] == 0) {
                    recursiveClear.add((x - 1) * 10 + (y - 1));
                }
            }
            if (y > 0 && front[x][y - 1].isEnabled()) {//izquierda
                front[x][y - 1].setText(bgCount[x][y - 1] + "");
                front[x][y - 1].setEnabled(false);
                if (bgCount[x][y - 1] == 0) {
                    recursiveClear.add(x * 10 + (y - 1));
                }
            }
            if (x < bgCount.length - 1 && y > 0 && front[x + 1][y - 1].isEnabled()) {//abajo izquierda
                front[x + 1][y - 1].setText(bgCount[x + 1][y - 1] + "");
                front[x + 1][y - 1].setEnabled(false);
                if (bgCount[x + 1][y - 1] == 0) {
                    recursiveClear.add((x + 1) * 10 + (y - 1));
                }
            }
            if (x > 0 && front[x - 1][y].isEnabled()) {//arriba
                front[x - 1][y].setText(bgCount[x - 1][y] + "");
                front[x - 1][y].setEnabled(false);
                if (bgCount[x - 1][y] == 0) {
                    recursiveClear.add((x - 1) * 10 + y);
                }
            }

            if (x < bgCount.length - 1 && front[x + 1][y].isEnabled()) {//derecha
                front[x + 1][y].setText(bgCount[x + 1][y] + "");
                front[x + 1][y].setEnabled(false);
                if (bgCount[x + 1][y] == 0) {
                    recursiveClear.add((x + 1) * 10 + y);
                }
            }
            if (x > 0 && y < bgCount[0].length - 1 && front[x - 1][y + 1].isEnabled()) {//arriba y a la izquierda
                front[x - 1][y + 1].setText(bgCount[x - 1][y + 1] + "");
                front[x - 1][y + 1].setEnabled(false);
                if (bgCount[x - 1][y + 1] == 0) {
                    recursiveClear.add((x - 1) * 10 + (y + 1));
                }
            }
            if (y < bgCount[0].length - 1 && front[x][y + 1].isEnabled()) {//abajo
                front[x][y + 1].setText(bgCount[x][y + 1] + "");
                front[x][y + 1].setEnabled(false);
                if (bgCount[x][y + 1] == 0) {
                    recursiveClear.add(x * 10 + (y + 1));
                }
            }
            if (x < bgCount.length - 1 && y < bgCount[0].length - 1 && front[x + 1][y + 1].isEnabled()) {//derecha
                front[x + 1][y + 1].setText(bgCount[x + 1][y + 1] + "");
                front[x + 1][y + 1].setEnabled(false);
                if (bgCount[x + 1][y + 1] == 0) {
                    recursiveClear.add((x + 1) * 10 + (y + 1));
                }
            }
        }
        Zeros(recursiveClear);
    }

    @Override
    public void mouseClicked(MouseEvent hello) {
        if (!hello.isMetaDown()) {
            if (hello.getSource().equals(reset)) {
                for (int x = 0; x < front.length; x++) {
                    for (int y = 0; y < front[0].length; y++) {
                        front[x][y].setText("");
                        front[x][y].setEnabled(true);
                        flagCount[x][y] = 0;
                    }
                }
                createMines(xVal, yVal);
            } else {
                for (int x = 0; x < front.length; x++) {
                    for (int y = 0; y < front[0].length; y++) {
                        if (front[x][y].isEnabled() && hello.getSource().equals(front[x][y]) && front[x][y].getText() != "F" && front[x][y].getText() != "F?") {
                            if (bgCount[x][y] == MINES) {
                                front[x][y].setText("X");
                                front[x][y].setForeground(Color.RED);
                                YouLose();
                            } else if (bgCount[x][y] == 0 && bgCount[x][y] != 10) {
                                front[x][y].setText(bgCount[x][y] + "");
                                front[x][y].setEnabled(false);
                                ArrayList<Integer> Limpiar = new ArrayList<>();
                                Limpiar.add(x * 10 + y);
                                Zeros(Limpiar);
                                if (checkWin()) {
                                    JOptionPane.showMessageDialog(null, "YOU WIN!", "YOU WIN!", JOptionPane.PLAIN_MESSAGE);
                                }
                            } else {
                                front[x][y].setText(bgCount[x][y] + "");
                                front[x][y].setEnabled(false);
                                if (checkWin()) {
                                    youWin();
                                }
                            }
                        }
                    }
                }
            }
        } else {
            for (int x = 0; x < front.length; x++) {
                for (int y = 0; y < front[0].length; y++) {
                    if (hello.getSource().equals(front[x][y]) && front[x][y].isEnabled()) {
                        flagCount[x][y]++;
                        switch (flagCount[x][y]) {
                            case 2: {
                                front[x][y].setText("F?");
                                front[x][y].setForeground(Color.black);
                                break;
                            }
                            case 1: {
                                front[x][y].setText("F");
                                front[x][y].setForeground(Color.red);
                                break;
                            }
                            case 3: {
                                front[x][y].setText("");
                                flagCount[x][y] = 0;
                                break;
                            }
                            default: {
                                break;

                            }
                        }
                    }
                }
            }
        }
    }

    void youWin() {
        JOptionPane.showMessageDialog(null, "YOU WIN!");
        for (int i = 0; i < yVal; i++) {
            for (int j = 0; j < xVal; j++) {
                front[i][j].setText(":D");
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent hello) {
        //hello
    }

    @Override
    public void mouseReleased(MouseEvent hello) {
        //do nothing
    }

    @Override
    public void mouseEntered(MouseEvent hello) {
        //do nothing
    }

    @Override
    public void mouseExited(MouseEvent hello) {
        //do nothing
    }
}
