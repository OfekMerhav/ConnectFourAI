/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newpackage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
/**
 *
 * @author ofekm
 */
public class GameModel {
    
    private int row =6 ,column =7;
    int last_col,last_row;
    char[][] Board;

    public GameModel()
    {
        Board = new char[row][column];
        //Initializing the Board
        for (int i = 0; i < this.row; i++)
        {
            for (int j = 0; j < column; j++)
            {
                Board[i][j] = '0';
            }

        }
    }

    public void RestartGame()
    {
        for (int i = 0; i < this.row; i++)
        {
            for (int j = 0; j < column; j++)
            {
                Board[i][j] = '0';
            }

        }
    }
    public boolean CheckLegalMove(int col)
    {
        if(this.isValidCul(col) && this.Board[0][col] == '0')
        {
            return true;
        }
        else
            return false;

    }

    public void MakeMove(int col,char CurrentPlayer)
    {
       int row = GetRow(col);
       this.last_col = col;
       this.last_row = row;
       this.Board[row][col] =CurrentPlayer;
    }
    //Returns the current Row in the column
    public int GetRow(int col)
    {
        int i;

        for(i=0;i<row;i++)
        {
            if(this.Board[i][col] != '0')
            {
                return i-1;
            }
        }
        return row-1;
    }
    public void UnDo(int col)
    {
        int i;

        for(i=0;i<row;i++)
        {
            if (this.Board[i][col] != '0')
            {
                Board[i][col] = '0';
                break;
            }
        }
    }
    //Checks for win
    public boolean CheckWin(char CurrentPlayer)
    {
        int col = this.last_col;
        int CurrentRow = last_row;

        int winCount =1;
        //Checks for a Vertical win
        for(int i = CurrentRow+1;i <= CurrentRow+3;i++)
        {
            if(isValidRow(i))
            {
                if (this.Board[i][col] == CurrentPlayer) {
                    winCount++;
                }
            }
            else break;
        }
        if(winCount == 4)
        {
            return true;
        }
        winCount = 0;
        //Checks for horizontal win
        for(int i =0;i < column;i++)
        {

             if (this.Board[CurrentRow][i] == CurrentPlayer)
             {
                winCount++;
                 if(winCount == 4)
                 {
                     return true;
                 }
             }
             else
             {
                 winCount = 0;
             }

        }
        if(winCount == 4)
        {
            return true;
        }
        winCount = 0;
        //Checks for main diagonal win
        for(int i = col-3;i <=col+3;i++)
        {
           if(isValidCul(i))
           {
               if(isValidRow(CurrentRow - (col - i)))
               {
                   if (Board[CurrentRow - (col - i)][i] == CurrentPlayer) {
                       winCount++;
                       if (winCount == 4) {
                           return true;
                       }
                   } else {
                       winCount = 0;
                   }
               }

           }
        }
        if(winCount == 4)
        {
            return true;
        }
        winCount = 0;
        //Checks for secondary diagonal win
        for(int i = col+3;i >=col-3;i--)
        {
            if(isValidCul(i))
            {
                if(isValidRow(CurrentRow + (col - i)))
                {
                    if (Board[CurrentRow + (col - i)][i] == CurrentPlayer) {
                        winCount++;
                        if (winCount == 4) {
                            return true;
                        }
                    } else {
                        winCount = 0;
                    }
                }

            }
        }
        if (winCount == 4)
            return true;
        return false;

    }


    //Returns array of available column indexes
    private LinkedList<Move> AllPossibleMoves()
    {
        LinkedList<Move> legal = new LinkedList<>();
        for(int col=0;col<this.column;col++)
        {
            if(CheckLegalMove(col))
            {
                legal.add(new Move(col));
            }
        }
        return legal;
    }

    private char ChangePlayer(char currentplayer)
    {
        switch (currentplayer)
        {
            case('1'):
                return '2';

            case('2'):
                return '1';

            default:
                break;
        }
        return currentplayer;
    }
    //the megaMAX Func
    public Move StartAi(int depth,char CurrentPlayer,int TurnCount)
    {
        Random r = new Random();

        if(CheckWin(ChangePlayer(CurrentPlayer)) == true || depth == 0 || IsBoardFull())
        {
            Move m = new Move(0);
            m.setScore(Evaluate(ChangePlayer(CurrentPlayer),depth,TurnCount));
            return m;
        }

        LinkedList<Move> possibleMoves = AllPossibleMoves();
        Move bestMove = new Move(possibleMoves.get(r.nextInt(possibleMoves.size())).getCol());
        //bestMove.setScore(0);
        if(CurrentPlayer == '2')
        {
            bestMove.setScore(-1000);
        }
        else
        {
            bestMove.setScore(1000);
        }

        for(Move m: possibleMoves)
        {
           MakeMove(m.getCol(),CurrentPlayer);

            Move move = StartAi(depth - 1,ChangePlayer(CurrentPlayer),TurnCount+1);
           // move.negateScore();


            if(CurrentPlayer == '2')
            {
                if (move.getScore() > bestMove.getScore() ) {
                    bestMove.setCol(m.getCol());
                    bestMove.setScore(move.getScore());
                }

            }
            else
            {
                if (move.getScore() < bestMove.getScore() ) {
                    bestMove.setCol(m.getCol());
                    bestMove.setScore(move.getScore());
                }

            }


            // undo move
            UnDo(m.getCol());

        }


        return bestMove;


    }



    public float Evaluate(char CurrentPlayer,int depth,int TurnCount)
    {
        Random r = new Random();
        boolean state = CheckWin(CurrentPlayer);

        // if computer wins and computer turn 10, else other win -> -10
        if(state == true)
        {
           if(CurrentPlayer == '2')
           {
               return(100 -TurnCount*0.01f);
           }
           else
           {
               return (-100 +TurnCount*0.01f);
           }
        }
        else if(depth == 0)
        {
        return r.nextInt(10);
        }
        else if(IsBoardFull())
        {
            return r.nextInt(10);
        }

        return r.nextInt(20);

    }


    private boolean isValidRow(int row)
    {
        if(row>=this.row || row < 0)
            return false;
        return true;
    }
    private boolean isValidCul(int col)
    {
        if(col>=this.column || col < 0)
            return false;
        return true;
    }
    public boolean IsBoardFull()
    {
        for(int i=0;i<column;i++)
        {
            if(this.CheckLegalMove(i))
                return false;
        }
        return true;
    }
    
    
}
