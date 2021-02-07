/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newpackage;

import javax.json.Json;
import javax.json.JsonObject;

/**
 *
 * @author ofekm
 */
public class RowCol {
    public int row,col;
    
    public RowCol(int row,int col)
    {
        this.col = col;
        this.row = row;
    }
//    public JsonObject ToJson()
//    {
//      JsonObject jo = Json.createObjectBuilder().build();
//     
//     // jo. = Json.createObjectBuilder().add("Col", this.col).build();
//    }
}
