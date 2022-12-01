import javax.swing.JFrame;
import java.awt.*;

public class main {
    public static void main(String[] args) {

//        Making object of JFrame and giving title to our frame.
        JFrame frame = new JFrame("Snake Game");

//        Setting size and location of the frame(i.e. boundary)
        frame.setBounds(10, 10, 905, 700);

//        To restrict user to resize the frame.
        frame.setResizable(false);

//        If user close the game, then program also stop running.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//        Making object of GamePanel class.
        GamePanel panel = new GamePanel();

//        Setting background colour of game panel
        panel.setBackground(Color.WHITE);

//        Adding this panel to frame.
        frame.add(panel);

//        Frame is invisible/false by default so, make it visible/true;
        frame.setVisible(true);

    }
}
