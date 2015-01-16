import javax.swing.*;
import java.awt.*;

/**
 * Created by timur560 on 16.01.15.
 */
public class MainFrame extends JFrame {
    private GamePanel gamePane = new GamePanel(20, 200, 100);

    public MainFrame() {
        super();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(600, 800);
        gamePane.setBackground(Color.WHITE);
        gamePane.setLayout(null);
        add(gamePane);
        gamePane.startMove(225);
    }

}
