
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.HashMap;
import org.bson.Document;

/**
 *
 * @author nandita
 */
//Refrence: http://mongodb.github.io/mongo-java-driver/3.2/driver/getting-started/quick-tour/

/* MongoDBStore class is responsible for storing the tone related details into MOngoDB cloud storage */
public class MongoDBStore {

    public static HashMap<String, String> tone = new HashMap<>();
    public String requestString, timeStamp, model, deviceID;
    public long timeElapsed;

    public MongoDBStore(HashMap<String, String> tone, String requestString, String timeStamp, long timeElapsed, String model, String deviceID) {
        MongoDBStore.tone = tone;
        this.requestString = requestString;
        this.timeStamp = timeStamp;
        this.timeElapsed = timeElapsed;
        this.model = model;
        this.deviceID = deviceID;
    }

    /* StoreRecord method requests the MongoClient using the database username and password on Mlab and storing the collection one by one.  */
    public void storeRecord() {
        MongoClientURI connectionString = new MongoClientURI("mongodb://nandita:Kitkat16#@ds053216.mlab.com:53216/toneanalysis");
        MongoClient mongoClient = new MongoClient(connectionString);

        // Getting the databse access named toneanalysis
        MongoDatabase database = mongoClient.getDatabase("toneanalysis");

        // Getting the collection named tone_collection
        MongoCollection<Document> collection = database.getCollection("tone_collection");

        // Each record in that collection will consist of the fields like: model,deviceID,the text, timestamp, latency, each emotion's score
        Document doc = new Document("DeviceModel", model)
                .append("DeviceID", deviceID)
                .append("InputText", requestString)
                .append("Timestamp", timeStamp)
                .append("TimeElapsed", timeElapsed)
                .append("Anger", calculatePercentage("Anger"))
                .append("Disgust", calculatePercentage("Disgust"))
                .append("Fear", calculatePercentage("Fear"))
                .append("Joy", calculatePercentage("Joy"))
                .append("Sadness", calculatePercentage("Sadness"));

        collection.insertOne(doc);
    }

    /* To calculate the percentage score for each emotion */
    public double calculatePercentage(String emotion) {
        return Math.round(Double.parseDouble(tone.get(emotion)) * 100 * 100.0) / 100.0;
    }
}
