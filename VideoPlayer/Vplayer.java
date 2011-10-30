
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Vplayer.java
 *
 * Created on Apr 30, 2011, 7:30:43 PM
 */
/**
 *
 * @author ankola
 */
class Vplayer extends javax.swing.JFrame{


    static int chapLabel1_frameno = 1;
    static int chapLabel2_frameno = 1;
    static int chapLabel3_frameno = 1;
    static int chapLabel4_frameno = 1;
    static int chapLabel5_frameno = 1;
    
    
    static String BrowseDir = "C:\\TORDOWNLOADS\\CSCI-581_Multimedia Systems\\Content_Authoring_Project\\NetbeansProjectDirectory\\PlayAudioVideo\\";
    static Boolean PauseFlag = false;
    static Boolean StopFlag = false;
    static String videoFilename = "interview.rgb";
    static String audioFilename = "interview.wav";
    static String ContentFileName = "abc.rch";
    
    
    static int width = 352;    
    static int height = 288;
    static int startframe = 0;
    static int endframe = 0;
    static int curframe = 1;
    static int sizeofframe = 0;
    //File tempfile = new File(filename);
    static long videofilelen = 0;//tempfile.length();
    static int totalnumofframes = 1800;//(int) (filelen /sizeofframe);
    static PlayVideo vid; 
    static Thread videoThread;
    static int chapterspan=300;
    static int numofchapters=5;
    static long SyncwithAudio=10;
    static String isChapterneeded="false";
    static String isSliderneeded="false";
    static int totalnumofevents=0;
    static EventList[] Elist;
     
    
    static void openURI(String url)
    {   
       if( !java.awt.Desktop.isDesktopSupported() ) {

            System.err.println( "Desktop is not supported (fatal)" );
            System.exit( 1 );
        }
          java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
            if( !desktop.isSupported( java.awt.Desktop.Action.BROWSE ) ) {

            System.err.println( "Desktop doesn't support the browse action (fatal)" );
            System.exit( 1 );
        }
           try {

                java.net.URI uri = new java.net.URI(url);
                desktop.browse( uri );
            }
            catch ( Exception e ) {

                System.err.println( e.getMessage() );
            }
    }
    
    static void setVisibilityforChapters(Boolean val)
    {
        chapLabel1.setVisible(val);
        chapLabel2.setVisible(val);
        chapLabel3.setVisible(val);
        chapLabel4.setVisible(val);
        chapLabel5.setVisible(val);


         jFormattedTextField1.setVisible(val);
         jFormattedTextField2.setVisible(val);
         jFormattedTextField3.setVisible(val);
         jFormattedTextField4.setVisible(val);
         jFormattedTextField5.setVisible(val);
         
         jButton1.setVisible(val);
         jLabel1.setVisible(val);

    }



    public void setVisibilityforContentpane(Boolean val)
    {
        ContentLinkLabel.setVisible(val);
        ContentImageLabel.setVisible(val);
        ContentDescriptionTextArea.setVisible(val);
    }

     static void displayImageonContentImageLabel(String filename) throws IOException
     {
          File file = new File(filename);    
          BufferedImage testimg = ImageIO.read(file);
          ImageIcon imgicon = new ImageIcon(testimg.getScaledInstance( 250, 250,  java.awt.Image.SCALE_SMOOTH ));     
          ContentImageLabel.setIcon(imgicon);    
     }

     static void setLinkonContentLinkLabel(String link) throws IOException
     {
            ContentLinkLabel.setText(link);
     }

    public void setTextonContentDescrLabel(String description) throws IOException
     {
          ContentDescriptionTextArea.setVisible(true);
          jScrollPane1.setVisible(true);
          ContentDescriptionTextArea.setText(description);          

     }
    
     
     
