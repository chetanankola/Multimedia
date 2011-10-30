
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ankola
 */
class Player implements Runnable {
    
    
    static JFrame MainFrame = new JFrame();
    //static JInternalFrame iframe = new JInternalFrame();
    static JLabel videolabel = new JLabel();
    static JToolBar buttonBar = new JToolBar();
    static String PlayButtonFilename = "Button-Play-icon.jpg";
    static String PauseButtonFilename = "Button-Pause-icon.jpg";
    static String StopButtonFilename = "Button-Stop-icon.jpg";
    
    
    static void setImageinFrame()
    {
        
    }
    static void Initialize() throws IOException { //this will be called from main class..

        //MainFrame.getContentPane().setLayout(new GridBagLayout());
        MainFrame.getContentPane().add(videolabel, BorderLayout.CENTER); 
        
        
        File file = new File(PlayButtonFilename);
	BufferedImage playimage = ImageIO.read(file);
        //Image scaledimage = playimage.getScaledInstance( , 10,  java.awt.Image.SCALE_SMOOTH ) ; 
        ImageIcon playicon = new ImageIcon(playimage);
       
        JButton playbutton = new JButton();
        playbutton.setIcon(playicon);   
        
        MainFrame.getContentPane().add(playbutton, BorderLayout.NORTH); 
       
        
        MainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        MainFrame.setTitle("CHEMACHA PLAYER");
        MainFrame.setResizable(true);
       // MainFrame.setLocationRelativeTo(null);
        MainFrame.pack();
        MainFrame.setSize(800, 600);
        MainFrame.setVisible(true);
        //frame.setLayout(null); //check LayoutManager
    }
    
    
    @Override
    public void run() {
       /* if(curframenum==1000)
        {
            BufferedImage testimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            getframe(1000, testimg); 
            JLabel label2 = new JLabel();
            label2.setIcon(new ImageIcon(testimg));
            //frame.getContentPane().add(label, BorderLayout.CENTER);
            frame.getContentPane().add(label2, BorderLayout.EAST);
            
        }
        
        */
    }
    
    
    
}
