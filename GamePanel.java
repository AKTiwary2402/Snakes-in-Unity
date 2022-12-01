import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

//    To store position of snake body in x and y direction, we require two arrays.
    private int[] snakeLengthX = new int[750];
    private int[] snakeLengthY = new int[750];
    private int lengthOfSnake = 3; // Initial length of snake

//    Position of enemy in game panel(xPos and y Pos)
    int[] xPos = {25, 50, 75, 100, 125, 150, 175, 200, 225, 250, 275, 300, 325, 350, 375, 400, 425, 450,
            475, 500, 525, 550, 575, 600, 625, 650, 675, 700, 725, 750, 775, 800, 825, 850};

    int[] yPos = {75, 100, 125, 150, 175, 200, 225, 250, 275, 300, 325, 350, 375, 400, 425, 450, 475, 500,
            525, 550, 575};

    private Random random = new Random();

    private int enemyX, enemyY; // Enemy's variable in x and y direction
    private boolean left = false;
    private boolean right = true; // Staring direction of snake is in right direction, so other three are false.
    private boolean up = false;
    private boolean down = false;

    private int moves = 0; // Snake is not moving.

    private int score = 0; // score variable
    private boolean gameOver = false; // game over variable

//    Import all downloaded images
    private ImageIcon snake = new ImageIcon(getClass().getResource("redSquare.png"));
//    private ImageIcon rightMouth = new ImageIcon(getClass().getResource("redSquare.png"));
//    private ImageIcon upMouth = new ImageIcon(getClass().getResource("redSquare.png"));
//    private ImageIcon downMouth = new ImageIcon(getClass().getResource("redSquare.png"));
//    private ImageIcon snakeBody = new ImageIcon(getClass().getResource("redSquare.png"));
    private ImageIcon enemyImage = new ImageIcon(getClass().getResource("redSquare.png"));


    private Timer timer;   // Object of Timer class
    private int delay = 100; // speed of moving snake(i.e. to make illusion)
    GamePanel(){
        addKeyListener(this); // Calling ActionListener method
        setFocusable(true);
        setFocusTraversalKeysEnabled(true);
        timer = new Timer(delay, this); // Call ActionListener method after every 100 ms.
        timer.start(); // Starting timer

        newEnemy();  // Calling newEnemy method
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(Color.WHITE); // Setting colour
        g.drawRect(24, 10, 851, 55); // Setting boundary of snakeTitle board

        g.drawRect(20, 74, 851, 576); // Setting boundary of Game board

        g.setColor(Color.BLACK);
        g.fillRect(24,10, 851, 55);
        g.setColor(Color.pink);
        g.setFont(new Font("Retro Video Game", Font.BOLD, 50));
        g.drawString("SNAKES IN UNITY", 230, 50);

        g.setColor(Color.WHITE); // Setting colour of game board
        g.fillRect(20, 74, 851, 576); // Fill colour in our game board

        enemyImage.paintIcon(this, g, enemyX, enemyY); // Setting enemy image position

        if(moves == 0){
//            Setting initial position of snake in x direction.
            snakeLengthX[0] = 100; // head
            snakeLengthX[1] = 75; // body part(i.e. middle)
            snakeLengthX[2] = 50; // body part(last)

//            Similarly, initial position of snake in y direction.
            snakeLengthY[0] = 100;
            snakeLengthY[1] = 100;
            snakeLengthY[2] = 100;

        }
//        If snake is in left direction, show left mouth, Similarly for other cases.
        if(left || right || up || down){
            snake.paintIcon(this, g, snakeLengthX[0], snakeLengthY[0]);
        }

//        Increasing length after eating enemy(i.e. adding image of enemy)
        for(int i=1; i<lengthOfSnake; i++){
            snake.paintIcon(this, g, snakeLengthX[i], snakeLengthY[i]);
        }

//        If game is over.
        if(gameOver){
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("GAME OVER", 300, 300);

            g.setColor(Color.GREEN);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Press 'Enter' key to restart", 320, 350);
        }

//        To show the score and length to the user
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 750, 30);
        g.drawString("Length: "+ lengthOfSnake, 750, 50);

        g.dispose(); // dispose what we done above
    }
    public void actionPerformed(ActionEvent e){

//        This loop is for move snake's body with all together.
        for(int i=lengthOfSnake-1; i>0; i--){
            snakeLengthX[i] = snakeLengthX[i-1];
            snakeLengthY[i] = snakeLengthY[i-1];
        }

//        To move snake in left/right/up/down direction, we need to change snake's body position.
        if(left){
            snakeLengthX[0] = snakeLengthX[0] - 25;
        }
        if(right){
            snakeLengthX[0] = snakeLengthX[0] + 25;
        }
        if(up){
            snakeLengthY[0] = snakeLengthY[0] - 25;
        }
        if(down){
            snakeLengthY[0] = snakeLengthY[0] + 25;
        }
//      If snake touches game panel boundary then it returns back from the opposite direction(X-axis/left-right).
        if(snakeLengthX[0] > 850) snakeLengthX[0] = 25;
        if(snakeLengthX[0] < 25) snakeLengthX[0] = 850;

//        Similarly for y-axis/up-down.
        if(snakeLengthY[0] > 625) snakeLengthY[0] = 75;
        if(snakeLengthY[0] < 75) snakeLengthY[0] = 625;

        collidesWithEnemy(); // Calling method
        collidesWithBody(); // Calling method
        repaint();  // Repaint the snake body to show it is moving
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
//      If user enter key, game restart
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            restart();
        }
//        If user press left key. Move towards left direction
        if(e.getKeyCode() == KeyEvent.VK_LEFT && !right){ // !right condition to do not move reverse/opposite path
            left = true;
            right = false;
            up = false;
            down = false;
            moves++;
        }
//        Same for right direction
        if(e.getKeyCode() == KeyEvent.VK_RIGHT && !left){
            left = false;
            right = true;
            up = false;
            down = false;
            moves++;
        }
//        Same for up direction
        if(e.getKeyCode() == KeyEvent.VK_UP && !down){
            left = false;
            right = false;
            up = true;
            down = false;
            moves++;
        }
//        Same for down direction
        if(e.getKeyCode() == KeyEvent.VK_DOWN && !up){
            left = false;
            right = false;
            up = false;
            down = true;
            moves++;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void newEnemy(){
//        Setting random position of enemy in x and y direction(34 and 21 are size of array)
        enemyX = xPos[random.nextInt(34)];
        enemyY = yPos[random.nextInt(21)];

//        If enemy position is at snake's body, then call function again.
        for(int i=lengthOfSnake-1; i>=0; i--){
            if(snakeLengthX[i] == enemyX && snakeLengthY[i] == enemyY){
                newEnemy();
            }
        }
    }
//    If snake eat enemy, then call new enemy, increment in length of snake and score both
    private void collidesWithEnemy(){
        if(snakeLengthX[0] == enemyX && snakeLengthY[0] == enemyY){
            newEnemy();
            lengthOfSnake++;
            score++;
        }
    }
//    If snake collides with its own body, then stop timer and show game over
    private void collidesWithBody(){
        for(int i=lengthOfSnake-1; i>0; i--){
            if(snakeLengthX[i] == snakeLengthX[0] && snakeLengthY[i] == snakeLengthY[0]){
                timer.stop();
                gameOver = true;
            }
        }
    }
//    Function when user press enter to restart.
    private void restart() {
        gameOver = false;
        moves = 0;
        score = 0;
        lengthOfSnake = 3;
        left = false;
        right = true;
        up = false;
        down = false;
        timer.start();
        repaint();
    }
}
