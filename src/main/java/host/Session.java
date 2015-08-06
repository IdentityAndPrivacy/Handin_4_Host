package host;

public class Session {

    private final long id;
    private final String content;

    public Session(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}