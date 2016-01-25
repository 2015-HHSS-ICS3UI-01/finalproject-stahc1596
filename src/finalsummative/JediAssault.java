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
public class JediAssault extends JComponent implements KeyListener, MouseListener {

    // Height and Width of our game
    static final int WIDTH = 800;
    static final int HEIGHT = 600;
    // sets the framerate and delay for our game
    // you just need to select an approproate framerate
    long desiredFPS = 60;
    long desiredTime = (1000) / desiredFPS;
    //the color tan
    Color tan = new Color(240, 194, 165);
    //booleans which determine whether or not the player is moving, jumping, etc
    boolean a = false;
    boolean d = false;
    boolean jump = false;
    boolean jumpR = true;
    boolean eJump = false;
    int inAir = 0;
    //The life counter. You start with 3 lives.
    int life = 3;
    //An array which holds a bunch of certain things like blocks and enemies.
    ArrayList<Rectangle> blocks = new ArrayList<>();
    ArrayList<Rectangle> enemies = new ArrayList<>();
    ArrayList<Rectangle> bullets = new ArrayList<>();
    int[] enemiesAry = new int[11];
    int[] enemiesY = new int[11];
    //This is your player.
    Rectangle player = new Rectangle(400, 100, 40, 55);
    //These integers determine how fast something is moving.
    int moveX = 0;
    int moveY = 0;
    int eMoveX = 0;
    int eMoveY = 0;
    int gravity = 1;
    int shotX = 0;
    //This is the amount of jumps the player takes.
    int jumps = 0;
    //How many kills you have gotten or more accurately how many enemies are on the screen.
    int kill = 1;
    //whether or not the enemy has shot his blaster.
    boolean fire = false;
    boolean shot = false;
    boolean block = false;
    //Every picture used in the game.
    BufferedImage jedi = loadImage("PixelArtJedi.png");
    BufferedImage jediLeft = loadImage("PixelArtJediLeft.png");
    BufferedImage jediAttack = loadImage("PixelArtJediAttack.png");
    BufferedImage jediAttackLeft = loadImage("PixelArtJediAttackLeft.png");
    BufferedImage heart = loadImage("heart.png");
    BufferedImage heartB = loadImage("heartB.png");
    BufferedImage tatooine = loadImage("tatooineBackground.png");
    BufferedImage gameOver = loadImage("gameover.png");
    BufferedImage YouWin = loadImage("YouWin.png");
    //Determines which way the player is facing.
    boolean jLeft = false;
    //Determines whether or not the player is attacking.
    boolean attack = false;

