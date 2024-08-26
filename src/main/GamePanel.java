package main;
import javax.swing.JPanel;

import Entity.Player;

import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    //screen setting
    final int originalTileSize  = 16; //16x16 la tiêu chuản bit đồ họa
    final int scale = 3;
    //tang kich thuoc cho bit vi 16 la qua nho mang hinh hien dai
    public final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;

    final int screenWidth = maxScreenCol * tileSize;  //768 pixels
    final int screenHeight = maxScreenRow * tileSize; //576 pixels


    //FPS
    int FPS = 60;

    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;


    Player player = new Player(this, keyHandler);

    //set player default position
    // int playerX = 100;
    // int playerY = 100;
    // int playerSpeed = 4;

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true); 
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }   
    //(Sleep) method
    // @Override
    // public void run() {
    //     
    //     double drawInterval = 1000000000 / FPS; 
    //     double nextDrawTime = System.nanoTime() + drawInterval;

    //     while (gameThread != null) {                  

    //         // 1 update character position
    //         update();
    //         // 2 draw the screen with the updated position           
    //         repaint();
            
    //         try{
    //             double remainingTime = nextDrawTime - System.nanoTime();
    //             //chuyển đổi từ nano sang mili
    //             remainingTime = remainingTime / 1000000;
    //             //nếu remainingTime < 0 thì đặt lại thành 0
    //             if(remainingTime < 0){
    //                 remainingTime = 0;
    //             }
    //             //khoản dừng đến khi đến thời điểm nextDrawTime
    //             Thread.sleep((long) remainingTime);
    //             nextDrawTime += drawInterval;
    //         }catch(InterruptedException e){
    //             e.printStackTrace();
    //         }
    //     }

    // }

    //(Delta/Accumulator) method
    @Override
    public void run() {
        // Tính toán khoảng thời gian giữa mỗi lần vẽ (drawInterval) dựa trên FPS mong muốn
        double drawInterval = 1000000000 / FPS; 
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;
        
        while (gameThread != null) {    
            // Lấy thời gian hiện tại
            currentTime = System.nanoTime();
            
            // Tính toán delta (thời gian trôi qua kể từ lần update/draw cuối cùng)
            delta += (currentTime - lastTime) / drawInterval;
            
            // Cộng dồn thời gian cho bộ đếm FPS
            timer += (currentTime - lastTime);
            
            // Cập nhật lastTime cho vòng lặp tiếp theo
            lastTime = currentTime;

            // Nếu đã đủ thời gian cho một frame
            if(delta >= 1){
                // Cập nhật trạng thái game
                update();
                // Vẽ lại màn hình
                repaint();
                // Giảm delta đi 1 đơn vị
                delta--;
                // Tăng số lượng frame đã vẽ
                drawCount++;
            }
    
            // Nếu đã trôi qua 1 giây
            if(timer >= 1000000000){
                // In ra số FPS (số frame đã vẽ trong 1 giây)
                System.out.println("FPS: " + drawCount);
                // Reset bộ đếm frame và timer
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update(){
        player.update();
        
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        // g2.setColor(Color.WHITE);
        // g2.fillRect(player.x, player.y, tileSize,tileSize);
        player.draw(g2);
        g2.dispose();
    }
}
