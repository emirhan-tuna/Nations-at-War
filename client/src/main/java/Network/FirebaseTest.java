package Network;
import com.badlogic.gdx.files.FileHandle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import network.Routes;

public class FirebaseTest {

    FileHandle file = Gdx.files.internal("secrets.json");
    String fileString = file.readString();

    JsonReader reader = new JsonReader();
    JsonValue secrets = reader.parse(file);

    String API_KEY = secrets.getString("API_KEY");
    String PROJECT_ID = secrets.getString("PROJECT_ID");


    public void doesAccountExist(String email, AccountCheck check) {
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();

        String jsonPayload = "{\"identifier\":\"" + email + "\",\"continueUri\":\"http://localhost\"}";
        String url = "https://identitytoolkit.googleapis.com/v1/accounts:createAuthUri?key=" + API_KEY;

        Net.HttpRequest request = requestBuilder.newRequest().method(Net.HttpMethods.POST).url(url).header("Content-type", "application/json").content(jsonPayload).build();

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse response) {
                int statusCode = response.getStatus().getStatusCode();
                String responseString = response.getResultAsString();

                if (statusCode == 200) {
                    JsonReader reader = new JsonReader();
                    JsonValue file = reader.parse(responseString);

                    boolean exists = file.getBoolean("registered", false);
                    check.onResult(exists);
                }
            }
            @Override
            public void failed(Throwable t) {
                System.out.println("Failed");
                t.printStackTrace();
            }
            @Override
            public void cancelled() {
                System.out.println("Cancelled");
            }
        });
    }

    public void signUp(String email, String password, String username, Stats aStats) {

        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();

        String jsonPayload = "{\"email\":\"" + email + "\",\"password\":\"" + password + "\",\"returnSecureToken\":true}";
        String url = "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=" + API_KEY;

        Net.HttpRequest request = requestBuilder.newRequest().method(Net.HttpMethods.POST).url(url).header("Content-type", "application/json").content(jsonPayload).build();

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse response) {
                int statusCode = response.getStatus().getStatusCode();
                String responseString = response.getResultAsString();

                if (statusCode == 200 || statusCode == 201) {
                    JsonReader reader = new JsonReader();
                    JsonValue json = reader.parse(responseString);

                    String idToken = json.getString("idToken");
                    String userId = json.getString("localId");

                    createNewUser(userId, idToken, username, aStats);
                    getPlayerStats(userId, aStats);
                    aStats.getUserID(userId);
                } else {
                    System.out.println("Not 200");
                }
            }
            @Override
            public void failed(Throwable t) {
                System.out.println("Failed");
            }
            @Override
            public void cancelled() {
                System.out.println("Cancelled");
            }
        });
    }

    public void login(String email, String password, Stats aStats) {
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();

        String jsonPayload = "{\"email\":\"" + email + "\",\"password\":\"" + password + "\",\"returnSecureToken\":true}";
        String url = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + API_KEY;

        Net.HttpRequest request = requestBuilder.newRequest().method(Net.HttpMethods.POST).url(url).header("Content-type", "application/json").content(jsonPayload).build();

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse response) {
                int statusCode = response.getStatus().getStatusCode();
                String responseString = response.getResultAsString();

                if (statusCode == 200) {
                    JsonReader reader = new JsonReader();

                    JsonValue file = reader.parse(responseString);
                    String userId = file.getString("localId");

                    getPlayerStats(userId, aStats);
                    aStats.getUserID(userId);
                } else {
                    System.out.println("Status code not 200.");
                }
            }
            @Override
            public void failed(Throwable t) {
                System.out.println("Failed" + t.getMessage());
                t.printStackTrace();
            }
            @Override
            public void cancelled() {
                System.out.println("Cancelled");
            }
        });
    }
    
    public void getPlayerStats (String uID, Stats aStats) {
        HttpRequestBuilder requestBuild = new HttpRequestBuilder();

        String url = "https://firestore.googleapis.com/v1/projects/" + PROJECT_ID + "/databases/(default)/documents/users/" + uID  + "?key=" + API_KEY;

        Net.HttpRequest request = requestBuild.newRequest().method(Net.HttpMethods.GET).url(url).build();

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse (Net.HttpResponse response) {
                int statusCode = response.getStatus().getStatusCode();
                String responseString = response.getResultAsString();

                if (statusCode == 200) {
                    JsonReader json = new JsonReader();

                    JsonValue file = json.parse(responseString);
                    JsonValue fields = file.get("fields");

                    String username = fields.get("username").getString("stringValue");
                    int playedGames = Integer.parseInt(fields.get("playedGames").getString("integerValue"));
                    int wins = Integer.parseInt(fields.get("wins").getString("integerValue"));

                    aStats.statsLoaded(username, playedGames, wins);
                } else {
                    System.out.println("Not 200 or 404.");
                }
            }

            @Override
            public void failed(Throwable t) {
                System.out.println("Failed. get player");
            }

            @Override
            public void cancelled() {
                System.out.println("Cancelled.");
            }
        });
    }

    public void createNewUser(String uID, String idToken, String username, Stats aStats) {
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();

        String jsonPayload = "{\"username\":\"" + username + "\"}";
        String url = "https://" + Routes.API_HOST + Routes.API_PROFILE;
        Net.HttpRequest request = requestBuilder.newRequest()
            .method(Net.HttpMethods.POST)
            .url(url)
            .header("Content-type", "application/json")
            .header("Authorization", "Bearer " + idToken) 
            .content(jsonPayload)
            .build();

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse response) {
                int statusCode = response.getStatus().getStatusCode();
                
                if (statusCode == 201 ||statusCode == 200) {
                    System.out.println("Created user.");
                    aStats.statsLoaded(username, 0, 0);
                } else {
                    System.out.println("Failed to create user.");
                }
            }
            @Override
            public void failed(Throwable t) {
                System.out.println("Failed create new");
            }
            @Override
            public void cancelled() {
                System.out.println("Cancelled");
            }
        });
    }


}