    //Helps load the images on the screen, if they can't be loaded onto the screen
    //then the program will tell me.
    public BufferedImage loadImage(String filename) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(filename));
        } catch (Exception e) {
            System.out.println("Error Loading " + filename);

        }
        return img;
    }
    // drawing of the game happens in here
    // we use the Graphics object, g, to perform the drawing
    // NOTE: This is already double buffered!(helps with framerate/speed)

    @Override
    public void paintComponent(Graphics g) {
        // always clear the screen first!
        g.clearRect(0, 0, WIDTH, HEIGHT);

        // GAME DRAWING GOES HERE 
        //This is the tatooine background.
        g.drawImage(tatooine, 0, 0, WIDTH, HEIGHT, null);

        //These are the platforms that are in the map
        g.setColor(tan);
        for (Rectangle block : blocks) {
            g.fillRect(block.x, block.y, block.width, block.height);

        }
        //These are all the animations of hte player.
        if (!jLeft && attack) {
            g.drawImage(jediAttack, player.x, player.y, player.width, player.height, null);
        } else if (jLeft && attack) {
            g.drawImage(jediAttackLeft, player.x, player.y, player.width, player.height, null);
        } else if (!jLeft) {
            g.drawImage(jedi, player.x, player.y, player.width, player.height, null);
        } else {
            g.drawImage(jediLeft, player.x, player.y, player.width, player.height, null);
        }
        //This is the enemy.
        g.setColor(Color.RED);
        for (Rectangle enemy : enemies) {
            g.fillRect(enemy.x, enemy.y, enemy.width, enemy.height);
        }
        //This is the enemy's bullet.
        g.setColor(Color.GREEN);
        for (Rectangle bullet : bullets) {
            g.fillRect(bullet.x, bullet.y, bullet.width, bullet.height);
        }
        //This is the life counter that you will see at the top left of the screen.
        //If your life is zero then the game over image appears.
        if (life == 3) {
            g.drawImage(heart, 0, 0, 25, 25, null);
            g.drawImage(heart, 30, 0, 25, 25, null);
            g.drawImage(heart, 60, 0, 25, 25, null);
        } else if (life == 2) {
            g.drawImage(heart, 0, 0, 25, 25, null);
            g.drawImage(heart, 30, 0, 25, 25, null);
            g.drawImage(heartB, 60, 0, 25, 25, null);
        } else if (life == 1) {
            g.drawImage(heart, 0, 0, 25, 25, null);
            g.drawImage(heartB, 30, 0, 25, 25, null);
            g.drawImage(heartB, 60, 0, 25, 25, null);
        } else if (life == 0) {
            g.drawImage(heartB, 0, 0, 25, 25, null);
            g.drawImage(heartB, 30, 0, 25, 25, null);
            g.drawImage(heartB, 60, 0, 25, 25, null);
            g.drawImage(gameOver, 0, 0, 800, 600, null);
            kill = -1000;
        }
        //if you get 10 kills, you win the game.
        if (kill == 11) {
            g.drawImage(YouWin, 0, 0, 800, 600, null);
            life = 1000;
        }

        // GAME DRAWING ENDS HERE
    }

    // The main game loop
    // In here is where all the logic for my game will go
    public void run() {

        //These are all the blocks, enemies, and bullets.
        blocks.add(new Rectangle(0, 550, 250, 50));
        blocks.add(new Rectangle(550, 550, 250, 50));
        blocks.add(new Rectangle(0, 500, 200, 50));
        blocks.add(new Rectangle(0, 450, 100, 50));
        blocks.add(new Rectangle(0, 400, 50, 50));
        blocks.add(new Rectangle(600, 500, 200, 50));
        blocks.add(new Rectangle(700, 450, 100, 50));
        blocks.add(new Rectangle(750, 400, 50, 50));
        enemies.add(new Rectangle(-50, 400, 40, 55));
        bullets.add(new Rectangle(-40, 430, 20, 5));
        enemies.add(new Rectangle(850, 400, 40, 55));
        bullets.add(new Rectangle(860, 430, 20, 5));
        enemies.add(new Rectangle(850, 400, 40, 55));
        bullets.add(new Rectangle(860, 430, 20, 5));
        enemies.add(new Rectangle(850, 400, 40, 55));
        bullets.add(new Rectangle(860, 430, 20, 5));
        enemies.add(new Rectangle(850, 400, 40, 55));
        bullets.add(new Rectangle(860, 430, 20, 5));
        enemies.add(new Rectangle(850, 400, 40, 55));
        bullets.add(new Rectangle(860, 430, 20, 5));
        enemies.add(new Rectangle(850, 400, 40, 55));
        bullets.add(new Rectangle(860, 430, 20, 5));
        enemies.add(new Rectangle(850, 400, 40, 55));
        bullets.add(new Rectangle(860, 430, 20, 5));
        enemies.add(new Rectangle(850, 400, 40, 55));
        bullets.add(new Rectangle(860, 430, 20, 5));
        enemies.add(new Rectangle(850, 400, 40, 55));
        bullets.add(new Rectangle(860, 430, 20, 5));
        // Used to keep track of time used to draw and update the game
        // This is used to limit the framerate later on
        long startTime;
        long deltaTime;

        // the main game loop section
        // game will end if you set done = false;
        boolean done = false;
        while (!done) {
            // determines when we started so we can keep a framerate
            startTime = System.currentTimeMillis();

            // all your game rules and move is done in here
            // GAME LOGIC STARTS HERE 
            //depending on what button you press, your player will move that way.
            if (a) {
                moveX = -3;
                jLeft = true;
            } else if (d) {
                moveX = 3;
                jLeft = false;
            } else {
                moveX = 0;
            }
            //This is the force of gravity that affects your player.
            moveY = moveY + gravity;
            //This is the jump that your player has, you can double jump.
            if (jump && inAir != 2 && jumpR) {
                jumpR = false;
                moveY = -15;
                inAir++;
            }
            //Then your player officially moves in which ever direction you wanted
            //him to move.
            player.x = player.x + moveX;
            player.y = player.y + moveY;
            //This keeps your player from falling off the bottom of the screen.
            if (player.y + player.height > HEIGHT) {
                player.y = HEIGHT - player.height;
                moveY = 0;
                inAir = 0;
                //This keeps your player from moving off the map whether it's to the
                //right or left of the screen.
            }
            if (player.x < 0) {
                player.x = 0;
            } else if (player.x > 760) {
                player.x = 760;
            }
            //This is everything that the enemy does.
            for (int i = 0; i < kill; i++) {
                Rectangle enemy = enemies.get(i);
                //Depending on where the player is, the enemy will move to a certain spot.
                if (enemy.x > player.x + 250) {
                    eMoveX = - 3;
                } else if (enemy.x < player.x - 250) {
                    eMoveX = 3;
                } else if (!shot && enemy.x > player.x - 250 && enemy.x < player.x + 250) {
                    eMoveX = 0;
                    //If the enemy is in range, he will shoot at the player.
                    shot = true;
                } else {
                    eMoveX = 0;
                }
                //The enemy officially moves to where he wants to go.
                enemy.x = enemy.x + eMoveX;
                //Enemy is also affected by gravity.
                enemiesY[i] = enemiesY[i] + gravity;
                enemy.y = enemy.y + enemiesY[i];
                //This keeps the enemy from falling off the screen just like the player.
                if (enemy.y + enemy.height > HEIGHT) {
                    enemy.y = HEIGHT - enemy.height;
                    enemiesY[i] = 0;
                }
                //And this keeps the enemy from moving outside of the screen.
                if (enemy.x < 0 && eMoveX < 0) {
                    enemy.x = 0;
                } else if (enemy.x > 760 && eMoveX > 0) {
                    enemy.x = 760;
                }
                Rectangle bullet = bullets.get(i);
                //This keeps the bullet with the enemy unless he shot the bullet.
                if (!shot) {
                    bullet.x = enemy.x + 10;
                    bullet.y = enemy.y + 30;
                } //If he shot the bullet it will move until it hits the edge of the screen.
                else if (shot && enemy.x > player.x && !fire) {
                    shotX = -5;
                    fire = true;
                } else if (shot && enemy.x < player.x && !fire) {
                    shotX = 5;
                    fire = true;
                }
                //This is where the bullet officially moves.
                bullet.x = bullet.x + shotX;
                //This stops the bullet from actually going off screen and returns
                //to the enemy.
                if (bullet.x <= 0 || bullet.x >= 800) {
                    shot = false;
                    fire = false;
                    bullet.x = enemy.x + 10;
                    bullet.y = enemy.y + 30;
                    shotX = 0;
                }
                //If the bullet hits the player, the player loses a life, and the bullet
                //returns to the enemy.
                if (bullet.intersects(player)) {
                    if (!block) {
                        life = life - 1;
                        shot = false;
                        fire = false;
                        bullet.x = enemy.x + 10;
                        bullet.y = enemy.y + 30;
                    } else if (block) {
                        shotX = shotX * -1;
                    }

                }
            }
            //if the player attacks and hits an enemy, he dies and two more spawn in.
            //Kind of like a hydra.
            if (attack) {
                for (Rectangle enemy : enemies) {
                    if (player.intersects(enemy)) {
                        Rectangle intersection = player.intersection(enemy);
                        if (enemy.x + 20 > player.x + 20 && !jLeft) {
                            enemy.x = -100;
                            kill++;
                        } else if (enemy.x + 20 < player.x + 20 && jLeft) {
                            enemy.x = -100;
                            kill++;
                        }
                    }
                }

            }
            //This keeps the player from merging into the blocks.
            for (Rectangle block : blocks) {
                if (player.intersects(block)) {
                    Rectangle intersection = player.intersection(block);
                    if (intersection.width < intersection.height) {
                        if (player.x < block.x && !jLeft) {
                            player.x = player.x - intersection.width - 2;
                        } else if (player.x > block.x && jLeft) {
                            player.x = player.x + intersection.width + 2;
                        }

                    } else {
                        if (player.y > block.y) {
                            player.y = player.y + intersection.height;
                            moveY = 0;
                        } else {
                            player.y = player.y - intersection.height;
                            moveY = 0;
                            inAir = 0;
                        }
                    }

                }
                //This keeps the enemy from merging with the blocks
                for (int i = 0; i < kill; i++) {
                    Rectangle enemy = enemies.get(i);
                    if (enemy.intersects(block)) {
                        Rectangle intersection = enemy.intersection(block);
                        if (intersection.width < intersection.height) {
                            //The enemy actually climbs over blocks instead of being
                            //stopped by them or jumping on them.
                            if (enemy.x < block.x) {
                                enemy.y = enemy.y - intersection.height;
                            } else {
                                enemy.y = enemy.y - intersection.height;
                            }

                        } else {
                            if (enemy.y > block.y) {
                                enemy.y = enemy.y + intersection.height;
                                enemiesY[i] = 0;
                            } else if (enemy.y < block.y) {
                                enemy.y = enemy.y - intersection.height;
                                enemiesY[i] = 0;
                            }
                        }
                    }

                }
                //This keeps the enemies from merging with each other.
                for (int i = 0; i < kill; i++) {
                    for (int u = 0; u < enemies.size(); u++) {
                        Rectangle enemy1 = enemies.get(i);
                        Rectangle enemy2 = enemies.get(u);
                        if (i == u) {

                        } else if (enemy1.intersects(enemy2)) {
                            Rectangle intersection = enemy1.intersection(enemy2);
                            if (intersection.width < intersection.height) {
                                if (enemy1.x < enemy2.x) {
                                    enemy1.x = enemy1.x - intersection.width;
                                } else {
                                    enemy1.x = enemy1.x + intersection.width;
                                }
                            } else {
                                if (enemy1.y > enemy2.y) {
                                    enemy1.y = enemy1.y + intersection.height;
                                    enemiesY[i] = 0;
                                } else if (enemy1.y < enemy2.y) {
                                    enemy1.y = enemy1.y - intersection.height;
                                    enemiesY[i] = 0;
                                }
                            }
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
            if (deltaTime > desiredTime) {
                //took too much time, don't wait
            } else {
                try {
                    Thread.sleep(desiredTime - deltaTime);
                } catch (Exception e) {
                };
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
        game.setPreferredSize(new Dimension(WIDTH, HEIGHT));
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

    //These are all the controls, they include movement, attacks, jumps, etc.
    //They also involve the keyboard and mouse.

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //create a left right and jump
        if (e.getKeyCode() == KeyEvent.VK_A) {
            a = true;
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            d = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            jump = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A) {
            a = false;
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            d = false;
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
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
        if (e.getButton() == MouseEvent.BUTTON1) {
            attack = true;
        } else if (e.getButton() == MouseEvent.BUTTON2) {
            block = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            attack = false;
        } else if (e.getButton() == MouseEvent.BUTTON2) {
            block = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
