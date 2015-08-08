package com;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by mixmox on 08/08/15.
 */
public class Utility {
    public static BigInteger hash(BigInteger bi) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.reset();
            return new BigInteger(md.digest(bi.toString().getBytes()));
        }
        catch (Exception e) {
            System.out.println("Hash error: " + e.getMessage());
            return new BigInteger("0");
        }
    }
    public static BigInteger stringToBigInteger(String s){
        StringBuilder sb = new StringBuilder();
        long asciiInt;
        for (int i = 0; i < s.length(); i++){
            char c = s.charAt(i);
            asciiInt = (int)c;
            System.out.println(c +"="+asciiInt);
            sb.append(asciiInt);
        }
        return new BigInteger(sb.toString());
    }

    public static String prettifyBigInteger(BigInteger input){
        return input.toString().substring(0, 10) + "...";
    }
}
