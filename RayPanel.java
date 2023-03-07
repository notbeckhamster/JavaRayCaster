import java.awt.*;
import javax.swing.*;
import javax.swing.text.DefaultStyledDocument.ElementSpec;

import java.awt.event.*;
import java.util.random.RandomGeneratorFactory;

public class RayPanel extends JPanel {
   private static double px = 512 / 2, py = 512 / 2;
   private static double pa = 0;
   private static int x0 = 64, y0 = 64;
   private static double xBase = 0, yOpp = 0;
   private static double hrx = 0, hry = 0, vrx = 0, vry = 0;
   private static double distOfRay = 0;
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
         if (pa > 2 * Math.PI) {
            pa = 0.001;
         }
         pa += 0.05;
      } else if (key.equals("right")) {
         if (pa < 0) {
            pa = 2 * Math.PI;
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

      public static double detectHorDis(Graphics g, double ra) {

         int count = 0;
         if (ra < Math.PI / 2 || ra > (3 * Math.PI) / 2) {
            while (count < 12) {
               xBase = (64 - (px % 64)) + x0 * count;
               yOpp = xBase * Math.tan(ra);

              
               hrx = xBase + px;
               hry = py - yOpp;
               int mx = (int) (hrx/ 64.0);
               int my = (int) (hry / 64);
               if ((hrx>0 && hrx<512)&&(hry>0&& hry<512)) {
                  if (gridNum[mx][my] == 1) {
                     

                     count = 11;
                  }
               } else {
                  count = 11;
               }
               
               count++;
            }

         } else if (ra > Math.PI / 2 && ra < (3 * Math.PI) / 2) {
            while (count < 12) {
               xBase = ((px % 64)) + x0 * count;
               if (ra < Math.PI ) {
                  yOpp = xBase * Math.tan(Math.PI - ra);
               } else {
                  yOpp = xBase * Math.tan(ra - Math.PI);
               }

              
               hrx = px - xBase;
                     if (ra > Math.PI/2 && ra < Math.PI) {
                        hry = py - yOpp;
                     } else {
                        hry = py + yOpp;
                     }

                     int mx = (int) ((hrx - 0.001) / 64.0);
                     int my = (int) ((hry - 0.001) / 64);
               if ((hrx>0 && hrx<512)&&(hry>0&&hry<512)){
                  if (gridNum[mx][my] == 1) {
                     count = 11;

                  
               }
            }else{
               count=11;

            }
               
               

               count++;
            }

         }
         return Math.sqrt(Math.pow((px - hrx), 2) + Math.pow((py - hry), 2));
      }

      public static double detectVerDis(Graphics g, double ra) {
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
                  int mx = (int) (Math.floor(vrx / 64));
                  int my;
                  if (ra < Math.PI / 2) {
                     my = (int) (Math.floor((vry - 0.001) / 64));
                  } else {
                     my = (int) (Math.floor(vry / 64));
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
                  int mx = (int) (Math.floor(vrx / 64));
                  int my;
                  if (ra < Math.PI) {
                     my = (int) (Math.floor((vry - 0.01) / 64));
                  } else {
                     my = (int) (Math.floor((vry) / 64));
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

      public static void drawRays(Graphics g) {
         double radPerDeg = (Math.PI) / 360;
         double vertDistance, horzDistance;
         double wallHeight = 0;
         horzDistance = detectHorDis(g, pa);
         vertDistance = detectVerDis(g, pa);
            int count=0;
         for (double ra=pa+64*radPerDeg;ra>pa-64*radPerDeg;ra-=radPerDeg){
            double ta;
            if (ra>2*Math.PI){
               ta=ra-2*Math.PI;
            } else if (ra<0){
               ta=ra+2*Math.PI;
            } else{
               ta=ra;
            }
            horzDistance = detectHorDis(g, ta);
            vertDistance = detectVerDis(g, ta);
            double actualHorzDistance, actualVertDistance;
            if (ra>pa){
            actualHorzDistance = horzDistance*Math.cos(ta-pa);
            actualVertDistance = vertDistance*Math.cos(ta-pa);
            }else{
               actualHorzDistance = horzDistance*Math.cos(pa-ta);
               actualVertDistance = vertDistance*Math.cos(pa-ta);
            }
            if (actualHorzDistance > actualVertDistance) {
               g.setColor(Color.blue);
               g.drawLine((int) px , (int) py, (int) vrx, (int) vry);
               wallHeight=50*(512/actualVertDistance)+50;
            } else if (actualVertDistance > actualHorzDistance) {
               g.setColor(Color.red);
               g.drawLine((int) px, (int) py, (int) hrx, (int) hry);
               wallHeight=50*(512/actualHorzDistance)+50;
            } 
            g.fillRect(512+count*4, (int)(256-wallHeight/2), 4, (int)wallHeight);
            count++;
         }
      
         


      }

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
