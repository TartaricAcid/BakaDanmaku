package com.github.tartaricacid.bakadanmaku.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.regex.Matcher;

public class BilibiliConfig implements IConfig {
    @Expose
    private static final String SITE_NAME = "哔哩哔哩";

    @Expose
    private static final String CONFIG_NAME = "bilibili";

    @SerializedName("room")
    private Room room = new Room();

    @SerializedName("danmaku")
    private Danmaku danmaku = new Danmaku();

    @SerializedName("gift")
    private Gift gift = new Gift();

    @SerializedName("enter")
    private Enter enter = new Enter();

    @SerializedName("guard")
    private Guard guard = new Guard();

    @SerializedName("sc")
    private SpecialChat sc = new SpecialChat();

    public BilibiliConfig deco() {
        this.danmaku = danmaku.deco();
        this.gift = gift.deco();
        this.enter = enter.deco();
        this.guard = guard.deco();
        this.sc = sc.deco();
        return this;
    }

    @Override
    public String getSiteName() {
        return SITE_NAME;
    }

    @Override
    public String getConfigName() {
        return CONFIG_NAME;
    }

    public Room getRoom() {
        return room;
    }

    public Danmaku getDanmaku() {
        return danmaku;
    }

    public Gift getGift() {
        return gift;
    }

    public Enter getEnter() {
        return enter;
    }

    public Guard getGuard() {
        return guard;
    }

    public SpecialChat getSc() {
        return sc;
    }

    public static class Room {
        @SerializedName("id")
        private int id = -1;

