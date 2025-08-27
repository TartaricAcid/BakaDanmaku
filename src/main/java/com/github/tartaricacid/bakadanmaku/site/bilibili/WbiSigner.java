package com.github.tartaricacid.bakadanmaku.site.bilibili;


import com.github.tartaricacid.bakadanmaku.BakaDanmaku;
import com.google.common.net.HttpHeaders;
import com.google.common.net.PercentEscaper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

/**
 * 参考自：<a href="https://socialsisteryi.github.io/bilibili-API-collect/docs/misc/sign/wbi.html">BAC Document：WBI 签名</a>
 */
public class WbiSigner {
    private static final PercentEscaper ESCAPER = new PercentEscaper("-_.~", false);
    private static final String NAV_URL = "https://api.bilibili.com/x/web-interface/nav";
    private static final Gson GSON = new Gson();

    private static final int[] MIXIN_KEY_ENC_TAB = new int[]{
            46, 47, 18, 2, 53, 8, 23, 32, 15, 50, 10, 31, 58, 3, 45, 35,
            27, 43, 5, 49, 33, 9, 42, 19, 29, 28, 14, 39, 12, 38, 41, 13,
            37, 48, 7, 16, 24, 55, 40, 61, 26, 17, 0, 1, 60, 51, 30, 4,
            22, 25, 54, 21, 56, 59, 6, 63, 57, 62, 11, 36, 20, 34, 44, 52
    };

    static String wbiSign(Map<String, String> params) {
        long wts = System.currentTimeMillis() / 1000L;

        String query = encodeQuery(params);

        Map<String, String> forSign = new TreeMap<>();
        for (Map.Entry<String, String> e : params.entrySet()) {
            String v = e.getValue() == null ? "" : e.getValue();

            v = v.replaceAll("[!'()*]", "");
            forSign.put(e.getKey(), v);
        }
        forSign.put("wts", String.valueOf(wts));

        String mixinKey = getMixinKey();
        String wRid = DigestUtils.md5Hex((encodeQuery(forSign) + mixinKey).getBytes(StandardCharsets.UTF_8));

        return query + "&wts=" + wts + "&w_rid=" + wRid;
    }

    static String encodeQuery(Map<String, String> params) {
        StringBuilder query = new StringBuilder();

        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!first) {
                query.append('&');
            }
            first = false;

            query.append(ESCAPER.escape(entry.getKey()))
                    .append("=")
                    .append(ESCAPER.escape(entry.getValue()));
        }

        return query.toString();
    }

    static String getMixinKey() {
        try {
            // 获取 img_url 和 sub_url
            HttpURLConnection conn = (HttpURLConnection) new URL(NAV_URL).openConnection();

            conn.setRequestMethod("GET");
            conn.addRequestProperty(HttpHeaders.USER_AGENT, "Mozilla/5.0");
            conn.addRequestProperty(HttpHeaders.REFERER, "https://www.bilibili.com/");

            String data = IOUtils.toString(conn.getInputStream(), StandardCharsets.UTF_8);
            conn.disconnect();

            JsonObject response = GSON.fromJson(data, JsonObject.class);
            JsonObject wbiImg = response.getAsJsonObject("data").getAsJsonObject("wbi_img");

            String imgKey = getFileName(wbiImg.get("img_url").getAsString());
            String subKey = getFileName(wbiImg.get("sub_url").getAsString());

            String raw = imgKey + subKey;
            StringBuilder mixed = new StringBuilder();
            for (int idx : MIXIN_KEY_ENC_TAB) {
                if (idx < raw.length()) {
                    mixed.append(raw.charAt(idx));
                }
            }

            return mixed.substring(0, 32);
        } catch (Exception e) {
            BakaDanmaku.LOGGER.error(e);
        }
        return null;
    }

    static String getFileName(String url) {
        String fullFileName = url.substring(url.lastIndexOf('/') + 1);
        return fullFileName.substring(0, fullFileName.lastIndexOf('.'));
    }
}