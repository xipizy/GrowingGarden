import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class GamePanel extends JPanel implements Runnable {

    // Sreen Settings
    final int originalTitleSize = 16; // 32x32 tile
    final int scaler = 3;

    final int tileSize = originalTitleSize * scaler;
    final int maxScreenCol = 16;
    final int maxScreenRow = 16;
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;

    // Setting default selection position
    int selectX = 100;
    int selectY = 100;
    int selectSpeed = 15;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        // Game Loop

        // Setting the time between each draw
        double drawInterval = 1000000000/60; // 60 = FPS
        double nextDrawTime = System.nanoTime() + drawInterval;
        

        while(gameThread != null) {

            // 1: Update information, status of plants
            update();
            // 2: Draw the new screen
            repaint();

            // Setting delay between each update and repaint
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime/1000000;
                if (remainingTime < 0) {
                    remainingTime = 0;
                }
                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        if(keyH.upPressed == true) {
            selectY -= selectSpeed;
        } else if(keyH.downPressed == true) {
            selectY += selectSpeed;
        } else if(keyH.rightPressed == true) {
            selectX += selectSpeed;
        } else if(keyH.leftPressed == true) {
            selectX -= selectSpeed;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.white);
        g2.fillRect(selectX, selectY, tileSize, tileSize);
        g2.dispose();
    }
}
