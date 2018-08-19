package nandita1.cmu.edu.project4android;

import java.io.IOException;
import java.net.URL;
import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;



/**
 * Created by nandita on 07/11/16.
 */

/* Reference: http://stackoverflow.com/questions/1995439/get-android-phone-model-programmatically
              http://stackoverflow.com/questions/2785485/is-there-a-unique-android-device-id
*/

/*
 * This class provides capabilities to send the search string to the server and get the tone analysis response from the server.
 * Network operations cannot be done from the UI thread, therefore this class makes use of an AsyncTask inner class that will do the network
 * operations in a separate worker thread.  However, any UI updates should be done in the UI thread so avoid any synchronization problems.
 * onPostExecution runs in the UI thread, and it calls the ImageView pictureReady method to do the update.
 *
 */

public class GetEmotion {

    MyTone mt = null;

    public void analyze(String analyzeText, MyTone mt){
        this.mt = mt;
        new AsyncToneAnalyzer().execute(analyzeText);

    }

    /*
	 * AsyncTask provides a simple way to use a thread separate from the UI thread in which to do network operations.
	 * doInBackground is run in the helper thread.
	 * onPostExecute is run in the UI thread, allowing for safe UI updates.
	 */
    private class AsyncToneAnalyzer extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            return searchText(urls[0]);
        }

        protected void onPostExecute(String toneText) {
            mt.emotionSurveyOutput(toneText);
        }

        private String searchText(String analyzeText) {
            String emotionURL = null;

            try {
                emotionURL="https://agile-cove-45583.herokuapp.com//EmotionAnalyzer/" + analyzeText;  // Project4 Task1
               // emotionURL="https://nameless-thicket-87312.herokuapp.com//ToneAnalysisServer/" + analyzeText; // Project4 Task2


                URL u = new URL(emotionURL);
                return getRemoteAnalysis(u);
            } catch (Exception e) {
                e.printStackTrace();
                return null; // so compiler does not complain
            }

        }

        /*
         * Given a URL referring to an tone, return a string of that tone
         */
        private String getRemoteAnalysis(final URL toneurl) {
            String response = "";
            HttpURLConnection conn;
            int status;
            try {
                conn = (HttpURLConnection) toneurl.openConnection();

                conn.setRequestMethod("GET");
                // tell the server what format we want back
                conn.setRequestProperty("Accept", "text/plain");

                // wait for response
                status = conn.getResponseCode();
                System.out.println("response code: " + status);

                // If things went poorly, get the error status and message
                if (status != 200) {
                    // not using msg
                    String msg = conn.getResponseMessage();
                    return conn.getResponseCode() + " " + msg;
                }

                // get the response
                String output;
                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

                while ((output = br.readLine()) != null) {
                    response += output + "\n";
                }

                conn.disconnect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;

        }

    }


}
