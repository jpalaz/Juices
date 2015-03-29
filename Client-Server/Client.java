import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

public class Client implements Runnable {

    private List<Message> history = new ArrayList<Message>();
    private MessageExchange messageExchange = new MessageExchange();
    private String host;
    private Integer port;
    private Random rand = new Random();
    private String username = "User" + rand.nextInt(10000);

    public Client(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) {
        if (args.length != 2)
            System.out.println("Usage: java ChatClient host port");
        else {
            System.out.println("Connection to server...");
            String serverHost = args[0];
            Integer serverPort = Integer.parseInt(args[1]);
            Client client = new Client(serverHost, serverPort);
            new Thread(client).start();
            System.out.println("Connected to server: " + serverHost + ":" + serverPort);
            client.listen();
        }
    }

    private HttpURLConnection getHttpURLConnection() throws IOException {
        URL url = new URL("http://" + host + ":" + port + "/chat?token=" + messageExchange.getToken(history.size()));
        return (HttpURLConnection) url.openConnection();
    }

    public List<Message> getMessages() {
        List<Message> list = new ArrayList<Message>();
        HttpURLConnection connection = null;
        try {
            connection = getHttpURLConnection();
            connection.connect();
            String response = messageExchange.inputStreamToString(connection.getInputStream());
            JSONObject jsonObject = messageExchange.getJSONObject(response);
            JSONArray jsonArray = (JSONArray) jsonObject.get("messages");
            for (Object o : jsonArray) {
                JSONObject message = (JSONObject)o;
                System.out.println(message.get("text").toString());
                list.add(new Message(message.get("text").toString(),
                        message.get("username").toString(),
                        Integer.parseInt(message.get("id").toString())));
            }
        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
        } catch (ParseException e) {
            System.err.println("ERROR: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return list;
    }

    public void sendMessage(Message message) {
        HttpURLConnection connection = null;
        try {
            connection = getHttpURLConnection();
            connection.setDoOutput(true);

            connection.setRequestMethod("POST");

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

            byte[] bytes = messageExchange.getClientSendMessageRequest(message).getBytes();
            wr.write(bytes, 0, bytes.length);
            wr.flush();
            wr.close();

            connection.getInputStream();

        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public void listen() {
        while (true) {
            List<Message> list = getMessages();

            if (list.size() > 0) {
                history.addAll(list);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.err.println("ERROR: " + e.getMessage());
            }
        }
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String message = scanner.nextLine();
            sendMessage(new Message(message, username, 0));
        }
    }
}
