package com.github.tartaricacid.bakadanmaku.site.bilibili;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RoomId {
    private static final String INIT_URL = "https://api.live.bilibili.com/room/v1/Room/room_init";
    private static final Gson GSON = new Gson();
    private static final Pattern EXTRACT_ROOM_ID = Pattern.compile("\"room_id\":(\\d+),");

    public static int getRealRoomId(int roomId) {
        String realRoomId = null;
        try {
            URL url = new URL(INIT_URL + "?id=" + roomId);
            String data = IOUtils.toString(url, StandardCharsets.UTF_8);
            Matcher matcher = EXTRACT_ROOM_ID.matcher(data);
            if (matcher.find()) {
                realRoomId = matcher.group(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (realRoomId == null) {
            return -1;
        }
        return Integer.parseInt(realRoomId);
    }
}
