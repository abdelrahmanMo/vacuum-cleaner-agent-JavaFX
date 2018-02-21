 package vacuum;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXButton.ButtonType;
import com.jfoenix.controls.JFXNodesList;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXSnackbar;
import com.sun.java.swing.plaf.windows.resources.windows;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Interpolator;
import javafx.animation.KeyValue;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.sql.Connection;
import java.util.Random;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static sun.management.snmp.jvminstr.JvmThreadInstanceEntryImpl.ThreadStateMap.Byte0.runnable;

          
/**
 *
 * @author danml
 */

   enum  Constant {
     DIRTY_P(".8"), SUCK_SUCC_P(".5"), waitTime("5000") ;
    public String value;
    private Constant(String value)
        {
        this.value=value;
        }
        };
 enum Room {
     A("Room A"), B("Room B"), C("Room C"), D("Room D") ;
    public String value;
    private Room(String value)
        {
        this.value=value;
        }
        };
enum RoomStatus {
     Clean("is Clean"), Dirty("is Dirty") ;
    public String value;
    private RoomStatus(String value)
        {
        this.value=value;
        }
        };
enum Action {
     NoOp("Sleep"), Left("Move Left"), Right("Move Right"), Suck("Suck") ;
    public String value;
    private Action(String value)
        {
        this.value=value;
        }
        };




public class MenusController implements Initializable {

    @FXML
    public AnchorPane rootPane;
    @FXML
    private StackPane fabsContainer;
    @FXML
    private AnchorPane paneUsers;
    @FXML
    private AnchorPane paneTickets;
    @FXML
    private AnchorPane paneBuses;
    @FXML
    private AnchorPane paneDrivers;
    @FXML
    private HBox boxMenus;

    @FXML
    private Label labelstate2;
    @FXML
   private Label  labelA;

     @FXML
   private Label  labelB;
      @FXML
   private Label  labelC;
       @FXML
   private Label  labelD;
       @FXML
       private Label labelstate;

       @FXML
    private JFXButton btnEdit;
    @FXML
    private ImageView imageA;
     @FXML
    private ImageView imageB;
      @FXML
    private ImageView imageC;
       @FXML
    private ImageView imageD;
       @FXML
     private ImageView imageTrashA;
         @FXML
     private ImageView imageTrashB;
           @FXML
     private ImageView imageTrashC;
             @FXML
     private ImageView imageTrashD;
    @FXML
    private Circle circ;
    
    public static String A1;
    public static String A2;
    public static String A3;
    public static String A4;
    public static String A5;
    
    

    public class Agent{
    private String location;
    private String status;
    public String previousRoom="first";
    public String lastchange;
    public String act(String location,String status)
    {
        this.location=location;
        this.status=status;
        if(this.location!=this.previousRoom)
        {
            this.lastchange=this.previousRoom;
        }
        
        if(status == RoomStatus.Dirty.value)
        {
            this.previousRoom=location ;
           
            return Action.Suck.value;

        }
        else if(location == Room.A.value )
        {
            this.previousRoom=location ;
            return Action.Right.value;
        }
        else if(location == Room.B.value && (previousRoom==Room.A.value || previousRoom =="first") )
        {
            this.previousRoom=location ;
            return Action.Right.value;
        }
        
        else if(location == Room.B.value && previousRoom==Room.C.value  )
        {
            this.previousRoom=location ;
            return Action.Left.value;
        }
        else if(location == Room.B.value && previousRoom==Room.B.value && (this.lastchange==Room.A.value||this.lastchange=="first") )
        {
            this.previousRoom=location ;
            return Action.Right.value;
        }
        else if(location == Room.B.value && previousRoom==Room.B.value && this.lastchange==Room.C.value )
        {
            this.previousRoom=location ;
            return Action.Left.value;
        }
        else if(location == Room.C.value && (previousRoom==Room.B.value || previousRoom =="first") )
        {
            this.previousRoom=location ;
            return Action.Right.value;
        }
        else if(location == Room.C.value && previousRoom==Room.D.value)
        {
            this.previousRoom=location ;
            return Action.Left.value;
        }
         else if(location == Room.C.value && previousRoom==Room.C.value &&this.lastchange==Room.D.value)
        {
            this.previousRoom=location ;
            return Action.Left.value;
        }
          else if(location == Room.C.value && previousRoom==Room.C.value &&(this.lastchange==Room.B.value||this.lastchange=="first"))
        {
            this.previousRoom=location ;
            return Action.Right.value;
        }
        else if(location == Room.D.value )
        {
            this.previousRoom=location ;
            return Action.Left.value;
        }

        
        return "NAN";
    }

}
    public class write extends Thread{
        
