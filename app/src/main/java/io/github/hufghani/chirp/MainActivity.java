package io.github.hufghani.chirp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

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
    private Context context;
    private ChirpSDK chirpSDK;
    private Pet user = new Pet();


    private TextView txtName;
    private TextView txtType;
    private TextView txtHealth;

    private EditText editName;

    private Button btnSetName;

    private boolean bStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        chirpSDK = new ChirpSDK(context, API_KEY, API_SECRET);

        txtName = (TextView) findViewById(R.id.txtName);
        txtType = (TextView) findViewById(R.id.txtType);
        txtHealth = (TextView) findViewById(R.id.txtHealth);

        editName = (EditText) findViewById(R.id.editName);

        btnSetName = (Button) findViewById(R.id.btnSetName);

        txtType.setText(user.getType());

        btnSetName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String strNick = editName.getText().toString();
                user.setNick(strNick);
                txtName.post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        txtName.setText(strNick);
                        btnSetName.setVisibility(View.GONE);
                        editName.setVisibility(View.GONE);

                    }
                });
                if (!bStarted) {
                    final Handler h = new Handler();
                    final int delay = 10000; //milliseconds

                    h.postDelayed(new Runnable() {
                        public void run() {
                            if (!user.getNick().equals("")) {
                                user.speak(chirpSDK);
                            }
                            h.postDelayed(this, delay);
                        }
                    }, delay);
                    bStarted = true;
                }

            }
        });

        chirpSDK.setListener(new ChirpSDKListener() {

            @Override
            public void onChirpHeard(ShortCode shortCode) {

                chirpSDK.read(shortCode, new CallbackRead() {

                    @Override
                    public void onReadResponse(Chirp chirp) {
                        try {
                            final JSONObject attacker = chirp.getJsonData();
                            int health = user.getHealth();
                            if (attacker.get("type").equals("Fire")) {
                                if (user.getType().equals("Fire")) {
                                    health += 2;
                                }
                                if (user.getType().equals("Water")) {
                                    health -= 1;
                                }
                                if (user.getType().equals("Grass")) {
                                    health -= 20;
                                }
                            }
                            if (attacker.get("type").equals("Water")) {
                                if (user.getType().equals("Fire")) {
                                    health -= 20;
                                }
                                if (user.getType().equals("Water")) {
                                    health += 2;
                                }
                                if (user.getType().equals("Grass")) {
                                    health -= 1;
                                }
                            }
                            if (attacker.get("type").equals("Grass")) {
                                if (user.getType().equals("Fire")) {
                                    health -= 1;
                                }
                                if (user.getType().equals("Water")) {
                                    health -= 20;
                                }
                                if (user.getType().equals("Grass")) {
                                    health += 2;
                                }
                            }
                            user.setHealth(health);
                            txtHealth.post(new Runnable() {
                                @Override
                                public void run() {
                                    txtHealth.setText(user.getHealth());
                                }
                            });
                        } catch (JSONException e) {
                            Log.d("JSON", e.toString());
                        }
                    }

                    @Override
                    public void onReadError(ChirpError chirpError) {
                        Log.d("Error", chirpError.getMessage());
                    }
                });

            }

            @Override
            public void onChirpError(ChirpError chirpError) {
                Log.d("Error", chirpError.getMessage());
            }
        });
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
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        chirpSDK.stopListening();
    }
}
