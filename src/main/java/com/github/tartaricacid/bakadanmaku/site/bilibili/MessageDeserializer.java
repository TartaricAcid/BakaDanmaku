package com.github.tartaricacid.bakadanmaku.site.bilibili;

import com.github.tartaricacid.bakadanmaku.config.BilibiliConfig;
import com.google.gson.*;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;

public class MessageDeserializer implements JsonDeserializer<String> {
    private final BilibiliConfig config;

    public MessageDeserializer(BilibiliConfig config) {
        this.config = config;
    }

    @Override
    public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonObject()) {
            JsonObject data = json.getAsJsonObject();
            String type = data.get("cmd").getAsString();
            switch (type) {
                case "DANMU_MSG":
                    if (config.getDanmaku().isShow()) {
                        return handDanmaku(data);
                    }
                    break;
                case "SEND_GIFT":
                    if (config.getGift().isShow()) {
                        return handGift(data);
                    }
                    break;
                case "COMBO_SEND":
                    if (config.getGift().isShow()) {
                        return handComboGift(data);
                    }
                    break;
                case "INTERACT_WORD":
                    if (config.getEnter().isShowNormal()) {
                        return handNormalEnter(data);
                    }
                    break;
                case "WELCOME":
                    if (config.getEnter().isShowNormal()) {
                        return handWelcome(data);
                    }
                    break;
                case "WELCOME_GUARD":
                    if (config.getEnter().isShowGuard()) {
                        return handGuardWelcome(data);
                    }
                    break;
                case "GUARD_BUY":
                    if (config.getGuard().isShow()) {
                        return handBuyGuard(data);
                    }
                    break;
                case "SUPER_CHAT_MESSAGE":
                    if (config.getSc().isShow()) {
                        return handSuperChat(data);
                    }
                    break;
                default:
            }
        }
        return null;
    }

    private String handDanmaku(JsonObject dataIn) {
        JsonArray info = dataIn.getAsJsonArray("info");
        JsonArray user = info.get(2).getAsJsonArray();

        String userName = user.get(1).getAsString();
        String danmaku = info.get(1).getAsString();
        boolean isAdmin = user.get(2).getAsInt() == 1;
        boolean isGuard = StringUtils.isNotBlank(user.get(7).getAsString());

        for (String block : config.getDanmaku().getBlockWord()) {
            if (danmaku.contains(block)) {
                return null;
            }
        }

        if (isAdmin) {
            return String.format(config.getDanmaku().getAdminStyleFormatted(), userName, danmaku);
        }
        if (isGuard) {
            return String.format(config.getDanmaku().getGuardStyleFormatted(), userName, danmaku);
        }

        return String.format(config.getDanmaku().getNormalStyleFormatted(), userName, danmaku);
    }

    private String handGift(JsonObject dataIn) {
        JsonObject data = dataIn.getAsJsonObject("data");
        String userName = data.get("uname").getAsString();
        String action = data.get("action").getAsString();
        String giftName = data.get("giftName").getAsString();
        int num = data.get("num").getAsInt();

        for (String block : config.getGift().getBlockGift()) {
            if (giftName.equals(block)) {
                return null;
            }
        }

        return String.format(config.getGift().getStyleFormatted(), userName, action, giftName, num);
    }

    private String handComboGift(JsonObject dataIn) {
        JsonObject data = dataIn.getAsJsonObject("data");
        String userName = data.get("uname").getAsString();
        String action = data.get("action").getAsString();
        String giftName = data.get("gift_name").getAsString();
        int num = data.get("total_num").getAsInt();

        for (String block : config.getGift().getBlockGift()) {
            if (giftName.equals(block)) {
                return null;
            }
        }

        return String.format(config.getGift().getStyleFormatted(), userName, action, giftName, num);
    }

    private String handNormalEnter(JsonObject dataIn) {
        JsonObject data = dataIn.getAsJsonObject("data");
        String userName = data.get("uname").getAsString();

        return String.format(config.getEnter().getNormalStyleFormatted(), userName);
    }

    private String handWelcome(JsonObject dataIn) {
        JsonObject data = dataIn.getAsJsonObject("data");
        String userName = data.get("uname").getAsString();

        return String.format(config.getEnter().getNormalStyleFormatted(), userName);
    }

    private String handGuardWelcome(JsonObject dataIn) {
        JsonObject data = dataIn.getAsJsonObject("data");
        String userName = data.get("username").getAsString();
        int level = data.get("guard_level").getAsInt();
        switch (level) {
            case 1:
                return String.format(config.getEnter().getGuardStyle1Formatted(), userName);
            case 2:
                return String.format(config.getEnter().getGuardStyle2Formatted(), userName);
            case 3:
                return String.format(config.getEnter().getGuardStyle3Formatted(), userName);
            default:
                return null;
        }
    }

    private String handBuyGuard(JsonObject dataIn) {
        JsonObject data = dataIn.getAsJsonObject("data");
        String userName = data.get("username").getAsString();
        int level = data.get("guard_level").getAsInt();
        switch (level) {
            case 1:
                return String.format(config.getGuard().getGuardStyle1Formatted(), userName);
            case 2:
                return String.format(config.getGuard().getGuardStyle2Formatted(), userName);
            case 3:
                return String.format(config.getGuard().getGuardStyle3Formatted(), userName);
            default:
                return null;
        }
    }

    private String handSuperChat(JsonObject dataIn) {
        JsonObject data = dataIn.getAsJsonObject("data");
        String userName = data.getAsJsonObject("user_info").get("uname").getAsString();
        String message = data.get("message").getAsString();
        int price = data.get("price").getAsInt();

        return String.format(config.getSc().getStyleFormatted(), userName, message, price);
    }
}
