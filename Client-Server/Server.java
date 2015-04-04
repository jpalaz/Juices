import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.*;

public class Server implements HttpHandler {
    private MessageExchange messageExchange = new MessageExchange();
    private TreeSet<String> clientsNames = new TreeSet<String>();
    private static Message[] predefinedTasks = {
            new Message("Hello, World!", "User1", 0, "04.04<br>15:09:49"),
            new Message("Hello!", "World", 1, "04.04<br>15:11:20")
    };
    private List<Message> history = new ArrayList<Message>(Arrays.asList(predefinedTasks));

    public static void main(String[] args) {
        if (args.length != 1)
            System.out.println("Usage: java Server port");
        else {
            try {
                System.out.println("Server is starting...");
                Integer port = Integer.parseInt(args[0]);
                HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
                System.out.println("Server started.");
                String serverHost = InetAddress.getLocalHost().getHostAddress();
                System.out.println("Get list of messages: GET http://" + serverHost + ":" + port + "/chat?token={token}");
                System.out.println("Send message: POST http://" + serverHost + ":" + port + "/chat provide body json in format {\"message\" : \"{message}\"} ");

                server.createContext("/chat", new Server());
                server.setExecutor(null);
                server.start();
            } catch (IOException e) {
                System.out.println("Error creating http server: " + e);
            }
        }
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println("Begin request " + httpExchange.getRequestMethod());
        String response = "";

        if ("GET".equals(httpExchange.getRequestMethod())) {
            response = doGet(httpExchange);
        } else if ("POST".equals(httpExchange.getRequestMethod())) {
            doPost(httpExchange);
        } else if ("DELETE".equals(httpExchange.getRequestMethod())) {
            doDelete(httpExchange);
        } else if ("PUT".equals(httpExchange.getRequestMethod())) {
            doPut(httpExchange);
        } else if ("OPTIONS".equals(httpExchange.getRequestMethod())) {
            response = "";
        } else {
            response = "Unsupported http method: " + httpExchange.getRequestMethod();
        }

        sendResponse(httpExchange, response);
        System.out.println("Response sent, size " + response.length());
        System.out.println("End request " + httpExchange.getRequestMethod());
    }

    private String doGet(HttpExchange httpExchange) {
        String query = httpExchange.getRequestURI().getQuery();
        System.out.println("Query " + query);

        if (query != null) {
            Map<String, String> map = queryToMap(query);
            String token = map.get("token");
            if (token != null && !"".equals(token)) {
                int index = messageExchange.getIndex(token);
                return messageExchange.getServerResponse(history.subList(index, history.size()));
            } else {
                return "Token query parameter is absent in url: " + query;
            }
        }
        return  "Absent query in url";
    }

    private void doPost(HttpExchange httpExchange) {
        try {
            Message message = messageExchange.getClientMessage(httpExchange.getRequestBody());
            System.out.println("Get Message from " + message.getUsername()
                    + ": " + message.getText());
            message.setId(history.size());
            history.add(message);
        } catch (ParseException e) {
            System.err.println("Invalid user message: " + httpExchange.getRequestBody() + " " + e.getMessage());
        }
    }

    private void doDelete(HttpExchange httpExchange) {
        String query = httpExchange.getRequestURI().getQuery();
        System.out.println("Query " + query);

        if (query != null) {
            Map<String, String> map = queryToMap(query);
            String idToken = map.get("id");
            if (idToken != null && !"".equals(idToken)) {
                Integer id = Integer.parseInt(idToken);
                Message message = history.get(id);
                System.out.println("Delete Message from " + message.getUsername()
                        + ": " + message.getText());
                message.deleteMessage();
            }
        }
    }

    private void doPut(HttpExchange httpExchange) {
        try {
            Message edited = messageExchange.getClientMessageEdit(httpExchange.getRequestBody());
            if(!"".equals(edited.getText())) {
                Message message = history.get(edited.getId());
                System.out.println("Before Edit: Message from " + message.getUsername()
                        + ": " + message.getText());
                message.editMessage(edited.getText());
                System.out.println("After Edit: Message from " + message.getUsername()
                        + ": " + message.getText());
            }
        } catch (ParseException e) {
            System.err.println("Invalid user message: " + httpExchange.getRequestBody() + " " + e.getMessage());
        }
    }

    private void sendResponse(HttpExchange httpExchange, String response) {
        try {
            byte[] bytes = response.getBytes();
            Headers headers = httpExchange.getResponseHeaders();
            headers.add("Access-Control-Allow-Origin", "*");

            if ("OPTIONS".equals(httpExchange.getRequestMethod())) {
                headers.add("Access-Control-Allow-Methods", "PUT, DELETE, POST, GET, OPTIONS");
            }

            httpExchange.sendResponseHeaders(200, bytes.length);
            writeBody(httpExchange, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeBody(HttpExchange httpExchange, byte[] bytes) throws IOException {
        OutputStream os = httpExchange.getResponseBody();
        os.write( bytes);
        os.flush();
        os.close();
    }

    private Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<String, String>();
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length > 1) {
                result.put(pair[0], pair[1]);
            } else {
                result.put(pair[0], "");
            }
        }
        return result;
    }
}
