import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import network.Routes;

public class NotifyApi {
    private StartServer server;
    private ScheduledExecutorService heartbeatTimer;

    public NotifyApi(StartServer server) {
        this.server = server;
    }

    public long getCurrentGameId() {return server.getCurrentGameId();}

    public void startHeartbeat() {

        System.out.println("Starting API heartbeat for game: " + getCurrentGameId());
        
        heartbeatTimer = Executors.newSingleThreadScheduledExecutor();

        heartbeatTimer.scheduleAtFixedRate(() -> ping(), 10, 10, TimeUnit.SECONDS);
    }

    private void ping() {
        try {

            if(getCurrentGameId() == 0) {return;}

            URL url = new URL(Routes.API_HOST + ":" + Routes.API_PORT + Routes.API_HEARTBEAT);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + Routes.SERVER_SECRET);
            conn.setConnectTimeout(5000); 
            conn.setReadTimeout(5000);
            conn.setDoOutput(true);
            
            String jsonBody = "{\"host\": \"" + Routes.SERVER_IP + "\", \"port\": " + Routes.SERVER_PORT + ", \"gameId\": " + getCurrentGameId() + "}";
            
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            
            int responseCode = conn.getResponseCode();
            
            if (responseCode != 200 && responseCode != 204) {
                System.err.println("heartbeat fail(?????): " + responseCode);
            }
        } catch (Exception e) {
            System.err.println("failed to send heartbeat: " + e.getMessage());
        }
    }

    public boolean reserveGameFromApi() {
        System.out.println("reserving game...");
        
        try {
            URL url = new URL(Routes.API_HOST + ":" + Routes.API_PORT + "/reserve");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + Routes.SERVER_SECRET);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setDoOutput(true);
            
            String jsonBody = "{\"host\": \"" + Routes.SERVER_IP + "\", \"port\": " + Routes.SERVER_PORT + ", \"gameId\": " + getCurrentGameId() + "}";
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 200 || responseCode == 204) {
                
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                
                System.out.println("api gameadd response: " + response.toString());
                System.out.println("successfully reserved game: " + getCurrentGameId());
                return true;
                
            } else {
                System.err.println("reservation error: " + responseCode);
                return false;
            }

        } catch (Exception e) {
            System.err.println("api connection failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}