package com.undot.triviabackend;





import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

/**
 * Created by 0503337710 on 27/03/2016.
 */
public class OAuthService  {
    String CLIENT_ID_WEB ="588482060414-u1ji3fig361m77nln0sa0coh4ctaeant.apps.googleusercontent.com";
    String CLIENT_ID_ANDROID_SDK ="588482060414-18k4gqi2tm53aonmofmjmfsjkf4erfae.apps.googleusercontent.com";
    GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
            .setAudience(Arrays.asList(CLIENT_ID_ANDROID_SDK,CLIENT_ID_WEB))
            // If you retrieved the token on Android using the Play Services 8.3 API or newer, set
            // the issuer to "https://accounts.google.com". Otherwise, set the issuer to
            // "accounts.google.com". If you need to verify tokens from multiple sources, build
            // a GoogleIdTokenVerifier for each issuer and try them both.
            .setIssuer("https://accounts.google.com")
            .build();

    public User getGoogleUser(String googleIdToken){
        User userInfo = new User();
        try{
            GoogleIdToken idToken = verifier.verify(googleIdToken);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                // Print user identifier
                userInfo.setGoogleId(payload.getSubject());
                System.out.println("User ID: " + userInfo.getGoogleId());

                // Get profile information from payload
                userInfo.setEmail(payload.getEmail());
                userInfo.setName((String)payload.get("name"));
                userInfo.setValidToken(true);
                return userInfo;
                // Use or store profile information
                // ...

            } else {
                userInfo.setValidToken(false);
                System.out.println("Invalid ID token.");
                return userInfo;
            }

        }catch (IOException e) {
            e.printStackTrace();
            userInfo.setValidToken(false);
            return userInfo;


        }
        catch (GeneralSecurityException e){
            e.printStackTrace();
            userInfo.setValidToken(false);
            return userInfo;
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
            userInfo.setValidToken(false);
            return userInfo;
        }
        }

}

