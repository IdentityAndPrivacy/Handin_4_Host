package host;

import com.User;

import java.math.BigInteger;

/**
 * Created by mixmox on 08/08/15.
 */
public class PublicData {
    public String N;
    public String g;
    public String k;
    public String abort;

    public PublicData(BigInteger N, BigInteger g, BigInteger k, String abort){
        this.N = N.toString();
        this.g = g.toString();
        this.k = k.toString();
        this.abort = abort;
    }
}
