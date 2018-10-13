package github.tartaricacid.bakadanmaku.api.thread;

import java.util.HashMap;

public class DanmakuThreadFactory {
    private static final HashMap<String, BaseDanmakuThread> danmakuThreads = new HashMap<>();

    public static void setDanmakuThread(String name, BaseDanmakuThread thread) {
        danmakuThreads.put(name, thread);
    }

    public static BaseDanmakuThread getDanmakuThread(String platform) {
        return danmakuThreads.get(platform);
    }
}
