
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ankola
 */
public class EventList implements Runnable {
    
    
    public int startframe;
    public int endframe;
  /*  public String adImage;
    public String adLink;
    public String adDescription;*/
    public String Content[] = new String[3];
    public int type; //0 is image, 1 is link, 2 is description
    
    public void EventList()
    {
        for(int i=0;i<3;i++)
            Content[i] = new String();
    }

    @Override
    public void run() 
    {   
                try {                
                        while(true)
                        {
                          //updateblock("ASDSADSADASDSA",1);   
                            for(int i =0; i<Vplayer.totalnumofevents;i++)
                            {
                                if(Vplayer.curframe>=Vplayer.Elist[i].startframe && Vplayer.curframe <= Vplayer.Elist[i].endframe)
                                {
                                   String str = Vplayer.Elist[i].Content[Vplayer.Elist[i].type];
                                   updateblock(str,Vplayer.Elist[i].type);

                                }
                                else
                                {
                                    updateblock("",Vplayer.Elist[i].type);
                                }
                            }//end of for
                                try {
                                Thread.sleep(300);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Vplayer.class.getName()).log(Level.SEVERE, null, ex);
                                }
                        }//end                        
                    } catch (IOException ex) {
                Logger.getLogger(EventList.class.getName()).log(Level.SEVERE, null, ex);
                                         }
         }
                  

            public void updateblock(String str, int type) throws IOException 
            {
                if(type==0)
                {
                    try {
                            if(str.equals(""))
                            {
                                Vplayer.ContentImageLabel.setVisible(false);
                            }
                            else
                            {
                                 Vplayer.ContentImageLabel.setVisible(true);
                                  Vplayer.displayImageonContentImageLabel(str);
                            }
                       
                       } catch (IOException ex) {
                        Logger.getLogger(Vplayer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                }
                else if(type==1)
                {
                    Vplayer.setLinkonContentLinkLabel(str);
                }
             /*   else if(type==2)
                {
                    setTextonContentDescrLabel(str);
                }
               */     
            }
       
        
    

 
    
   
    
    
    
}
