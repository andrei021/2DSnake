/**
 * Created on: 16 September 2020
 * Author: Mihai Andrei
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 90;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];

    int bodyParts = 6;
    int applesEaten = 0;
    int appleX;
    int appleY;
    char direction = 'R';

    boolean running = false;
    Timer timer;
    Random random;

    GamePanel() {
        this.random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        generateApple();
        this.running = true;
        this.timer = new Timer(DELAY, this);
        this.timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
//        drawGrid(g);

        if(this.running) {
            g.setColor(Color.RED);
            g.fillRect(this.appleX, this.appleY, UNIT_SIZE, UNIT_SIZE);

            g.setColor(Color.GREEN);
            for(int i = 0; i < this.bodyParts; i++) {
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                g.setColor(new Color(45, 180, 0));
            }

            g.setColor(Color.RED);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString
                    ("Score: " + applesEaten,
                    (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,
                    g.getFont().getSize());

        } else {
            gameOver(g);
        }
    }

    public void generateApple() {
        this.appleX = this.random.nextInt((SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE);
        this.appleY = this.random.nextInt((SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE);
    }

    public void move() {
        for(int i = this.bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch(this.direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {
        if(x[0] == this.appleX && y[0] == this.appleY) {
            bodyParts++;
            applesEaten++;
            generateApple();
        }
    }

    public void checkCollisions() {
        // check if head collides with body
        for(int i = this.bodyParts; i > 0; i--) {
            if((x[0] == x[i]) && y[0] == y[i]) {
                this.running = false;
            }
        }

        // check if head touches left border
        if(x[0] < 0) {
            this.running = false;
        }

        // check if head touches right border
        if(x[0] > SCREEN_WIDTH) {
            this.running = false;
        }

        // check if head touches top border
        if(y[0] < 0) {
            this.running = false;
        }

        // check if head touches bottom border
        if(y[0] > SCREEN_HEIGHT) {
            this.running = false;
        }

        if(!this.running) {
            this.timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        // display score
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString
                ("Score: " + applesEaten,
                (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,
                g.getFont().getSize());

        // display game over text
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(this.running) {
             move();
             checkApple();
             checkCollisions();
        }

        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }

    private void drawGrid(Graphics g) {
        g.setColor(Color.PINK);
        for(int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
            g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
            g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
        }
    }
}