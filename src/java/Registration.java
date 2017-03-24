/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
/**
 *
 * @author praty
 */
@ManagedBean
@RequestScoped
public class Registration {

    /**
     * Creates a new instance of Registration
     */
    private String ssn;
    private String fname;
    private String lname;
    private String loginID;
    private String password;
    private String sq1;
    private String answer1;
    private String sq2;
    private String answer2;
    private double deposit;
   

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getLoginID() {
        return loginID;
    }

    public void setLoginID(String loginID) {
        this.loginID = loginID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSq1() {
        return sq1;
    }

    public void setSq1(String sq1) {
        this.sq1 = sq1;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getSq2() {
        return sq2;
    }

    public void setSq2(String sq2) {
        this.sq2 = sq2;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }
    public String registration(){
        
         try{
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch(Exception e){
            e.printStackTrace();
        }
         
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        
        
        try
        {
            final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/darams7291";
            
            //connect to the database with user name and password
            connection = DriverManager.getConnection(DATABASE_URL,"darams7291", "1463877");   
            statement = connection.createStatement();
            rs = statement.executeQuery("select * from brokerageaccount where login_id='"+loginID+"'");
            if(rs.next()){
               return ("Given LoginID already exists! Try again with another LoginID");
            }
            else{
                rs = statement.executeQuery("select * from nextaccnumber");
                int acc = 0;int nextnum = 0;
                if(rs.next()){
                 acc = rs.getInt(1);
                 nextnum = acc+1;
                }
                String accnum = "";
                accnum = ""+ acc;
                Statement stat2 = null;
                stat2 = connection.createStatement();
                int t2 = stat2.executeUpdate("update nextaccnumber set nextnum='"+nextnum+"'");
                int t1 = stat2.executeUpdate("insert into securityquestions values ('"+accnum+"','"+sq1+"','"+answer1+"','"+sq2+"','"+answer2+"')");
                 int t = stat2.executeUpdate("insert into brokerageaccount values ('"+accnum+"','"+fname+"','"+lname+"','"+ssn+"','"+deposit+"','"+loginID+"','"+password+"','active')");
                
                
                  return ("Registration Successful!");
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            return ("Internal Error! Please try again later.");
        } 
         finally
        {
            try
            {
                rs.close();
                statement.close();
                connection.close();
                
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    } 
}
