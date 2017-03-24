/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.inject.Named;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.faces.event.ValueChangeEvent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author praty
 */
@ManagedBean
@SessionScoped
public class BrokerageAccount implements Serializable {

    /**
     * Creates a new instance of BrokerageAccount
     */
    private String loginid;
    private String order_type;
    private String order_category;
    private int num;
    private String ss;
    private double price;
    private ArrayList<Orders> view_orders;
    
    public BrokerageAccount() {

    }

    public BrokerageAccount(String id) {
        loginid = id;

    }

    public String trade(String loginID) {
      
        if (order_type.equals("buy") && order_category.equals("market")) {
            final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/darams7291";
            Connection conn = null;
            Statement stat = null;
            ResultSet rs = null;
            ResultSet rs1 = null;
            ResultSet rs2 = null;
            try{
                    conn = DriverManager.getConnection(DB_URL,"darams7291","1463877");
                   stat = conn.createStatement();
                   Statement mainstat = null;
                        mainstat = conn.createStatement();
                        Statement st = null;
                            st = conn.createStatement();int c = 0;ResultSet r = null;
                            r = st.executeQuery("select stock_symbol from selling where status='pending'");
                           while(r.next()){
                               if(ss.equalsIgnoreCase(r.getString(1))){
                                   c++;
                                   break;
                               }
                               else{
                                   c=0;
                               }
                           }
                           if(c>0){
                               ResultSet mainrs = null;boolean update = false;int id = 0;
                        mainrs = mainstat.executeQuery("select * from selling where stock_symbol='"+ss+"'and status = 'pending' and Price is not null order by Price asc" );
                        while(mainrs.next()){
                            ArrayList<Orders> orders_new = new ArrayList<Orders>();
                            double balance = 0;
                            orders_new.add(new Orders(mainrs.getString(1),mainrs.getString(2),mainrs.getString(3),mainrs.getInt(4),mainrs.getInt(5),mainrs.getDouble(6),mainrs.getString(7), mainrs.getString(8)));
                            String acc = "";double amount = 0;
                /*Count check*/   if(num< mainrs.getInt(5)){
                                    amount = num*mainrs.getDouble(6);
                                    Statement stat1 = null;
                                    stat1 = conn.createStatement();
                                    rs1 = stat1.executeQuery("select * from brokerageaccount where login_id='"+loginID+"'");
                                    if(rs1.next()){
                                         balance = rs1.getDouble(5);
                                         acc = rs1.getString(1);
                                    }
                                    if(amount<=balance ){
                                        double result = balance-amount;
                                        int n = 0;
                                          Statement stat3 = conn.createStatement();
                                           stat3 = conn.createStatement();
                                           if(update == false){
                                               Statement stat2 = conn.createStatement();
                                        rs2= stat2.executeQuery("select * from nextordernum");
                                        
                                        if(rs2.next()){
                                             n = rs2.getInt(1);
                                        }
                                      id = n;
                                        int t = stat3.executeUpdate("insert into buying values ('B"+n+"','"+acc+"','"+ss+"','"+num+"','"+0+
                                                "','"+orders_new.get(0).getPrice()+"','MO','completed','"+DateandTime.DateTime().toString()+"','"+DateandTime.DateTime().toString()+"')");
                                        t = stat3.executeUpdate("update selling set share_count='"+(orders_new.get(0).getShare_count()- num)+"',date_updated='"+DateandTime.DateTime().toString()+ "' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"' and order_id='"+orders_new.get(0).getOrder_id()+"'");
                                        t = stat3.executeUpdate("update nextordernum set nextnum="+(n+1));
                                        t = stat3.executeUpdate("update brokerageaccount set balance = balance+'"+amount+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'");
                                        t = stat3.executeUpdate("update brokerageaccount set balance='"+result+"' where login_id='"+loginID+"'");
                                        rs = stat3.executeQuery("select * from sharescount where brokerageaccnum='"+acc+"' and stock_symbol='"+ss+"'");
                                        if(rs.next()){
                                            int t1= stat3.executeUpdate("update sharescount set count=count + '"+num+"' where brokerageaccnum='"+acc+"'and stock_symbol='"+ss+"'");
                                        }

                                        else{
                                            int t1 = stat3.executeUpdate("insert into sharescount values ('"+acc+"','"+ss+"','"+num+"')");
                                        }
                                        
                                        rs = stat3.executeQuery("select * from sharescount where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"' and stock_symbol = '"+ss+"'");
                                        if(rs.next()){
                                            int t1= stat3.executeUpdate("update sharescount set count=count - '"+num+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'and stock_symbol='"+ss+"'");
                                        }
                                        else{
                                            int t1 = stat3.executeUpdate("insert into sharescount values ('"+orders_new.get(0).getBrokerageAccNum()+"','"+ss+"','"+num+"')");
                                        }
                                        update = true;
                                       
                                       // new BrokerageAccount().Updatebuy(orders_new.get(0).getPrice(),orders_new.get(0).getPrice(), num, ss);
                                          return ("Order placed!");
                                           }
                                            else if(update == true){
                                            int t3 =stat3.executeUpdate("update buying set share_count='"+0+"',price='"+orders_new.get(0).getPrice()+"',status='completed',date_updated='"+DateandTime.DateTime()+"' where order_id='B"+id+"' and brokerageaccnum='"+acc+"'");
                                            t3 = stat3.executeUpdate("update selling set share_count='"+(orders_new.get(0).getShare_count()-num)+"',status='pending',date_updated='"+DateandTime.DateTime()+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"' and order_id='"+orders_new.get(0).getOrder_id()+"'");
                                            
                                             t3 = stat3.executeUpdate("update brokerageaccount set balance = balance+'"+amount+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'");
                                        t3 = stat3.executeUpdate("update brokerageaccount set balance='"+result+"' where login_id='"+loginID+"'");
                                        rs = stat3.executeQuery("select * from sharescount where brokerageaccnum='"+acc+"' and stock_symbol='"+ss+"'");
                                        if(rs.next()){
                                            int t1= stat3.executeUpdate("update sharescount set count=count + '"+num+"' where brokerageaccnum='"+acc+"'and stock_symbol='"+ss+"'");
                                        }

                                        else{
                                            int t1 = stat3.executeUpdate("insert into sharescount values ('"+acc+"','"+ss+"','"+num+"')");
                                        }
                                        rs = stat3.executeQuery("select * from sharescount where brokerageaccnum ='"+orders_new.get(0).getBrokerageAccNum()+"' and stock_symbol = '"+ss+"'");
                                        if(rs.next()){
                                            int t1= stat3.executeUpdate("update sharescount set count=count - '"+orders_new.get(0).getShare_count()+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'and stock_symbol='"+ss+"'");
                                        }
                                        else{
                                            int t1 = stat3.executeUpdate("insert into sharescount values ('"+orders_new.get(0).getBrokerageAccNum()+"','"+ss+"','"+orders_new.get(0).getShare_count()+"')");
                                        }
                                         return ("order placed!");
//new BrokerageAccount().Updatebuy(orders_new.get(0).getPrice(),orders_new.get(0).getPrice(), num, ss);
                                          }
                                        
                                        break;
                                }
                                    else{
                                        return ("Balance insufficient!");
                                        //flag1 = false;f1=false;f=false;
                                       // break;
                                    }
                            }
                            else if(num > mainrs.getInt(5)){
                                //System.out.println("Does not have stocks");
                                 int num1 = num - mainrs.getInt(5);
                                      amount = mainrs.getInt(5)*mainrs.getDouble(6);
                                      Statement stat1 = null;
                                              stat1 = conn.createStatement();
                                    rs1 = stat1.executeQuery("select * from brokerageaccount where login_id='"+loginID+"'");
                                    if(rs1.next()){
                                         balance = rs1.getDouble(5);
                                         acc = rs1.getString(1);
                                    }
                                    if(amount<=balance ){
                                        double result = balance-amount;
                                          conn = DriverManager.getConnection(DB_URL, "darams7291", "1463877");
                                          Statement stat3 = null; 
                                          stat3 = conn.createStatement();
                                          int n = 0;
                                          if(update == false){
                                              Statement stat2 = null;
                                                stat2 = conn.createStatement();
                                        rs2= stat2.executeQuery("select * from nextordernum");     
                                        if(rs2.next()){
                                             n = rs2.getInt(1);
                                        }
                                              id=n;
                                             
                                        int t = stat3.executeUpdate("insert into buying values ('B"+n+"','"+acc+"','"+ss+"','"+num+"','"+num1+
                                                "','"+orders_new.get(0).getPrice()+"','MO','pending','"+DateandTime.DateTime()+"','"+DateandTime.DateTime()+"')");
                                        t = stat3.executeUpdate("update selling set share_count='"+0+"',status='completed',date_updated='"+DateandTime.DateTime()+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"' and order_id='"+orders_new.get(0).getOrder_id()+"'");
                                        t = stat3.executeUpdate("update nextordernum set nextnum="+(n+1));
                                        t = stat3.executeUpdate("update brokerageaccount set balance = balance+'"+amount+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'");
                                        t = stat3.executeUpdate("update brokerageaccount set balance=balance-'"+amount+"' where login_id='"+loginID+"'");
                                        rs = stat3.executeQuery("select * from sharescount where brokerageaccnum='"+acc+"' and stock_symbol='"+ss+"'");
                                        if(rs.next()){
                                            int t1= stat3.executeUpdate("update sharescount set count=count + '"+orders_new.get(0).getNo_of_shares()+"' where brokerageaccnum='"+acc+"'and stock_symbol='"+ss+"'");
                                        }

                                        else{
                                            int t1 = stat3.executeUpdate("insert into sharescount values ('"+acc+"','"+ss+"','"+orders_new.get(0).getNo_of_shares()+"')");
                                        }
                                        rs = stat3.executeQuery("select * from sharescount where brokerageaccnum ='"+orders_new.get(0).getBrokerageAccNum()+"' and stock_symbol = '"+ss+"'");
                                        if(rs.next()){
                                            int t1= stat3.executeUpdate("update sharescount set count=count - '"+orders_new.get(0).getShare_count()+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'and stock_symbol='"+ss+"'");
                                        }
                                        else{
                                            int t1 = stat3.executeUpdate("insert into sharescount values ('"+orders_new.get(0).getBrokerageAccNum()+"','"+ss+"','"+orders_new.get(0).getShare_count()+"')");
                                        }
                                         update=true;
                                         return ("Order placed!"); 
                                          //new BrokerageAccount().Updatebuy(orders_new.get(0).getPrice(),orders_new.get(0).getPrice(), orders_new.get(0).getShare_count(), ss);
                                          }
                                          else if(update == true){
                                            int t3 =stat3.executeUpdate("update buying set share_count='"+num1+"',price='"+orders_new.get(0).getPrice()+"',status='pending',date_updated='"+DateandTime.DateTime()+"' where order_id='B"+id+"' and brokerageaccnum='"+acc+"'");
                                            t3 = stat3.executeUpdate("update selling set share_count='"+0+"',status='completed',date_updated='"+DateandTime.DateTime()+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"' and order_id='"+orders_new.get(0).getOrder_id()+"'");
                                            
                                             t3 = stat3.executeUpdate("update brokerageaccount set balance = balance+'"+amount+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'");
                                        t3 = stat3.executeUpdate("update brokerageaccount set balance=balance-'"+amount+"' where login_id='"+loginID+"'");
                                        rs = stat3.executeQuery("select * from sharescount where brokerageaccnum='"+acc+"' and stock_symbol='"+ss+"'");
                                        if(rs.next()){
                                            int t1= stat3.executeUpdate("update sharescount set count=count + '"+orders_new.get(0).getShare_count()+"' where brokerageaccnum='"+acc+"'and stock_symbol='"+ss+"'");
                                        }

                                        else{
                                            int t1 = stat3.executeUpdate("insert into sharescount values ('"+acc+"','"+ss+"','"+orders_new.get(0).getShare_count()+"')");
                                        }
                                        rs = stat3.executeQuery("select * from sharescount where brokerageaccnum ='"+orders_new.get(0).getBrokerageAccNum()+"' and stock_symbol = '"+ss+"'");
                                        if(rs.next()){
                                            int t1= stat3.executeUpdate("update sharescount set count=count - '"+orders_new.get(0).getShare_count()+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'and stock_symbol='"+ss+"'");
                                        }
                                        else{
                                            int t1 = stat3.executeUpdate("insert into sharescount values ('"+orders_new.get(0).getBrokerageAccNum()+"','"+ss+"','"+orders_new.get(0).getShare_count()+"')");
                                        }
                                         //new BrokerageAccount().Updatebuy(orders_new.get(0).getPrice(),orders_new.get(0).getPrice(), orders_new.get(0).getShare_count(), ss);
                                         return ("order placed!");
                                          }
                                       num = num1;
                                }
                                    else{
                                        return ("Balance  for next transaction!");
                                       // flag1 = false;f1=false;f=false;
                                       //break;
                                    }
                            }
                            else if(num == mainrs.getInt(5)){
                                
                                amount = num*mainrs.getDouble(6);
                                    Statement stat1 = null;
                                    stat1 = conn.createStatement();
                                    rs1 = stat1.executeQuery("select * from brokerageaccount where login_id='"+loginID+"'");
                                    if(rs1.next()){
                                         balance = rs1.getDouble(5);
                                         acc = rs1.getString(1);
                                    }
                                    if(amount<=balance ){
                                        double result = balance-amount;
                                        Statement stat2 = conn.createStatement();
                                        rs2= stat2.executeQuery("select * from nextordernum");
                                        int n = 0;
                                        if(rs2.next()){
                                             n = rs2.getInt(1);
                                        }
                                        id =n;
                                          Statement stat3 = conn.createStatement();
                                           stat3 = conn.createStatement();
                                           if(update == false){
                                        int t = stat3.executeUpdate("insert into buying values ('B"+n+"','"+acc+"','"+ss+"','"+num+"','"+0+
                                                "','"+orders_new.get(0).getPrice()+"','MO','completed','"+DateandTime.DateTime()+"','"+DateandTime.DateTime()+"')");
                                        t = stat3.executeUpdate("update selling set share_count='"+0+"',status='completed',date_updated='"+DateandTime.DateTime()+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"' and order_id='"+orders_new.get(0).getOrder_id()+"'");
                                        t = stat3.executeUpdate("update nextordernum set nextnum="+(n+1));
                                        t = stat3.executeUpdate("update brokerageaccount set balance = balance+'"+amount+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'");
                                        t = stat3.executeUpdate("update brokerageaccount set balance='"+result+"' where login_id='"+loginID+"'");
                                        rs = stat3.executeQuery("select * from sharescount where brokerageaccnum='"+acc+"' and stock_symbol='"+ss+"'");
                                        if(rs.next()){
                                            int t1= stat3.executeUpdate("update sharescount set count=count + '"+num+"' where brokerageaccnum='"+acc+"'and stock_symbol='"+ss+"'");
                                        }

                                        else{
                                            int t1 = stat3.executeUpdate("insert into sharescount values ('"+acc+"','"+ss+"','"+num+"')");
                                        }
                                        
                                        rs = stat3.executeQuery("select * from sharescount where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"' and stock_symbol = '"+ss+"'");
                                        if(rs.next()){
                                            int t1= stat3.executeUpdate("update sharescount set count=count - '"+num+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'and stock_symbol='"+ss+"'");
                                        }
                                        else{
                                            int t1 = stat3.executeUpdate("insert into sharescount values ('"+orders_new.get(0).getBrokerageAccNum()+"','"+ss+"','"+num+"')");
                                        }
                                        update = true;
                                       
                                         //new BrokerageAccount().Updatebuy(orders_new.get(0).getPrice(),orders_new.get(0).getPrice(), num, ss);
                                         return ("Order placed!");
                                           }
                                           else if(update == true){
                                            int t3 =stat3.executeUpdate("update buying set share_count='"+0+"',price='"+orders_new.get(0).getPrice()+"',status='completed',date_updated='"+DateandTime.DateTime()+"' where order_id='B"+id+"' and brokerageaccnum='"+acc+"'");
                                            t3 = stat3.executeUpdate("update selling set share_count='"+0+"',status='completed',date_updated='"+DateandTime.DateTime()+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"' and order_id='"+orders_new.get(0).getOrder_id()+"'");
                                            
                                             t3 = stat3.executeUpdate("update brokerageaccount set balance = balance+'"+amount+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'");
                                        t3 = stat3.executeUpdate("update brokerageaccount set balance='"+result+"' where login_id='"+loginID+"'");
                                        rs = stat3.executeQuery("select * from sharescount where brokerageaccnum='"+acc+"' and stock_symbol='"+ss+"'");
                                        if(rs.next()){
                                            int t1= stat3.executeUpdate("update sharescount set count=count + '"+num+"' where brokerageaccnum='"+acc+"'and stock_symbol='"+ss+"'");
                                        }

                                        else{
                                            int t1 = stat3.executeUpdate("insert into sharescount values ('"+acc+"','"+ss+"','"+num+"')");
                                        }
                                        rs = stat3.executeQuery("select * from sharescount where brokerageaccnum ='"+orders_new.get(0).getBrokerageAccNum()+"' and stock_symbol = '"+ss+"'");
                                        if(rs.next()){
                                            int t1= stat3.executeUpdate("update sharescount set count=count - '"+orders_new.get(0).getShare_count()+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'and stock_symbol='"+ss+"'");
                                        }
                                        else{
                                            int t1 = stat3.executeUpdate("insert into sharescount values ('"+orders_new.get(0).getBrokerageAccNum()+"','"+ss+"','"+orders_new.get(0).getShare_count()+"')");
                                        }
                                         //new BrokerageAccount().Updatebuy(orders_new.get(0).getPrice(),orders_new.get(0).getPrice(), num, ss);
                                          }
                                        break;
                                }
                                    else{
                                        return ("Balance insufficient!");
                                        //f1=false;f=false;flag1=false;
                                        //break;
                                    }
                                
                            }
                        }
                           }
                           else if(c ==0){
                               Statement stat2 = conn.createStatement();
                                        rs2= stat2.executeQuery("select * from nextordernum");
                                        String acc1 = "";
                                        int n = 0;
                                        if(rs2.next()){
                                             n = rs2.getInt(1);
                                        }
                                        rs2 = stat2.executeQuery("select brokerageaccnum from brokerageaccount where login_id='"+loginID+"'");
                                        if(rs2.next()){
                                         acc1 = rs2.getString(1);
                                        }
                               int t2 = mainstat.executeUpdate("insert into buying values ('B"+n+"','"+acc1+"','"+ss+"','"+num+"','"+num+
                                                "','"+0+"','MO','pending','"+DateandTime.DateTime()+"','"+DateandTime.DateTime()+"')");
                                int t = mainstat.executeUpdate("update nextordernum set nextnum="+(n+1));
                                return ("Order placed!");
                                
                           }
                       //f1 = false;f=false;
                 
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
        if (order_type.equals("sell") && order_category.equals("market")) {
            final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/darams7291";
            Connection conn = null;
            Statement stat = null;
            ResultSet rs = null;
            ResultSet rs1 = null;
            ResultSet rs2 = null;
           try{
                    conn = DriverManager.getConnection(DB_URL,"darams7291","1463877");
                   stat = conn.createStatement();
                    //rs = stat.executeQuery("select stock_symbol from stocks");
//                    while(rs.next()){
                   //   if(ss.equalsIgnoreCase(rs.getString(1))){
//                            count++;
//                            break;
//                        }
//                        else{
//                            count = 0;
//                        }
//                    }
                 //  if(count == 1){
                        boolean f1 = true;
                        while(f1){
                            double balance = 0;
                        //System.out.println("Enter number of shares:");
                        //int num = sc.nextInt();
                        conn = DriverManager.getConnection(DB_URL, "darams7291", "1463877");
                        Statement mainstat = null;
                        mainstat = conn.createStatement();
                        rs = stat.executeQuery("select brokerageaccnum from brokerageaccount where login_id='"+loginID+"'");
                        String accountnum = "";
                        if(rs.next()){
                             accountnum = rs.getString(1);
                        }
                        
                        rs = stat.executeQuery("select count from sharescount where brokerageaccnum='"+accountnum+"' and stock_symbol='"+ss+"'");
                         if(rs.next()){
                             if(num<=rs.getInt(1)){    
                        if(num>0){
                            Statement st = null;
                            st = conn.createStatement();int c = 0;ResultSet r = null;
                            r = st.executeQuery("select stock_symbol from buying where status='pending'");
                           while(r.next()){
                               if(ss.equalsIgnoreCase(r.getString(1))){
                                   c++;
                                   break;
                               }
                               else{
                                   c=0;
                               }
                           }
                           if(c>0){
                               ResultSet mainrs = null;boolean update = false;int id = 0;
                        mainrs = mainstat.executeQuery("select * from buying where stock_symbol='"+ss+"'and status = 'pending' and Price is not null order by Price desc" );
                        while(mainrs.next()){
                            ArrayList<Orders> orders_new = new ArrayList<Orders>();
                            orders_new.add(new Orders(mainrs.getString(1),mainrs.getString(2),mainrs.getString(3),mainrs.getInt(4),mainrs.getInt(5),mainrs.getDouble(6),mainrs.getString(7), mainrs.getString(8)));
                            String acc = "";double amount = 0;
                /*Count check*/   if(num< mainrs.getInt(5)){
                                    amount = num*mainrs.getDouble(6);
                                    Statement stat1 = null;
                                    stat1 = conn.createStatement();
                                    rs1 = stat1.executeQuery("select * from brokerageaccount where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'");
                                    if(rs1.next()){
                                         balance = rs1.getDouble(5);
                                         acc = rs1.getString(1);
                                    }
                                    if(amount<=balance ){
                                        double result = balance-amount;
                                        int n = 0;
                                          Statement stat3 = conn.createStatement();
                                           stat3 = conn.createStatement();
                                           if(update == false){
                                               Statement stat2 = conn.createStatement();
                                        rs2= stat2.executeQuery("select * from nextordernum");
                                        
                                        if(rs2.next()){
                                             n = rs2.getInt(1);
                                        }
                                      id = n;
                                        int t = stat3.executeUpdate("insert into selling values ('S"+n+"','"+accountnum+"','"+ss+"','"+num+"','"+0+
                                                "','"+orders_new.get(0).getPrice()+"','MO','completed','"+DateandTime.DateTime().toString()+"','"+DateandTime.DateTime().toString()+"')");
                                        t = stat3.executeUpdate("update buying set share_count='"+(orders_new.get(0).getShare_count()- num)+"',date_updated='"+DateandTime.DateTime().toString()+ "' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"' and order_id='"+orders_new.get(0).getOrder_id()+"'");
                                        t = stat3.executeUpdate("update nextordernum set nextnum="+(n+1));
                                        t = stat3.executeUpdate("update brokerageaccount set balance = '"+result+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'");
                                        t = stat3.executeUpdate("update brokerageaccount set balance= balance+'"+amount+"' where login_id='"+loginID+"'");
                                        int t1= stat3.executeUpdate("update sharescount set count=count - '"+num+"' where brokerageaccnum='"+accountnum+"'and stock_symbol='"+ss+"'");
                                        rs = stat3.executeQuery("select * from sharescount where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"' and stock_symbol = '"+ss+"'");
                                        if(rs.next()){
                                             t1= stat3.executeUpdate("update sharescount set count=count + '"+num+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'and stock_symbol='"+ss+"'");
                                        }
                                        else{
                                             t1 = stat3.executeUpdate("insert into sharescount values ('"+orders_new.get(0).getBrokerageAccNum()+"','"+ss+"','"+num+"')");
                                        }
                                        update = true;
                                        return ("Order placed!");
                                       // new BrokerageAccount().UpdateSell(orders_new.get(0).getPrice(), orders_new.get(0).getPrice(), num, ss);
                                           }
                                            else if(update == true){
                                            int t3 =stat3.executeUpdate("update selling set share_count='"+0+"',price='"+orders_new.get(0).getPrice()+"',status='completed',date_updated='"+DateandTime.DateTime()+"' where order_id='S"+id+"' and brokerageaccnum='"+accountnum+"'");
                                            t3 = stat3.executeUpdate("update buying set share_count='"+(orders_new.get(0).getShare_count()-num)+"',status='pending',date_updated='"+DateandTime.DateTime()+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"' and order_id='"+orders_new.get(0).getOrder_id()+"'");
                                            
                                             t3 = stat3.executeUpdate("update brokerageaccount set balance = balance-'"+amount+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'");
                                        t3 = stat3.executeUpdate("update brokerageaccount set balance=balance+'"+amount+"' where login_id='"+loginID+"'");
                                       
                                       int t1= stat3.executeUpdate("update sharescount set count=count - '"+num+"' where brokerageaccnum='"+accountnum+"'and stock_symbol='"+ss+"'");
                                        
                                        rs = stat3.executeQuery("select * from sharescount where brokerageaccnum ='"+orders_new.get(0).getBrokerageAccNum()+"' and stock_symbol = '"+ss+"'");
                                        if(rs.next()){
                                            t1= stat3.executeUpdate("update sharescount set count=count + '"+orders_new.get(0).getShare_count()+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'and stock_symbol='"+ss+"'");
                                        }
                                        else{
                                            t1 = stat3.executeUpdate("insert into sharescount values ('"+orders_new.get(0).getBrokerageAccNum()+"','"+ss+"','"+orders_new.get(0).getShare_count()+"')");
                                        }
                                       // new BrokerageAccount().UpdateSell(orders_new.get(0).getPrice(), orders_new.get(0).getPrice(), num, ss);
                                          }
                                        
                                        break;
                                }
                                    else{
                                        
                                        continue;
                                        
                                    }
                            }
                            else if(num > mainrs.getInt(5)){
                                //System.out.println("Does not have stocks");
                                 int num1 = num - mainrs.getInt(5);
                                      amount = mainrs.getInt(5)*mainrs.getDouble(6);
                                      Statement stat1 = null;
                                              stat1 = conn.createStatement();
                                    rs1 = stat1.executeQuery("select * from brokerageaccount where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'");
                                    if(rs1.next()){
                                         balance = rs1.getDouble(5);
                                         acc = rs1.getString(1);
                                    }
                                    if(amount<=balance ){
                                        double result = balance-amount;
                                          conn = DriverManager.getConnection(DB_URL, "darams7291", "1463877");
                                          Statement stat3 = null; 
                                          stat3 = conn.createStatement();
                                          int n = 0;
                                          if(update == false){
                                              Statement stat2 = null;
                                                stat2 = conn.createStatement();
                                        rs2= stat2.executeQuery("select * from nextordernum");     
                                        if(rs2.next()){
                                             n = rs2.getInt(1);
                                        }
                                              id=n;
                                             
                                        int t = stat3.executeUpdate("insert into selling values ('S"+n+"','"+accountnum+"','"+ss+"','"+num+"','"+num1+
                                                "','"+orders_new.get(0).getPrice()+"','MO','pending','"+DateandTime.DateTime()+"','"+DateandTime.DateTime()+"')");
                                        t = stat3.executeUpdate("update buying set share_count='"+0+"',status='completed',date_updated='"+DateandTime.DateTime()+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"' and order_id='"+orders_new.get(0).getOrder_id()+"'");
                                        t = stat3.executeUpdate("update nextordernum set nextnum="+(n+1));
                                        t = stat3.executeUpdate("update brokerageaccount set balance = balance-'"+amount+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'");
                                        t = stat3.executeUpdate("update brokerageaccount set balance=balance+'"+amount+"' where login_id='"+loginID+"'");
                                       
                                            int t1= stat3.executeUpdate("update sharescount set count=count - '"+orders_new.get(0).getShare_count()+"' where brokerageaccnum='"+accountnum+"'and stock_symbol='"+ss+"'");
                       
                                        rs = stat3.executeQuery("select * from sharescount where brokerageaccnum ='"+orders_new.get(0).getBrokerageAccNum()+"' and stock_symbol = '"+ss+"'");
                                        if(rs.next()){
                                             t1= stat3.executeUpdate("update sharescount set count=count + '"+orders_new.get(0).getShare_count()+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'and stock_symbol='"+ss+"'");
                                        }
                                        else{
                                             t1 = stat3.executeUpdate("insert into sharescount values ('"+orders_new.get(0).getBrokerageAccNum()+"','"+ss+"','"+orders_new.get(0).getShare_count()+"')");
                                        }
                                         update=true;
                                         return ("Order placed!");
                                        // new BrokerageAccount().UpdateSell(orders_new.get(0).getPrice(), orders_new.get(0).getPrice(), orders_new.get(0).getShare_count(), ss);
                                          }
                                          else if(update == true){
                                            int t3 =stat3.executeUpdate("update selling set share_count='"+num1+"',price='"+orders_new.get(0).getPrice()+"',status='pending',date_updated='"+DateandTime.DateTime()+"' where order_id='S"+id+"' and brokerageaccnum='"+accountnum+"'");
                                            t3 = stat3.executeUpdate("update buying set share_count='"+0+"',status='completed',date_updated='"+DateandTime.DateTime()+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"' and order_id='"+orders_new.get(0).getOrder_id()+"'");
                                            
                                             t3 = stat3.executeUpdate("update brokerageaccount set balance = balance-'"+amount+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'");
                                        t3 = stat3.executeUpdate("update brokerageaccount set balance=balance+'"+amount+"' where login_id='"+loginID+"'");
                                       
                                            int t1= stat3.executeUpdate("update sharescount set count=count - '"+num1+"' where brokerageaccnum='"+accountnum+"'and stock_symbol='"+ss+"'");
                                        

                                       
                                        rs = stat3.executeQuery("select * from sharescount where brokerageaccnum ='"+orders_new.get(0).getBrokerageAccNum()+"' and stock_symbol = '"+ss+"'");
                                        if(rs.next()){
                                             t1= stat3.executeUpdate("update sharescount set count=count + '"+orders_new.get(0).getShare_count()+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'and stock_symbol='"+ss+"'");
                                        }
                                        else{
                                             t1 = stat3.executeUpdate("insert into sharescount values ('"+orders_new.get(0).getBrokerageAccNum()+"','"+ss+"','"+orders_new.get(0).getShare_count()+"')");
                                        }
                                        //new BrokerageAccount().UpdateSell(orders_new.get(0).getPrice(), orders_new.get(0).getPrice(), orders_new.get(0).getShare_count(), ss);
                                          }
                                       num = num1;
                                }
                                    else{
                                        continue;
                                    }
                            }
                            else if(num == mainrs.getInt(5)){
                                
                                amount = num*mainrs.getDouble(6);
                                    Statement stat1 = null;
                                    stat1 = conn.createStatement();
                                    rs1 = stat1.executeQuery("select * from brokerageaccount where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'");
                                    if(rs1.next()){
                                         balance = rs1.getDouble(5);
                                         acc = rs1.getString(1);
                                    }
                                    if(amount<=balance ){
                                        double result = balance-amount;
                                        Statement stat2 = conn.createStatement();
                                        rs2= stat2.executeQuery("select * from nextordernum");
                                        int n = 0;
                                        if(rs2.next()){
                                             n = rs2.getInt(1);
                                        }
                                        id =n;
                                          Statement stat3 = conn.createStatement();
                                           stat3 = conn.createStatement();
                                           if(update == false){
                                        int t = stat3.executeUpdate("insert into selling values ('S"+n+"','"+accountnum+"','"+ss+"','"+num+"','"+0+
                                                "','"+orders_new.get(0).getPrice()+"','MO','completed','"+DateandTime.DateTime()+"','"+DateandTime.DateTime()+"')");
                                        t = stat3.executeUpdate("update buying set share_count='"+0+"',status='completed',date_updated='"+DateandTime.DateTime()+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"' and order_id='"+orders_new.get(0).getOrder_id()+"'");
                                        t = stat3.executeUpdate("update nextordernum set nextnum="+(n+1));
                                        t = stat3.executeUpdate("update brokerageaccount set balance = balance-'"+amount+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'");
                                        t = stat3.executeUpdate("update brokerageaccount set balance=balance+'"+amount+"' where login_id='"+loginID+"'");
                                       
                                            int t1= stat3.executeUpdate("update sharescount set count=count - '"+num+"' where brokerageaccnum='"+accountnum+"'and stock_symbol='"+ss+"'");
                                        
                                        
                                        rs = stat3.executeQuery("select * from sharescount where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"' and stock_symbol = '"+ss+"'");
                                        if(rs.next()){
                                             t1= stat3.executeUpdate("update sharescount set count=count + '"+num+"' where brokerageaccnum='"+acc+"'and stock_symbol='"+ss+"'");
                                        }
                                        else{
                                             t1 = stat3.executeUpdate("insert into sharescount values ('"+orders_new.get(0).getBrokerageAccNum()+"','"+ss+"','"+num+"')");
                                        }
                                        update = true;
                                        return ("Order placed!");
                                        //new BrokerageAccount().UpdateSell(orders_new.get(0).getPrice(), orders_new.get(0).getPrice(), num, ss);
                                           }
                                           else if(update == true){
                                            int t3 =stat3.executeUpdate("update selling set share_count='"+0+"',price='"+orders_new.get(0).getPrice()+"',status='completed',date_updated='"+DateandTime.DateTime()+"' where order_id='S"+id+"' and brokerageaccnum='"+accountnum+"'");
                                            t3 = stat3.executeUpdate("update buying set share_count='"+0+"',status='completed',date_updated='"+DateandTime.DateTime()+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"' and order_id='"+orders_new.get(0).getOrder_id()+"'");
                                            
                                             t3 = stat3.executeUpdate("update brokerageaccount set balance = balance-'"+amount+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'");
                                        t3 = stat3.executeUpdate("update brokerageaccount set balance=balance+'"+amount+"' where login_id='"+loginID+"'");
                                       
                                            int t1= stat3.executeUpdate("update sharescount set count=count - '"+num+"' where brokerageaccnum='"+accountnum+"'and stock_symbol='"+ss+"'");
                                       
                                        rs = stat3.executeQuery("select * from sharescount where brokerageaccnum ='"+orders_new.get(0).getBrokerageAccNum()+"' and stock_symbol = '"+ss+"'");
                                        if(rs.next()){
                                             t1= stat3.executeUpdate("update sharescount set count=count + '"+orders_new.get(0).getShare_count()+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'and stock_symbol='"+ss+"'");
                                        }
                                        else{
                                             t1 = stat3.executeUpdate("insert into sharescount values ('"+orders_new.get(0).getBrokerageAccNum()+"','"+ss+"','"+orders_new.get(0).getShare_count()+"')");
                                        }
                                        //new BrokerageAccount().UpdateSell(orders_new.get(0).getPrice(), orders_new.get(0).getPrice(), num, ss);
                                          }
                                        break;
                                }
                                    else{
                                        continue;
                                    }
                                
                            }
                        }
                           }
                           else if(c ==0){
                               Statement stat2 = conn.createStatement();
                                        rs2= stat2.executeQuery("select * from nextordernum");
                                        String acc1 = "";
                                        int n = 0;
                                        if(rs2.next()){
                                             n = rs2.getInt(1);
                                        }
                                        rs2 = stat2.executeQuery("select brokerageaccnum from brokerageaccount where login_id='"+loginID+"'");
                                        if(rs2.next()){
                                         acc1 = rs2.getString(1);
                                        }
                               int t2 = mainstat.executeUpdate("insert into selling values ('S"+n+"','"+acc1+"','"+ss+"','"+num+"','"+num+
                                                "','"+0+"','MO','pending','"+DateandTime.DateTime()+"','"+DateandTime.DateTime()+"')");
                                int t = mainstat.executeUpdate("update nextordernum set nextnum="+(n+1));
                               return ("Order placed!");
                                
                           }
                      // f1 = false;f=false;
                        } 
                        else if(num<=0){
                           return ("Number of shares cannot be 0 or negative!");
                            //f1 = true;
                        }
                             }
                             else{
                                 return ("You do not have enough shares to sell!Try selling some other stocks.");
                                // flag1 = false;f1 = false;f=false;
                             }
                         }
                         else{
                             return ("You do not have shares for this Stock!");
                             //flag1=false;
                             //f1=false;
                             //f=false;
                         }
                    }
                   
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                 
        }
        if (order_type.equals("buy") && order_category.equals("limit")) {
            
             final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/darams7291";
            Connection conn = null;
            Statement stat = null;
            ResultSet rs = null;
            ResultSet rs1 = null;
            ResultSet rs2 = null;
           try{
                    conn = DriverManager.getConnection(DB_URL,"darams7291","1463877");
                   stat = conn.createStatement();
                    //rs = stat.executeQuery("select stock_symbol from stocks");
                    //while(rs.next()){
                      //  if(ss.equalsIgnoreCase(rs.getString(1))){
                        //    count++;
                          //  break;
                        //}
                        //else{
                          //  count = 0;
                        //}
                    //}
                    //if(count>0){
                        boolean flagn = true;
                        while(flagn){
                           // System.out.println("Enter number of stocks:");
                           // int num = sc.nextInt();
                            if(num>0){
                                boolean flagp = true;
                                while(flagp){
                             //       System.out.println("Enter the price:");
                               //     double price = sc.nextDouble();
                                    if(price>0){
                                        Statement st = null;
                            st = conn.createStatement();int c = 0;ResultSet r = null;
                            r = st.executeQuery("select stock_symbol from selling where status='pending'");
                           while(r.next()){
                               if(ss.equalsIgnoreCase(r.getString(1))){
                                   c++;
                                   break;
                               }
                               else{
                                   c=0;
                               }
                           }
                           if(c>0){
                                        double amount = num * price;
                                        rs = stat.executeQuery("select balance from brokerageaccount where login_id='"+loginID+"'");
                                        if(rs.next()){
                                            if(rs.getDouble(1)>amount){
                                                Statement mains = null;
                                                mains = conn.createStatement();ResultSet mr = null;
                                                boolean update = false;int id =0;String accnum = "";
                                                mr = mains.executeQuery("select * from selling where stock_symbol='"+ss+"'and status='pending' and price<'"+price+"' order by price asc");
                                           while(mr.next()){
                                             ArrayList<Orders> orders_new = new ArrayList<Orders>();
                                             orders_new.add(new Orders(mr.getString(1),mr.getString(2),mr.getString(3),mr.getInt(4),mr.getInt(5),mr.getDouble(6),mr.getString(7), mr.getString(8)));
                                             if(num<orders_new.get(0).getShare_count()){
                                                 amount = num * price;
                                                  int n = 0;
                                                  rs2 = stat.executeQuery("select brokerageaccnum from brokerageaccount where login_id='"+loginID+"'");
                                                  if(rs2.next()){
                                                      accnum = rs2.getString(1);
                                                  }
                                          Statement stat3 = conn.createStatement();
                                           stat3 = conn.createStatement();
                                      if(update == false){
                                               Statement stat2 = conn.createStatement();
                                        rs2= stat2.executeQuery("select * from nextordernum");
                                        
                                        if(rs2.next()){
                                             n = rs2.getInt(1);
                                        }
                                      id = n;
                                        int t = stat3.executeUpdate("insert into buying values ('B"+n+"','"+accnum+"','"+ss+"','"+num+"','"+0+
                                                "','"+price+"','LO','completed','"+DateandTime.DateTime().toString()+"','"+DateandTime.DateTime().toString()+"')");
                                        t = stat3.executeUpdate("update selling set share_count='"+(orders_new.get(0).getShare_count()- num)+"',date_updated='"+DateandTime.DateTime().toString()+ "' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"' and order_id='"+orders_new.get(0).getOrder_id()+"'");
                                        t = stat3.executeUpdate("update nextordernum set nextnum="+(n+1));
                                        t = stat3.executeUpdate("update brokerageaccount set balance = balance+'"+amount+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'");
                                        t = stat3.executeUpdate("update brokerageaccount set balance=balance-'"+amount+"' where login_id='"+loginID+"'");
                                        rs = stat3.executeQuery("select * from sharescount where brokerageaccnum='"+accnum+"' and stock_symbol='"+ss+"'");
                                        if(rs.next()){
                                            int t1= stat3.executeUpdate("update sharescount set count=count + '"+num+"' where brokerageaccnum='"+accnum+"'and stock_symbol='"+ss+"'");
                                        }

                                        else{
                                            int t1 = stat3.executeUpdate("insert into sharescount values ('"+accnum+"','"+ss+"','"+num+"')");
                                        }
                                        
                                            int t1= stat3.executeUpdate("update sharescount set count=count - '"+num+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'and stock_symbol='"+ss+"'");
                                      
                                        update = true;
                                       return ("Order placed!");
                                        //new BrokerageAccount().Updatebuy(price, price, num, ss);
                                           }
                                      else if(update == true){
                                    int t =stat3.executeUpdate("update buying set share_count='"+0+"',price='"+price+"',status='completed',date_updated='"+DateandTime.DateTime()+"' where order_id='B"+id+"' and brokerageaccnum='"+accnum+"'");
                                        t = stat3.executeUpdate("update selling set share_count='"+(orders_new.get(0).getShare_count()- num)+"',status='pending',date_updated='"+DateandTime.DateTime().toString()+ "' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"' and order_id='"+orders_new.get(0).getOrder_id()+"'");
                                       
                                        t = stat3.executeUpdate("update brokerageaccount set balance = balance+'"+amount+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'");
                                        t = stat3.executeUpdate("update brokerageaccount set balance=balance-'"+amount+"' where login_id='"+loginID+"'");
                                        rs = stat3.executeQuery("select * from sharescount where brokerageaccnum='"+accnum+"' and stock_symbol='"+ss+"'");
                                        if(rs.next()){
                                            int t1= stat3.executeUpdate("update sharescount set count=count + '"+num+"' where brokerageaccnum='"+accnum+"'and stock_symbol='"+ss+"'");
                                        }

                                        else{
                                            int t1 = stat3.executeUpdate("insert into sharescount values ('"+accnum+"','"+ss+"','"+num+"')");
                                        }
                                        
                                            int t1= stat3.executeUpdate("update sharescount set count=count - '"+num+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'and stock_symbol='"+ss+"'");
                                     // new BrokerageAccount().Updatebuy(price, price, num, stocksym);
                                     return ("Order placed!");
                                      }
                                      break;
                                             }
                                             else if(num>orders_new.get(0).getShare_count()){
                                                 int num1 = num- orders_new.get(0).getShare_count();
                                                 amount = orders_new.get(0).getShare_count() * price;
                                                           int n = 0;
                                                  rs2 = stat.executeQuery("select brokerageaccnum from brokerageaccount where login_id='"+loginID+"'");
                                                  if(rs2.next()){
                                                      accnum = rs2.getString(1);
                                                  }
                                          Statement stat3 = conn.createStatement();
                                           stat3 = conn.createStatement();
                                      if(update == false){
                                               Statement stat2 = conn.createStatement();
                                        rs2= stat2.executeQuery("select * from nextordernum");
                                        
                                        if(rs2.next()){
                                             n = rs2.getInt(1);
                                        }
                                      id = n;
                                        int t = stat3.executeUpdate("insert into buying values ('B"+n+"','"+accnum+"','"+ss+"','"+num+"','"+num1+
                                                "','"+price+"','LO','pending','"+DateandTime.DateTime().toString()+"','"+DateandTime.DateTime().toString()+"')");
                                        t = stat3.executeUpdate("update selling set share_count='"+0+"',status='completed',date_updated='"+DateandTime.DateTime().toString()+ "' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"' and order_id='"+orders_new.get(0).getOrder_id()+"'");
                                        t = stat3.executeUpdate("update nextordernum set nextnum="+(n+1));
                                        t = stat3.executeUpdate("update brokerageaccount set balance = balance+'"+amount+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'");
                                        t = stat3.executeUpdate("update brokerageaccount set balance=balance-'"+amount+"' where login_id='"+loginID+"'");
                                        rs = stat3.executeQuery("select * from sharescount where brokerageaccnum='"+accnum+"' and stock_symbol='"+ss+"'");
                                        if(rs.next()){
                                            int t1= stat3.executeUpdate("update sharescount set count=count + '"+orders_new.get(0).getShare_count()+"' where brokerageaccnum='"+accnum+"'and stock_symbol='"+ss+"'");
                                        }

                                        else{
                                            int t1 = stat3.executeUpdate("insert into sharescount values ('"+accnum+"','"+ss+"','"+orders_new.get(0).getNo_of_shares()+"')");
                                        }
                                        
                                            int t1= stat3.executeUpdate("update sharescount set count=count - '"+orders_new.get(0).getShare_count()+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'and stock_symbol='"+ss+"'");
                                      
                                        update = true;
                                       return("Order placed!");
                                       // new BrokerageAccount().Updatebuy(price, price, orders_new.get(0).getShare_count(), stocksym);
                                           }
                                      else if(update == true){
                                    int t =stat3.executeUpdate("update buying set share_count='"+num1+"',price='"+price+"',status='pending',date_updated='"+DateandTime.DateTime()+"' where order_id='B"+id+"' and brokerageaccnum='"+accnum+"'");
                                        t = stat3.executeUpdate("update selling set share_count='"+0+"',status='completed',date_updated='"+DateandTime.DateTime().toString()+ "' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"' and order_id='"+orders_new.get(0).getOrder_id()+"'");
                                       
                                        t = stat3.executeUpdate("update brokerageaccount set balance = balance+'"+amount+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'");
                                        t = stat3.executeUpdate("update brokerageaccount set balance=balance-'"+amount+"' where login_id='"+loginID+"'");
                                        rs = stat3.executeQuery("select * from sharescount where brokerageaccnum='"+accnum+"' and stock_symbol='"+ss+"'");
                                        if(rs.next()){
                                            int t1= stat3.executeUpdate("update sharescount set count=count + '"+orders_new.get(0).getNo_of_shares()+"' where brokerageaccnum='"+accnum+"'and stock_symbol='"+ss+"'");
                                        }

                                        else{
                                            int t1 = stat3.executeUpdate("insert into sharescount values ('"+accnum+"','"+ss+"','"+orders_new.get(0).getShare_count()+"')");
                                        }
                                        
                                            int t1= stat3.executeUpdate("update sharescount set count=count - '"+orders_new.get(0).getShare_count()+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'and stock_symbol='"+ss+"'");
                                      //new BrokerageAccount().Updatebuy(price, price, orders_new.get(0).getShare_count(), stocksym);
                                     return ("Order placed!");
                                      } 
                                                 
                                       num = num1;          
                                               
                                             }
                                             else if(num==orders_new.get(0).getShare_count()){
                                                 amount = num * price;
                                                  int n = 0;
                                                  rs2 = stat.executeQuery("select brokerageaccnum from brokerageaccount where login_id='"+loginID+"'");
                                                  if(rs2.next()){
                                                      accnum = rs2.getString(1);
                                                  }
                                          Statement stat3 = conn.createStatement();
                                           stat3 = conn.createStatement();
                                      if(update == false){
                                               Statement stat2 = conn.createStatement();
                                        rs2= stat2.executeQuery("select * from nextordernum");
                                        
                                        if(rs2.next()){
                                             n = rs2.getInt(1);
                                        }
                                      id = n;
                                        int t = stat3.executeUpdate("insert into buying values ('B"+n+"','"+accnum+"','"+ss+"','"+num+"','"+0+
                                                "','"+price+"','LO','completed','"+DateandTime.DateTime().toString()+"','"+DateandTime.DateTime().toString()+"')");
                                        t = stat3.executeUpdate("update selling set share_count='"+(orders_new.get(0).getShare_count()- num)+"',status='pending',date_updated='"+DateandTime.DateTime().toString()+ "' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"' and order_id='"+orders_new.get(0).getOrder_id()+"'");
                                        t = stat3.executeUpdate("update nextordernum set nextnum="+(n+1));
                                        t = stat3.executeUpdate("update brokerageaccount set balance = balance+'"+amount+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'");
                                        t = stat3.executeUpdate("update brokerageaccount set balance=balance-'"+amount+"' where login_id='"+loginID+"'");
                                        rs = stat3.executeQuery("select * from sharescount where brokerageaccnum='"+accnum+"' and stock_symbol='"+ss+"'");
                                        if(rs.next()){
                                            int t1= stat3.executeUpdate("update sharescount set count=count + '"+num+"' where brokerageaccnum='"+accnum+"'and stock_symbol='"+ss+"'");
                                        }

                                        else{
                                            int t1 = stat3.executeUpdate("insert into sharescount values ('"+accnum+"','"+ss+"','"+num+"')");
                                        }
                                        
                                            int t1= stat3.executeUpdate("update sharescount set count=count - '"+num+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'and stock_symbol='"+ss+"'");
                                      
                                        update = true;
                                       return ("Order placed!");
                                       // new BrokerageAccount().Updatebuy(price, price, num, stocksym);
                                           }
                                      else if(update == true){
                                    int t =stat3.executeUpdate("update buying set share_count='"+0+"',price='"+price+"',status='completed',date_updated='"+DateandTime.DateTime()+"' where order_id='B"+id+"' and brokerageaccnum='"+accnum+"'");
                                        t = stat3.executeUpdate("update selling set share_count='"+0+"',status='completed',date_updated='"+DateandTime.DateTime().toString()+ "' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"' and order_id='"+orders_new.get(0).getOrder_id()+"'");
                                        
                                        t = stat3.executeUpdate("update brokerageaccount set balance = balance+'"+amount+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'");
                                        t = stat3.executeUpdate("update brokerageaccount set balance=balance-'"+amount+"' where login_id='"+loginID+"'");
                                        rs = stat3.executeQuery("select * from sharescount where brokerageaccnum='"+accnum+"' and stock_symbol='"+ss+"'");
                                        if(rs.next()){
                                            int t1= stat3.executeUpdate("update sharescount set count=count + '"+num+"' where brokerageaccnum='"+accnum+"'and stock_symbol='"+ss+"'");
                                        }

                                        else{
                                            int t1 = stat3.executeUpdate("insert into sharescount values ('"+accnum+"','"+ss+"','"+num+"')");
                                        }
                                        
                                            int t1= stat3.executeUpdate("update sharescount set count=count - '"+num+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'and stock_symbol='"+ss+"'");
                                     // new BrokerageAccount().Updatebuy(price, price, num, stocksym);
                                     return ("Order placed!");
                                      }
                                      break;
                                             }
                                           
                                           } 
                                           
                                           }
                                            else{
                                                return ("Balance Insufficient!");
                                               // flagn=false;flagp=false;
                                            }
                                        }
                                        flagp=false;
                                    }
                                    else if(price<=0){
                                        return ("Price cannot be negative or 0!");
                                       // flagp = true;
                                    }
                                    else if(c==0){
                                         Statement stat2 = conn.createStatement();
                                        rs2= stat2.executeQuery("select * from nextordernum");
                                        String acc1 = "";
                                        int n = 0;
                                        if(rs2.next()){
                                             n = rs2.getInt(1);
                                        }
                                        rs2 = stat2.executeQuery("select brokerageaccnum from brokerageaccount where login_id='"+loginID+"'");
                                        if(rs2.next()){
                                         acc1 = rs2.getString(1);
                                        }
                               int t2 = stat.executeUpdate("insert into buying values ('B"+n+"','"+acc1+"','"+ss+"','"+num+"','"+num+
                                                "','"+price+"','LO','pending','"+DateandTime.DateTime()+"','"+DateandTime.DateTime()+"')");
                                int t = stat.executeUpdate("update nextordernum set nextnum="+(n+1));
                                return ("Order placed!");
                                //flagp = false;
                                    }
                                    }
                                }
                                flagn=false;
                            }
                            else if(num<=0){
                                return ("Stocks entered cannot be 0 or negative!");
                              //  flagn = true;
                            }
                        }
                        
                        //flags = false;
             }
             catch(SQLException e){
                 e.printStackTrace();
             }
        }
        if (order_type.equals("sell") && order_category.equals("limit")) {
            final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/darams7291";
            Connection conn = null;
            Statement stat = null;
            ResultSet rs = null;
            ResultSet rs1 = null;
            ResultSet rs2 = null;
           try{
                    conn = DriverManager.getConnection(DB_URL,"darams7291","1463877");
                   stat = conn.createStatement();
                    //rs = stat.executeQuery("select stock_symbol from stocks");
//                    while(rs.next()){
//                        if(stocksym.equalsIgnoreCase(rs.getString(1))){
//                            count++;
//                            break;
//                        }
//                        else{
//                            count = 0;
//                        }
//                    }
                  //  if(count>0){
                        boolean flagn = true;
                        while(flagn){
                           // System.out.println("Enter number of stocks:");
                            //int num = sc.nextInt();
                            if(num>0){
                                String acc_1 = "";
                                rs = stat.executeQuery("select brokerageaccnum from brokerageaccount where login_id='"+loginID+"'");
                                if(rs.next()){
                                    acc_1 = rs.getString(1);
                                }
                                rs = stat.executeQuery("select count from sharescount where stock_symbol='"+ss+"' and brokerageaccnum='"+acc_1+"'");
                                if(rs.next()){
                                    if(rs.getInt(1)>num){
                                boolean flagp = true;
                                while(flagp){
                                   // System.out.println("Enter the price:");
                                   // double price = sc.nextDouble();
                                    if(price>0){
                                        Statement st = null;
                            st = conn.createStatement();int c = 0;ResultSet r = null;
                            r = st.executeQuery("select stock_symbol from buying where status='pending'");
                           while(r.next()){
                               if(ss.equalsIgnoreCase(r.getString(1))){
                                   c++;
                                   break;
                               }
                               else{
                                   c=0;
                               }
                           }
                           if(c>0){
                                        double amount = 0;
                                       // rs = stat.executeQuery("select balance from brokerageaccount where login_id='"+loginID+"'");
                                        //if(rs.next()){
                                           // if(rs.getDouble(1)>amount){
                                                Statement mains = null;
                                                mains = conn.createStatement();
                                                boolean update = false;int id =0;String accnum = "";ResultSet mr = null;
                                                mr = mains.executeQuery("select * from buying where stock_symbol='"+ss+"'and status='pending' and price>'"+price+"' order by price desc");
                                           while(mr.next()){
                                             ArrayList<Orders> orders_new = new ArrayList<Orders>();
                                             orders_new.add(new Orders(mr.getString(1),mr.getString(2),mr.getString(3),mr.getInt(4),mr.getInt(5),mr.getDouble(6),mr.getString(7), mr.getString(8)));
                                             if(num<orders_new.get(0).getShare_count()){
                                                 amount = num * price;
                                                  int n = 0;
                                                  rs2 = stat.executeQuery("select brokerageaccnum from brokerageaccount where login_id='"+loginID+"'");
                                                  if(rs2.next()){
                                                      accnum = rs2.getString(1);
                                                  }
                                          Statement stat3 = conn.createStatement();
                                           stat3 = conn.createStatement();
                                      if(update == false){
                                               Statement stat2 = conn.createStatement();
                                        rs2= stat2.executeQuery("select * from nextordernum");
                                        
                                        if(rs2.next()){
                                             n = rs2.getInt(1);
                                        }
                                      id = n;
                                        int t = stat3.executeUpdate("insert into selling values ('S"+n+"','"+accnum+"','"+ss+"','"+num+"','"+0+
                                                "','"+price+"','LO','completed','"+DateandTime.DateTime().toString()+"','"+DateandTime.DateTime().toString()+"')");
                                        t = stat3.executeUpdate("update buying set share_count='"+(orders_new.get(0).getShare_count()- num)+"',date_updated='"+DateandTime.DateTime().toString()+ "' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"' and order_id='"+orders_new.get(0).getOrder_id()+"'");
                                        t = stat3.executeUpdate("update nextordernum set nextnum="+(n+1));
                                        t = stat3.executeUpdate("update brokerageaccount set balance = balance-'"+amount+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'");
                                        t = stat3.executeUpdate("update brokerageaccount set balance=balance+'"+amount+"' where login_id='"+loginID+"'");
                                        rs = stat3.executeQuery("select * from sharescount where brokerageaccnum='"+accnum+"' and stock_symbol='"+ss+"'");
                                        if(rs.next()){
                                            int t1= stat3.executeUpdate("update sharescount set count=count + '"+num+"' where brokerageaccnum='"+accnum+"'and stock_symbol='"+ss+"'");
                                        }

                                        else{
                                            int t1 = stat3.executeUpdate("insert into sharescount values ('"+accnum+"','"+ss+"','"+num+"')");
                                        }
                                        
                                            int t1= stat3.executeUpdate("update sharescount set count=count - '"+num+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'and stock_symbol='"+ss+"'");
                                      
                                        update = true;
                                        return ("Order placed!");
                                       // new BrokerageAccount().UpdateSell(price, price, num, stocksym);
                                           }
                                      else if(update == true){
                                    int t =stat3.executeUpdate("update selling set share_count='"+0+"',price='"+price+"',status='completed',date_updated='"+DateandTime.DateTime()+"' where order_id='S"+id+"' and brokerageaccnum='"+accnum+"'");
                                        t = stat3.executeUpdate("update buying set share_count='"+(orders_new.get(0).getShare_count()- num)+"',status='pending',date_updated='"+DateandTime.DateTime().toString()+ "' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"' and order_id='"+orders_new.get(0).getOrder_id()+"'");
                                       
                                        t = stat3.executeUpdate("update brokerageaccount set balance = balance-'"+amount+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'");
                                        t = stat3.executeUpdate("update brokerageaccount set balance=balance+'"+amount+"' where login_id='"+loginID+"'");
                                        rs = stat3.executeQuery("select * from sharescount where brokerageaccnum='"+accnum+"' and stock_symbol='"+ss+"'");
                                        if(rs.next()){
                                            int t1= stat3.executeUpdate("update sharescount set count=count + '"+num+"' where brokerageaccnum='"+accnum+"'and stock_symbol='"+ss+"'");
                                        }

                                        else{
                                            int t1 = stat3.executeUpdate("insert into sharescount values ('"+accnum+"','"+ss+"','"+num+"')");
                                        }
                                        
                                            int t1= stat3.executeUpdate("update sharescount set count=count - '"+num+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'and stock_symbol='"+ss+"'");
                                      //new BrokerageAccount().UpdateSell(price, price, num, stocksym);
                                      return ("order placed!");
                                      }
                                      break;
                                             }
                                             else if(num>orders_new.get(0).getShare_count()){
                                                 int num1 = orders_new.get(0).getShare_count() - num;
                                                 amount = orders_new.get(0).getShare_count() * price;
                                                           int n = 0;
                                                  rs2 = stat.executeQuery("select brokerageaccnum from brokerageaccount where login_id='"+loginID+"'");
                                                  if(rs2.next()){
                                                      accnum = rs2.getString(1);
                                                  }
                                          Statement stat3 = conn.createStatement();
                                           stat3 = conn.createStatement();
                                      if(update == false){
                                               Statement stat2 = conn.createStatement();
                                        rs2= stat2.executeQuery("select * from nextordernum");
                                        
                                        if(rs2.next()){
                                             n = rs2.getInt(1);
                                        }
                                      id = n;
                                        int t = stat3.executeUpdate("insert into selling values ('S"+n+"','"+accnum+"','"+ss+"','"+num+"','"+num1+
                                                "','"+price+"','LO','pending','"+DateandTime.DateTime().toString()+"','"+DateandTime.DateTime().toString()+"')");
                                        t = stat3.executeUpdate("update buying set share_count='"+0+"',status='completed',date_updated='"+DateandTime.DateTime().toString()+ "' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"' and order_id='"+orders_new.get(0).getOrder_id()+"'");
                                        t = stat3.executeUpdate("update nextordernum set nextnum="+(n+1));
                                        t = stat3.executeUpdate("update brokerageaccount set balance = balance-'"+amount+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'");
                                        t = stat3.executeUpdate("update brokerageaccount set balance=balance+'"+amount+"' where login_id='"+loginID+"'");
                                        rs = stat3.executeQuery("select * from sharescount where brokerageaccnum='"+accnum+"' and stock_symbol='"+ss+"'");
                                        if(rs.next()){
                                            int t1= stat3.executeUpdate("update sharescount set count=count + '"+orders_new.get(0).getShare_count()+"' where brokerageaccnum='"+accnum+"'and stock_symbol='"+ss+"'");
                                        }

                                        else{
                                            int t1 = stat3.executeUpdate("insert into sharescount values ('"+accnum+"','"+ss+"','"+orders_new.get(0).getNo_of_shares()+"')");
                                        }
                                        
                                            int t1= stat3.executeUpdate("update sharescount set count=count - '"+orders_new.get(0).getShare_count()+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'and stock_symbol='"+ss+"'");
                                      
                                        update = true;
                                        return ("Order placed!");
                                        //new BrokerageAccount().UpdateSell(price, price, orders_new.get(0).getShare_count(), ss);
                                           }
                                      else if(update == true){
                                    int t =stat3.executeUpdate("update selling set share_count='"+num1+"',price='"+price+"',status='pending',date_updated='"+DateandTime.DateTime()+"' where order_id='S"+id+"' and brokerageaccnum='"+accnum+"'");
                                        t = stat3.executeUpdate("update buying set share_count='"+0+"',status='completed',date_updated='"+DateandTime.DateTime().toString()+ "' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"' and order_id='"+orders_new.get(0).getOrder_id()+"'");
                                       
                                        t = stat3.executeUpdate("update brokerageaccount set balance = balance-'"+amount+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'");
                                        t = stat3.executeUpdate("update brokerageaccount set balance=balance+'"+amount+"' where login_id='"+loginID+"'");
                                        rs = stat3.executeQuery("select * from sharescount where brokerageaccnum='"+accnum+"' and stock_symbol='"+ss+"'");
                                        if(rs.next()){
                                            int t1= stat3.executeUpdate("update sharescount set count=count + '"+orders_new.get(0).getNo_of_shares()+"' where brokerageaccnum='"+accnum+"'and stock_symbol='"+ss+"'");
                                        }

                                        else{
                                            int t1 = stat3.executeUpdate("insert into sharescount values ('"+accnum+"','"+ss+"','"+orders_new.get(0).getShare_count()+"')");
                                        }
                                        
                                            int t1= stat3.executeUpdate("update sharescount set count=count - '"+orders_new.get(0).getShare_count()+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'and stock_symbol='"+ss+"'");
                                     // new BrokerageAccount().UpdateSell(price, price, orders_new.get(0).getShare_count(), stocksym);
                                     return ("Order placed!");
                                      } 
                                                 
                                       num = num1;          
                                               
                                             }
                                             else if(num==orders_new.get(0).getShare_count()){
                                                 amount = num * price;
                                                  int n = 0;
                                                  rs2 = stat.executeQuery("select brokerageaccnum from brokerageaccount where login_id='"+loginID+"'");
                                                  if(rs2.next()){
                                                      accnum = rs2.getString(1);
                                                  }
                                          Statement stat3 = conn.createStatement();
                                           stat3 = conn.createStatement();
                                      if(update == false){
                                               Statement stat2 = conn.createStatement();
                                        rs2= stat2.executeQuery("select * from nextordernum");
                                        
                                        if(rs2.next()){
                                             n = rs2.getInt(1);
                                        }
                                      id = n;
                                        int t = stat3.executeUpdate("insert into selling values ('S"+n+"','"+accnum+"','"+ss+"','"+num+"','"+0+
                                                "','"+price+"','LO','completed','"+DateandTime.DateTime().toString()+"','"+DateandTime.DateTime().toString()+"')");
                                        t = stat3.executeUpdate("update buying set share_count='"+(orders_new.get(0).getShare_count()- num)+"',status='completed',date_updated='"+DateandTime.DateTime().toString()+ "' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"' and order_id='"+orders_new.get(0).getOrder_id()+"'");
                                        t = stat3.executeUpdate("update nextordernum set nextnum="+(n+1));
                                        t = stat3.executeUpdate("update brokerageaccount set balance = balance-'"+amount+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'");
                                        t = stat3.executeUpdate("update brokerageaccount set balance=balance+'"+amount+"' where login_id='"+loginID+"'");
                                        rs = stat3.executeQuery("select * from sharescount where brokerageaccnum='"+accnum+"' and stock_symbol='"+ss+"'");
                                        if(rs.next()){
                                            int t1= stat3.executeUpdate("update sharescount set count=count + '"+num+"' where brokerageaccnum='"+accnum+"'and stock_symbol='"+ss+"'");
                                        }

                                        else{
                                            int t1 = stat3.executeUpdate("insert into sharescount values ('"+accnum+"','"+ss+"','"+num+"')");
                                        }
                                        
                                            int t1= stat3.executeUpdate("update sharescount set count=count - '"+num+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'and stock_symbol='"+ss+"'");
                                      
                                        update = true;
                                        return ("Order placed!");
                                        //new BrokerageAccount().UpdateSell(price, price, num, stocksym);
                                           }
                                      else if(update == true){
                                    int t =stat3.executeUpdate("update selling set share_count='"+0+"',price='"+price+"',status='completed',date_updated='"+DateandTime.DateTime()+"' where order_id='S"+id+"' and brokerageaccnum='"+accnum+"'");
                                        t = stat3.executeUpdate("update buying set share_count='"+0+"',status='completed',date_updated='"+DateandTime.DateTime().toString()+ "' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"' and order_id='"+orders_new.get(0).getOrder_id()+"'");
                                        
                                        t = stat3.executeUpdate("update brokerageaccount set balance = balance-'"+amount+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'");
                                        t = stat3.executeUpdate("update brokerageaccount set balance=balance+'"+amount+"' where login_id='"+loginID+"'");
                                        rs = stat3.executeQuery("select * from sharescount where brokerageaccnum='"+accnum+"' and stock_symbol='"+ss+"'");
                                        if(rs.next()){
                                            int t1= stat3.executeUpdate("update sharescount set count=count + '"+num+"' where brokerageaccnum='"+accnum+"'and stock_symbol='"+ss+"'");
                                        }

                                        else{
                                            int t1 = stat3.executeUpdate("insert into sharescount values ('"+accnum+"','"+ss+"','"+num+"')");
                                        }
                                        
                                            int t1= stat3.executeUpdate("update sharescount set count=count - '"+num+"' where brokerageaccnum='"+orders_new.get(0).getBrokerageAccNum()+"'and stock_symbol='"+ss+"'");
                                     // new BrokerageAccount().UpdateSell(price, price, num, stocksym);
                                     return ("Order placed!");
                                      }
                                      break;
                                             }
                                           
                                           } 
                                           
                                          // }
                                           // else{
                                             //   System.out.println("Balance Insufficient!");
                                               // flags=false;flagn=false;flagp=false;
                                            //}
                                       //}
                                        flagp=false;
                                    }
                                    else if(price<=0){
                                        return ("Price cannot be negative or 0!");
                                        //flagp = true;
                                    }
                                    else if(c==0){
                                         Statement stat2 = conn.createStatement();
                                        rs2= stat2.executeQuery("select * from nextordernum");
                                        String acc1 = "";
                                        int n = 0;
                                        if(rs2.next()){
                                             n = rs2.getInt(1);
                                        }
                                        rs2 = stat2.executeQuery("select brokerageaccnum from brokerageaccount where login_id='"+loginID+"'");
                                        if(rs2.next()){
                                         acc1 = rs2.getString(1);
                                        }
                               int t2 = stat.executeUpdate("insert into selling values ('S"+n+"','"+acc1+"','"+ss+"','"+num+"','"+num+
                                                "','"+price+"','LO','pending','"+DateandTime.DateTime()+"','"+DateandTime.DateTime()+"')");
                                int t = stat.executeUpdate("update nextordernum set nextnum="+(n+1));
                                return ("Order placed!");
                                //flagp=false;
                                    }
                                    }
                                }
                                flagn=false;
                                    }
                                    else{
                                        return ("Not enough stocks to place the order!");
                                        //flagn = true;
                                    }
                                }
                                else{
                                    return ("No stocks available to sell!");
                                    //flagn = false;//flags=false;
                                }
                                
                            }
                            else if(num<=0){
                                return ("Stocks entered cannot be 0 or negative!");
                               // flagn = true;
                            }
                        }
                        
                       // flags = false;
//                    }   
//                    else if(count == 0){
//                        System.out.println("Invalid input! Please enter appropriate stock symbol.");
//                        flags = true;
//                    }
             }
             catch(SQLException e){
                 e.printStackTrace();
             } 
        }
        return ("Could not place order due to reasons like no stocks present or not enough balance!");
    }

    public ArrayList<Orders> view_orders(String loginID){
        ArrayList<Orders> view_list = new ArrayList<Orders>();
         final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/darams7291";
    Connection conn = null;
    Statement stat = null;
    ResultSet rs = null;
    try{
        conn = DriverManager.getConnection(DB_URL,"darams7291","1463877");
        stat = conn.createStatement();
       rs = stat.executeQuery("select brokerageaccnum from brokerageaccount where login_id='"+loginID+"'");
       String acc = "";
       if(rs.next()){
           acc = rs.getString(1);
       }
        rs = stat.executeQuery("select na.* from (select * from buying where brokerageaccnum='"+acc+"' union all select * from selling where brokerageaccnum='"+acc+"') na order by date_updated desc");
       int i=0;
        while(rs.next()){
           view_list.add(new Orders(rs.getString(1),rs.getString(2), rs.getString(3),rs.getInt(4),rs.getInt(5),rs.getDouble(6), rs.getString(7),rs.getString(8),rs.getString(9), rs.getString(10)));
          if(i<4){
           i++;
          }
          else{
              break;
          }
        }
    }
    catch(SQLException e){
        e.printStackTrace();
    }   
     return view_list;
    }
    public String getLoginid() {
        return loginid;
    }

    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getOrder_category() {
        return order_category;
    }

    public void setOrder_category(String order_category) {

        this.order_category = order_category;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getSs() {
        return ss;
    }

    public void setSs(String ss) {
        this.ss = ss;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ArrayList<Orders> getView_orders() {
       // view_orders = view_orders(loginid);
        return view_orders;
    }

}
