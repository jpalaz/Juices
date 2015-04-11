import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

/**
 * Created by Jan on 27.03.15.
 */
public class Message implements JSONAware {
    private String text;
    private String username;
    private int id;
    private String time;
    private boolean edited;
    private boolean deleted;

    public Message(String text, String username, int id) {
        this.text = text;
        this.username = username;
        this.id = id;
        this.edited = this.deleted = false;
    }

    public Message(String text, String username, int id, String time) {
        this.text = text;
        this.username = username;
        this.id = id;
        this.time = time;
        this.edited = this.deleted = false;
    }

    public Message(String text, String username, int id, String time, boolean edited, boolean deleted) {
        this.text = text;
        this.username = username;
        this.id = id;
        this.time = time;
        this.edited = edited;
        this.deleted = deleted;
    }

    public Message(Message message, boolean deleted) {
        this.username = message.username;
        this.id = message.id;
        this.time = message.time;
        this.edited = message.edited;

        if (deleted)
            this.deleteMessage();
        else {
            this.text = message.text;
            this.deleted = message.deleted;
        }
    }

    @Override
    public String toJSONString(){
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("username", username);
        obj.put("text", text);
        obj.put("time", time);
        obj.put("deleted", deleted);
        obj.put("edited", edited);
        return obj.toString();
    }

    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public boolean isEdited() {
        return edited;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void deleteMessage() {
        deleted = true;
        text = "This message has been deleted";
    }

    public void editMessage(String text) {
        if(!deleted) {
            edited = true;
            this.text = text;
        }
        else
            System.out.println("Message has been deleted. You can't edit it.");
    }
}
