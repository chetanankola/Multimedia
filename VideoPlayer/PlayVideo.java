
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ankola
 */
public class PlayVideo implements Runnable{
    
    public String filename;
    public int width;
    public int height;
    public int framerate;
    public int totalnumofframes;
    public BufferedImage VideoFrame[]; 
    public long filelen;
    public int sizeofframe;
    public int startframe;
    public int endframe;
    public  BufferedImage img;
    //public JFrame frame; //will now be run on static class Player.frame
    public JLabel label;
    

    /* Constructor
     * 
     */
    
    //DEFAULT CONSTRUCTOR
    private int chapdispframenum;
    private int funcnum;
    private int labelnumber;
    

    
    
      public PlayVideo(String filename,int width, int height) {
	this.filename = filename;
        this.width = width;
        this.height = height;  
        this.sizeofframe=height*width*3;   
        File tempfile = new File(filename);
        this.filelen = tempfile.length();
        this.totalnumofframes=(int) (filelen /sizeofframe);
        this.img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.label = new JLabel();
        this.startframe = 1;
        this.endframe = totalnumofframes;
        
      }
    //CONSTRUCTOR
    public PlayVideo(String filename,int width, int height,int startframe, int endframe ) {
	this.filename = filename;
        this.width = width;
        this.height = height;  
        this.sizeofframe=height*width*3;   
        File tempfile = new File(filename);
        this.filelen = tempfile.length();
        this.totalnumofframes=(int) (filelen /sizeofframe);
        this.img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.label = new JLabel();

        this.startframe = startframe;
        this.endframe = endframe;
      /* VideoFrame = new BufferedImage[30];
       for(int i=0;i<30;i++)
       {
           VideoFrame[i] = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
       }
         
         */
    }
    
    
    @Override
    public void run() {
        
         try {
              play();
           } catch (InterruptedException ex) {
                Logger.getLogger(PlayVideo.class.getName()).log(Level.SEVERE, null, ex);
            }    

    }
public void getframe(int curframenum,BufferedImage retimg) throws FileNotFoundException, IOException
{
           File file = new File(filename);
           InputStream is = new FileInputStream(file);
           int seeknumRead=0;
           long frameoffset = curframenum*sizeofframe;
           is.skip(frameoffset);
           
           
           int numRead = 0;
           int k = 0;
           byte[] bytes = new byte[(int)sizeofframe];
            if((numRead=is.read(bytes,  0, sizeofframe)) >=0) {
                     // VideoFrame[framenum] = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

                        for(int y = 0; y < height; y++){

                            for(int x = 0; x < width; x++){
                                byte a = 0;
                                byte r = bytes[k];
                                byte g = bytes[k+height*width];
                                byte b = bytes[k+height*width*2]; 
                                int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
                                //int pix = ((a << 24) + (r << 16) + (g << 8) + b);
                                retimg.setRGB(x,y,pix);             
                                k++;
                            }//end of for x
                        }//end of for y
                }//end of if    
            
            else
            {
                System.out.println("Something went wrong. number of bytes read = numRead= "+numRead);
            }
            
            
            
            
}


public void displaychapter(int framenum, int labelnum) throws IOException
{
    
         chapdispframenum = framenum;
         labelnumber = labelnum;
         
         
           BufferedImage testimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
           File file = new File(filename);
           InputStream is = new FileInputStream(file);
           int seeknumRead=0;
           long frameoffset = chapdispframenum*sizeofframe;
           is.skip(frameoffset);
           
           
           int numRead = 0;
           int k = 0;
           byte[] bytes = new byte[(int)sizeofframe];
            if((numRead=is.read(bytes,  0, sizeofframe)) >=0) {
                     // VideoFrame[framenum] = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

                        for(int y = 0; y < height; y++){

                            for(int x = 0; x < width; x++){
                                byte a = 0;
                                byte r = bytes[k];
                                byte g = bytes[k+height*width];
                                byte b = bytes[k+height*width*2]; 
                                int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
                                //int pix = ((a << 24) + (r << 16) + (g << 8) + b);
                                testimg.setRGB(x,y,pix);             
                                k++;
                            }//end of for x
                        }//end of for y
                                   
          // ImageIcon imgicon = new ImageIcon(testimg); 
         

              Image simg = testimg.getScaledInstance( 100, 100,  java.awt.Image.SCALE_SMOOTH ) ;
              ImageIcon imgiconscaled = new ImageIcon(simg);
 

                int chapnum = framenum;//(framenum/Vplayer.chapterspan);
                int untilchapnum = framenum+(Vplayer.chapterspan-1);
                
                
                switch(labelnumber)
                {
                    
                    case 1:Vplayer.chapLabel1.setIcon(imgiconscaled); 
                           Vplayer.jFormattedTextField1.setText(""+chapnum+"-"+untilchapnum);
                           Vplayer.chapLabel1_frameno = framenum;    break;
                        
                        
                    case 2:Vplayer.chapLabel2.setIcon(imgiconscaled);  
                           Vplayer.chapLabel2_frameno = framenum;  
                           Vplayer.jFormattedTextField2.setText(""+chapnum+"-"+untilchapnum);
                           break;
                    case 3:Vplayer.chapLabel3.setIcon(imgiconscaled);  
                           Vplayer.chapLabel3_frameno = framenum;   
                           Vplayer.jFormattedTextField3.setText(""+chapnum+"-"+untilchapnum);
                           break;
                        
                    case 4:Vplayer.chapLabel4.setIcon(imgiconscaled);  
                           Vplayer.chapLabel4_frameno = framenum; 
                           Vplayer.jFormattedTextField4.setText(""+chapnum+"-"+untilchapnum);
                           break;
                        
                    case 5:Vplayer.chapLabel5.setIcon(imgiconscaled); 
                           Vplayer.chapLabel5_frameno = framenum;  
                           Vplayer.jFormattedTextField5.setText(""+chapnum+"-"+untilchapnum);
                           break;
                    //case 6:Vplayer.videolabel.setIcon(imgicon);      break; //this one is for main frame
                    default: break;
                }       
                        
            }//end of if    
            
            else
            {
                System.out.println("Something went wrong. number of bytes read = numRead= "+numRead);
            }
            
 // System.out.println("END OF DISPLAY CHAPTER THREAD: chapter displayed ="+chapdispframenum+"on label:"+labelnumber);           
            
}
        
    
public void displayImage(int curframenum) throws FileNotFoundException, IOException
{
    
           BufferedImage testimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);;
           File file = new File(filename);
           InputStream is = new FileInputStream(file);
           int seeknumRead=0;
           long frameoffset = curframenum*sizeofframe;
           is.skip(frameoffset);
           
           
           int numRead = 0;
           int k = 0;
           byte[] bytes = new byte[(int)sizeofframe];
            if((numRead=is.read(bytes,  0, sizeofframe)) >=0) {
                     // VideoFrame[framenum] = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

                        for(int y = 0; y < height; y++){

                            for(int x = 0; x < width; x++){
                                byte a = 0;
                                byte r = bytes[k];
                                byte g = bytes[k+height*width];
                                byte b = bytes[k+height*width*2]; 
                                int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
                                //int pix = ((a << 24) + (r << 16) + (g << 8) + b);
                                testimg.setRGB(x,y,pix);             
                                k++;
                            }//end of for x
                        }//end of for y
                                   
                
                ImageIcon imgicon = new ImageIcon(testimg);     
                Vplayer.videolabel.setIcon(imgicon);      
                        
                        
                }//end of if    
            
