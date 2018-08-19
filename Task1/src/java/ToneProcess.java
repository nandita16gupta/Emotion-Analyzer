
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author nandita
 */
public class ToneProcess {

    public String urlString;

    public ToneProcess(String urlString) {
        this.urlString = urlString;
    }

    // Fetch the response from the URL string
    public String fetch() {
        String response = "";
        try {
            URL url = new URL(urlString);
            /*
             * Create an HttpURLConnection.  This is useful for setting headers
             * and for getting the path of the resource that is returned (which 
             * may be different than the URL above if redirected).
             * HttpsURLConnection (with an "s") can be used if required by the site.
             */
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // Read all the text returned by the server
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String str;
            // Read each line of "in" until done, adding each to "response"
            while ((str = in.readLine()) != null) {
                // str is one line of text readLine() strips newline characters
                response += str;
            }
            //System.out.println("Output: " + response);
            in.close();
        } catch (IOException e) {
            System.out.println("Eeek, an exception");
        }
        return response;
    }

    /* This method performs the function of writing the input emotionlist hashmap consisting of different tones 
    and their respective scores back into the json format string */
    public String WriteJason(HashMap<String, String> emotionsList, String category) {

        JSONObject obj = new JSONObject();
        Set<Map.Entry<String, String>> set = emotionsList.entrySet();

        // Iterate to get all the emotions from the hashmap and put in json object
        JSONArray list = new JSONArray();
        for (Map.Entry<String, String> me : set) {
            obj.put(me.getKey(), me.getValue());
        }

        list.put(obj);
        JSONObject mainObj = new JSONObject();
        mainObj.put(category, list);      // Putting the category id from the json string
        String jsonText = mainObj.toString();
        System.out.print(jsonText);

        return jsonText;
    }

}
