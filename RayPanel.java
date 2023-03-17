import java.awt.*;
import javax.swing.*;
import java.awt.event.*;


public class RayPanel extends JPanel {
   private static double px = 512 / 2, py = 512 / 2;
   private static double pa = 0;
   private static int blockSize = 64;
   private static double xBase = 0, yOpp = 0;
   private static double hrx = 0, hry = 0, vrx = 0, vry = 0;
   private static double distOfRay = 0;
   private final static int playerHeight = 32;
   private final static int projectionPlaneX = 512;
   private final static int projectionPlaneY = 512-64;
   private final static int fieldOfView = 128;
   private final static double radPerDeg = (Math.PI)/360.0;
   private final static double fieldOfViewRads = 128*radPerDeg;
   private final static double angleBetweenEachRay = fieldOfView/(double)projectionPlaneX;
   private final static double angleBetweenEachRayRad = angleBetweenEachRay*radPerDeg;
   private final static int projectionPlaneDistanceFromPlayer = (int)((projectionPlaneX/2)*Math.tan(fieldOfViewRads));
   private JPanel leftPanel = new JPanel(), rightPanel = new JPanel();
   private static int[][] gridNum = { 
         { 1, 1, 1, 1, 1, 1, 1, 1 },
         { 1, 0, 0, 0, 0, 0, 0, 1 },
         { 1, 0, 0, 0, 0, 0, 0, 1 },
         { 1, 0, 0, 0, 0, 0, 0, 1 },
         { 1, 0, 0, 0, 0, 0, 0, 1 },
         { 1, 0, 0, 0, 0, 0, 0, 1 },
         { 1, 0, 0, 0, 0, 0, 0, 1 },
         { 1, 1, 1, 1, 1, 1, 1, 1 }, };

   public RayPanel() {
      Timer timerObj = new Timer(1000 / 60, new timerListener());
      timerObj.start();
      leftPanel = new GridPanel();
      add(leftPanel);

   }

   private class timerListener implements ActionListener {
      public void actionPerformed(ActionEvent ae) {
         repaint();

      }
   }

   public static void movePlayer(String key) {
      double bpx=px;
      double bpy=py;
   
      if (key.equals("up")) {
         px += (Math.ceil(5 * Math.cos(pa)));
         py -= (Math.ceil(5 * Math.sin(pa)));
      } else if (key.equals("down")) {
         px -= (Math.ceil(5 * Math.cos(pa)));
         py += (Math.ceil(5 * Math.sin(pa)));
      } else if (key.equals("left")) {
         if (pa > 2*Math.PI) {
            pa = 2 * Math.PI;
         }
         pa += 0.05;
      } else if (key.equals("right")) {
         if (pa < 0 ) {
            pa += 2*Math.PI;
         }
         pa -= 0.05;
      }
      int my= (int)Math.floor((py-20)/64);
      int mx= (int)Math.floor(px/64);
      if (gridNum[my][mx]==1){
         if(py<bpy){
            py=bpy;
         }
      }
      my= (int)Math.floor(py/64);
      mx= (int)Math.floor((px-20)/64);
      if (gridNum[my][mx]==1){
         if(px<bpx){
            px=bpx;
         }
      }


      my= (int)Math.floor((py+20)/64);
      mx= (int)Math.floor(px/64);
      if (gridNum[my][mx]==1){
         if(py>bpy){
            py=bpy;
         }
      } 

      my= (int)Math.floor(py/64);
      mx= (int)Math.floor((px+20)/64);
      if (gridNum[my][mx]==1){
         if(px>bpx){
            px=bpx;
         }
      } 
   }



   private class GridPanel extends JPanel {
      public GridPanel() {
         setPreferredSize(new Dimension(512*2, 512));

      }

      public void paintComponent(Graphics g) {
         drawGrid(g);
         drawPlayer(g);
         drawRays(g);
      }

