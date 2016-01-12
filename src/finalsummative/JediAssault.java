/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package finalsummative;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 *
 * @author stahc1596
 */

public class JediAssault extends JComponent implements KeyListener, MouseListener{

    // Height and Width of our game
    static final int WIDTH = 800;
    static final int HEIGHT = 600;
    
    // sets the framerate and delay for our game
    // you just need to select an approproate framerate
    long desiredFPS = 60;
    long desiredTime = (1000)/desiredFPS;
    Color brown = new Color(107, 88, 88);
    boolean a = false;
    boolean d = false;
    boolean jump = false;
    boolean inAir = false;
    ArrayList<Rectangle> blocks = new ArrayList<>();
    ArrayList<Rectangle> ship = new ArrayList<>();
    
    Rectangle player = new Rectangle(300, 100, 80, 110);
    int moveX = 0;
    int moveY = 0;
    int gravity = 1;
    
    BufferedImage jedi = loadImage("PixelArtJedi.png");
    BufferedImage jediLeft = loadImage("PixelArtJediLeft.png");
    BufferedImage jediAttack = loadImage("PixelArtJediAttack.png");
    BufferedImage jediAttackLeft = loadImage("PixelArtJediAttackLeft.png");
    boolean jLeft = false;
    boolean attack = false;
    public BufferedImage loadImage(String filename){
        BufferedImage img = null;
        try{
            img = ImageIO.read(new File(filename));
        }catch(Exception e){
            System.out.println("Error Loading " + filename);
            
        }
        return img;
    }
    // drawing of the game happens in here
    // we use the Graphics object, g, to perform the drawing
    // NOTE: This is already double buffered!(helps with framerate/speed)
    @Override
    public void paintComponent(Graphics g)
    {
        // always clear the screen first!
        g.clearRect(0, 0, WIDTH, HEIGHT);
        
        // GAME DRAWING GOES HERE 
        if(!jLeft && attack){
            g.drawImage(jediAttack, player.x, player.y, player.width, player.height, null);
        }else if(jLeft && attack){
            g.drawImage(jediAttackLeft, player.x, player.y, player.width, player.height, null);
        }else if(!jLeft){
            g.drawImage(jedi, player.x, player.y, player.width, player.height, null);
        }else{
            g.drawImage(jediLeft, player.x, player.y, player.width, player.height, null);
        }
        for (Rectangle block: blocks){
            g.fillRect(block.x, block.y, block.width, block.height);
        }g.setColor(Color.GRAY);
        for (Rectangle block: ship){
            g.fillRect(block.x, block.y, block.width, block.height);
        }
        
        // GAME DRAWING ENDS HERE
    }
    
    
    // The main game loop
    // In here is where all the logic for my game will go
    public void run()
    {
        
        blocks.add(new Rectangle(200, 450, 100, 50));
        blocks.add(new Rectangle(150, 400, 50, 50));
        blocks.add(new Rectangle(100, 350, 50, 50));
        blocks.add(new Rectangle(0, 300, 100, 50));
        
        ship.add(new Rectangle(300, 100, 200, 100));
        // Used to keep track of time used to draw and update the game
        // This is used to limit the framerate later on
        long startTime;
        long deltaTime;
        
        // the main game loop section
        // game will end if you set done = false;
        boolean done = false; 
        while(!done)
        {
            // determines when we started so we can keep a framerate
            startTime = System.currentTimeMillis();
            
            // all your game rules and move is done in here
            // GAME LOGIC STARTS HERE 
            
            if (a){
                moveX = -5;
                jLeft = true;
            }else if (d){
                moveX = 5;
                jLeft = false;
            }else{
                moveX = 0;
            }
            moveY = moveY + gravity;
            
            if (jump && !inAir){
                moveY = -20;
                inAir = true;
            }
            player.x = player.x + moveX;
            player.y = player.y + moveY;
            
            if (player.y + player.height > HEIGHT){
                player.y = HEIGHT - player.height;
                moveY = 0;
                inAir = false;
            }
            if(attack){}
            for (Rectangle block: blocks){
                if (player.intersects(block)){
                    Rectangle intersection = player.intersection(block);
                    if (intersection.width < intersection.height){
                        if (player.x < block.x && jLeft){
                            player.x = player.x - intersection.width;
                        }else{
                            player.x = player.x + intersection.width;
                        }
                    }else{
                        if (player.y > block.y){
                            player.y = player.y + intersection.height;
                            moveY = 0;
                        }else{
                            player.y = player.y - intersection.height;
                            moveY = 0;
                            inAir = false;
                        }
                    }
                }
            }

            // GAME LOGIC ENDS HERE 
            
            // update the drawing (calls paintComponent)
            repaint();
            
            
            
            // SLOWS DOWN THE GAME BASED ON THE FRAMERATE ABOVE
            // USING SOME SIMPLE MATH
            deltaTime = System.currentTimeMillis() - startTime;
            if(deltaTime > desiredTime)
            {
                //took too much time, don't wait
            }else
            {
                try
                {
                    Thread.sleep(desiredTime - deltaTime);
                }catch(Exception e){};
            }
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // creates a windows to show my game
        JFrame frame = new JFrame("My Game");
       
        // creates an instance of my game
        JediAssault game = new JediAssault();
        // sets the size of my game
        game.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        // adds the game to the window
        frame.add(game);
         
        // sets some options and size of the window automatically
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        // shows the window to the user
        frame.setVisible(true);
        frame.addKeyListener(game);
        game.addMouseListener(game);
        // starts my game loop
        game.run();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //create a left right and jump
        if (e.getKeyCode() == KeyEvent.VK_A){
            a = true;
        }else if (e.getKeyCode() == KeyEvent.VK_D){
            d = true;
        }else if (e.getKeyCode() == KeyEvent.VK_SPACE){
            jump = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A){
            a = false;
        }else if (e.getKeyCode() == KeyEvent.VK_D){
            d = false;
        }else if (e.getKeyCode() == KeyEvent.VK_SPACE){
            jump = false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //left click will attack and right will deflect
        if(e.getButton() == MouseEvent.BUTTON1){
            attack = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1){
            attack = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
