/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author praty
 */
@Named(value = "login")
@SessionScoped
public class Login implements Serializable {

    /**
     * Creates a new instance of Login
     */
    private String loginid;
    private String pasword;
    private BrokerageAccount account;
    public String login() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch(Exception e){
            e.printStackTrace();
        }
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/darams7291";
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        try{
            conn = DriverManager.getConnection(DB_URL,"darams7291","1463877");
            stat = conn.createStatement();
            rs = stat.executeQuery("select * from brokerageaccount where login_id='"+loginid+"'");
            if(rs.next()){
                if(pasword.equals(rs.getString(7))){
                    account = new BrokerageAccount(loginid);
                    loginid = "";
                    pasword = "";
                    return "homepage";
                }
                else{
                    loginid = "";
                    pasword = "";
                return "loginnotok";
                }
            }
            else{
                loginid = "";
                pasword = "";
                return "loginnotok";
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            return "error";
        }
         finally{
            try{
            rs.close();
            stat.close();
            conn.close();
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
    }

    public String getLoginid() {
        return loginid;
    }

    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }

    public String getPasword() {
        return pasword;
    }

    public void setPasword(String pasword) {
        this.pasword = pasword;
    }

    public BrokerageAccount getAccount() {
        return account;
    }

    public void setAccount(BrokerageAccount account) {
        this.account = account;
    }
    
}
