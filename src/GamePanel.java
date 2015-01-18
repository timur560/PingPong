import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Created by timur560 on 16.01.15.
 */
public class GamePanel extends JPanel implements Runnable {
    public static final int STATE_STOP = 0;
    public static final int STATE_MOVE = 1;

    public static final int PLAYER_STATE_STAYING = 0;
    public static final int PLAYER_STATE_MOVING_LEFT = 1;
    public static final int PLAYER_STATE_MOVING_RIGHT = 2;

    public static final int SPEED_MODE_NORMAL = 1;
    public static final int SPEED_MODE_SLOWER = 2;

    private int radius = 50;
    private int[] position = {225, 500};
    private int direction = 30;
    private int state = STATE_STOP;
    private int speed = 30;
    private int player1Position = 200;
    private int player1Length = 200;
    private int player1Height = 52;
    private int speedMode = SPEED_MODE_NORMAL;

    private static BufferedImage ballImage, backImage, platformImage;

    private int score = 0;

    private int player1State = PLAYER_STATE_STAYING;

    public GamePanel() {
        super();

        try {
            ballImage = ImageIO.read(new File(this.getClass().getResource("images/ball.png").getFile()));
            backImage = ImageIO.read(new File(this.getClass().getResource("images/background.jpg").getFile()));
            platformImage = ImageIO.read(new File(this.getClass().getResource("images/platform.png").getFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setFocusable(true);
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    player1State = PLAYER_STATE_MOVING_LEFT;
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    player1State = PLAYER_STATE_MOVING_RIGHT;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                player1State = PLAYER_STATE_STAYING;
            }


        });
    }

