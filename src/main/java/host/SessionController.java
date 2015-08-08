package host;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import com.Client;
import com.Server;
import com.User;
import com.Utility;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {

    static Server server;
    static Client client;
    static BigInteger N = new BigInteger("167609434410335061345139523764350090260135525329813904557420930309800865859473551531551523800013916573891864789934747039010546328480848979516637673776605610374669426214776197828492691384519453218253702788022233205683635831626913357154941914129985489522629902540768368409482248290641036967659389658897350067939");
    //static BigInteger N = new BigInteger("1907");
    static BigInteger g = new BigInteger("2");
    String abort = "false";

    @RequestMapping("/session")
    public Session session(@RequestParam(value="up", defaultValue="") String up) {
        // From web client
        String[] parts = up.split(";");
        String username = parts[0];
        String password = parts[1];
//        String username = "kasper";
//        String password = "111";

        BigInteger I = Utility.stringToBigInteger(username);

        BigInteger k = new BigInteger("0");

        k = Utility.hash(new BigInteger(N.toString() + g.toString()));

        // Flow after HTTP-request is received
        client = new Client(I, password, N, g, k);
        server = new Server(N, g, k);
        server.users = createLookupFile();

        // Client computations
        client.generateRandomNumber_a();
        client.calculateA();

        // Client --> Server
        send_I_A(I, client.A);
        //Server verify A
        if(!server.verify_A()){
            abort = "true";
            return fillSession(client, server, k, abort);
        }

        // Server computations
        server.lookup(server.I);
        server.generateRandomNumber_b();
        server.calculateB();

        // Server --> Client
        send_s_B(server.currentUser.s, server.B);
        // Client verify B
        if(!client.verify_B()){
            abort = "true";
            return fillSession(client, server, k, abort);
        }

        // Client computation
        client.calculate_u();
        // Client verify u
        if(!client.verify_u()){
            abort = "true";
            return fillSession(client, server, k, abort);
        }

        client.calculate_x();
        client.calculate_S();
        client.calculate_K();
        client.calculate_M1();


        // Server computation
        server.calculate_u();
        server.calculate_S();
        server.calculate_K();
        server.calculate_M1();

        // Client --> Server
        send_M1(client.M1);

        // Server computations
        if (server.verify_M1()) {
            server.calculate_M2();
            client.calculate_M2();

            // Server --> Client
            send_M2(server.M2);

            if (client.verify_M2()) {
                // Print success
                System.out.println("Success! M2 is the same on client and server");
                // return session
                return fillSession(client, server, k, abort);
            }

        }
        // return session message: error
        return fillSession(client, server, k, abort);
    }

    private Session fillSession(Client client, Server server, BigInteger k, String abort) {
        PublicData publicData = new PublicData(N, g, k, abort);
        Session session = new Session(client, server, publicData);

        return session;
    }


    private static ArrayList<User> createLookupFile() {
        ArrayList<User> users = new ArrayList<User>();
        users.add(new User(Utility.stringToBigInteger("kasper"), new BigInteger("112"), g, N));
        users.add(new User(Utility.stringToBigInteger("nikolas"), new BigInteger("111"), g, N));
        users.add(new User(Utility.stringToBigInteger("martin"), new BigInteger("123"), g, N));
        users.add(new User(Utility.stringToBigInteger("gert"), new BigInteger("123"), g, N));
        return users;
    }

    public static void send_I_A(BigInteger I, BigInteger A){
        server.I = I;
        server.A = A;
    }

    public static void send_s_B(BigInteger s, BigInteger B){
        client.s = s;
        client.B = B;
    }

    public static void send_M1(BigInteger M1){
        server.M1_client = M1;
    }
    public static void send_M2(BigInteger M2){
        client.M2_server = M2;
    }

}