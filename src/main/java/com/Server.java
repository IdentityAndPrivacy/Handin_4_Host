package com;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mixmox on 07/08/15.
 */
public class Server {
    public BigInteger A;
    private BigInteger b;
    public BigInteger B;
    private BigInteger g;
    public BigInteger I;
    private BigInteger k;
    private BigInteger K;
    public BigInteger M1_client;
    private BigInteger M1;
    public BigInteger M2;
    private BigInteger N;
    private BigInteger S;
    private BigInteger x;
    private BigInteger u;
    public ArrayList<User> users;
    public User currentUser;

    public Server(BigInteger N, BigInteger g, BigInteger k){
        this.N = N;
        this.g = g;
        this.k = k;
    }

    public void lookup(BigInteger I) {
        // Find user in database from 'I'
        for (User user: users ){
            if (user.I.equals(I)){
                currentUser = user;
                return;
            }
        }
    }

    public void generateRandomNumber_b() {
        b = new BigInteger(32, new Random());
    }

    public void calculateB() {
        B = k.multiply(currentUser.v).add(g.modPow(b,N)); // k*v + g^b (mod N)
    }


    public void calculate_u(){
        u = Utility.hash(new BigInteger((A.toString() + B.toString()).getBytes()));
    }

    public void calculate_S() {
        S = (A.multiply(currentUser.v.modPow(u,N))).modPow(b, N);
        System.out.println("S in Server: " + S.toString());
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
        I_Hash = Utility.hash(currentUser.I);

        // Calculate M1
        M1 = Utility.hash(new BigInteger((N_Hash.xor(g_Hash).toString() + I_Hash.toString() + currentUser.s.toString() + A.toString() + B.toString() + K.toString()  ).getBytes()));
        System.out.println("Server M1: " + M1.toString());
    }

    public boolean verify_M1() {
        return M1.equals(M1_client);
    }

    public void calculate_M2() {
        M2 = Utility.hash(new BigInteger((A.toString() + M1.toString() + K.toString()).getBytes()));
    }
}
