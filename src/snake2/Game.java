package snake2;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author Iara
 */

public class Game extends Canvas implements Runnable, KeyListener {
    
    public Node[] nodeSnake = new Node[40]; // Tamanho do corpo da cobra
    
    public boolean left, right, up,down; //Direção da cobra 
    
    public int score = 0;// total de pontos baseados na quantidade de macas
    
    public int macaX = 0, macaY  = 0;
    
    public int spd = 1; 
    
    public Game(){
        this.setPreferredSize(new Dimension(480, 480));
        for(int i =0; i < nodeSnake.length; i++){
            nodeSnake[i] = new Node(0,0);
        }
        this.addKeyListener(this);
    }
    
    public void tick(){
        
        for(int i = nodeSnake.length - 1; i > 0; i--){ // Aqui faz o rabo seguir o corpo
            nodeSnake[i].x = nodeSnake[i-1].x;
            nodeSnake[i].y = nodeSnake[i-1].y;
        }
        
        if(nodeSnake[0].x + 10 < 0){ //Se a cobra sair da tela ela volta do outro lado
            nodeSnake[0].x = 480;
        } else if(nodeSnake[0].x >= 480){
            nodeSnake[0].x = -10;
        }
        
        if(nodeSnake[0].y + 10 < 0){
            nodeSnake[0].y = 480;
        } else if(nodeSnake[0].y >= 480){
            nodeSnake[0].y = -10; 
        }
      
        
        // Caminhos que a cobra vai seguir 
        if(right){
            nodeSnake[0].x+=spd;
        } else if(up){
            nodeSnake[0].y-=spd;
        } else if(left){
            nodeSnake[0].x-=spd;
        }  else if (down){
            nodeSnake[0].y+=spd;
        }
        
        //todo momento que houver uma colisão
        
        if(new Rectangle(nodeSnake[0].x,nodeSnake[0].y,10,10).intersects(new Rectangle(macaX,macaY,10,10))){
            macaX = new Random().nextInt(480-10); //Para não passar do mapa
            macaY = new Random().nextInt(480-10);
            score++;
            spd++;
            System.out.println("Pontos "+ score);
        }
    }
    
    public void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, 480, 480);
        for(int i = 0; i < nodeSnake.length; i++){
            g.setColor(Color.BLUE);
            g.fillRect(nodeSnake[i].x, nodeSnake[i].y, 10, 10);
        }
        
        g.setColor(Color.RED);
        g.fillRect(macaX, macaY, 10, 10);
        
        g.dispose();
        bs.show();
    }
    
    public static void main(String args[]) {
        Game game = new Game();
        JFrame frame = new JFrame("Snake");
        frame.add(game);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
        new Thread(game).start();
        
    }
    
    
    @Override
    public void run() {
        while(true) {
            tick();
            render();
            try {
                Thread.sleep(1000/60);
            } catch (InterruptedException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) { // o evento é assionado quando as teclas são pressionadas
        if (e.getKeyCode() == KeyEvent.VK_RIGHT){
            right = true;
            left = false;
            down = false;
            up = false;
        } else if(e.getKeyCode() == KeyEvent.VK_LEFT){
            left = true;
            right = false;
            up = false;
            down = false;
        } else if(e.getKeyCode() == KeyEvent.VK_UP) {
            up = true;
            right = false;
            left = false;
            down = false;
        } else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
            down = true;
            right = false;
            left = false;
            up = false;
        } 
    }

    @Override
    public void keyReleased(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
