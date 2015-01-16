import javax.swing.*;
import java.awt.*;

/**
 * Created by qwer on 16.01.15.
 */
public class MainFrame extends JFrame {
//    private Ball ball = new Ball(30, 30, 200, 200);

    private GamePanel gamePane = new GamePanel(20, 200, 100);

    public MainFrame() {
        super();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(600, 800);
        gamePane.setBackground(Color.WHITE);
        gamePane.setLayout(null);
        // gamePane.add(ball);
        add(gamePane);
        gamePane.startMove(225);
    }

}
