package io.github.hufghani.chirp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import io.chirp.sdk.CallbackRead;
import io.chirp.sdk.ChirpSDK;
import io.chirp.sdk.ChirpSDKListener;
import io.chirp.sdk.model.Chirp;
import io.chirp.sdk.model.ChirpError;
import io.chirp.sdk.model.ShortCode;

public class MainActivity extends AppCompatActivity {
    private static final int RESULT_REQUEST_RECORD_AUDIO = 0;
    private final String API_KEY= "6pLityejVwLzaVJgiUGGzziKU";
    private final String API_SECRET  = "IdS6phZPntPpCbWmd2j7I5O5cs12gNM7mqkpoqcoUkY374gXT1";
    Context context;
    ChirpSDK chirpSDK;
    TextView txtView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtView = (TextView) findViewById(R.id.textView);
        context = this;
        chirpSDK = new ChirpSDK(context, API_KEY, API_SECRET);

        chirpSDK.setListener(new ChirpSDKListener() {

            @Override
            public void onChirpHeard(ShortCode shortCode) {

                chirpSDK.read(shortCode, new CallbackRead() {

                    @Override
                    public void onReadResponse(Chirp chirp) {
                        Log.d("Chirp: ", chirp.toString());
                        txtView.setText(chirp.getJsonData().toString());
                    }

                    @Override
                    public void onReadError(ChirpError chirpError) {

                    }
                });

            }

            @Override
            public void onChirpError(ChirpError chirpError) {

            }
        });
    }

    public void test(View view) {
        chirpSDK.play(new ShortCode("parrotbill"));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, RESULT_REQUEST_RECORD_AUDIO);
        }
        else {
            chirpSDK.startListening();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RESULT_REQUEST_RECORD_AUDIO: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chirpSDK.startListening();
                }
                return;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        chirpSDK.stopListening();
    }
}