        public void run(){
            
        }
    }
public class Environment extends Thread{
        private String A_Status;
        private String B_Status;
        private String C_Status;
        private String D_Status;
        private String AgentLocation;
        private Agent agent;
        Random rand = new Random();
        public void init(Agent agent)
        {
            this.A_Status=(rand.nextFloat()<Float.parseFloat(Constant.DIRTY_P.value)) ? RoomStatus.Dirty.value : RoomStatus.Clean.value;
            this.B_Status=(rand.nextFloat()<Float.parseFloat(Constant.DIRTY_P.value)) ? RoomStatus.Dirty.value : RoomStatus.Clean.value;
            this.C_Status=(rand.nextFloat()<Float.parseFloat(Constant.DIRTY_P.value)) ? RoomStatus.Dirty.value : RoomStatus.Clean.value;
            this.D_Status=(rand.nextFloat()<Float.parseFloat(Constant.DIRTY_P.value)) ? RoomStatus.Dirty.value : RoomStatus.Clean.value;
            float pos=rand.nextFloat();
             //imageTrashD.visibleProperty().set(true);
            if(pos<.25)
            {
                this.AgentLocation=Room.A.value;
            }
            else if(pos<.50 && pos>.25)
            {
                 this.AgentLocation=Room.B.value;
            }
            else if(pos<.750&&pos>.50)
            {
                 this.AgentLocation=Room.C.value;
            }
            else if(pos<1&&pos>.75)
            {
                 this.AgentLocation=Room.D.value;
            }
           
            this.agent=agent;
        }
        
        
        public void runn() throws InterruptedException  {
            boolean running =true;
            
//              
    
                    
   while(true)
   {
            
       
                if(this.AgentLocation == Room.A.value)
                {
                     Platform.runLater(new Runnable()

                {

                    @Override

                    public void run()

                    {
                        imageA.setVisible(true);
                    imageB.setVisible(false);
                    imageC.setVisible(false);
                    imageD.setVisible(false);

                    }

                });
                    
                }
               
                 else if(this.AgentLocation == Room.C.value)
                {
                     Platform.runLater(new Runnable()

                {

                    @Override

                    public void run()

                    {
                    imageA.setVisible(false);
                    imageB.setVisible(false);
                    imageC.setVisible(true);
                    imageD.setVisible(false);
                    }

                });
                    
                  
                }
                 else if(this.AgentLocation == Room.D.value)
                {
                    Platform.runLater(new Runnable()

                {

                    @Override

                    public void run()

                    {
                          imageA.setVisible(false);
                    imageB.setVisible(false);
                    imageC.setVisible(false);
                    imageD.setVisible(true);
                    }

                });
                 
                }
                 else if(this.AgentLocation == Room.B.value)
                {
                     Platform.runLater(new Runnable()

                {

                    @Override

                    public void run()

                    {
                         imageA.setVisible(false);
                    imageB.setVisible(true);
                    imageC.setVisible(false);
                    imageD.setVisible(false);
                    }

                });
                     
                }
              
                
                if(this.A_Status==RoomStatus.Dirty.value)
                {
                      Platform.runLater(new Runnable()

                {

                    @Override

                    public void run()

                    {
                        imageTrashA.setVisible(true);
                    labelA.setText("Room A is Dirty");
                    labelA.setStyle( "-fx-text-fill:#ff0808");
                    }

                });
                     
                         

                  
                }
                else
                {
                       Platform.runLater(new Runnable()

                {

                    @Override

                    public void run()

                    {
                      
                        imageTrashA.setVisible(false);
                       labelA.setText("Room A is Clean");
                    labelA.setStyle( "-fx-text-fill:#0ab24b");
                    }

                });
                    
                
                  
                    //Thread.sleep(500);
                }   

                if(this.B_Status==RoomStatus.Dirty.value)
                {
                       Platform.runLater(new Runnable()

                {

                    @Override

                    public void run()

                    {
                      
                        imageTrashB.setVisible(true);
                    labelB.setText("Room B is Dirty");
                    labelB.setStyle( "-fx-text-fill:#ff0808");
                    }

                });
                    
                }
                else
                {
                  Platform.runLater(new Runnable()

                {

                    @Override

                    public void run()

                    {
                      
                          imageTrashB.setVisible(false);
                    labelB.setText("Room B is Clean");
                    labelB.setStyle( "-fx-text-fill:#0ab24b");
                    }

                });
                 
                }

                if(this.C_Status==RoomStatus.Dirty.value)
                {
                     Platform.runLater(new Runnable()
                {

                    @Override

                    public void run()

                    {
                      
                            imageTrashC.setVisible(true);  
                    labelC.setText("Room C is Dirty");
                    labelC.setStyle( "-fx-text-fill:#ff0808");
                    }

                });
                  
                }   
                else
                {
                       Platform.runLater(new Runnable()
                {

                    @Override

                    public void run()

                    {
                      
                           imageTrashC.setVisible(false);
                    labelC.setText("Room C is Clean");
                    labelC.setStyle( "-fx-text-fill:#0ab24b");
                    }

                });
                    
                }

                if(this.D_Status==RoomStatus.Dirty.value)
                {
                      
                    Platform.runLater(new Runnable()
                {

                    @Override

                    public void run()

                    {
                      
                            imageTrashD.setVisible(true);
                    labelD.setText("Room D is Dirty");
                    labelD.setStyle( "-fx-text-fill:#ff0808");
                    }

                });
                  
                }
                else
                {
                     Platform.runLater(new Runnable()
                {

                    @Override

                    public void run()

                    {
                      
                             imageTrashD.setVisible(false);
                    labelD.setText("Room D is Clean");
                    labelD.setStyle( "-fx-text-fill:#0ab24b");
                    }

                });
                  
                }

              A1=this.A_Status;
              A2=this.B_Status;
              A3=this.C_Status;
              A4=this.D_Status;
              A5=this.AgentLocation;
               
              //  System.out.println(Room.A.value +" "+ this.A_Status + "," + Room.B.value +" "+this.B_Status + "," + Room.C.value +" "+this.C_Status + "," + Room.D.value +" "+this.D_Status +   " ,Agent is in "+this.AgentLocation);
                     Platform.runLater(new Runnable()
                {

                    @Override

                    public void run()

                    {
                       labelstate.setText(Room.A.value +" "+ A1 + "," + Room.B.value +" "+A2 + "," + Room.C.value +" "+A3 + "," + Room.D.value +" "+A4 +   " ,Agent is in "+A5);
                    }

                });
               
             
                
                String currentstatus="";
                if(this.AgentLocation==Room.A.value)
                {
                    currentstatus=this.A_Status;
                }
                else if(this.AgentLocation == Room.B.value)
                {
                    currentstatus=this.B_Status;
                }
                else if(this.AgentLocation == Room.C.value)
                {
                    currentstatus=this.C_Status;
                }
                else if(this.AgentLocation == Room.D.value)
                {
                    currentstatus=this.D_Status;
                }
                String action=this.agent.act(AgentLocation,currentstatus);
              //  System.out.println("Agent decided to "+action);
              //  labelstate2.setStyle( "-fx-text-fill:#ff0808");
                 Platform.runLater(new Runnable()

                {

                    @Override

                    public void run()

                    {
                         labelstate2.setText("Agent decided to "+action);
                    }

                });
               
                 
                if(action==Action.Right.value&&this.agent.previousRoom==Room.A.value)
                {
                    this.AgentLocation=Room.B.value;
                }
                if(action==Action.Left.value&&this.agent.previousRoom==Room.A.value)
                {
                    this.AgentLocation=Room.A.value;
                }
                else if(action==Action.Right.value&&this.agent.previousRoom==Room.B.value)
                {
                    this.AgentLocation=Room.C.value;
                }
                  else if(action==Action.Right.value&&this.agent.previousRoom==Room.C.value)
                {
                    this.AgentLocation=Room.D.value;
                }
               else if(action==Action.Left.value&&this.agent.previousRoom==Room.D.value)
                {
                    this.AgentLocation=Room.C.value;
                }
               else if(action==Action.Right.value&&this.agent.previousRoom==Room.D.value)
                {
                    this.AgentLocation=Room.D.value;
                }
                else if(action==Action.Left.value&&this.agent.previousRoom==Room.C.value)
                {
                    this.AgentLocation=Room.B.value;
                }
                else if(action==Action.Left.value&&this.agent.previousRoom==Room.B.value)
                {
                    this.AgentLocation=Room.A.value;
                }
              else if(action==Action.Suck.value)
              { 
                  float x= rand.nextFloat();
                 if(Float.parseFloat(Constant.SUCK_SUCC_P.value)>x)
                 { if(this.AgentLocation==Room.A.value)
                         this.A_Status=RoomStatus.Clean.value;
                     else if(this.AgentLocation==Room.B.value)
                         this.B_Status=RoomStatus.Clean.value;
                     else if(this.AgentLocation==Room.C.value)
                         this.C_Status=RoomStatus.Clean.value;
                     else if(this.AgentLocation==Room.D.value)
                         this.D_Status=RoomStatus.Clean.value;
                 }
              }         
               
          Thread.sleep(700);
      
              
   }
            
            

        }
        public void startTask()
    {

        // Create a Runnable

        Runnable task = new Runnable()

        {

            public void run()

            {

                try {
                    runn();
                } catch (InterruptedException ex) {
                    Logger.getLogger(MenusController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        };

 

        // Run the task in a background thread

        Thread backgroundThread = new Thread(task);

        // Terminate the running thread if the application exits

        backgroundThread.setDaemon(true);

        // Start the thread

        backgroundThread.start();

    }  

    }
Environment en=new Environment();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       // System.out.println(LoginController.privilage);

        //DbHandler handler = new DbHandler();
       
        setUpFabs();
        setUpRipples();
            //   Environment en=new Environment();
       
      /* Environment en=new Environment();
       Agent agent=new Agent();
        en.init(agent);
       
        try {
            en.run();
        } catch (InterruptedException ex) {
            Logger.getLogger(MenusController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MenusController.class.getName()).log(Level.SEVERE, null, ex);
        }
       */
                //Image image=new Image("/icons/right.png");
                  //     imageA.setImage(image);
    }

   

    @FXML
    private void switchToUsers(MouseEvent event) {
         en.A_Status=RoomStatus.Dirty.value;
         imageTrashA.setVisible(true);
          labelA.setText("Room A is Dirty");
          labelA.setStyle( "-fx-text-fill:#ff0808");
    }


    @FXML
    private void switchToTickets(MouseEvent event) {
      en.D_Status=RoomStatus.Dirty.value;
         imageTrashD.setVisible(true);
          labelD.setText("Room D is Dirty");
          labelD.setStyle( "-fx-text-fill:#ff0808");
    }
     @FXML
    private void editDriver(ActionEvent event)  {
     
            
            Agent agent=new Agent();
        en.init(agent);
        en.startTask();
      
       
       }
                 
      
        
    @FXML
    private void switchToBuses(MouseEvent event) {
        en.C_Status=RoomStatus.Dirty.value;
         imageTrashC.setVisible(true);
          labelC.setText("Room C is Dirty");
          labelC.setStyle( "-fx-text-fill:#ff0808");
    }

    @FXML
    private void switchToDrivers(MouseEvent event) {
         en.B_Status=RoomStatus.Dirty.value;
         imageTrashB.setVisible(true);
          labelB.setText("Room B is Dirty");
          labelB.setStyle( "-fx-text-fill:#ff0808");

    }

    private void setUpFabs() {
        //Setting up icons for button
        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.CIRCLE_ALT_NOTCH);
        icon.setStyle("-fx-fill:#ffffff;-fx-size:13px;");

        FontAwesomeIconView closeicon = new FontAwesomeIconView(FontAwesomeIcon.TIMES);
        closeicon.setStyle("-fx-fill:#ffffff;-fx-size:13px;");
        FontAwesomeIconView logicon = new FontAwesomeIconView(FontAwesomeIcon.UNLOCK_ALT);
        logicon.setStyle("-fx-fill:#ffffff;-fx-size:13px;");

        JFXButton button1 = new JFXButton();
        Label label1 = new Label("G1");
        button1.setGraphic(icon);
        label1.setStyle("-fx-text-fill:WHITE");
        button1.setButtonType(ButtonType.RAISED);
        button1.setStyle("-fx-pref-width:30px;-fx-background-color:#F6C574;"
                + "-fx-background-radius:30px;-fx-pref-height:30px;");

        JFXButton button2 = new JFXButton();
        button2.setTooltip(new Tooltip("Log off"));
        button2.setButtonType(ButtonType.RAISED);
        button2.setGraphic(logicon);
        button2.setStyle("-fx-pref-width:30px;-fx-background-color:#F6C574;"
                + "-fx-background-radius:30px;-fx-pref-height:30px;");
        button2.setOnAction((ActionEvent event) -> {
            try {
                paneDrivers.getScene().getWindow().hide();
                Parent rood = FXMLLoader.load(getClass().getResource("Login.fxml"));
                Scene scene = new Scene(rood);
                Stage driverStage = new Stage();
                driverStage.setScene(scene);
                driverStage.show();
            } catch (IOException ex) {
                Logger.getLogger(MenusController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        JFXButton button3 = new JFXButton();
        button3.setButtonType(ButtonType.RAISED);
        button3.setTooltip(new Tooltip("Exit"));
        button3.setGraphic(closeicon);
        button3.setStyle("-fx-pref-width:30px;-fx-background-color:#F87951;"
                + "-fx-background-radius:30px;-fx-pref-height:30px;");
        button3.setOnAction((ActionEvent event) -> {
            Platform.exit();
        });

        JFXNodesList nodesList = new JFXNodesList();
        nodesList.setSpacing(10);
        nodesList.addAnimatedNode(button1, (expanded) -> {
            return new ArrayList<KeyValue>() {
                {
                    add(new KeyValue(button1.rotateProperty(), expanded ? 360 : 0, Interpolator.EASE_BOTH));
                }
            };
        });
        nodesList.addAnimatedNode(button2);
        nodesList.addAnimatedNode(button3);

        fabsContainer.getChildren().add(nodesList);

    }

    private void setUpRipples() {
        JFXRippler ripplerUser = new JFXRippler(paneUsers, JFXRippler.RipplerMask.RECT, JFXRippler.RipplerPos.FRONT);
        JFXRippler ripplerDriver = new JFXRippler(paneDrivers, JFXRippler.RipplerMask.RECT, JFXRippler.RipplerPos.FRONT);
        JFXRippler ripplerBuses = new JFXRippler(paneBuses, JFXRippler.RipplerMask.RECT, JFXRippler.RipplerPos.FRONT);
        JFXRippler ripplerTickets = new JFXRippler(paneTickets, JFXRippler.RipplerMask.RECT, JFXRippler.RipplerPos.FRONT);

        boxMenus.getChildren().addAll(ripplerUser, ripplerDriver, ripplerBuses, ripplerTickets);
    }

}
