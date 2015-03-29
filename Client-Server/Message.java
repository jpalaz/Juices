import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

/**
 * Created by Jan on 27.03.15.
 */
public class Message implements JSONAware {
    private String username;
    private int id;
    private String text;
    private boolean edited;
    private boolean deleted;

    public Message(String text, String username, int id) {
        this.text = text;
        this.username = username;
        this.id = id;
    }

    /*@Override
    public String toString() {
        return  "\"id\":\"" + id +
                "\", \"username\":\"" + username +
                "\", \"text\":\"" + text + "\"";
    }*/

    @Override
    public String toJSONString(){
        JSONObject obj = new JSONObject();
        obj.put("id", new Integer(id));
        obj.put("username", username);
        obj.put("text", text);
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
}
