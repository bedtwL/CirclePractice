package me.itsglobally.circlePractice.managers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class DaemonManager extends WebSocketClient {
    private final Gson gson = new Gson();

    public DaemonManager(URI serverUri) {
        super(serverUri);
    }

    private static JsonObject basic(String code) {
        JsonObject obj = new JsonObject();
        obj.addProperty("server", "smp");
        if (code != null) {
            obj.addProperty("code", "0");
        }
        return obj;
    }

    private static JsonObject basic() {
        JsonObject obj = new JsonObject();
        obj.addProperty("server", "smp");
        obj.addProperty("code", "0");
        return obj;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Bukkit.getLogger().info("Connected to server");
        JsonObject obj = basic(null);
        obj.addProperty("message", "connected");
        send(gson.toJson(obj));
    }

    @Override
    public void onMessage(String message) {
        try {
            JsonObject json = gson.fromJson(message, JsonObject.class);
            String server = json.has("server") ? json.get("server").getAsString() : "";
            String cmd = json.has("cmd") ? json.get("cmd").getAsString() : "";
            if (cmd.isEmpty()) return;
            switch (cmd) {
                default:
            }
        } catch (Exception e) {
            Bukkit.getLogger().info("Invalid JSON: " + message);
            e.printStackTrace();
            JsonObject obj = basic(null);
            obj.addProperty("message", e.getMessage());
            send(gson.toJson(obj));
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Bukkit.getLogger().warning("Connection closed: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

}
