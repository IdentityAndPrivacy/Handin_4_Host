package host;

import com.User;
import com.Utility;

import java.math.BigInteger;

/**
 * Created by mixmox on 08/08/15.
 */
public class PublicData {
    public BigInteger N;
    public BigInteger g;
    public String bitGroup;

    public String getAbort() {
        return abort;
    }

    public BigInteger getK() {
        return k;
    }

    public BigInteger getG() {
        return g;
    }

    public BigInteger getN() {
        return N;
    }

    public String getBitGroup(){
        return bitGroup;
    }

    public BigInteger k;
    public String abort;

    public PublicData(BigInteger N, BigInteger g, BigInteger k, String abort, String bitGroup){
        this.N = N;
        this.g = g;
        this.k = k;
        this.abort = abort;
        this.bitGroup = bitGroup;
    }
}