      public static int[] detectHorDis(Graphics g, double rayAngle) {

         boolean isUp = rayAngle < Math.PI;
         int firstIntersectionY = isUp ? (int)(Math.floor(py/64)*64-1) : (int)(Math.floor(py/64)*64+64);
         int firstIntersectionX = (int)(px + (py-firstIntersectionY)/Math.tan(rayAngle));
         int[] curr = new int[]{firstIntersectionX, firstIntersectionY};
         int offsetY = isUp ? -blockSize : blockSize;
         int offsetX = (int)(blockSize/Math.tan(rayAngle));

         while (true){
            if (((curr[0]<0 || curr[0]>512) || (curr[1]<0 || curr[1]>512))) return new int[]{0,0};
            if (gridNum[curr[0]/64][curr[1]/64] == 1) return new int[]{curr[0], curr[1]};
            curr[0] += offsetX;
            curr[1] += offsetY;
         }

      }

      /* public static double detectVerDis(Graphics g, double ra) {
         int count = 0;
         if (ra < Math.PI / 2 || ra > 3 * Math.PI / 2) {
            while (count < 12) {

               if (ra < Math.PI / 2) {
                  yOpp = py % 64 + count * y0;
                  xBase = yOpp / Math.tan(ra);
                  vry = (py - yOpp);
               } else {
                  yOpp = (64 - py % 64) + count * y0;
                  xBase = yOpp / Math.tan(2 * Math.PI - ra);
                  vry = (py + yOpp);
               }
               vrx = (px + xBase);
               if ((vrx > 0 && vrx < 512) && (vry > 0 && vry < 512)) {
                  int mx = (int) (Math.floor(vrx / 64.0));
                  int my;
                  if (ra < Math.PI / 2) {
                     my = (int) (Math.floor((vry - 0.001) / 64.0));
                  } else {
                     my = (int) (Math.floor(vry / 64.0));
                  }
                 

                     if (gridNum[mx][my] == 1) {

                        count = 11;
                     }
                  
               } else{
                  count=11;
               }
            
               count++;
            }
         } else if (ra > Math.PI / 2 && ra < 3 * Math.PI / 2) {
            while (count < 12) {

               if (ra > Math.PI / 2 && ra < Math.PI) {
                  yOpp = py % 64 + count * y0;
                  xBase = yOpp / Math.tan(Math.PI - ra);
                  vry = (py - yOpp);
               } else {
                  yOpp = (64 - py % 64) + count * y0;
                  xBase = yOpp / Math.tan(ra - Math.PI);
                  vry = (py + yOpp);
               }
               vrx = (px - xBase);
               if ((vrx > 0 && vrx < 512) && (vry > 0 && vry < 512)) {
                  int mx = (int) (Math.floor(fg / 64.0));
                  int my;
                  if (ra < Math.PI) {
                     my = (int) (Math.floor((vry - 0.01) / 64.0));
                  } else {
                     my = (int) (Math.floor((vry) / 64.0));
                  }
                  
                     if (gridNum[mx][my] == 1) {
                        count = 11;
                     }
                 
               } else{
                  count=11;
               }
               count++;
            }
         }

         return Math.sqrt(Math.pow((px - vrx), 2) + Math.pow((py - vry), 2));
      }
 */
      public static void drawRays(Graphics g) {
         int[] horzDistance;
         horzDistance = detectHorDis(g, pa);
         //vertDistance = detectVerDis(g, pa);

         g.setColor(Color.blue);
         g.drawLine((int) px , (int) py, (int) horzDistance[0], (int) horzDistance[1]);
         g.setColor(Color.green);
         Graphics2D g2d = (Graphics2D) g;
         g2d.setStroke(new BasicStroke(2.0F));
         g2d.drawLine((int) px , (int) py, (int) (px + 30*Math.cos(pa)), (int) (py- 30*Math.sin(pa)));

         }
 
         


      

   

   public static void drawPlayer(Graphics g) {
      g.setColor(Color.red);
      g.fillRect((int) px, (int) py, 5, 5);

   }
   
   /** 
    * This Method draws the grid based on 

    */
   public static void drawGrid(Graphics g) {
      int xCord = 0;
      int yCord = 0;
      for (int i = 0; i < gridNum.length; i++) {
         for (int j = 0; j < gridNum[i].length; j++) {
            if (gridNum[i][j] == 1) {
               g.setColor(Color.black);
               g.fillRect(xCord, yCord, 63 - 1, 63);
            } else if (gridNum[i][j] == 0) {
               g.setColor(Color.gray);
               g.fillRect(xCord, yCord, 63, 63);
            }

            xCord += 512 / 8;
         }
         yCord += 512 / 8;
         xCord = 0;
      }
   }
}
}