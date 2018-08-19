
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author nandita
 */

/* Reference: Professor's REST Lab and homeworks
              https://watson-api-explorer.mybluemix.net/apis/tone-analyzer-v3#!/tone/get_v3_tone
 */
@WebServlet(name = "ToneAnalysisServer", urlPatterns = {"/ToneAnalysisServer/*", "/getAnalysis"})
public class ToneAnalysisServer extends HttpServlet {

    public static HashMap<String, String> tone = new HashMap<>();
    public static String Category_id = null;

    // GET returns a value given a key
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("Console: doGET visited");

        // If the request string is /getanalysis in the url then perform the action to retrieve operations from the MongoDB database
        if (request.getServletPath().equals("/getAnalysis")) {
            RecordRetrieve eAnalysis = new RecordRetrieve();
            eAnalysis.retrieveRecords();
            String[] html = new String[10];
            html = eAnalysis.getHtmlforTables();

            response.setStatus(200);
            // tell the client the type of the response
            response.setContentType("text/plain;charset=UTF-8");

            // return the value from a GET request
            request.setAttribute("aText", html[0]);
            request.setAttribute("dText", html[1]);
            request.setAttribute("fText", html[2]);
            request.setAttribute("jText", html[3]);
            request.setAttribute("sText", html[4]);
            request.setAttribute("highJoy", html[5]);
            request.setAttribute("highAngry", html[6]);
            request.setAttribute("total", html[7]);
            request.setAttribute("dump", html[8]);
            request.setAttribute("latency", html[9]);

            // Dispatching the view with all attributes to the index.jsp page 
            RequestDispatcher view = request.getRequestDispatcher("/index.jsp");
            view.forward(request, response);
        } else {
            String output = "";
            PrintWriter out = response.getWriter();

            // Getting the Get request and the info content in it
            String requestString = request.getPathInfo();
            String[] sepString = requestString.split("/");
            String str = sepString[1];
            System.out.println("str:" + str);
            sepString[1] = sepString[1].replace(" ", "%20");
            String name = sepString[1];

            // fetching the timestamp of the request to the API
            String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            long start = System.currentTimeMillis();

            // the query string for the api
            String queryString = "https://watson-api-explorer.mybluemix.net/tone-analyzer/api/v3/tone?version=2016-05-19&text=" + name;

            FetchResponse fResponse = new FetchResponse();
            String jsonString = fResponse.fetch(queryString);
            System.out.println(jsonString);
            long stop = System.currentTimeMillis();
            long timeElapsed = (stop - start);

            //JSON Parsing
            JsonOperations jp = new JsonOperations(tone);
            tone = jp.JSONParser(jsonString);

            //Writing JSON text for Android App
            String jsonText = jp.WriteJason(tone);

            // Storing the complete content fetched from android app and watson API into the mongoDB database 
            MongoDBStore mgDB = new MongoDBStore(tone, str, timestamp, timeElapsed, sepString[2], sepString[3]);
            mgDB.storeRecord();
            out.println(jsonText);

        }

    }
}
