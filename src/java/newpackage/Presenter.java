/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newpackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonStructure;

/**
 *
 * @author ofekm
 */
@ServerEndpoint("/endpoint")
public class Presenter implements IPresenter{

    GameModel model;
    char CurrentPlayer;
    //Iview view;
    
    @OnOpen
    public void onopen(Session session)
    {
        new Presenter();
    }
    public Presenter()
    {
        this.model = new GameModel();
        this.CurrentPlayer = '1';
       // this.view = view;
    }

    @Override
    public void startGame() 
    {
        
    }


    private void ChangePlayer()
    {
        switch (this.CurrentPlayer)
        {
            case('1'):
                this.CurrentPlayer = '2';
                break;
            case('2'):
                this.CurrentPlayer = '1';
                break;

        }
    }
    
    
    
    @OnMessage
    public int moveTurn(int col,Session session) {
        if(col == -1)
        {
            this.model.RestartGame();
             this.CurrentPlayer = '1';
            
        }else{
       if(this.model.CheckLegalMove(col))
        {
            this.model.MakeMove(col,this.CurrentPlayer);

           // this.view.updateBoard(this.model.GetRow(col),col,CurrentPlayer);
            // check win
             if(!this.model.CheckWin(CurrentPlayer))
             {
                 this.ChangePlayer();
                 Move move = model.StartAi(8,CurrentPlayer,0);
                 int Colmove = move.getCol();
                 System.out.println(move.getScore());
                 this.model.MakeMove(Colmove,this.CurrentPlayer);
                 
               //  RowCol rc = new RowCol(this.model.GetRow(Colmove), Colmove);
                
                 try{
                 session.getBasicRemote().sendText(String.valueOf(Colmove)+String.valueOf(this.model.GetRow(Colmove)+1));
                 }
                 catch(Exception e)
                 {
                     System.out.println("  ");
                 }
                 //this.view.updateBoard(this.model.GetRow(Colmove),Colmove,CurrentPlayer);


                 if(this.model.CheckWin(CurrentPlayer))
                 {
                    try{
                        if(this.CurrentPlayer == 1)
                            session.getBasicRemote().sendText("You Won!!");
                        else
                            session.getBasicRemote().sendText("The bot Won ;)");
                    }
                    catch(Exception e)
                    {
                        System.out.println("  ");
                    }                 
                 }
                this.ChangePlayer();
             }
            // else -  player Win
             // notify relevant methods in view
            else if(!this.model.IsBoardFull())
             {
                  try{
                        if(this.CurrentPlayer == 1)
                            session.getBasicRemote().sendText("You Won!!");
                        else
                            session.getBasicRemote().sendText("The bot Won ;)");
                    }
                    catch(Exception e)
                    {
                        System.out.println("  ");
                    }                              
             }
            else{
                   try{
                    session.getBasicRemote().sendText("Game Over, There is no winner");
                    }
                    catch(Exception e)
                    {
                        System.out.println("  ");
                    }   
             }

        //    this.ChangePlayer();
            //this.ChangePlayer();
        }
        else
        {
            try{
                    session.getBasicRemote().sendText("");
                    }
                    catch(Exception e)
                    {
                        System.out.println("  ");
                    }   
//            this.view.displayMessage("Your Move is Illegal");
        }
    }

        // dummy return
        return 1;
    }
    

    
    @OnClose
    public void onClose (Session session)
    {
        
    }
    
   
    
}
