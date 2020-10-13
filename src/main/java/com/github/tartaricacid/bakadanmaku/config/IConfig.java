package com.github.tartaricacid.bakadanmaku.config;

public interface IConfig {
    /**
     * 直播平台的名称
     *
     * @return 直播平台
     */
    String getSiteName();

    /**
     * 配置文件的名称
     *
     * @return 配置文件的名称
     */
    String getConfigName();
}
