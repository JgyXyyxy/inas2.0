package com.sudu.inas.util;

import com.sudu.inas.beans.Event;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;


/**
 * Created by J on  17-10-23.
 */

public class CommonUtil {

    public static String genRandomNum(){
        int  maxNum = 36;
        int i;
        int count = 0;
        char[] str = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
                'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
        StringBuffer pwd = new StringBuffer("");
        Random r = new Random();
        while(count < 8){
            i = Math.abs(r.nextInt(maxNum));
            if (i >= 0 && i < str.length) {
                pwd.append(str[i]);
                count ++;
            }
        }
        return pwd.toString();
    }

    public static int toDays(String date){
        String[] strings = date.split("-");
        Calendar ca = Calendar.getInstance();
        if (strings.length == 3){
            ca.setTime(new Date(Integer.valueOf(strings[0]),Integer.valueOf(strings[1])-1,Integer.valueOf(strings[2])));
        }

        if (strings.length == 2){
            ca.setTime(new Date(Integer.valueOf(strings[0]),Integer.valueOf(strings[1])-1,1));
        }else {
            ca.setTime(new Date(Integer.valueOf(strings[0]),1,1));
        }
        return Integer.valueOf(strings[0])*365 + ca.get(Calendar.DAY_OF_YEAR);
    }

    public static String getUUID(){
        UUID uuid=UUID.randomUUID();
        return uuid.toString();
    }


    public static void main(String[] args) {

        System.out.println(toDays("1961-8-4"));

    }
}
