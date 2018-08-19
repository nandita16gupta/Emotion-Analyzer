
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author nandita
 */
// Reference: https://watson-api-explorer.mybluemix.net/apis/tone-analyzer-v3#!/tone/get_v3_tone
@WebServlet(urlPatterns = {"/EmotionAnalyzer/*"})
public class EmotionAnalyzer extends HttpServlet {

    public static HashMap<String, String> tone = new HashMap<>();
    public static String Category_id = null;

    // GET returns a value given a key
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("Console: doGET visited");

        String output = "";
        PrintWriter out = response.getWriter();

        // fetching the requested string from the http srevlet request
        String requestString = request.getPathInfo();
        String str = requestString;
        String[] sepString = requestString.split("/");
        sepString[1] = sepString[1].replace(" ", "%20");
        String name = sepString[1];

        // the query string for the api
        String queryString = "https://watson-api-explorer.mybluemix.net/tone-analyzer/api/v3/tone?version=2016-05-19&text=" + name;

        ToneProcess tp = new ToneProcess(queryString);
        String jsonString = tp.fetch();

        //JSON Parsing
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject object = jsonObject.getJSONObject("document_tone");

        JSONArray subArray = object.getJSONArray("tone_categories");
        JSONObject c = subArray.getJSONObject(0);
        JSONArray toneArray = c.getJSONArray("tones");

        // looping through All tones
        for (int i = 0; i < toneArray.length(); i++) {
            JSONObject j = toneArray.getJSONObject(i);

            double score = j.getDouble("score");
            String tone_name = j.getString("tone_name");

            // Storing the score against the tone name in a hashmap called tone
            tone.put(tone_name, new String(Double.toString(score)));
        }
        Category_id = c.getString("category_name");

        String jsonText = tp.WriteJason(tone, Category_id);

        //Set the response
        response.setStatus(200);

        // tell the client the type of the response
        response.setContentType("text/plain;charset=UTF-8");

        // return the value from a GET request
        out.println(jsonText);

    }

}
