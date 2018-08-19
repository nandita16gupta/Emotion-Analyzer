package nandita1.cmu.edu.project4android;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.provider.Settings.Secure;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyTone extends AppCompatActivity {

    String searchText = null;
    EditText AngerText,FearText,DisgustText,JoyText, SadnessText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tone);
        /*
         * The click listener
         */
        final MyTone ma = this;

        Button analyzeButton = (Button)findViewById(R.id.analyze);
        Button clearButton = (Button)findViewById(R.id.clear);

        // Add a listener to the send button
        analyzeButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View viewParam) {
                searchText = ((EditText)findViewById(R.id.searchText)).getText().toString();
                GetEmotion ge = new GetEmotion();
                String model = Build.MODEL;     // Fetching the model name
                String deviceID = Secure.getString(MyTone.this.getContentResolver(), Secure.ANDROID_ID);    // fetching the device ID of the android phone
                ge.analyze(searchText+"/"+model+"/"+deviceID+"/", ma); // Done asynchronously in another thread.  It calls ge.emotionSurveyOutput() in this thread when complete.
            }
        });

        // When clear button is pressed, clear all the fields
        clearButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View viewParam) {
                EditText clearText = ((EditText)findViewById(R.id.searchText));
                clearText.setText("");
                AngerText.setText("");
                FearText.setText("");
                DisgustText.setText("");
                JoyText.setText("");
                SadnessText.setText("");
            }
        });

    }

    /* This method performs the action of storing all the respective tone scores in textviews. */
    public void emotionSurveyOutput(String tone) {

        AngerText = (EditText) findViewById(R.id.AngerText);
        FearText = (EditText) findViewById(R.id.FearText);
        DisgustText = (EditText) findViewById(R.id.DisgustText);
        JoyText = (EditText) findViewById(R.id.JoyText);
        SadnessText = (EditText) findViewById(R.id.SadnessText);

        if (tone != null) {
            String[] toneReply = null;
            toneReply = jsonParser(tone);
            AngerText.setText(toneReply[0]);
            FearText.setText(toneReply[1]);
            DisgustText.setText(toneReply[2]);
            JoyText.setText(toneReply[3]);
            SadnessText.setText(toneReply[4]);
            AngerText.setVisibility(View.VISIBLE);
            FearText.setVisibility(View.VISIBLE);
            DisgustText.setVisibility(View.VISIBLE);
            JoyText.setVisibility(View.VISIBLE);
            SadnessText.setVisibility(View.VISIBLE);
        } else {        // If a particular tone is not found
            AngerText.setText("Cannot find the score");
            FearText.setText("Cannot find the score.");
            DisgustText.setText("Cannot find the score.");
            JoyText.setText("Cannot find the score.");
            SadnessText.setText("Cannot find the score.");
            AngerText.setVisibility(View.VISIBLE);
            FearText.setVisibility(View.VISIBLE);
            DisgustText.setVisibility(View.VISIBLE);
            JoyText.setVisibility(View.VISIBLE);
            SadnessText.setVisibility(View.VISIBLE);
        }
    }

    /* This method parses the json string fetched from the server */
    public String[] jsonParser(String toneResponse){
        JSONObject jsonObject = null;
        //System.out.println("ddddddd "+toneResponse);
        String[] toneText = new String[5];
        try {
            jsonObject = new JSONObject(toneResponse);
            JSONArray toneArray = jsonObject.getJSONArray("Emotion Tone");

            for (int i = 0; i < toneArray.length(); i++) {
                JSONObject jObj = toneArray.getJSONObject(i);

                toneText[0] = "Anger : " + String.valueOf(Math.round(Double.parseDouble(jObj.getString("Anger"))*100*100.0)/100.0) + "%";
                toneText[1] = "Fear : " + String.valueOf(Math.round(Double.parseDouble(jObj.getString("Fear"))*100*100.0)/100.0) + "%";
                toneText[2] = "Disgust : " + String.valueOf(Math.round(Double.parseDouble(jObj.getString("Disgust"))*100*100.0)/100.0) + "%";
                toneText[3] = "Joy : " + String.valueOf(Math.round(Double.parseDouble(jObj.getString("Joy"))*100*100.0)/100.0) + "%";
                toneText[4] = "Sadness : " + String.valueOf(Math.round(Double.parseDouble(jObj.getString("Sadness"))*100*100.0)/100.0) + "%";

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return toneText;
    }
}

