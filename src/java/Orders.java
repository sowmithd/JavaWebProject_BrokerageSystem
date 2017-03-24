/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author praty
 */
@Named(value = "orders")
@RequestScoped
public class Orders {

     private String order_id;
    private String BrokerageAccNum;
    private String Stock_Symbol;
    private int no_of_shares;
    private int share_count;
    private double price;
    private String order_type;
    private String Status;
    private String date_created;
    private String data_updated;
    public Orders(String order_id, String BrokerageAccNum, String Stock_Symbol, int no_of_shares, int share_count, double price, String order_type, String Status) {
        this.order_id = order_id;
        this.BrokerageAccNum = BrokerageAccNum;
        this.Stock_Symbol = Stock_Symbol;
        this.no_of_shares = no_of_shares;
        this.share_count = share_count;
        this.price = price;
        this.order_type = order_type;
        this.Status = Status;
    }

    public Orders(String order_id, String BrokerageAccNum, String Stock_Symbol, int no_of_shares, int share_count, double price, String order_type, String Status, String date_created, String data_updated) {
        this.order_id = order_id;
        this.BrokerageAccNum = BrokerageAccNum;
        this.Stock_Symbol = Stock_Symbol;
        this.no_of_shares = no_of_shares;
        this.share_count = share_count;
        this.price = price;
        this.order_type = order_type;
        this.Status = Status;
        this.date_created = date_created;
        this.data_updated = data_updated;
    }

    
    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getBrokerageAccNum() {
        return BrokerageAccNum;
    }

    public void setBrokerageAccNum(String BrokerageAccNum) {
        this.BrokerageAccNum = BrokerageAccNum;
    }

    public String getStock_Symbol() {
        return Stock_Symbol;
    }

    public void setStock_Symbol(String Stock_Symbol) {
        this.Stock_Symbol = Stock_Symbol;
    }

    public int getNo_of_shares() {
        return no_of_shares;
    }

    public void setNo_of_shares(int no_of_shares) {
        this.no_of_shares = no_of_shares;
    }

    public int getShare_count() {
        return share_count;
    }

    public void setShare_count(int share_count) {
        this.share_count = share_count;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getData_updated() {
        return data_updated;
    }

    public void setData_updated(String data_updated) {
        this.data_updated = data_updated;
    }
    
    
}