    @Override
    public void paint(Graphics g) {
    // public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // clear area
        // g2.setColor(Color.WHITE);
        g2.setColor(new Color(238, 238, 238));
        g2.fillRect(0, 0, getWidth(), getHeight());
        // g2.drawImage(backImage, 0, 0, null);

        // score
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 50));
        g2.drawString(score + "", 20, 100);

        // draw ball
        g2.setColor(Color.BLACK);
        // g2.fill(new Ellipse2D.Double(positionX, positionY, radius, radius));
        // g2.fillRect(positionX, positionY, 20, 20);
        g2.drawImage(ballImage, position[0], position[1], null);


        // draw bottom player
        // g2.fillRect(player1Position, getHeight() - player1Height, player1Length, player1Height);
        g2.drawImage(platformImage, player1Position, getHeight() - player1Height, null);


    }

    @Override
    public void run() {
        try {
            while (state == STATE_MOVE) {
                repaint();

                if (getWidth() == 0 || getHeight() == 0) {
                    continue;
                }

                switch (player1State) {
                    case PLAYER_STATE_MOVING_LEFT:
                        player1Position -= 2;
                        break;
                    case PLAYER_STATE_MOVING_RIGHT:
                        player1Position += 2;
                        break;
                }

                changeBallPosition(direction);

                checkBorders();

                repaint();

                switch (speedMode) {
                    case SPEED_MODE_NORMAL:
                        Thread.sleep(Math.round(100.0 / (float)speed));
                        break;
                    case SPEED_MODE_SLOWER:
                        Thread.sleep(Math.round((100.0 / (float)speed) * 1.7));
                        break;
                }

            }
        } catch (InterruptedException ie) {
            //
        }
    }

    /**
     *
     * @param direction - angle between 0 .. 360
     */
    private void changeBallPosition(int direction) {

        direction = normalizeDirection(direction);

        if (direction % 45 == 0) {
            speedMode = SPEED_MODE_NORMAL;
        } else {
            speedMode = SPEED_MODE_SLOWER;
        }

        switch (direction) {
            case 30:
            case 45:
            case 90:
            case 135:
            case 150:
                position[0] ++;
                break;

            case 60:
            case 120:
                position[0] += 2;
                break;

            case 210:
            case 225:
            case 270:
            case 315:
            case 330:
                position[0] --;
                break;

            case 240:
            case 300:
                position[0] -= 2;
                break;
        }

        switch (direction) {
            case 120:
            case 135:
            case 180:
            case 225:
            case 240:
                position[1] ++;
                break;

            case 150:
            case 210:
                position[1] += 2;
                break;

            case 45:
            case 60:
            case 300:
            case 315:
            case 360:
            case 0:
                position[1] --;
                break;

            case 30:
            case 330:
                position[1] -= 2;
                break;
        }

    }

    private void checkBorders() {
        // check if ball bounce on player

        direction = normalizeDirection(direction);

        if (position[1] + radius == getHeight() - player1Height
                || position[1] + radius + 1 == getHeight() - player1Height) {
            position[1]--; // = getHeight() - player1Height - radius;
            if (position[0] + radius >= player1Position && position[0] <= player1Position + player1Length) {
                int touchPosition = position[0] + radius - player1Position;
                direction = normalizeDirection(300 + 120 * touchPosition / player1Length);
                // System.out.println(touchPosition + " " + direction);
                score += 10;
                //if (score % 100 == 0) {
                    speed += 1;
                //}
            }
        }

        // check area borders
        else if (position[0] + radius > getWidth() && position[1] + radius > getHeight()
                || position[0] < 0 && position[1] < 0
                || position[0] < 0 && position[1] + radius > getHeight()
                || position[0] + radius > getWidth() && position[1] < 0) { // corners
            direction = (direction + 180) % 360;
        } else if (position[0] + radius > getWidth()) { // right
            switch (direction) {
                case 30: direction = 330; break;
                case 45: direction = 315; break;
                case 60: direction = 300; break;
                case 90: direction = normalizeDirection(new Random().nextInt(340 - 200) + 200); break;
                case 120: direction = 240; break;
                case 135: direction = 225; break;
                case 150: direction = 210; break;
            }
        } else if (position[1] + radius > getHeight()) { // bottom
            lose();
            reset();
//            if (direction < 180) {
//                direction = (direction - 90 < 0) ? (direction - 90 + 360) : (direction - 90);
//            } else {
//                direction = (direction + 90) % 360;
//            }
        } else if (position[1] <= 0) { // top
            switch (direction) {
                case 300: direction = 240; break;
                case 315: direction = 225; break;
                case 330: direction = 210; break;
                case 360: case 0: direction = 180; break;
                case 30: direction = 150; break;
                case 45: direction = 135; break;
                case 60: direction = 120; break;
            }
        } else if (position[0] <= 0) { // left
            switch (direction) {
                case 330: direction = 30; break;
                case 315: direction = 45; break;
                case 300: direction = 60; break;
                case 270: direction = normalizeDirection(new Random().nextInt(160 - 20) + 20); break;
                case 240: direction = 120; break;
                case 225: direction = 135; break;
                case 210: direction = 150; break;
            }
        }

        // check player surface

        if (player1Position + player1Length >= getWidth()) {
            player1State = PLAYER_STATE_STAYING;
            player1Position = getWidth() - player1Length;
        } else if (player1Position <= 0) {
            player1State = PLAYER_STATE_STAYING;
            player1Position = 0;
        }
    }

    private int normalizeDirection(int direction) {
        direction = direction % 360;

        if (direction % 30 > direction % 45) {
            return direction / 45 * 45;
        } else {
            return direction / 30 * 30;
        }

    }

    private void reset() {
        score = 0;
        speed = 30;
        position = new int[]{225, 500};
        direction = 30;
    }

    private void lose() {
        JOptionPane.showMessageDialog(this, "Your score: " + score, "Game over", JOptionPane.INFORMATION_MESSAGE);
    }

    Thread animationThread = new Thread(this);

    public void startMove() {
        state = STATE_MOVE;
        // this.direction = direction;
        animationThread.start();
    }


    public void stopMove() {
        animationThread.interrupt(); // stop();
    }

}
