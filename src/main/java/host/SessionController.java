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
    static BigInteger N;
    static BigInteger g;
    static String bitGroup = "";

    String abort = "";

    @RequestMapping("/session")
    public Session session(@RequestParam(value="up", defaultValue="") String up) {
        // From web client
        String[] parts = up.split(";");
        String username = parts[0];
        String password = parts[1];
        String bitGroup = parts[2];
        switch (bitGroup){
            case "1024":
                bitGroup = "1024";
                N = new BigInteger("167609434410335061345139523764350090260135525329813904557420930309800865859473551531551523800013916573891864789934747039010546328480848979516637673776605610374669426214776197828492691384519453218253702788022233205683635831626913357154941914129985489522629902540768368409482248290641036967659389658897350067939");
                g = new BigInteger("2");
                break;
            case "2048":
                bitGroup = "2048";
                N = new BigInteger("21766174458617435773191008891802753781907668374255538511144643224689886235383840957210909013086056401571399717235807266581649606472148410291413364152197364477180887395655483738115072677402235101762521901569820740293149529620419333266262073471054548368736039519702486226506248861060256971802984953561121442680157668000761429988222457090413873973970171927093992114751765168063614761119615476233422096442783117971236371647333871414335895773474667308967050807005509320424799678417036867928316761272274230314067548291133582479583061439577559347101961771406173684378522703483495337037655006751328447510550299250924469288819");
                g = new BigInteger("2");
            case "8192":
                bitGroup = "8192";
                N = new BigInteger("1090748135619415929450294929359784500348155124953172211774101106966150168922785639028532473848836817769712164169076432969224698752674677662739994265785437233596157045970922338040698100507861033047312331823982435279475700199860971612732540528796554502867919746776983759391475987142521315878719577519148811830879919426939958487087540965716419167467499326156226529675209172277001377591248147563782880558861083327174154014975134893125116015776318890295960698011614157721282527539468816519319333337503114777192360412281721018955834377615480468479252748867320362385355596601795122806756217713579819870634321561907813255153703950795271232652404894983869492174481652303803498881366210508647263668376514131031102336837488999775744046733651827239395353540348414872854639719294694323450186884189822544540647226987292160693184734654941906936646576130260972193280317171696418971553954161446191759093719524951116705577362073481319296041201283516154269044389257727700289684119460283480452306204130024913879981135908026983868205969318167819680850998649694416907952712904962404937775789698917207356355227455066183815847669135530549755439819480321732925869069136146085326382334628745456398071603058051634209386708703306545903199608523824513729625136659128221100967735450519952404248198262813831097374261650380017277916975324134846574681307337017380830353680623216336949471306191686438249305686413380231046096450953594089375540285037292470929395114028305547452584962074309438151825437902976012891749355198678420603722034900311364893046495761404333938686140037848030916292543273684533640032637639100774502371542479302473698388692892420946478947733800387782741417786484770190108867879778991633218628640533982619322466154883011452291890252336487236086654396093853898628805813177559162076363154436494477507871294119841637867701722166609831201845484078070518041336869808398454625586921201308185638888082699408686536045192649569198110353659943111802300636106509865023943661829436426563007917282050894429388841748885398290707743052973605359277515749619730823773215894755121761467887865327707115573804264519206349215850195195364813387526811742474131549802130246506341207020335797706780705406945275438806265978516209706795702579244075380490231741030862614968783306207869687868108423639971983209077624758080499988275591392787267627182442892809646874228263172435642368588260139161962836121481966092745325488641054238839295138992979335446110090325230955276870524611359124918392740353154294858383359");
                g = new BigInteger("19");
            default:
                // Default is 1024
                bitGroup = "1024";
                N = new BigInteger("167609434410335061345139523764350090260135525329813904557420930309800865859473551531551523800013916573891864789934747039010546328480848979516637673776605610374669426214776197828492691384519453218253702788022233205683635831626913357154941914129985489522629902540768368409482248290641036967659389658897350067939");
                g = new BigInteger("2");
        }
        abort = "";
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
            abort = "ABORT - A == 0";
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
            abort = "ABORT - B == 0";
            return fillSession(client, server, k, abort);
        }

        // Client computation
        client.calculate_u();
        // Client verify u
        if(!client.verify_u()){
            abort = "ABORT - u == 0";
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
            else {
                abort = "ABORT - M2 (proof of K) received from the Server is incorrect";
                return fillSession(client, server, k, abort);
            }

        } else {
            abort = "ABORT - M1 (proof of K) received from the Client is incorrect";
            return fillSession(client, server, k, abort);
        }
    }

    private Session fillSession(Client client, Server server, BigInteger k, String abort) {
        PublicData publicData = new PublicData(N, g, k, abort, bitGroup);
        Session session = new Session(client, server, publicData);

        return session;
    }


    private static ArrayList<User> createLookupFile() {
        ArrayList<User> users = new ArrayList<User>();
        users.add(new User(Utility.stringToBigInteger("kasper"), new BigInteger("112"), g, N, "kasper"));
        users.add(new User(Utility.stringToBigInteger("nikolas"), new BigInteger("111"), g, N, "nikolas"));
        users.add(new User(Utility.stringToBigInteger("martin"), new BigInteger("123"), g, N, "martin"));
        users.add(new User(Utility.stringToBigInteger("gert"), new BigInteger("123"), g, N, "gert"));
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