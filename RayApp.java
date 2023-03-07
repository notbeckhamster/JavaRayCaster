
import javax.swing.*;
import java.awt.event.*;
public class RayApp{
   public static void main(String[] args){
      JFrame frame = new JFrame("RayCastTest");
      //Determines what happens when close button is pressed
      frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      //Adds the Ray Panel to frame object
      frame.getContentPane().add(new RayPanel());
      //Auto size the frame
      frame.pack();
      //Ensure the frame is visible 
      frame.setVisible(true);
      
      //Add key listener to the frame
      frame.addKeyListener(new KeyListener() {
   
    public void keyPressed(KeyEvent e) {       
         if (e.getKeyCode() == 38){
            RayPanel.movePlayer("up");
         } else if (e.getKeyCode() == 40){
            RayPanel.movePlayer("down");
         } else if (e.getKeyCode() == 39){
            RayPanel.movePlayer("right");
         }else if (e.getKeyCode() == 37){
            RayPanel.movePlayer("left");
         }
    
     }

    public void keyReleased(KeyEvent e) { }

    public void keyTyped(KeyEvent e) {  }
});
   
   }
}