            else
            {
  
                
                System.out.println("Something went wrong. number of bytes read = numRead= "+numRead);
            }
            
            
            
            
}
    
public BufferedImage getframe(int curframenum) throws FileNotFoundException, IOException
{
    
           BufferedImage testimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);;
           File file = new File(filename);
           InputStream is = new FileInputStream(file);
           int seeknumRead=0;
           long frameoffset = curframenum*sizeofframe;
           is.skip(frameoffset);
           
           
           int numRead = 0;
           int k = 0;
           byte[] bytes = new byte[(int)sizeofframe];
            if((numRead=is.read(bytes,  0, sizeofframe)) >=0) {
                     // VideoFrame[framenum] = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

                        for(int y = 0; y < height; y++){

                            for(int x = 0; x < width; x++){
                                byte a = 0;
                                byte r = bytes[k];
                                byte g = bytes[k+height*width];
                                byte b = bytes[k+height*width*2]; 
                                int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
                                //int pix = ((a << 24) + (r << 16) + (g << 8) + b);
                                testimg.setRGB(x,y,pix);             
                                k++;
                            }//end of for x
                        }//end of for y
                }//end of if    
            
            else
            {
                System.out.println("Something went wrong. number of bytes read = numRead= "+numRead);
            }
            
            
            return testimg;
            
}
    
    public void play() throws InterruptedException //will play from start frame to end frame as specified by constructor.
    {
          try {

           File file = new File(filename);
           InputStream is = new FileInputStream(file);
           long len = filelen;
         /*System.out.println("Length of the file "+filename+"="+len);
           System.out.println("width="+width+" Height =" +height);
           System.out.println("totalframes="+totalnumofframes);
           System.out.println("sizeofframe="+sizeofframe);
          */ 
           int seeknumRead=0;
           int curframenum=startframe;
           long frameoffset = curframenum*sizeofframe;
           is.skip(frameoffset);

           int numRead = 0;
           int k;
           byte[] bytes = new byte[(int)sizeofframe];
           while(numRead!=-1 && curframenum!=endframe ){
                k = 0;
                numRead = 0;
                
                if(Vplayer.PauseFlag)
                {
                    Vplayer.curframe = curframenum;
                    Vplayer.StopFlag = false;
                    break;
                }
                
                if(Vplayer.StopFlag)
                {
                    Vplayer.curframe = 1;
                    curframenum=totalnumofframes;
                    Vplayer.StopFlag = false;
                    break;
                }
                
                
                if((numRead=is.read(bytes,  0, sizeofframe)) >=0) {
                     // VideoFrame[framenum] = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

                        for(int y = 0; y < height; y++){

                            for(int x = 0; x < width; x++){
                                byte a = 0;
                                byte r = bytes[k];
                                byte g = bytes[k+height*width];
                                byte b = bytes[k+height*width*2]; 
                                int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
                                //int pix = ((a << 24) + (r << 16) + (g << 8) + b);
                               img.setRGB(x,y,pix);             

                                k++;
                            }//end of for x
                        }//end of for y
                }//end of if    
                
                
               // Image simg = img.getScaledInstance( 340, 250,  java.awt.Image.SCALE_SMOOTH ) ; 
                ImageIcon imgicon = new ImageIcon(img);    
                
                //Player.videolabel.setIcon(imgicon);    
                Vplayer.videolabel.setIcon(imgicon);
                Vplayer.videoThread.sleep(Vplayer.SyncwithAudio);
                curframenum++;
                Vplayer.curframe = curframenum;
         }      //end of while //end of for(f=0 to framenumber

         System.out.println("END OF VIDEO");
         System.out.println("Currentframe = "+Vplayer.curframe);
         if(curframenum==totalnumofframes)
         {
             Vplayer.jToggleButton1.setSelected(false);
             Vplayer.curframe=1;
             displayImage(1);
         }
       } catch (FileNotFoundException e) {
    } catch (IOException e) {
 }

//System.exit(0);

}
} //end of play