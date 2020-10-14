package com.github.tartaricacid.bakadanmaku.utils;

import java.util.regex.Pattern;

public final class BilibiliMsgSplit {
    private static final Pattern SPLIT = Pattern.compile("}.{16}\\{\"cmd\"");

    public static String[] split(String msg) {
        msg = msg.replaceAll("\n", "\u0020").replaceAll("\r", "\u0020");
        String[] split = SPLIT.split(msg);
        if (split.length > 1) {
            for (int i = 0; i < split.length; i++) {
                if (i == 0) {
                    split[0] = split[0] + "}";
                } else if (i == split.length - 1) {
                    split[i] = "{\"cmd\"" + split[i];
                } else {
                    split[i] = "{\"cmd\"" + split[i] + "}";
                }
            }
        }
        return split;
    }
}
