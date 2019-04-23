import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GuestBook implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {


        String response = "";
        String method = httpExchange.getRequestMethod();

        // Send a form if it wasn't submitted yet.
        if (method.equals("GET")) {
            response = "<html><body>" +
                    "<h1>Guestbook</h1>\n" +
                    "<form method=\"POST\">\n" +
                    "  Message:<br>\n" +
                    "  <textarea rows=\"4\" cols=\"50\" name=\"msg\" value=\"Enter your msg here...\">\n" +
                    "  </textarea>\n"+
                    "  <br>\n" +
                    "  Name:<br>\n" +
                    "  <input type=\"text\" name=\"name\" value=\"Mickey Mouse\">\n" +
                    "  <br><br>\n" +
                    "  <input type=\"submit\" value=\"Submit\">\n" +
                    "</form> " +
                    "</body><html>";
        }

        // If the form was submitted, retrieve it's content.
        if (method.equals("POST")) {
            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody());
            BufferedReader br = new BufferedReader(isr);
            String formData = br.readLine();

            System.out.println(formData);
            Map inputs = parseFromData(formData);

            String currDate = getDate();

            response = "<html><body>" +
                    "<h1>Guestbook</h1>\n" +
                    "<div>"+
                    inputs.get("msg") +
                    "<p>Name: " + inputs.get("name") +
                    "</p>"+
                    "<p>Date: " + currDate +
                    "</p>"+
                    "</div>\n"+
                    "<form method=\"POST\">\n" +
                    "  Message:<br>\n" +
                    "  <textarea rows=\"4\" cols=\"50\" name=\"msg\" value=\"Enter your msg here...\">\n" +
                    "  </textarea>\n"+
                    "  <br>\n" +
                    "  Name:<br>\n" +
                    "  <input type=\"text\" name=\"name\" value=\"Mickey Mouse\">\n" +
                    "  <br><br>\n" +
                    "  <input type=\"submit\" value=\"Submit\">\n" +
                    "</form> " +
                    "</body><html>";
        }

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    /**
     * Form data is sent as a urlencoded string. Thus we have to parse this string to get data that we want.
     * See: https://en.wikipedia.org/wiki/POST_(HTTP)
     */
    private static Map<String, String> parseFromData(String formData) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        String[] pairs = formData.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            // We have to decode the value because it's urlencoded. see: https://en.wikipedia.org/wiki/POST_(HTTP)#Use_for_submitting_web_forms
            String value = new URLDecoder().decode(keyValue[1], "UTF-8");
            map.put(keyValue[0], value);
        }
        return map;
    }

    private String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        return dateFormat.format(date);
    }
}
