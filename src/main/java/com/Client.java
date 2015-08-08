package com;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Random;

/**
 * Created by mixmox on 07/08/15.
 */
public class Client {
    private String username;
    private BigInteger I;
    private BigInteger p;
    private BigInteger a;
    public BigInteger A;
    public BigInteger B;
    private BigInteger g;
    private BigInteger k;
    private BigInteger K;
    public BigInteger M1;
    private BigInteger N;
    public BigInteger s;
    private BigInteger S;
    private BigInteger x;
    private BigInteger u;
    public BigInteger M2;
    public BigInteger M2_server;

    public Client(BigInteger I, String password, BigInteger N, BigInteger g, BigInteger k){
        this.I = I;
        this.p = new BigInteger(password);
        this.N = N;
        this.g = g;
        this.k = k;
    }

    public void generateRandomNumber_a() {
        a = new BigInteger(32, new Random());
    }


    public void calculateA() {
        A = g.modPow(a, N);
    }

    public void calculate_u() {
        u = Utility.hash(new BigInteger((A.toString() + B.toString()).getBytes()));
    }

    public void calculate_x() {
        x = Utility.hash(new BigInteger((s.toString() + p.toString()).getBytes()));
    }

    public void calculate_S() {
        S = B.subtract(k.multiply(g.modPow(x, N))).modPow(a.add(u.multiply(x)), N); // S = (B - (k*g^x))^(a+ux)

        System.out.println("S in Client: " + S.toString());
    }

    public void calculate_K() {
        K = Utility.hash(new BigInteger(S.toString().getBytes()));
    }

    public void calculate_M1() {
        BigInteger N_Hash;
        BigInteger g_Hash;
        BigInteger I_Hash;

        // Calculate H(N)
        N_Hash = Utility.hash(N);

        // Calculate H(g)
        g_Hash = Utility.hash(g);

        // Calculate H(I)
        I_Hash = Utility.hash(I);

        // Calculate M1
        M1 = Utility.hash(new BigInteger((N_Hash.xor(g_Hash).toString() + I_Hash.toString() + s.toString() + A.toString() + B.toString() + K.toString()  ).getBytes()));
        System.out.println("Client M1: " + M1.toString());
    }
    public void calculate_M2() {
        M2 = Utility.hash(new BigInteger((A.toString() + M1.toString() + K.toString()).getBytes()));
    }

    public boolean verify_M2() {
        return M2.equals(M2_server);
    }

    // Getters
    public String getA(){
        return A.toString();
    }
}