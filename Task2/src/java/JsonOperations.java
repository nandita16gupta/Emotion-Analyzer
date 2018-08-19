
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author nandita
 */

/* JSonOperations class is responsible for parsing the json format string and getting the useful fields out of it
and also writing the given string in Json format */
public class JsonOperations {

    public static HashMap<String, String> tone = new HashMap<>();
    public static String Category_id = null;

    public JsonOperations(HashMap<String, String> tone) {
        this.tone = tone;
    }

    /* This method performs the operation of Json parsing and getting the emotional tones out of it with their respective scores out of it */
    public HashMap<String, String> JSONParser(String jsonString) {

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
        return tone;
    }

    /* This method performs the function of writing the input emotionlist hashmap consisting of different tones 
   and their respective scores back into the json format string */
    public String WriteJason(HashMap<String, String> emotionsList) {

        JSONObject obj = new JSONObject();
        Set<Map.Entry<String, String>> set = emotionsList.entrySet();

        JSONArray list = new JSONArray();
        // Iterate to get all the emotions from the hashmap and put in json object
        for (Map.Entry<String, String> me : set) {
            obj.put(me.getKey(), me.getValue());
        }

        list.put(obj);
        JSONObject mainObj = new JSONObject();
        mainObj.put(Category_id, list); // Putting the category id from the json string
        String jsonText = mainObj.toString();

        return jsonText;
    }
}
