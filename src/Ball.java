import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.util.TimerTask;

/**
 * Created by qwer on 16.01.15.
 */
public class Ball extends JComponent implements Runnable { // ActionListener

    private final int width;
    private final int height;
    private int positionX;
    private int positionY;

    private Timer timer;

    public Ball(int width, int height, int positionX, int positionY) {
        super();
        this.width = width;
        this.height = height;
        this.positionX = positionX;
        this.positionY = positionY;

        setBounds(positionX, positionY, width, height);

        repaint();
    }

    @Override
    public void paint(Graphics g) {
        // super.paint(g);
//        Graphics2D g2 = (Graphics2D) g;
//        g2.setColor(Color.BLACK);
//        g2.fill(new Ellipse2D.Double(0, 0, width, height));
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.BLACK);
        g2.fillRect(0,0,20,20);

    }



    public void startMove(int direction) {

        Thread t = new Thread(this);
        t.start();


//        if (timer == null) {
//            timer = new Timer(500, this);
//            timer.start();
//        }
    }

//    @Override
//    public void actionPerformed(ActionEvent e) {
//        positionX += 30;
//        positionY += 30;
//        setBounds(positionX, positionY, width, height);
//    }

    public void repaingBall() {
        repaint();
    }

    public void stopMove(int direction) {
        // TODO
    }

    @Override
    public void run() {
        try {
            while (true) {
                positionX += 10;
                positionY += 10;
                setBounds(positionX, positionY, width, height);
                repaint();
                Thread.sleep(100);
            }
        } catch (InterruptedException ie) {

        }

    }
}
