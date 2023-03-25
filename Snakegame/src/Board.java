import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener{
    int B_HEIGHT = 400;
    int B_WIDTH = 400;
    int MAX_DOTS = 1600;
    int DOT_SIZE = 10;
    int DOTS;
    int[] X = new int[MAX_DOTS];
    int[] Y = new int[MAX_DOTS];
    int apple_X;
    int apple_Y;
//    Images..
    Image body,head,apple;
    Timer timer;
    int DELAY = 100;
    boolean leftDirection = true;
    boolean rightDirection = false;
    boolean upDirection = false;
    boolean downDirection = false;
    boolean inGame = true;
    Board(){
        TAdapter tAdapter = new TAdapter();
        addKeyListener(tAdapter);
        setFocusable(true);
        setPreferredSize(new Dimension( B_WIDTH,B_HEIGHT));
        setBackground(Color.black);
        initGame();
        loadImages();
    }
//    Intialiazing the Game..
    public void initGame() {
        DOTS = 3;
//        After that we are going to initialize the snake's position in the game....
        X[0] = 200;
        Y[0] = 200;
        for(int i=1;i<DOTS;i++){
            X[i] = X[0]+DOT_SIZE*i;
            Y[i] = Y[0];
        }
//        Intializing the Apple's position(Food in the Game)..
//        apple_X = 150;
//        apple_Y = 150;
        locateApple();
        timer = new Timer(DELAY,this);
        timer.start();
    }
//    Load images from resources folder to Image object
    public void loadImages(){
        ImageIcon bodyIcon = new ImageIcon("src/resources/dot.png");
        body = bodyIcon.getImage();
        ImageIcon headIcon = new ImageIcon("src/resources/head.png");
        head = headIcon.getImage();
        ImageIcon appleIcon = new ImageIcon("src/resources/apple.png");
        apple = appleIcon.getImage();
    }
//    Draw Images at snakes and apple's position....

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        doDrawing(g);
    }
//    draw image
    public void doDrawing(Graphics g){
        if(inGame) {
            g.drawImage(apple,apple_X,apple_Y,this);
            for(int i=0;i<DOTS;i++){
                if(i==0){
                    g.drawImage(head,X[0],Y[0],this);
                }
                else{
                    g.drawImage(body,X[i],Y[i],this);
                }
            }
        }
        else{
            gameOver(g);
            timer.stop();
        }
    }
//    Randomize the apple's position...
    public void locateApple(){
        apple_X = ((int)(Math.random()*39))*DOT_SIZE;
        apple_Y = ((int)(Math.random()*39))*DOT_SIZE;
    }
//    check collisions......
    public void checkCollision(){
//        collision with the snake body....
        for(int i=1;i<DOTS;i++){
            if(i>4 && X[0] == X[i]&&Y[0]==Y[i]){
                inGame = false;
            }
        }
//        collision with border...
        if(X[0]<0){
            inGame = false;
        }
        if(X[0]>B_WIDTH){
            inGame = false;
        }
        if(Y[0]<0){
            inGame = false;
        }
        if(Y[0]>B_HEIGHT){
            inGame = false;
        }
    }
    public void gameOver(Graphics g){
        String msg = "GAME OVER";
        int score = (DOTS-3)*25;
        String scoremsg = "SCORE:"+Integer.toString(score);
        Font small = new Font("Helvetica",Font.BOLD,14);
        FontMetrics fontMetrics = getFontMetrics(small);

        g.setColor(Color.WHITE);
        g.setFont(small);
        g.drawString(msg,(B_WIDTH-fontMetrics.stringWidth(msg))/2,B_HEIGHT/4);
        g.drawString(scoremsg,(B_WIDTH-fontMetrics.stringWidth(scoremsg))/2,3*(B_HEIGHT/4));
    }
    @Override
    public void actionPerformed(ActionEvent actionEvent){
        if(inGame){
            checkApple();
            checkCollision();
            move();
        }
        repaint();
    }
    public void move(){
        for(int i=DOTS-1;i>0;i--){
            X[i] = X[i-1];
            Y[i] = Y[i-1];
        }
        if(leftDirection){
            X[0]-=DOT_SIZE;
        }
        if(rightDirection){
            X[0]+=DOT_SIZE;
        }
        if(upDirection){
            Y[0]-=DOT_SIZE;
        }
        if(downDirection){
            Y[0]+=DOT_SIZE;
        }
    }
//    make snake to eat food...
    public void checkApple(){
        if(apple_X == X[0] && apple_Y == Y[0]){
            DOTS++;
            locateApple();
        }
    }
//    Implements contols...
    private class TAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent keyEvent){
            int key = keyEvent.getKeyCode();
            if(key == KeyEvent.VK_LEFT && !rightDirection){
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if(key == KeyEvent.VK_RIGHT && !leftDirection){
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if(key == KeyEvent.VK_UP && !downDirection){
                upDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
            if(key == KeyEvent.VK_DOWN && !upDirection){
                leftDirection = false;
                rightDirection = false;
                downDirection = true;
            }
        }
}
}
