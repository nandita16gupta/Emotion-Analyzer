
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Projections.excludeId;
import java.util.HashMap;
import org.bson.Document;
import org.json.JSONObject;
import static com.mongodb.client.model.Filters.exists;
import static com.mongodb.client.model.Indexes.descending;

/**
 *
 * @author nandita
 */

/* This class is responsible for retrieving the records from the MongoDB NoSQL cloud storage 
and also perform functions like constructing the html format strings to send it to the JSP page.*/
public class RecordRetrieve {

    public static HashMap<String, String> angerMap = new HashMap<>();
    public static HashMap<String, String> disgustMap = new HashMap<>();
    public static HashMap<String, String> fearMap = new HashMap<>();
    public static HashMap<String, String> joyMap = new HashMap<>();
    public static HashMap<String, String> sadnessMap = new HashMap<>();
    public static HashMap<String, String> allRecordsMap = new HashMap<>();

    public String[] Htmlbody = new String[10];
    public static double prev_a = 0, prev_j = 0;
    public static String aPrev_text, jPrev_text;
    public static String htmlstr = null, lText;
    public static Double hLatency = 0.0;

    // html header and tail used to construct html string
    public static String htmlHeader = "<!DOCTYPE html>\n"
            + "<html>\n"
            + "<head>\n"
            + "<style>\n"
            + "table, th, td {\n"
            + "border: 1px solid black;\n"
            + "}\n"
            + "</style>\n"
            + "</head>\n"
            + "<body>\n"
            + "<table style=\"width:100%\">\n"
            + "<tr>\n"
            + "<th>Input Text</th>\n"
            + "<th>Anger(%)</th>\n"
            + "<th>Disgust(%)</th>\n"
            + "<th>Fear(%)</th>\n"
            + "<th>Joy(%)</th>\n"
            + "<th>Sadness(%)</th>\n"
            + "</tr>\n";
    public static String htmlTop = "<!DOCTYPE html>\n"
            + "<html>\n"
            + "<head>\n"
            + "<style>\n"
            + "table, th, td {\n"
            + "border: 1px solid black;\n"
            + "}\n"
            + "</style>\n"
            + "</head>\n"
            + "<body>\n"
            + "<table style=\"width:100%\">\n"
            + "<tr>\n"
            + "<th>Device Model</th>\n"
            + "<th>Device ID</th>\n"
            + "<th>Input Text</th>\n"
            + "<th>Timestamp(yyyy.mm.dd.hh.mm.sec)</th>\n"
            + "<th>Latency(mSec)</th>\n"
            + "<th>Anger(%)</th>\n"
            + "<th>Disgust(%)</th>\n"
            + "<th>Fear(%)</th>\n"
            + "<th>Joy(%)</th>\n"
            + "<th>Sadness(%)</th>\n"
            + "</tr>\n";
    public static String htmlTail
            = "</table>\n"
            + "</body>\n"
            + "</html>\n";

