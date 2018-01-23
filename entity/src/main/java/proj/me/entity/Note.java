package proj.me.entity;

/**
 * Created by root on 23/1/18.
 */

public class Note {
    private int id = -1;
    private String title;
    private String text;
    private long createTimestamp;
    private long updateTimestamp;
    private String pathUri;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(long createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public long getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(long updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public String getPathUri() {
        return pathUri;
    }

    public void setPathUri(String pathUri) {
        this.pathUri = pathUri;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        Note note = (Note) obj;
        return createTimestamp == note.createTimestamp;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (title == null ? 0 : title.hashCode());
        result = 31 * result + (int)((createTimestamp >> 32) & createTimestamp);
        return result;
    }
}
