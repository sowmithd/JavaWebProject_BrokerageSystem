/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import java.sql.*;
import java.util.*;
import javax.faces.bean.ManagedBean;
/**
 *
 * @author praty
 */
@Named(value = "securityQuestions")
@RequestScoped
public class SecurityQuestions {

    private ArrayList<String> questions;
    /**
     * Creates a new instance of SecurityQuestions
     */
    public ArrayList<String> securityquestions() {
   Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        questions = new ArrayList<String>();
         try{
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch(Exception e){
            e.printStackTrace();
        }
         try
        {
            final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/darams7291";
            
            //connect to the database with user name and password
            conn = DriverManager.getConnection(DB_URL, "darams7291", "1463877");   
            stat = conn.createStatement();
            rs= stat.executeQuery("select securityquestions from websecurityquetions");
            while(rs.next()){
                questions.add(rs.getString(1));
            }
    }
         catch(SQLException e){
             e.printStackTrace();
         }
     finally
        {
            try
            {
                rs.close();
                stat.close();
                conn.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();         
            }
        }     
         return questions;
    }

    public ArrayList<String> getQuestions() {
        
        return questions;
    }
    
}
