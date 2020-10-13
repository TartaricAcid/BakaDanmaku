//package com.github.tartaricacid.bakadanmaku.gui;
//
//import com.github.tartaricacid.bakadanmaku.BakaDanmaku;
//import com.github.tartaricacid.bakadanmaku.config.BilibiliConfig;
//import com.github.tartaricacid.bakadanmaku.gui.bilibili.*;
//import com.github.tartaricacid.bakadanmaku.utils.OpenCloseDanmaku;
//import com.google.common.collect.Maps;
//import com.mojang.blaze3d.systems.RenderSystem;
//import net.minecraft.client.gui.screen.Screen;
//import net.minecraft.client.gui.widget.TextFieldWidget;
//import net.minecraft.client.gui.widget.button.ImageButton;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.text.StringTextComponent;
//
//import java.util.HashMap;
//import java.util.UnknownFormatConversionException;
//
//import static com.github.tartaricacid.bakadanmaku.config.ConfigManger.saveBilibiliConfig;
//
//public abstract class ConfigGui extends Screen {
//    protected static final ResourceLocation ICON = new ResourceLocation(BakaDanmaku.MOD_ID, "textures/icon.png");
//    private final HashMap<String, TextFieldWidget> textFields = Maps.newHashMap();
//    protected int configWidth;
//    protected int configHeight;
//    protected int configStartX;
//    protected int configStartY;
//    protected BilibiliConfig config;
//
//    public ConfigGui(BilibiliConfig config) {
//        super(new StringTextComponent("bakadanmaku.config"));
//        this.config = config;
//    }
//
//    /**
//     * 添加文本框
//     *
//     * @param textFields 文本框组
//     */
//    public abstract void addTextFields(HashMap<String, TextFieldWidget> textFields);
//
//    /**
//     * 绘制配置界面自己的元素
//     */
//    public abstract void drawConfigScreen(int mouseX, int mouseY, float partialTicks);
//
//    @Override
//    public void init() {
//        this.buttons.clear();
//        this.textFields.clear();
//
//        if (minecraft != null) {
//            minecraft.keyboardListener.enableRepeatEvents(true);
//        }
//
//        configHeight = height - 10;
//        configWidth = width - 260;
//        configStartX = 105;
//        configStartY = 5;
//
//        this.addTextFields(textFields);
//        this.children.addAll(textFields.values());
//
//        addButton(new ImageButton(2, 21, 96, 15,
//                128, 128, 0, ICON,
//                (i) -> {
//                    if (minecraft != null) {
//                        minecraft.displayGuiScreen(new RoomGui(config));
//                    }
//                }));
////        buttons.add(new ImageButton(2, 21 + 15, 96, 15,
////                128, 128, 0, ICON,
////                (i) -> {
////                    if (minecraft != null) {
////                        minecraft.displayGuiScreen(new DanmakuGui(config));
////                    }
////                }));
////        buttons.add(new ImageButton(2, 21 + 15 * 2, 96, 15,
////                128, 128, 0, ICON,
////                (i) -> {
////                    if (minecraft != null) {
////                        minecraft.displayGuiScreen(new GiftGui(config));
////                    }
////                }));
////        buttons.add(new ImageButton(2, 21 + 15 * 3, 96, 15,
////                128, 128, 0, ICON,
////                (i) -> {
////                    if (minecraft != null) {
////                        minecraft.displayGuiScreen(new EnterGui(config));
////                    }
////                }));
////        buttons.add(new ImageButton(2, 21 + 15 * 4, 96, 15,
////                128, 128, 0, ICON,
////                (i) -> {
////                    if (minecraft != null) {
////                        minecraft.displayGuiScreen(new GuardGui(config));
////                    }
////                }));
////        buttons.add(new ImageButton(2, 21 + 15 * 5, 96, 15,
////                128, 128, 0, ICON,
////                (i) -> {
////                    if (minecraft != null) {
////                        minecraft.displayGuiScreen(new ScGui(config));
////                    }
////                }));
//
//
//        addButton(new ImageButton(5, height - 50, 90, 20,
//                128, 128, 0, ICON,
//                (i) -> {
//                    if (minecraft != null) {
//                        minecraft.displayGuiScreen(null);
//                        saveBilibiliConfig(config);
//                        OpenCloseDanmaku.closeDanmaku();
//                        OpenCloseDanmaku.openDanmaku();
//                    }
//                }));
//        addButton(new ImageButton(2, height - 25, 96, 20,
//                128, 128, 0, ICON,
//                (i) -> {
//                    if (minecraft != null) {
//                        minecraft.displayGuiScreen(null);
//                    }
//                }));
//    }
//
//    @Override
//    public void render(int mouseX, int mouseY, float partialTicks) {
//        renderBackground();
//        // 左侧按钮列表
//        fillGradient(0, 0, 100, height, 0xdd000000, 0xdd000000);
//        // 标题背景
//        fillGradient(2, 2, 100 - 2, 20 - 2, 0xdd1f1f1f, 0xdd1f1f1f);
//
//        // drawGradientRect(2, 25 - 4, 98, 25 + 15 - 4, 0xdd339966, 0xdd339966);
//
//        // 右侧弹幕样式显示背景
//        fillGradient(width - 150, 0, width, height, 0xdd1f1f1f, 0xdd1f1f1f);
//        // 中间配置界面
//        fillGradient(configStartX, configStartY, configStartX + configWidth,
//                configStartY + configHeight, 0xaa000000, 0xaa000000);
//
//        // 最下方按钮
//        fillGradient(5, height - 50, 95, height - 30, 0xff31343f, 0xff31343f);
//        fillGradient(5, height - 25, 95, height - 5, 0xff31343f, 0xff31343f);
//
//        drawConfigScreen(mouseX, mouseY, partialTicks);
//
//        font.drawString("弹幕模组配置界面", 6, 6, 0xffffff);
//        font.drawString("房间设置", 12, 25, 0xffffff);
//        font.drawString("弹幕设置", 12, 25 + 15, 0xffffff);
//        font.drawString("送礼设置", 12, 25 + 15 * 2, 0xffffff);
//        font.drawString("进房设置", 12, 25 + 15 * 3, 0xffffff);
//        font.drawString("上舰设置", 12, 25 + 15 * 4, 0xffffff);
//        font.drawString("醒目留言（SC）", 12, 25 + 15 * 5, 0xffffff);
//
//        String text1 = "保存并重载";
//        font.drawString(text1, (100 - font.getStringWidth(text1)) / 2, height - 44, 0xffffff);
//        String text2 = "直接退出";
//        font.drawString(text2, (100 - font.getStringWidth(text2)) / 2, height - 19, 0xffffff);
//
//        int startY = 10;
//        int startX = width - 150 + 8;
//        if (config.getDanmaku().isShow()) {
//            startY = drawExampleMsg(config.getDanmaku().getNormalStyleFormatted(), startX, startY, "酒石酸菌", "我是普通观众");
//            startY = drawExampleMsg(config.getDanmaku().getAdminStyleFormatted(), startX, startY, "琥珀酸", "我是房管");
//            startY = drawExampleMsg(config.getDanmaku().getGuardStyleFormatted(), startX, startY, "帕金伊", "我上舰了");
//        }
//        if (config.getGift().isShow()) {
//            startY = drawExampleMsg(config.getGift().getStyleFormatted(), startX, startY,
//                    "天幂", "投喂了", "风铃", 943);
//        }
//        if (config.getEnter().isShowNormal()) {
//            startY = drawExampleMsg(config.getEnter().getNormalStyleFormatted(), startX, startY, "梨木利亚");
//        }
//        if (config.getEnter().isShowGuard()) {
//            startY = drawExampleMsg(config.getEnter().getGuardStyle1Formatted(), startX, startY, "嚼铝赤城桑");
//            startY = drawExampleMsg(config.getEnter().getGuardStyle2Formatted(), startX, startY, "花玥");
//            startY = drawExampleMsg(config.getEnter().getGuardStyle3Formatted(), startX, startY, "天顶乌");
//        }
//
//        if (config.getGuard().isShow()) {
//            startY = drawExampleMsg(config.getGuard().getGuardStyle1Formatted(), startX, startY, "迺逸夫");
//            startY = drawExampleMsg(config.getGuard().getGuardStyle2Formatted(), startX, startY, "雪尼");
//            startY = drawExampleMsg(config.getGuard().getGuardStyle3Formatted(), startX, startY, "青芙");
//        }
//
//        if (config.getSc().isShow()) {
//            startY = drawExampleMsg(config.getSc().getStyleFormatted(), startX, startY, "秋夕", "这是醒目留言！！！", 943);
//        }
//
//        for (TextFieldWidget textField : textFields.values()) {
//            textField.render(mouseX, mouseY, partialTicks);
//        }
//
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//        super.render(mouseX, mouseY, partialTicks);
//    }
//
//    private int drawExampleMsg(String style, int x, int y, Object... args) {
//        String text = "格式错误！";
//        try {
//            text = String.format(style, args);
//        } catch (UnknownFormatConversionException ignore) {
//        }
//        font.drawString(text, x, y, 0xffffff);
//        y += 10;
//        return y;
//    }
//
//    @Override
//    public void tick() {
//        for (TextFieldWidget textField : textFields.values()) {
//            textField.tick();
//        }
//    }
//
//    @Override
//    public void removed() {
//        super.removed();
//        if (minecraft != null) {
//            minecraft.keyboardListener.enableRepeatEvents(false);
//        }
//    }
//}
