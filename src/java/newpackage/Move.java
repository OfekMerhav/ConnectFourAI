/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newpackage;

/**
 *
 * @author ofekm
 */
public class Move {
    
     int col;
    float score;


    public Move(int col) {
        this.col = col;
        this.score = 0;
    }

    public int getCol() {
        return col;
    }

    public float getScore() {
        return score;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public void negateScore()
    {
        this.score *=-1;
    }
    
}
