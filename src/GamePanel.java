import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Created by qwer on 16.01.15.
 */
public class GamePanel extends JPanel implements Runnable {
    private int radius = 20;
    private int positionX = 50;
    private int positionY = 50;

    public static final int STATE_STOP = 0;
    public static final int STATE_MOVE = 1;

    private int state = STATE_STOP;
    private int direction = 135;
    private int speed = 1;

    public GamePanel(int radius, int positionX, int positionY) {
        super();
        this.radius = radius;
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public void paint(Graphics g) {
        // super.paint(g);
//        Graphics2D g2 = (Graphics2D) g;
//        g2.setColor(Color.BLACK);
//        g2.fill(new Ellipse2D.Double(0, 0, width, height));

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // g2.setPaint(new GradientPaint(0, 0, Color.blue, 20, 10, Color.red, true));
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setColor(Color.BLACK);
        g2.fill(new Ellipse2D.Double(positionX, positionY, radius, radius));
        // g2.fillRect(positionX, positionY, 20, 20);

    }

    @Override
    public void run() {
        try {
            while (state == STATE_MOVE) {
                repaint();
                if (getWidth() == 0 || getHeight() == 0) {
                    continue;
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
// System.out.println(positionX + " " + positionY);

                if (positionX + radius > getWidth() && positionY + radius > getHeight() || positionX < 0 && positionY < 0) {
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

                repaint();

                Thread.sleep(1000 / 400);
            }
        } catch (InterruptedException ie) {
            //
        }
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
