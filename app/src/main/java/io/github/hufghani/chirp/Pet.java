package io.github.hufghani.chirp;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import io.chirp.sdk.CallbackCreate;
import io.chirp.sdk.ChirpSDK;
import io.chirp.sdk.model.Chirp;
import io.chirp.sdk.model.ChirpError;
import io.chirp.sdk.model.ShortCode;

public class Pet {
    private JSONObject chirp;
    final private String[] types = {"Fire", "Water", "Grass"};
    public Pet() {
        this.chirp = new JSONObject();
        try {
            this.chirp.put("nick", "");
            int idx = new Random().nextInt(types.length);
            String type = (types[idx]);
            this.chirp.put("type", type);
            this.chirp.put("health", 100);
        } catch (JSONException e) {
            Log.d("JSON", e.toString());
        }
    }
    public String getNick() {
        try {
            return (String) this.chirp.get("nick");
        } catch (JSONException e) {
            Log.d("JSON", e.toString());
        }
        return null;
    }
    public String getType() {
        try {
            return (String) this.chirp.get("type");
        } catch (JSONException e) {
            Log.d("JSON", e.toString());
        }
        return null;
    }
    public int getHealth() {
        try {
            return (int) this.chirp.get("health");
        } catch (JSONException e) {
            Log.d("JSON", e.toString());
        }
        return 0;
    }
    public void setNick(String strNick) {
        try {
            this.chirp.put("nick", strNick);
        } catch (JSONException e) {
            Log.d("JSON", e.toString());
        }
    }
    public void speak(final ChirpSDK chirpSDK) {
        Chirp chirp = new Chirp(this.chirp);
        chirpSDK.create(chirp, new CallbackCreate() {

            @Override
            public void onCreateResponse(ShortCode shortCode) {
                chirpSDK.play(shortCode);
            }

            @Override
            public void onCreateError(ChirpError chirpError) {
                Log.d("Error", chirpError.getMessage());
            }
        });
    }
}
