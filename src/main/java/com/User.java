package com;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Random;

/**
 * Created by mixmox on 07/08/15.
 */
public class User {
    public BigInteger I;
    public BigInteger s;
    public BigInteger v;

    public User(BigInteger I, BigInteger p, BigInteger g, BigInteger N){
        this.I = I;
        generateRandomSalt();
        calculate_v(N, g, p);
    }

    private void calculate_v(BigInteger N, BigInteger g, BigInteger p) {
        BigInteger x = Utility.hash(new BigInteger((s.toString() + p.toString()).getBytes()));
        v = g.modPow( x, N);
    }

    public void generateRandomSalt(){
        s = new BigInteger(10, new Random());
    }

    public BigInteger getBigI(){
        return I;
    }
}
