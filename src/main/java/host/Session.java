package host;

import com.Client;
import com.Server;

public class Session {

    private final Client client;

    public Server getServer() {
        return server;
    }

    public Client getClient() {
        return client;
    }

    private final Server server;

    public Session(Client client, Server server) {
        this.client = client;
        this.server = server;
    }


}