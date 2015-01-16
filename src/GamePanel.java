import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;

/**
 * Created by timur560 on 16.01.15.
 */
public class GamePanel extends JPanel implements Runnable {
    private int radius = 20;
    private int positionX = 50;
    private int positionY = 50;

    public static final int STATE_STOP = 0;
    public static final int STATE_MOVE = 1;

    public static final int STATE_STAYING = 0;
    public static final int STATE_MOVING_LEFT = 1;
    public static final int STATE_MOVING_RIGHT = 2;

    private int state = STATE_STOP;
    private int direction = 135;
    private int speed = 1;
    private int player1Position = 200;

    private int player1State = STATE_STAYING;

    public GamePanel(int radius, int positionX, int positionY) {
        super();
        this.radius = radius;
        this.positionX = positionX;
        this.positionY = positionY;

        setFocusable(true);
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    player1State = STATE_MOVING_LEFT;
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    player1State = STATE_MOVING_RIGHT;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                player1State = STATE_STAYING;
            }


        });
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // clear area
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // draw ball
        g2.setColor(Color.BLACK);
        g2.fill(new Ellipse2D.Double(positionX, positionY, radius, radius));
        // g2.fillRect(positionX, positionY, 20, 20);

        // draw bottom player
        g2.fillRect(player1Position, getHeight() - 20, 200, 20);

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
                    case STATE_MOVING_LEFT:
                        player1Position -= 2;
                        break;
                    case STATE_MOVING_RIGHT:
                        player1Position += 2;
                        break;
                }

                switch (direction) {
                    case 45:
                        positionX += speed;
                        positionY -= speed;
                        break;
                    case 135:
                        positionX += speed;
                        positionY += speed;
                        break;
                    case 225:
                        positionX -= speed;
                        positionY += speed;
                        break;
                    case 315:
                        positionX -= speed;
                        positionY -= speed;
                        break;
                }

                checkBorders();

                repaint();

                Thread.sleep(1000 / 400);
            }
        } catch (InterruptedException ie) {
            //
        }
    }

    private void checkBorders() {
        // check area borders
        if (positionX + radius > getWidth() && positionY + radius > getHeight()
                || positionX < 0 && positionY < 0
                || positionX < 0 && positionY + radius > getHeight()
                || positionX + radius > getWidth() && positionY < 0) {
            direction = (direction + 180) % 360;
        } else if (positionX + radius > getWidth()) {
            if (direction < 90) {
                direction = (direction - 90 < 0) ? (direction - 90 + 360) : (direction - 90);
            } else {
                direction = (direction + 90)  % 360;
            }
        } else if (positionY + radius > getHeight()) {
            if (direction < 180) {
                direction = (direction - 90 < 0) ? (direction - 90 + 360) : (direction - 90);
            } else {
                direction = (direction + 90) % 360;
            }
        } else if (positionY <= 0) {
            if (positionY < 90) {
                direction = (direction + 90) % 360;
            } else {
                direction = (direction - 90 < 0) ? (direction - 90 + 360) : (direction - 90);
            }
        } else if (positionX <= 0) {
            if (positionY > 270) {
                direction = (direction + 90) % 360;
            } else {
                direction = (direction - 90 < 0) ? (direction - 90 + 360) : (direction - 90);
            }
        }

        // check player surface

        // TODO
    }

    Thread animationThread = new Thread(this);

    public void startMove(int direction) {
        state = STATE_MOVE;
        this.direction = direction;
        animationThread.start();
    }


    public void stopMove() {
        animationThread.interrupt(); // stop();
    }

}
