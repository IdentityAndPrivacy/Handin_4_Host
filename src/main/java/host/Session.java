package host;

import com.Client;
import com.Server;

public class Session {

    private final Client client;
    private final Server server;
    private final PublicData publicData;

    public Server getServer() {
        return server;
    }

    public Client getClient() {
        return client;
    }

    public PublicData getPublicData() {
        return publicData;
    }


    public Session(Client client, Server server, PublicData publicData) {
        this.client = client;
        this.server = server;
        this.publicData = publicData;
    }


}