package io.github.hufghani.chirp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import io.chirp.sdk.CallbackRead;
import io.chirp.sdk.ChirpSDK;
import io.chirp.sdk.ChirpSDKListener;
import io.chirp.sdk.model.Chirp;
import io.chirp.sdk.model.ChirpError;
import io.chirp.sdk.model.ShortCode;

public class MainActivity extends AppCompatActivity {

    final String API_KEY= "6pLityejVwLzaVJgiUGGzziKU";
    final String API_SECRET  = "IdS6phZPntPpCbWmd2j7I5O5cs12gNM7mqkpoqcoUkY374gXT1";
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

        chirpSDK.startListening();

    }

    public void test(View view) {
        chirpSDK.play(new ShortCode("parrotbill"));
    }
}