     public void Validate(String ContentFilename) throws FileNotFoundException, IOException
     {
        /* ContentLinkLabel.setVisible(true);
         ContentLinkLabel.setText("http://www.google.com");*/
       // BufferedReader in = new BufferedReader(new FileReader(ContentFilename));
         //setVisibilityforContentpane(false);
       
             FileInputStream fstream = new FileInputStream(ContentFilename);
             //Get the object of DataInputStream
             DataInputStream in = new DataInputStream(fstream);
             BufferedReader br = new BufferedReader(new InputStreamReader(in));
             String strLine;
             //Read File Line By Line
             String wholefile="";
             int count = 0;
             while ((strLine = br.readLine()) != null)   {
              // Print the content on the console
               switch(count)
               {
                   case 0:videoFilename = strLine;
                         // System.out.println(strLine);
                          count++;
                          break;
                   case 1: audioFilename = strLine;
                          System.out.println(strLine);
                          count++;
                          break;
                   case 2: width = Integer.parseInt(strLine);
                           System.out.println("width="+width);
                           count++;
                           break;
                   case 3: height = Integer.parseInt(strLine);
                          System.out.println("height="+height);
                            count++;
                            break;
                   case 4: isSliderneeded = strLine;//sliderneeded
                           System.out.println(strLine);
                            count++;
                            break;
                   case 5:  isChapterneeded = strLine;
                           System.out.println(strLine);
                             count++;
                             break;
                   case 6: totalnumofevents = 0;
                            Elist= new EventList[100];
                           while (true)
                           {
                               System.out.println(strLine);
                              Elist[totalnumofevents] = new EventList();
                              Integer n = new Integer(strLine);
                              Elist[totalnumofevents].startframe = n.parseInt(strLine);
                              System.out.println("ASDASD"+Elist[totalnumofevents].startframe);
                              strLine = br.readLine();
                              Integer p = new Integer(strLine);
                              Elist[totalnumofevents].endframe = p.parseInt(strLine);//strLine;
                              System.out.println("ASDSAD"+Elist[totalnumofevents].endframe);
                              
                              strLine = br.readLine();
                              if(strLine.compareTo("_IMAGE")==0)
                                     {
                                           strLine = br.readLine();
                                            Elist[totalnumofevents].type = 0;
                                           // Elist[i].adImage = strLine;
                                            Elist[totalnumofevents].Content[Elist[totalnumofevents].type] = strLine;
                                           
                                     }
                                     else if(strLine.compareTo("_URL")==0)
                                     {
                                            strLine = br.readLine();
                                            Elist[totalnumofevents].type = 1;
                                           // Elist[i].adLink = strLine;
                                            Elist[totalnumofevents].Content[Elist[totalnumofevents].type] = strLine;
                                     }
                              
                                      
                              

                              if((strLine = br.readLine()) == null)  
                              {
                                  break;
                              }
                              else
                              {
                                  totalnumofevents++;
                              }

                           }
                       count++;
                       break;   
                       
                       
                /*  case 6: //strLine = br.readLine();
                          totalnumofevents=Integer.parseInt(strLine);
                          System.out.println(strLine);
                         //ContentLinkLabel.setText("numofevents="+totalnumofevents);
                          if(totalnumofevents>0)
                          {
                              Elist= new EventList[totalnumofevents];
                              for(int i=0;i<totalnumofevents;i++)
                              {
                                  Elist[i] = new EventList();
                              }
                              System.out.println("totalevents="+totalnumofevents);
                               for(int i=0;i<totalnumofevents;i++)
                                {
                                     strLine = br.readLine(); //startframe 
                                     System.out.println("startframe="+strLine);
                                     Elist[i].startframe = Integer.parseInt(strLine);
                                     strLine = br.readLine(); //endframe
                                     System.out.println("Endframe="+strLine);
                                     Elist[i].endframe = Integer.parseInt(strLine);
                                     strLine = br.readLine(); //keyword
                                     if(strLine.equals("_image"))
                                     {
                                           strLine = br.readLine();
                                            Elist[i].type = 0;
                                           // Elist[i].adImage = strLine;
                                            Elist[i].Content[Elist[i].type] = strLine;
                                           
                                     }
                                     else if(strLine.equals("_url"))
                                     {
                                            strLine = br.readLine();
                                            Elist[i].type = 1;
                                           // Elist[i].adLink = strLine;
                                            Elist[i].Content[Elist[i].type] = strLine;
                                     }
                                     else if(strLine.equals("_desc"))
                                     {
                                         Elist[i].type = 2;
                                         Elist[i].Content[Elist[i].type] = "";
                                         
                                         while(true)
                                         {
                                             strLine = br.readLine();
                                             if(strLine.equals("_end"))break;
                                             
                                             // Elist[i].adDescription+=strLine+"\n";
                                              Elist[i].Content[Elist[i].type] += strLine+"\n";
                                         }
                                     }
       
                                }
                              
                          }
                         
                          count++;
                          break;
                  */
                          
                            //for(int i=0;i<Integer.parseInt(strLine);i++) 
                                
                       
                   
               }//end of switch case
    
                wholefile=wholefile+strLine+"\n";
             }//end of while
            //Close the input stream
            in.close(); 
        // System.out.println(wholefile);
     /*      
     String allevents="";
     for(int i=0;i<totalnumofevents;i++)
     {
         int t = Elist[i].type;
      allevents += Elist[i].startframe+"\n"+Elist[i].endframe+"\n"+Elist[i].Content[t]+"\n";
      
     }
        System.out.println(allevents);
       setTextonContentDescrLabel(allevents);
   
       */   
     }//end of validate
    
