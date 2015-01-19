import javax.swing.*;
import java.awt.*;

/**
 * Created by timur560 on 16.01.15.
 */
public class MainFrame extends JFrame {
    private GamePanel gamePane = new GamePanel();

    public MainFrame() {
        super();
        setTitle("Ping-pong game");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(500, 700);
        gamePane.setBackground(Color.WHITE);
        gamePane.setLayout(null);
        add(gamePane);
        gamePane.startMove(); //225);
    }

}
