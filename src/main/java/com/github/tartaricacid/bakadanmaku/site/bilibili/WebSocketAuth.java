package com.github.tartaricacid.bakadanmaku.site.bilibili;

import com.github.tartaricacid.bakadanmaku.BakaDanmaku;
import com.github.tartaricacid.bakadanmaku.config.BilibiliConfig;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class WebSocketAuth {
    private static final String INIT_URL = "https://api.live.bilibili.com/xlive/web-room/v1/index/getDanmuInfo?id=%d&type=0";
    private static final Gson GSON = new Gson();
    private static final String AUTH_FORMAT = "{\"uid\":%d,\"roomid\":%d,\"protover\":3,\"buvid\":\"%s\",\"platform\":\"web\",\"type\":2,\"key\":\"%s\"}";

    public static byte[] newAuth(BilibiliConfig.Room room) {
        RoomInfo roomInfo = RoomInfo.getRoomInfo(room.getId());
        if (roomInfo == null) return null;

        if (room.isManualAuth()) {
            return room
                    .getAuth()
                    .replace("${roomId}", String.valueOf(roomInfo.getRoomId()))
                    .getBytes(StandardCharsets.UTF_8);
        }

        String cookie = "";
        String buvid3 = "";
        boolean ownerAuth = true;
        try {
            if (room.getCookie() == null) {
                // 游客方式
                ownerAuth = false;
                Buvid buvid = Buvid.newBuvid(roomInfo.getRoomId());
                if (buvid != null) {
                    cookie = String.format(
                            "buvid3=%s; buvid4=%s",
                            URLEncoder.encode(buvid.getBuvid3(), "UTF-8"),
                            URLEncoder.encode(buvid.getBuvid4(), "UTF-8")
                    );
                    buvid3 = buvid.getBuvid3();
                }
                BakaDanmaku.LOGGER.info("[BakaDanmaku] Login Be Guest");
            } else {
                // 登陆方式
                buvid3 = room.getCookie().getOrDefault("buvid3", Buvid.getBuvid3(roomInfo.getRoomId()));
                StringBuilder sb = new StringBuilder();
                room.getCookie().forEach((k, v) -> {
                    if ("buvid3".equals(k)) return;
                    try {
                        v = URLEncoder.encode(v, "UTF-8");
                    } catch (UnsupportedEncodingException ignored) {

                    }
                    sb.append(k).append("=").append(v).append("; ");
                });
                cookie = String.format("buvid3=%s; %s", URLEncoder.encode(buvid3, "UTF-8"), sb);
                BakaDanmaku.LOGGER.info("[BakaDanmaku] Login Be User");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (buvid3 == null) return null;
        try {
            URL url = new URL(String.format(INIT_URL, roomInfo.getRoomId()));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            try {
                conn.addRequestProperty("Cookie", cookie);
                conn.setRequestMethod("GET");
                String data = IOUtils.toString(conn.getInputStream());
                JsonObject response = GSON.fromJson(data, JsonObject.class);
                String token = response.getAsJsonObject("data").get("token").getAsString();
                String auth = String.format(
                        AUTH_FORMAT, ownerAuth ? roomInfo.getOwnerId() : 0, roomInfo.getRoomId(), buvid3, token
                );
                return auth.getBytes(StandardCharsets.UTF_8);
            } finally {
                conn.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class Buvid {
        static final String INIT_URL = "https://api.bilibili.com/x/frontend/finger/spi";
        static final Gson GSON = new Gson();

        private final String buvid3;
        private final String buvid4;

        private Buvid(String buvid3, String buvid4) {
            this.buvid3 = buvid3;
            this.buvid4 = buvid4;
        }

        public static Buvid newBuvid(long roomId) {
            try {
                URL url = new URL(INIT_URL + "?id=" + roomId);
                String data = IOUtils.toString(url, StandardCharsets.UTF_8);
                JsonObject response = GSON.fromJson(data, JsonObject.class);
                JsonObject obj = response.getAsJsonObject("data");
                return new Buvid(
                        obj.get("b_3").getAsString(),
                        obj.get("b_4").getAsString()
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public static String getBuvid3(long roomId) {
            Buvid buvid = newBuvid(roomId);
            return buvid == null ? null : buvid.buvid3;
        }

        public String getBuvid3() {
            return buvid3;
        }

        public String getBuvid4() {
            return buvid4;
        }
    }
}