    /* This method performs the operation of fetching the records from the MongoDB database collection */
    public void retrieveRecords() {
        MongoClientURI connectionString = new MongoClientURI("mongodb://nandita:Kitkat16#@ds053216.mlab.com:53216/toneanalysis");
        MongoClient mongoClient = new MongoClient(connectionString);

        MongoDatabase database = mongoClient.getDatabase("toneanalysis");

        MongoCollection<Document> collection = database.getCollection("tone_collection");

        // Counting the total number of records in the collection
        MongoCursor<Document> cursor = collection.find().iterator();
        String allRecord = htmlTop;
        try {
            while (cursor.hasNext()) {
                allRecord = allRecord + allRecordJsonParser(cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }
        Htmlbody[8] = allRecord + htmlTail;

        // Sorting the complete collection based on the field "Anger" in descending order and storing the top 5 sorted records in html format
        MongoCursor<Document> anger_sort = collection.find(exists("Anger")).projection(excludeId()).sort(descending("Anger")).iterator();
        String angerHtml = htmlHeader;
        try {
            long i = collection.count();
            if (i >= 5) {
                i = 5;
            }
            while (i > 0) {
                angerHtml = angerHtml + JSONDataParser(anger_sort.next().toJson(), "Anger");
                i--;
            }
        } finally {
            anger_sort.close();
        }

        Htmlbody[0] = angerHtml + htmlTail;

        // Sorting the complete collection based on the field "Disgust" in descending order and storing the top 5 sorted records in html format
        MongoCursor<Document> disgust_sort = collection.find(exists("Disgust")).sort(descending("Disgust")).iterator();
        String disgustHtml = htmlHeader;
        try {
            long i = collection.count();
            if (i >= 5) {
                i = 5;
            }
            while (i > 0) {
                disgustHtml = disgustHtml + JSONDataParser(disgust_sort.next().toJson(), "Disgust");
                i--;
            }
        } finally {
            disgust_sort.close();
        }
        Htmlbody[1] = disgustHtml + htmlTail;

        // Sorting the complete collection based on the field "Fear" in descending order and storing the top 5 sorted records in html format       
        MongoCursor<Document> fear_sort = collection.find(exists("Fear")).sort(descending("Fear")).iterator();
        String fearHtml = htmlHeader;
        try {
            long i = collection.count();
            if (i >= 5) {
                i = 5;
            }
            while (i > 0) {
                fearHtml = fearHtml + JSONDataParser(fear_sort.next().toJson(), "Fear");
                i--;
            }
        } finally {
            fear_sort.close();
        }
        Htmlbody[2] = fearHtml + htmlTail;

        // Sorting the complete collection based on the field "Joy" in descending order and storing the top 5 sorted records in html format
        MongoCursor<Document> joy_sort = collection.find(exists("Joy")).sort(descending("Joy")).iterator();
        String joyHtml = htmlHeader;
        try {
            long i = collection.count();
            if (i >= 5) {
                i = 5;
            }
            while (i > 0) {
                joyHtml = joyHtml + JSONDataParser(joy_sort.next().toJson(), "Joy");
                i--;
            }
        } finally {
            joy_sort.close();
        }
        Htmlbody[3] = joyHtml + htmlTail;

        // Sorting the complete collection based on the field "Sadness" in descending order and storing the top 5 sorted records in html format
        MongoCursor<Document> sadness_sort = collection.find(exists("Sadness")).sort(descending("Sadness")).iterator();
        String sadHtml = htmlHeader;
        try {
            long i = collection.count();
            if (i >= 5) {
                i = 5;
            }
            while (i > 0) {
                sadHtml = sadHtml + JSONDataParser(sadness_sort.next().toJson(), "Sadness");
                i--;
            }
        } finally {
            sadness_sort.close();
        }

        Htmlbody[4] = sadHtml + htmlTail;
        Htmlbody[7] = "3. Total texts analyzed: " + "<b><font color=\"green\">" + String.valueOf(collection.count()) + "</font></b>";
        Htmlbody[6] = "2. Text having the highest Anger tone in it: " + "<b><i><font color=\"red\">" + aPrev_text + "</font></i></b>" + " Score =  " + String.valueOf(prev_a) + "%";
        Htmlbody[5] = "1. Text having the highest Joy tone in it: " + "<b><i><font color=\"orange\">" + jPrev_text + "</font></i></b>" + " Score =  " + String.valueOf(prev_j) + "%";
        Htmlbody[9] = "4. Highest Latency is : " + "<b><i><font color=\"blue\">" + hLatency + " msecs </font></i></b>" + " for: " + "<b><i><font color=\"blue\">" + lText + "</font></i></b>" + " input text";

    }

    /* Parsing json string containing all records and storing in respective fields */
    public String allRecordJsonParser(String jsonStr) {
        JSONObject reader = new JSONObject(jsonStr);
        allRecordsMap.put("DeviceModel", reader.getString("DeviceModel"));
        allRecordsMap.put("DeviceID", reader.getString("DeviceID"));
        allRecordsMap.put("InputText", reader.getString("InputText"));
        allRecordsMap.put("Timestamp", reader.getString("Timestamp"));
        allRecordsMap.put("TimeElapsed", reader.getJSONObject("TimeElapsed").getString("$numberLong"));
        allRecordsMap.put("Anger", Double.toString(reader.getDouble("Anger")));
        allRecordsMap.put("Disgust", Double.toString(reader.getDouble("Disgust")));
        allRecordsMap.put("Fear", Double.toString(reader.getDouble("Fear")));
        allRecordsMap.put("Joy", Double.toString(reader.getDouble("Joy")));
        allRecordsMap.put("Sadness", Double.toString(reader.getDouble("Sadness")));
        if (Double.parseDouble(reader.getJSONObject("TimeElapsed").getString("$numberLong")) > hLatency) {
            hLatency = Double.parseDouble(reader.getJSONObject("TimeElapsed").getString("$numberLong"));
            lText = reader.getString("InputText");
        }

        // Returning the html string constructed from the above extracted fields
        return createHTMLforDump(allRecordsMap);
    }

    /* This method is for parsing json string for particular emotions */
    public String JSONDataParser(String jsonStr, String emotion) {
        //Parse the json string here extract out the fields(req) from it.
        JSONObject reader = new JSONObject(jsonStr);
        String htmlPart = null;

        if (emotion.equals("Anger")) {
            angerMap.put("InputText", reader.getString("InputText"));
            angerMap.put("Anger", Double.toString(reader.getDouble("Anger")));
            angerMap.put("Disgust", Double.toString(reader.getDouble("Disgust")));
            angerMap.put("Fear", Double.toString(reader.getDouble("Fear")));
            angerMap.put("Joy", Double.toString(reader.getDouble("Joy")));
            angerMap.put("Sadness", Double.toString(reader.getDouble("Sadness")));
            if (reader.getDouble("Anger") > prev_a) {
                prev_a = reader.getDouble("Anger");
                aPrev_text = reader.getString("InputText");

            }
            htmlPart = createHTMLBody(angerMap);

        } else if (emotion.equals("Disgust")) {
            disgustMap.put("InputText", reader.getString("InputText"));
            disgustMap.put("Anger", Double.toString(reader.getDouble("Anger")));
            disgustMap.put("Disgust", Double.toString(reader.getDouble("Disgust")));
            disgustMap.put("Fear", Double.toString(reader.getDouble("Fear")));
            disgustMap.put("Joy", Double.toString(reader.getDouble("Joy")));
            disgustMap.put("Sadness", Double.toString(reader.getDouble("Sadness")));

            htmlPart = createHTMLBody(disgustMap);

        }
        if (emotion.equals("Fear")) {
            fearMap.put("InputText", reader.getString("InputText"));
            fearMap.put("Anger", Double.toString(reader.getDouble("Anger")));
            fearMap.put("Disgust", Double.toString(reader.getDouble("Disgust")));
            fearMap.put("Fear", Double.toString(reader.getDouble("Fear")));
            fearMap.put("Joy", Double.toString(reader.getDouble("Joy")));
            fearMap.put("Sadness", Double.toString(reader.getDouble("Sadness")));

            htmlPart = createHTMLBody(fearMap);

        }
        if (emotion.equals("Joy")) {
            joyMap.put("InputText", reader.getString("InputText"));
            joyMap.put("Anger", Double.toString(reader.getDouble("Anger")));
            joyMap.put("Disgust", Double.toString(reader.getDouble("Disgust")));
            joyMap.put("Fear", Double.toString(reader.getDouble("Fear")));
            joyMap.put("Joy", Double.toString(reader.getDouble("Joy")));
            joyMap.put("Sadness", Double.toString(reader.getDouble("Sadness")));
            if (reader.getDouble("Joy") > prev_j) {
                prev_j = reader.getDouble("Joy");
                jPrev_text = reader.getString("InputText");
            }
            htmlPart = createHTMLBody(joyMap);

        } else if (emotion.equals("Sadness")) {
            sadnessMap.put("InputText", reader.getString("InputText"));
            sadnessMap.put("Anger", Double.toString(reader.getDouble("Anger")));
            sadnessMap.put("Disgust", Double.toString(reader.getDouble("Disgust")));
            sadnessMap.put("Fear", Double.toString(reader.getDouble("Fear")));
            sadnessMap.put("Joy", Double.toString(reader.getDouble("Joy")));
            sadnessMap.put("Sadness", Double.toString(reader.getDouble("Sadness")));

            /* Creating HTML string for each emotional tone */
            htmlPart = createHTMLBody(sadnessMap);

        }
        return htmlPart;
    }

    /* HTML body consisting of the INput text and its respective tone's score */
    public String createHTMLBody(HashMap<String, String> recordMap) {

        htmlstr = "<tr>\n"
                + "<td>" + recordMap.get("InputText") + "</td>\n"
                + "<td>" + recordMap.get("Anger") + "</td>\n"
                + "<td>" + recordMap.get("Disgust") + "</td>\n"
                + "<td>" + recordMap.get("Fear") + "</td>\n"
                + "<td>" + recordMap.get("Joy") + "</td>\n"
                + "<td>" + recordMap.get("Sadness") + "</td>\n"
                + "</tr>\n";

        return htmlstr;

    }

    /* Creating hTML string for getting the database dump (all records displaying) */
    public String createHTMLforDump(HashMap<String, String> recordMap) {

        htmlstr = "<tr>\n"
                + "<td>" + recordMap.get("DeviceModel") + "</td>\n"
                + "<td>" + recordMap.get("DeviceID") + "</td>\n"
                + "<td>" + recordMap.get("InputText") + "</td>\n"
                + "<td>" + recordMap.get("Timestamp") + "</td>\n"
                + "<td>" + recordMap.get("TimeElapsed") + "</td>\n"
                + "<td>" + recordMap.get("Anger") + "</td>\n"
                + "<td>" + recordMap.get("Disgust") + "</td>\n"
                + "<td>" + recordMap.get("Fear") + "</td>\n"
                + "<td>" + recordMap.get("Joy") + "</td>\n"
                + "<td>" + recordMap.get("Sadness") + "</td>\n"
                + "</tr>\n";

        return htmlstr;

    }

    /* To get the complete constructed html string on request */
    public String[] getHtmlforTables() {
        return Htmlbody;
    }
}