        @SerializedName("enable")
        private boolean enable = false;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }
    }

    public static class Danmaku {
        @SerializedName("show")
        private boolean show = true;

        @SerializedName("normal_style")
        private String normalStyle = "&7[普] &b<%{user}> &f%{danmaku}";

        private transient String normalStyleFormatted = "";

        @SerializedName("guard_style")
        private String guardStyle = "&6[舰] &2<%{user}> &f%{danmaku}";

        private transient String guardStyleFormatted = "";

        @SerializedName("admin_style")
        private String adminStyle = "&4[房] &d<%{user}> &f%{danmaku}";

        private transient String adminStyleFormatted = "";

        @SerializedName("block_word")
        private String[] blockWord = {"小鬼", "biss", "嘴臭", "骂我", "傻逼", "弱智", "脑残", "cnm"};

        public boolean isShow() {
            return show;
        }

        public void setShow(boolean show) {
            this.show = show;
        }

        public String getNormalStyle() {
            return normalStyle;
        }

        public void setNormalStyle(String normalStyle) {
            this.normalStyle = normalStyle;
        }

        public String getGuardStyle() {
            return guardStyle;
        }

        public void setGuardStyle(String guardStyle) {
            this.guardStyle = guardStyle;
        }

        public String getAdminStyle() {
            return adminStyle;
        }

        public void setAdminStyle(String adminStyle) {
            this.adminStyle = adminStyle;
        }

        public String[] getBlockWord() {
            return blockWord;
        }

        public void setBlockWord(String[] blockWord) {
            this.blockWord = blockWord;
        }

        public String getNormalStyleFormatted() {
            return normalStyleFormatted;
        }

        public String getGuardStyleFormatted() {
            return guardStyleFormatted;
        }

        public String getAdminStyleFormatted() {
            return adminStyleFormatted;
        }

        public Danmaku deco() {
            this.normalStyleFormatted = normalStyle.replaceAll("&([0-9a-fk-or])", "§$1")
                    .replaceAll("%\\{user}", Matcher.quoteReplacement("%1$s"))
                    .replaceAll("%\\{danmaku}", Matcher.quoteReplacement("%2$s"));
            this.guardStyleFormatted = guardStyle.replaceAll("&([0-9a-fk-or])", "§$1")
                    .replaceAll("%\\{user}", Matcher.quoteReplacement("%1$s"))
                    .replaceAll("%\\{danmaku}", Matcher.quoteReplacement("%2$s"));
            this.adminStyleFormatted = adminStyle.replaceAll("&([0-9a-fk-or])", "§$1")
                    .replaceAll("%\\{user}", Matcher.quoteReplacement("%1$s"))
                    .replaceAll("%\\{danmaku}", Matcher.quoteReplacement("%2$s"));
            return this;
        }
    }

    public static class Gift {
        @SerializedName("show")
        private boolean show = true;

        @SerializedName("style")
        private String style = "&7%{user} %{action} [%{gift}] × %{num}";

        private transient String styleFormatted = "";

        @SerializedName("block_gift")
        private String[] blockGift = {"辣条", "小心心"};

        public boolean isShow() {
            return show;
        }

        public void setShow(boolean show) {
            this.show = show;
        }

        public String getStyle() {
            return style;
        }

        public void setStyle(String style) {
            this.style = style;
        }

        public String[] getBlockGift() {
            return blockGift;
        }

        public void setBlockGift(String[] blockGift) {
            this.blockGift = blockGift;
        }

        public String getStyleFormatted() {
            return styleFormatted;
        }

        public Gift deco() {
            this.styleFormatted = style.replaceAll("&([0-9a-fk-or])", "§$1")
                    .replaceAll("%\\{user}", Matcher.quoteReplacement("%1$s"))
                    .replaceAll("%\\{action}", Matcher.quoteReplacement("%2$s"))
                    .replaceAll("%\\{gift}", Matcher.quoteReplacement("%3$s"))
                    .replaceAll("%\\{num}", Matcher.quoteReplacement("%4$s"));
            return this;
        }
    }

    public static class Enter {
        @SerializedName("show_normal")
        private boolean showNormal = false;

        @SerializedName("show_guard")
        private boolean showGuard = true;

        @SerializedName("normal_style")
        private String normalStyle = "&7欢迎 %{user} 进入直播间";

        private transient String normalStyleFormatted = "";

        @SerializedName("guard_style_1")
        private String guardStyle1 = "&4欢迎总督 %{user} 进入直播间";

        private transient String guardStyle1Formatted = "";

        @SerializedName("guard_style_2")
        private String guardStyle2 = "&6欢迎提督 %{user} 进入直播间";

        private transient String guardStyle2Formatted = "";

        @SerializedName("guard_style_3")
        private String guardStyle3 = "&3欢迎舰长 %{user} 进入直播间";

        private transient String guardStyle3Formatted = "";

        public boolean isShowNormal() {
            return showNormal;
        }

        public void setShowNormal(boolean showNormal) {
            this.showNormal = showNormal;
        }

        public boolean isShowGuard() {
            return showGuard;
        }

        public void setShowGuard(boolean showGuard) {
            this.showGuard = showGuard;
        }

        public String getNormalStyle() {
            return normalStyle;
        }

        public void setNormalStyle(String normalStyle) {
            this.normalStyle = normalStyle;
        }

        public String getGuardStyle1() {
            return guardStyle1;
        }

        public void setGuardStyle1(String guardStyle1) {
            this.guardStyle1 = guardStyle1;
        }

        public String getGuardStyle2() {
            return guardStyle2;
        }

        public void setGuardStyle2(String guardStyle2) {
            this.guardStyle2 = guardStyle2;
        }

        public String getGuardStyle3() {
            return guardStyle3;
        }

        public void setGuardStyle3(String guardStyle3) {
            this.guardStyle3 = guardStyle3;
        }

        public String getNormalStyleFormatted() {
            return normalStyleFormatted;
        }

        public String getGuardStyle1Formatted() {
            return guardStyle1Formatted;
        }

        public String getGuardStyle2Formatted() {
            return guardStyle2Formatted;
        }

        public String getGuardStyle3Formatted() {
            return guardStyle3Formatted;
        }

        public Enter deco() {
            this.normalStyleFormatted = normalStyle.replaceAll("&([0-9a-fk-or])", "§$1")
                    .replaceAll("%\\{user}", Matcher.quoteReplacement("%1$s"));
            this.guardStyle1Formatted = guardStyle1.replaceAll("&([0-9a-fk-or])", "§$1")
                    .replaceAll("%\\{user}", Matcher.quoteReplacement("%1$s"));
            this.guardStyle2Formatted = guardStyle2.replaceAll("&([0-9a-fk-or])", "§$1")
                    .replaceAll("%\\{user}", Matcher.quoteReplacement("%1$s"));
            this.guardStyle3Formatted = guardStyle3.replaceAll("&([0-9a-fk-or])", "§$1")
                    .replaceAll("%\\{user}", Matcher.quoteReplacement("%1$s"));
            return this;
        }
    }

    public static class Guard {
        @SerializedName("show")
        private boolean show = true;

        @SerializedName("guard_style_1")
        private String guardStyle1 = "&4%{user} 开通了主播的总督";

        private transient String guardStyle1Formatted = "";

        @SerializedName("guard_style_2")
        private String guardStyle2 = "&6%{user} 开通了主播的提督";

        private transient String guardStyle2Formatted = "";

        @SerializedName("guard_style_3")
        private String guardStyle3 = "&3%{user} 开通了主播的舰长";

        private transient String guardStyle3Formatted = "";

        public boolean isShow() {
            return show;
        }

        public void setShow(boolean show) {
            this.show = show;
        }

        public String getGuardStyle1() {
            return guardStyle1;
        }

        public void setGuardStyle1(String guardStyle1) {
            this.guardStyle1 = guardStyle1;
        }

        public String getGuardStyle2() {
            return guardStyle2;
        }

        public void setGuardStyle2(String guardStyle2) {
            this.guardStyle2 = guardStyle2;
        }

        public String getGuardStyle3() {
            return guardStyle3;
        }

        public void setGuardStyle3(String guardStyle3) {
            this.guardStyle3 = guardStyle3;
        }

        public String getGuardStyle1Formatted() {
            return guardStyle1Formatted;
        }

        public String getGuardStyle2Formatted() {
            return guardStyle2Formatted;
        }

        public String getGuardStyle3Formatted() {
            return guardStyle3Formatted;
        }

        public Guard deco() {
            this.guardStyle1Formatted = guardStyle1.replaceAll("&([0-9a-fk-or])", "§$1")
                    .replaceAll("%\\{user}", Matcher.quoteReplacement("%1$s"));
            this.guardStyle2Formatted = guardStyle2.replaceAll("&([0-9a-fk-or])", "§$1")
                    .replaceAll("%\\{user}", Matcher.quoteReplacement("%1$s"));
            this.guardStyle3Formatted = guardStyle3.replaceAll("&([0-9a-fk-or])", "§$1")
                    .replaceAll("%\\{user}", Matcher.quoteReplacement("%1$s"));
            return this;
        }
    }

    public static class SpecialChat {
        @SerializedName("show")
        private boolean show = true;

        @SerializedName("style")
        private String style = "&4%{user} > %{msg} [¥%{price}]";

        private transient String styleFormatted = "";

        public boolean isShow() {
            return show;
        }

        public void setShow(boolean show) {
            this.show = show;
        }

        public String getStyle() {
            return style;
        }

        public void setStyle(String style) {
            this.style = style;
        }

        public String getStyleFormatted() {
            return styleFormatted;
        }

        public SpecialChat deco() {
            this.styleFormatted = style.replaceAll("&([0-9a-fk-or])", "§$1")
                    .replaceAll("%\\{user}", Matcher.quoteReplacement("%1$s"))
                    .replaceAll("%\\{msg}", Matcher.quoteReplacement("%2$s"))
                    .replaceAll("%\\{price}", Matcher.quoteReplacement("%3$s"));
            return this;
        }
    }
}
