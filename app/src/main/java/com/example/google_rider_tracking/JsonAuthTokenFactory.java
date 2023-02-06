package com.example.google_rider_tracking;

import android.net.Uri;

import com.google.android.libraries.mapsplatform.transportation.consumer.auth.AuthTokenContext;
import com.google.android.libraries.mapsplatform.transportation.consumer.auth.AuthTokenFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

class JsonAuthTokenFactory implements AuthTokenFactory {
    private String token;  // initially null
    private long expiryTimeMs = 0;

    // This method is called on a thread whose only responsibility is to send
    // location updates. Blocking is OK, but just know that no location updates
    // can occur until this method returns.
    @Override
    public String getToken(AuthTokenContext authTokenContext) {
        if (System.currentTimeMillis() > expiryTimeMs) {
            // The token has expired, go get a new one.
            fetchNewToken(authTokenContext.getVehicleId());
        }
        return token;
    }

    private void fetchNewToken(String vehicleId) {
        String url =
                new Uri.Builder()
                        .scheme("https")
                        .authority("xpress-366609.uc.r.appspot.com/token")
                        .appendPath("token")
                        .appendQueryParameter("vehicleId", vehicleId)
                        .build()
                        .toString();

        try (Reader r = new InputStreamReader(new URL(url).openStream())) {
            com.google.gson.JsonObject obj
                    = com.google.gson.JsonParser.parseReader(r).getAsJsonObject();
            token = obj.get("Token").getAsString();
            expiryTimeMs = obj.get("TokenExpiryMs").getAsLong();

            // The expiry time could be an hour from now, but just to try and avoid
            // passing expired tokens, we subtract 10 minutes from that time.
            expiryTimeMs -= 10 * 60 * 1000;
        } catch (IOException e) {
            // It's OK to throw exceptions here. The StatusListener you passed to
            // create the DriverContext class will be notified and passed along the failed
            // update warning.
            throw new RuntimeException("Could not get auth token", e);
        }
    }
}
