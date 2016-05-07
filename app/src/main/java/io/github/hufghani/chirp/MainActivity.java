package io.github.hufghani.chirp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.chirp.sdk.ChirpSDK;

public class MainActivity extends AppCompatActivity {

    final String API_KEY= "6pLityejVwLzaVJgiUGGzziKU";
    final String API_SECRET  = "IdS6phZPntPpCbWmd2j7I5O5cs12gNM7mqkpoqcoUkY374gXT1";
    Context context;
    ChirpSDK chirpSDK = new ChirpSDK(context, API_KEY, API_SECRET);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    chirpSDK.setListener(new ChirpSDKListener() {

        @Override
        public void onChirpHeard(ShortCode shortCode) {

        }

        @Override
        public void onChirpError(ChirpError chirpError) {

        }
    });
}
