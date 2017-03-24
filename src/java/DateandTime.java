/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.text.SimpleDateFormat;
import java.util.*;
/**
 *
 * @author praty
 */
public class DateandTime {
     public static final String DT_FORMAT = "dd MM yyyy HH:mm:ss";
     
    public static String DateTime(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DT_FORMAT);
        return sdf.format(cal.getTime());
    }
}
