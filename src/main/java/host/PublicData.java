package host;

import com.User;
import com.Utility;

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
        this.N = Utility.prettifyBigInteger(N);
        this.g = g.toString();
        this.k = Utility.prettifyBigInteger(k);
        this.abort = abort;
    }
}
