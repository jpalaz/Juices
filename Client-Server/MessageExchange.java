import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.List;

public class MessageExchange {

    private JSONParser jsonParser = new JSONParser();

    public String getToken(int index) {
        Integer number = index * 8 + 11;
        return "TN" + number + "EN";
    }

    public int getIndex(String token) {
        return (Integer.valueOf(token.substring(2, token.length() - 2)) - 11) / 8;
    }

    public String getServerResponse(List<Message> messages) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("messages", messages);
        jsonObject.put("token", getToken(messages.size()));
        return jsonObject.toJSONString();
    }

    public String getClientSendMessageRequest(Message message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", message);
        return jsonObject.toJSONString();
    }

    public Message getClientMessage(InputStream inputStream) throws ParseException {
        JSONObject json = getJSONObject(inputStreamToString(inputStream));
        return new Message(json.get("text").toString(), json.get("username").toString(),
                Integer.parseInt(json.get("id").toString()), json.get("time").toString());
    }

    public Message getClientMessageEdit(InputStream inputStream) throws ParseException {
        JSONObject json = (JSONObject)getJSONObject(inputStreamToString(inputStream)).get("message");
        return new Message(json.get("text").toString(), "",
                Integer.parseInt(json.get("id").toString()),
                json.get("time").toString());
    }

    public JSONObject getJSONObject(String json) throws ParseException {
        return (JSONObject) jsonParser.parse(json.trim());
    }

    public String inputStreamToString(InputStream in) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        try {
            while ((length = in.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String(baos.toByteArray());
    }
}
