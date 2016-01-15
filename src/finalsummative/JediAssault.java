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
    Color tan = new Color(240, 194, 165);
    boolean a = false;
    boolean d = false;
    boolean jump = false;
    boolean jumpR = true;
    int inAir = 0;
    int life = 3;
    ArrayList<Rectangle> blocks = new ArrayList<>();
    ArrayList<Rectangle> ship = new ArrayList<>();
    
    Rectangle player = new Rectangle(400, 100, 80, 110);
    Rectangle enemy = new Rectangle(100, 100, 80, 100);
    int moveX = 0;
    int moveY = 0;
    int eMoveX = 0;
    int eMoveY = 0;
    int gravity = 1;
    int distance = 0;
    boolean eDirectionLeft = false;
    
    BufferedImage jedi = loadImage("PixelArtJedi.png");
    BufferedImage jediLeft = loadImage("PixelArtJediLeft.png");
    BufferedImage jediAttack = loadImage("PixelArtJediAttack.png");
    BufferedImage jediAttackLeft = loadImage("PixelArtJediAttackLeft.png");
    BufferedImage heart = loadImage("heart.png");
    BufferedImage heartB = loadImage("heartB.png");
    BufferedImage tatooine = loadImage("tatooineBackground.png");
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
        g.drawImage(tatooine, 0, 0, WIDTH, HEIGHT, null);
        
        g.setColor(tan);
        for (Rectangle block: blocks){
            g.fillRect(block.x, block.y, block.width, block.height);
            
        }
                
        if(!jLeft && attack){
            g.drawImage(jediAttack, player.x, player.y, player.width, player.height, null);
        }else if(jLeft && attack){
            g.drawImage(jediAttackLeft, player.x, player.y, player.width, player.height, null);
        }else if(!jLeft){
            g.drawImage(jedi, player.x, player.y, player.width, player.height, null);
        }else{
            g.drawImage(jediLeft, player.x, player.y, player.width, player.height, null);
        }
        g.setColor(Color.RED);
        g.fillRect(enemy.x, enemy.y, enemy.width, enemy.height);
        
        if(life == 3){
            g.drawImage(heart, 0, 0, 25, 25, null);
            g.drawImage(heart, 30, 0, 25, 25, null);
            g.drawImage(heart, 60, 0, 25, 25, null);
        }else if (life == 2){
            g.drawImage(heart, 0, 0, 25, 25, null);
            g.drawImage(heart, 30, 0, 25, 25, null);
            g.drawImage(heartB, 60, 0, 25, 25, null);
        }else if (life == 1){
            g.drawImage(heart, 0, 0, 25, 25, null);
            g.drawImage(heartB, 30, 0, 25, 25, null);
            g.drawImage(heartB, 60, 0, 25, 25, null);
        }else{
            g.drawImage(heartB, 0, 0, 25, 25, null);
            g.drawImage(heartB, 30, 0, 25, 25, null);
            g.drawImage(heartB, 60, 0, 25, 25, null);
        }
        
        g.setColor(Color.GRAY);
        for (Rectangle block: ship){
            g.fillRect(block.x, block.y, block.width, block.height);
        }
        
        // GAME DRAWING ENDS HERE
    }
    
    
    // The main game loop
    // In here is where all the logic for my game will go
    public void run()
    {
        
        blocks.add(new Rectangle(550, 550, 100, 50));
        blocks.add(new Rectangle(150, 550, 100, 50));
        blocks.add(new Rectangle(650, 500, 50, 100));
        blocks.add(new Rectangle(700, 450, 100, 150));
        blocks.add(new Rectangle(250, 350, 300, 50));
        blocks.add(new Rectangle(100, 500, 50, 100));
        blocks.add(new Rectangle(0, 450, 100, 150));
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
            if (player.x != 400){
                player.x = 400;
            }
            if (a){
                moveX = 5;
                jLeft = true;
            }else if (d){
                moveX = -5;
                jLeft = false;
            }else{
                moveX = 0;
            }
            moveY = moveY + gravity;
            eMoveY = eMoveY + gravity;
            
            if (jump && inAir != 2 && jumpR){
                jumpR = false;
                moveY = -20;
                inAir++;
            }
            
            player.y = player.y + moveY;
            
            if (player.y + player.height > HEIGHT){
                player.y = HEIGHT - player.height;
                moveY = 0;
                inAir = 0;
            }
            
           if (enemy.x > player.x){
               distance = enemy.x - player.x;
               eDirectionLeft = true;
           }else if (enemy.x < player.x){
               distance = enemy.x + player.x;
               eDirectionLeft = false;
           }if (eDirectionLeft && distance > 100){
               eMoveX = - 5;
           }else if (!eDirectionLeft && distance < 100){
               eMoveX = 5;
           }else{
               eMoveX = 0;
           }
            enemy.x = enemy.x + moveX;
            for (Rectangle block: blocks){
                block.x = block.x + moveX;
            }
            
            if(attack){}
            for (Rectangle block: blocks){
                if (player.intersects(block)){
                    Rectangle intersection = player.intersection(block);
                    if (intersection.width < intersection.height){
                        if (player.x < block.x && !jLeft){
                            moveX = intersection.width;
                        }else{
                            moveX = - intersection.width;
                        }
                        
                        for (Rectangle b: blocks){
                        b.x = b.x + moveX;
                    }
                    }else{
                        if (player.y + 85 > block.y){
                            
                        }else{
                            player.y = player.y - intersection.height;
                            moveY = 0;
                            inAir = 0;
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
        }
         if (e.getKeyCode() == KeyEvent.VK_SPACE){
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
            jumpR = true;
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
