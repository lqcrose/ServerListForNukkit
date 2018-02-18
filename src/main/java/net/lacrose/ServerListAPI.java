package net.lacrose;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClient.BoundRequestBuilder;

import cn.nukkit.Server;

public class ServerListAPI {

    private final String token = "YOUR TOKEN HERE";

    private String accessToken = "";

    private final int maxPlayers = Server.getInstance().getMaxPlayers();

    public String login() {
        Map<String, String> data = new HashMap<>();
        data.put("max", String.valueOf(maxPlayers));
        data.put("now", "0");
        data.put("type", "start");
        data.put("server_token", this.token);
        String ret = this.sendData("update", data);

        data.clear();
        data.put("state", "1");
        data.put("access_token", this.accessToken);
        this.sendData("push", data);

        return ret;
    }

    public String logout() {
        Map<String, String> data = new HashMap<>();
        data.put("type", "stop");
        data.put("access_token", this.accessToken);
        return this.sendData("update", data);
    }

    public String event(String status) {
        status = status.equals("on") ? "event" : "normal";
        Map<String, String> data = new HashMap<>();
        data.put("type", status);
        data.put("access_token", this.accessToken);
        return this.sendData("update", data);
    }

    public String updatePlayers(String type) {
        Map<String, String> data = new HashMap<>();
        data.put("max", String.valueOf(maxPlayers));
        data.put("now", String.valueOf(Server.getInstance().getOnlinePlayers().size() - (type.equals("join") ? 0 : 1)));
        data.put("type", type);
        data.put("access_token", this.accessToken);
        return this.sendData("update", data);
    }

    public String updateTime() {
        Map<String, String> data = new HashMap<String, String>();
        data.put("max", String.valueOf(maxPlayers));
        data.put("type", "time");
        data.put("access_token", this.accessToken);
        return this.sendData("update", data);
    }

    private String sendData(String endPoint, Map<String, String> data) {
        AsyncHttpClient client = new AsyncHttpClient();
        String ret = "";
        try {
            BoundRequestBuilder builder = client.preparePost("http://api.pmmp.jp.net/" + endPoint);
            for (String key : data.keySet()) {
                String value = data.get(key);
                builder.addFormParam(key, value);
            }
            Map<String, String> responseData = new Gson().fromJson(builder.execute().get().getResponseBody("UTF-8"), 
                    new TypeToken<HashMap<String, String>>(){}.getType());
            if (responseData != null) {
                if (responseData.containsKey("token")) {
                    this.accessToken = responseData.get("token").replace("'", "");
                }
    
                if (responseData.containsKey("msg")) {
                    ret = responseData.get("msg");
                }
            }
        } catch (InterruptedException | ExecutionException | IOException e) {
            e.printStackTrace();
        } finally {
            client.close();
        }
        return ret;
    }
}
