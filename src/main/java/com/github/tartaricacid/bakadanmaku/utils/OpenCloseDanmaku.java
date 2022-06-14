package com.github.tartaricacid.bakadanmaku.utils;

import com.github.tartaricacid.bakadanmaku.BakaDanmaku;
import com.github.tartaricacid.bakadanmaku.site.bilibili.BilibiliSite;
import com.github.tartaricacid.bakadanmaku.websocket.WebSocketClient;

import static com.github.tartaricacid.bakadanmaku.config.ConfigManger.getBilibiliConfig;

public class OpenCloseDanmaku {
    public static void openDanmaku() {
        BilibiliSite site = new BilibiliSite(getBilibiliConfig());
        if (site.getConfig().getRoom().isEnable()) {
            BakaDanmaku.WEBSOCKET_CLIENT = new WebSocketClient(site);
            try {
                BakaDanmaku.WEBSOCKET_CLIENT.open();
            } catch (Exception e) {
                BakaDanmaku.WEBSOCKET_CLIENT = null;
                e.printStackTrace();
            }
        }
    }

    public static void closeDanmaku() {
        if (BakaDanmaku.WEBSOCKET_CLIENT == null) {
            return;
        }
        try {
            BakaDanmaku.WEBSOCKET_CLIENT.close();
        } catch (Exception ignore) {
        } finally {
            BakaDanmaku.WEBSOCKET_CLIENT = null;
        }
    }
}