    /** Creates new form Vplayer */
    
    public void  ReadContentfile() throws FileNotFoundException, IOException
    {
      System.out.println("contentfile name = "+ContentFileName);  
      Validate(ContentFileName);       
      
      if(isSliderneeded.equals("true"))
      {
          SeekSlider.setVisible(true);
      }
      else
      {
           SeekSlider.setVisible(false);
      }
      
      
      
      if(isChapterneeded.equals("true"))
      {
          setVisibilityforChapters(true);
      }
      else
      {
           setVisibilityforChapters(false);
      }
      

      
      /*
       Filename of video
       is slider needed
       is chapter needed
       is 
       */
      initDisplay();
      
      sizeofframe = width*height*3;  
      File tempfile = new File(videoFilename); 
      videofilelen = tempfile.length();
      totalnumofframes = (int) (videofilelen/sizeofframe);
      endframe = totalnumofframes;
      chapterspan=totalnumofframes/numofchapters;
      //System.out.println("Chapterspan ="+chapterspan +"numofchapters="+numofchapters);
       
      
      
   EventList ev = new EventList();   
   Thread th  = new Thread(ev);
  th.start();
   
        
      
      
    }
    
    
   static void initDisplay()
    {
        PlayVideo tempvid2 = new PlayVideo(videoFilename,width,height);    
        try {            
            tempvid2.displayImage(1);            
            for(int i=0; i<5;i++)
            {
                tempvid2.displaychapter((i*chapterspan)+1,i+1);
            }
       
        } catch (IOException ex) {
            Logger.getLogger(Vplayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Vplayer() {

        initComponents();
        
        SeekSlider.setVisible(false);
        setVisibilityforChapters(false);
        ContentDescriptionTextArea.setVisible(false);
        jScrollPane1.setVisible(false);
        //ReadContentfile();
        //
        
        

        
        
        
        
        
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton3 = new javax.swing.JToggleButton();
        videolabel = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        ContentImageLabel = new javax.swing.JLabel();
        ContentLinkLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ContentDescriptionTextArea = new javax.swing.JTextArea();
        chapLabel1 = new javax.swing.JLabel();
        chapLabel2 = new javax.swing.JLabel();
        chapLabel3 = new javax.swing.JLabel();
        chapLabel4 = new javax.swing.JLabel();
        chapLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jFormattedTextField2 = new javax.swing.JFormattedTextField();
        jFormattedTextField3 = new javax.swing.JFormattedTextField();
        jFormattedTextField4 = new javax.swing.JFormattedTextField();
        jFormattedTextField5 = new javax.swing.JFormattedTextField();
        SeekSlider = new javax.swing.JSlider();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(null);
        setResizable(false);

        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Button-Play-icon.jpg"))); // NOI18N
        jToggleButton1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Button-Pause-icon.jpg"))); // NOI18N
        jToggleButton1.setRolloverEnabled(false);
        jToggleButton1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/Button-Pause-icon.jpg"))); // NOI18N
        jToggleButton1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jToggleButton1ItemStateChanged(evt);
            }
        });
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jToggleButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Button-Stop-icon.jpg"))); // NOI18N
        jToggleButton3.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jToggleButton3MouseMoved(evt);
            }
        });
        jToggleButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton3ActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        ContentImageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        ContentLinkLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        ContentLinkLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ContentLinkLabelMouseClicked(evt);
            }
        });

        ContentDescriptionTextArea.setColumns(20);
        ContentDescriptionTextArea.setRows(5);
        ContentDescriptionTextArea.setBorder(null);
        jScrollPane1.setViewportView(ContentDescriptionTextArea);
        ContentDescriptionTextArea.getAccessibleContext().setAccessibleParent(jPanel1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(ContentImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ContentLinkLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ContentImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(ContentLinkLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        chapLabel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        chapLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        chapLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chapLabel1MouseClicked(evt);
            }
        });
        chapLabel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                chapLabel1MouseMoved(evt);
            }
        });

        chapLabel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        chapLabel2.setPreferredSize(new java.awt.Dimension(38, 18));
        chapLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chapLabel2MouseClicked(evt);
            }
        });
        chapLabel2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                chapLabel2MouseMoved(evt);
            }
        });

        chapLabel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        chapLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chapLabel3MouseClicked(evt);
            }
        });
        chapLabel3.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                chapLabel3MouseMoved(evt);
            }
        });

        chapLabel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        chapLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chapLabel4MouseClicked(evt);
            }
        });
        chapLabel4.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                chapLabel4MouseMoved(evt);
            }
        });

        chapLabel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        chapLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chapLabel5MouseClicked(evt);
            }
        });
        chapLabel5.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                chapLabel5MouseMoved(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/up.jpg"))); // NOI18N
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jButton1MouseMoved(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jFormattedTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField4ActionPerformed(evt);
            }
        });

        jFormattedTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField5ActionPerformed(evt);
            }
        });

        SeekSlider.setMajorTickSpacing(500);
        SeekSlider.setMaximum(1800);
        SeekSlider.setMinimum(1);
        SeekSlider.setMinorTickSpacing(1);
        SeekSlider.setSnapToTicks(true);
        SeekSlider.setToolTipText("Seek to Frame in Video");
        SeekSlider.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButton2.setText("Browse Video");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setText("Click to move up Chapters");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addContainerGap(10, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(chapLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                                    .addComponent(chapLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(chapLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(14, 14, 14)
                                    .addComponent(jFormattedTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jFormattedTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(chapLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(26, 26, 26)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jFormattedTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(chapLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(50, 50, 50))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(169, 169, 169)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(79, 79, 79)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(SeekSlider, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(videolabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 361, Short.MAX_VALUE))
                        .addGap(65, 65, 65)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jToggleButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(134, 134, 134)
                .addComponent(jLabel1)
                .addContainerGap(590, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton2)
                                .addGap(18, 18, 18)
                                .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jToggleButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(videolabel, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SeekSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(chapLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chapLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chapLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chapLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chapLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jFormattedTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFormattedTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFormattedTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(3, 3, 3)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton3ActionPerformed
        // TODO add your handling code here:  
      StopFlag = true;
      PauseFlag = false;
     
       curframe = 1;
     // BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
       PlayVideo vid1 = new PlayVideo(videoFilename,width,height,curframe,endframe);
        try {
            vid1.displayImage(1);// img);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Vplayer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Vplayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        jToggleButton1.setSelected(false);
        
     /*  ImageIcon imgicon = new ImageIcon(img);    
        videolabel.setIcon(imgicon);
       */ 
        
        
     
      
   
    }//GEN-LAST:event_jToggleButton3ActionPerformed

    private void jToggleButton1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jToggleButton1ItemStateChanged
        // TODO add your handling code here:
        if(evt.getStateChange() == ItemEvent.SELECTED)
        {                 
            
                PauseFlag = false;
                StopFlag = false;
               // System.out.println("current frame = "+curframe);
                vid = new PlayVideo(videoFilename,width,height,curframe,endframe); 
                videoThread = new Thread(vid); 
                videoThread.start();
               
                
       
                /*
                setLinkonContentLinkLabel("http://www.google.com");

                displayImageonContentImageLabel("up.jpg");

                setTextonContentDescrLabel("ASDSADAASDASDASDASDAS\n"+"SDASDADASADADASDASDADASDSADASDAS");
                */
                
                
              /*  PlaySound playSound = new PlaySound(audioFilename);
                Thread wavThread = new Thread(playSound);    
                wavThread.start();
                */
     
              
        }
        else
        {
       
     /*jToggleButton1.setEnabled(true);
      jToggleButton2.setEnabled(false);
      jToggleButton3.setEnabled(true);*/
      PauseFlag = true;
      StopFlag = false;
     // System.out.println("current frame = "+curframe);
        }
    }//GEN-LAST:event_jToggleButton1ItemStateChanged

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void chapLabel1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chapLabel1MouseMoved
        // TODO add your handling code here:    
        chapLabel1.setToolTipText("Framenum "+chapLabel1_frameno);
       
    }//GEN-LAST:event_chapLabel1MouseMoved

    private void chapLabel2MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chapLabel2MouseMoved
        // TODO add your handling code here:
        chapLabel2.setToolTipText("Framenum "+chapLabel2_frameno);
    }//GEN-LAST:event_chapLabel2MouseMoved

    private void chapLabel3MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chapLabel3MouseMoved
        // TODO add your handling code here:
        chapLabel3.setToolTipText("Framenum "+chapLabel3_frameno);
    }//GEN-LAST:event_chapLabel3MouseMoved

    private void chapLabel4MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chapLabel4MouseMoved
        // TODO add your handling code here:
        chapLabel4.setToolTipText("Framenum "+chapLabel4_frameno);
    }//GEN-LAST:event_chapLabel4MouseMoved

    private void chapLabel5MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chapLabel5MouseMoved
        // TODO add your handling code here:
        chapLabel5.setToolTipText("Framenum "+chapLabel5_frameno);
    }//GEN-LAST:event_chapLabel5MouseMoved

    private void chapLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chapLabel1MouseClicked
        // TODO add your handling code here:
        
         curframe = chapLabel1_frameno;
        if(PauseFlag || StopFlag)//meaning either Stop or play is on.. 
        {
            
             jToggleButton1.doClick();
        }
        else
        {
            jToggleButton3.doClick();
            curframe = chapLabel1_frameno;
            jToggleButton1.doClick();
            //do nothing
        }
 
        
        PlayVideo tempvid2 = new PlayVideo(videoFilename,width,height);    
        try {         
         //   System.out.println("chapterspan="+chapterspan);
            if(chapterspan>30)
            {    
                chapterspan = chapterspan/5;
                for(int i=0; i<5;i++)
                {
                     //tempvid2.displaychapter((i*chapterspan)+1,i+1);
                     tempvid2.displaychapter((chapLabel1_frameno+chapterspan*i),i+1);
                }

            }
       
        } catch (IOException ex) {
            Logger.getLogger(Vplayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_chapLabel1MouseClicked

    private void chapLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chapLabel2MouseClicked
        // TODO add your handling code here:
       curframe = chapLabel2_frameno;
       if(PauseFlag || StopFlag ||curframe ==1)//meaning either Stop or play is on.. 
        {
             
             jToggleButton1.doClick();
        }
        else
        {
            jToggleButton3.doClick();
            curframe = chapLabel2_frameno;
            jToggleButton1.doClick();
            //do nothing
        }
       
       
       
        PlayVideo tempvid2 = new PlayVideo(videoFilename,width,height);    
        try {         
         //   System.out.println("chapterspan="+chapterspan);
            if(chapterspan>30)
            {    
                chapterspan = chapterspan/5;
                for(int i=0; i<5;i++)
                {
                     //tempvid2.displaychapter((i*chapterspan)+1,i+1);
                     tempvid2.displaychapter((chapLabel2_frameno+chapterspan*i),i+1);
                }

            }
       
        } catch (IOException ex) {
            Logger.getLogger(Vplayer.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       
       
       
       
    }//GEN-LAST:event_chapLabel2MouseClicked

    private void chapLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chapLabel3MouseClicked
        // TODO add your handling code here:
       curframe = chapLabel3_frameno;
       if(PauseFlag || StopFlag ||curframe ==1)//meaning either Stop or play is on.. 
        {
             
             jToggleButton1.doClick();
        }
        else
        {
            jToggleButton3.doClick();
            curframe = chapLabel3_frameno;
            jToggleButton1.doClick();
            //do nothing
        }
       
       
               PlayVideo tempvid2 = new PlayVideo(videoFilename,width,height);    
        try {         
         //   System.out.println("chapterspan="+chapterspan);
            if(chapterspan>30)
            {    
                chapterspan = chapterspan/5;
                for(int i=0; i<5;i++)
                {
                     //tempvid2.displaychapter((i*chapterspan)+1,i+1);
                     tempvid2.displaychapter((chapLabel3_frameno+chapterspan*i),i+1);
                }

            }
       
        } catch (IOException ex) {
            Logger.getLogger(Vplayer.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       
       
       
    }//GEN-LAST:event_chapLabel3MouseClicked

    private void chapLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chapLabel4MouseClicked
        // TODO add your handling code here:
       curframe = chapLabel4_frameno;
       if(PauseFlag || StopFlag ||curframe ==1)//meaning either Stop or play is on.. 
        {
             
             jToggleButton1.doClick();
        }
        else
        {
            jToggleButton3.doClick();
            curframe = chapLabel4_frameno;
            jToggleButton1.doClick();
            //do nothing
        }
       
               PlayVideo tempvid2 = new PlayVideo(videoFilename,width,height);    
        try {         
         //   System.out.println("chapterspan="+chapterspan);
            if(chapterspan>30)
            {    
                chapterspan = chapterspan/5;
                for(int i=0; i<5;i++)
                {
                     //tempvid2.displaychapter((i*chapterspan)+1,i+1);
                     tempvid2.displaychapter((chapLabel4_frameno+chapterspan*i),i+1);
                }

            }
       
        } catch (IOException ex) {
            Logger.getLogger(Vplayer.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       
       
       
    }//GEN-LAST:event_chapLabel4MouseClicked

    private void chapLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chapLabel5MouseClicked
        // TODO add your handling code here:
       curframe = chapLabel5_frameno;
       if(PauseFlag || StopFlag ||curframe ==1)//meaning either Stop or play is on.. 
        {
             
             jToggleButton1.doClick();
        }
        else
        {
            jToggleButton3.doClick();
            curframe = chapLabel5_frameno;
            jToggleButton1.doClick();
            //do nothing
        }
       
               PlayVideo tempvid2 = new PlayVideo(videoFilename,width,height);    
        try {         
         //   System.out.println("chapterspan="+chapterspan);
            if(chapterspan>30)
            {    
                chapterspan = chapterspan/5;
                for(int i=0; i<5;i++)
                {
                     //tempvid2.displaychapter((i*chapterspan)+1,i+1);
                     tempvid2.displaychapter((chapLabel5_frameno+chapterspan*i),i+1);
                }

            }
       
        } catch (IOException ex) {
            Logger.getLogger(Vplayer.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       
       
       
       
       
    }//GEN-LAST:event_chapLabel5MouseClicked

    private void jFormattedTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedTextField4ActionPerformed

    private void jFormattedTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedTextField5ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        
        //curframe = chapLabel1_frameno;
        PlayVideo tempvid2 = new PlayVideo(videoFilename,width,height);    
        try {            
            
         //   System.out.println("on maximize chapterspan = "+chapterspan);
            if(chapterspan!=(totalnumofframes/numofchapters))
            {
                chapterspan = totalnumofframes/numofchapters;
                for(int i=0; i<5;i++)
                {
                    tempvid2.displaychapter((i*chapterspan)+1,i+1);
                }
                 
                 
              /*  
                 tempvid2.displaychapter((1*chapterspan)+chapLabel1_frameno,1);
                 tempvid2.displaychapter((2*chapterspan)+chapLabel2_frameno,2);
                 tempvid2.displaychapter((3*chapterspan)+chapLabel3_frameno,3);
                 tempvid2.displaychapter((4*chapterspan)+chapLabel4_frameno,4);
                 tempvid2.displaychapter((5*chapterspan)+chapLabel5_frameno,5);
                */
                
            }
       
        } catch (IOException ex) {
            Logger.getLogger(Vplayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseMoved
        // TODO add your handling code here:
        jButton1.setToolTipText("Click to move to root chapters");
    }//GEN-LAST:event_jButton1MouseMoved

    private void jToggleButton3MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jToggleButton3MouseMoved
        // TODO add your handling code here:
        jToggleButton3.setToolTipText("Stop button");
    }//GEN-LAST:event_jToggleButton3MouseMoved

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:        
        JFileChooser fileChooser = new JFileChooser();
        File f=new File(BrowseDir);
        
        fileChooser.setCurrentDirectory(f);
    
        ImagePreviewPanel preview = new ImagePreviewPanel();
        fileChooser.setAccessory(preview);
        fileChooser.addPropertyChangeListener(preview);
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            ContentFileName = selectedFile.getPath();
            try {
                ReadContentfile();
                //Constants.testimagelocation=selectedFile.getPath(); //delegated to detectbutton handler
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Vplayer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Vplayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        BrowseDir= fileChooser.getCurrentDirectory().getAbsolutePath();
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void ContentLinkLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ContentLinkLabelMouseClicked
        // TODO add your handling code here:
        openURI(ContentLinkLabel.getText());
    }//GEN-LAST:event_ContentLinkLabelMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
        
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Vplayer().setVisible(true);
            }
        });
        
        
  
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea ContentDescriptionTextArea;
    public static javax.swing.JLabel ContentImageLabel;
    public static javax.swing.JLabel ContentLinkLabel;
    public static javax.swing.JSlider SeekSlider;
    public static javax.swing.JLabel chapLabel1;
    public static javax.swing.JLabel chapLabel2;
    public static javax.swing.JLabel chapLabel3;
    public static javax.swing.JLabel chapLabel4;
    public static javax.swing.JLabel chapLabel5;
    private javax.swing.Box.Filler filler1;
    public static javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    public static javax.swing.JFormattedTextField jFormattedTextField1;
    public static javax.swing.JFormattedTextField jFormattedTextField2;
    public static javax.swing.JFormattedTextField jFormattedTextField3;
    public static javax.swing.JFormattedTextField jFormattedTextField4;
    public static javax.swing.JFormattedTextField jFormattedTextField5;
    public static javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JToggleButton jToggleButton1;
    public static javax.swing.JToggleButton jToggleButton3;
    public static javax.swing.JLabel videolabel;
    // End of variables declaration//GEN-END:variables
}
