package com.github.tartaricacid.bakadanmaku.gui;

import com.github.tartaricacid.bakadanmaku.BakaDanmaku;
import com.github.tartaricacid.bakadanmaku.config.BilibiliConfig;
import com.github.tartaricacid.bakadanmaku.gui.bilibili.*;
import com.github.tartaricacid.bakadanmaku.utils.OpenCloseDanmaku;
import com.google.common.collect.Maps;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.HashMap;
import java.util.UnknownFormatConversionException;

import static com.github.tartaricacid.bakadanmaku.config.ConfigManger.saveBilibiliConfig;

public abstract class ConfigGui extends GuiScreen {
    protected static final ResourceLocation ICON = new ResourceLocation(BakaDanmaku.MOD_ID, "textures/icon.png");
    private final HashMap<String, GuiTextField> textFields = Maps.newHashMap();
    protected int configWidth;
    protected int configHeight;
    protected int configStartX;
    protected int configStartY;
    protected BilibiliConfig config;

    public ConfigGui(BilibiliConfig config) {
        this.config = config;
    }

    /**
     * 判定是否该文本框是否输入此字符
     *
     * @param textFieldName 输入框名称
     * @param typedChar     输入的字符
     * @return 是否允许输入
     */
    public abstract boolean onKeyTyped(String textFieldName, GuiTextField guiTextField, char typedChar, int keyCode);

    /**
     * 在文本框输入完成之后的举动
     */
    public abstract void afterKeyTyped(String textFieldName, GuiTextField guiTextField, char typedChar, int keyCode);

    /**
     * 添加文本框
     *
     * @param textFields 文本框组
     */
    public abstract void addTextFields(HashMap<String, GuiTextField> textFields);

    /**
     * 绘制配置界面自己的元素
     */
    public abstract void drawConfigScreen(int mouseX, int mouseY, float partialTicks);

    @Override
    public void initGui() {
        this.buttonList.clear();
        this.textFields.clear();

        configHeight = height - 10;
        configWidth = width - 260;
        configStartX = 105;
        configStartY = 5;

        Keyboard.enableRepeatEvents(true);
        this.addTextFields(textFields);
        buttonList.add(new GuiButtonImage(100, 2, 21, 96, 15,
                128, 128, 0, ICON));
        buttonList.add(new GuiButtonImage(101, 2, 21 + 15, 96, 15,
                128, 128, 0, ICON));
        buttonList.add(new GuiButtonImage(102, 2, 21 + 15 * 2, 96, 15,
                128, 128, 0, ICON));
        buttonList.add(new GuiButtonImage(103, 2, 21 + 15 * 3, 96, 15,
                128, 128, 0, ICON));
        buttonList.add(new GuiButtonImage(104, 2, 21 + 15 * 4, 96, 15,
                128, 128, 0, ICON));
        buttonList.add(new GuiButtonImage(105, 2, 21 + 15 * 5, 96, 15,
                128, 128, 0, ICON));


        buttonList.add(new GuiButtonImage(943, 5, height - 50, 90, 20,
                128, 128, 0, ICON));
        buttonList.add(new GuiButtonImage(944, 2, height - 25, 96, 20,
                128, 128, 0, ICON));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        // 左侧按钮列表
        drawGradientRect(0, 0, 100, height, 0xdd000000, 0xdd000000);
        // 标题背景
        drawGradientRect(2, 2, 100 - 2, 20 - 2, 0xdd1f1f1f, 0xdd1f1f1f);

        // drawGradientRect(2, 25 - 4, 98, 25 + 15 - 4, 0xdd339966, 0xdd339966);

        // 右侧弹幕样式显示背景
        drawGradientRect(width - 150, 0, width, height, 0xdd1f1f1f, 0xdd1f1f1f);
        // 中间配置界面
        drawGradientRect(configStartX, configStartY, configStartX + configWidth,
                configStartY + configHeight, 0xaa000000, 0xaa000000);

        // 最下方按钮
        drawGradientRect(5, height - 50, 95, height - 30, 0xff31343f, 0xff31343f);
        drawGradientRect(5, height - 25, 95, height - 5, 0xff31343f, 0xff31343f);

        drawConfigScreen(mouseX, mouseY, partialTicks);

        fontRenderer.drawString("弹幕模组配置界面", 6, 6, 0xffffff);
        fontRenderer.drawString("房间设置", 12, 25, 0xffffff);
        fontRenderer.drawString("弹幕设置", 12, 25 + 15, 0xffffff);
        fontRenderer.drawString("送礼设置", 12, 25 + 15 * 2, 0xffffff);
        fontRenderer.drawString("进房设置", 12, 25 + 15 * 3, 0xffffff);
        fontRenderer.drawString("上舰设置", 12, 25 + 15 * 4, 0xffffff);
        fontRenderer.drawString("醒目留言（SC）", 12, 25 + 15 * 5, 0xffffff);

        String text1 = "保存并重载";
        fontRenderer.drawString(text1, (100 - fontRenderer.getStringWidth(text1)) / 2, height - 44, 0xffffff);
        String text2 = "直接退出";
        fontRenderer.drawString(text2, (100 - fontRenderer.getStringWidth(text2)) / 2, height - 19, 0xffffff);

        int startY = 10;
        int startX = width - 150 + 8;
        if (config.getDanmaku().isShow()) {
            startY = drawExampleMsg(config.getDanmaku().getNormalStyleFormatted(), startX, startY, "酒石酸菌", "我是普通观众");
            startY = drawExampleMsg(config.getDanmaku().getAdminStyleFormatted(), startX, startY, "琥珀酸", "我是房管");
            startY = drawExampleMsg(config.getDanmaku().getGuardStyleFormatted(), startX, startY, "帕金伊", "我上舰了");
        }
        if (config.getGift().isShow()) {
            startY = drawExampleMsg(config.getGift().getStyleFormatted(), startX, startY,
                    "天幂", "投喂了", "风铃", 943);
        }
        if (config.getEnter().isShowNormal()) {
            startY = drawExampleMsg(config.getEnter().getNormalStyleFormatted(), startX, startY, "梨木利亚");
        }
        if (config.getEnter().isShowGuard()) {
            startY = drawExampleMsg(config.getEnter().getGuardStyle1Formatted(), startX, startY, "嚼铝赤城桑");
            startY = drawExampleMsg(config.getEnter().getGuardStyle2Formatted(), startX, startY, "花玥");
            startY = drawExampleMsg(config.getEnter().getGuardStyle3Formatted(), startX, startY, "天顶乌");
        }

        if (config.getGuard().isShow()) {
            startY = drawExampleMsg(config.getGuard().getGuardStyle1Formatted(), startX, startY, "迺逸夫");
            startY = drawExampleMsg(config.getGuard().getGuardStyle2Formatted(), startX, startY, "雪尼");
            startY = drawExampleMsg(config.getGuard().getGuardStyle3Formatted(), startX, startY, "青芙");
        }

        if (config.getSc().isShow()) {
            startY = drawExampleMsg(config.getSc().getStyleFormatted(), startX, startY, "秋夕", "这是醒目留言！！！", 943);
        }

        for (GuiTextField textField : textFields.values()) {
            textField.drawTextBox();
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private int drawExampleMsg(String style, int x, int y, Object... args) {
        String text = "格式错误！";
        try {
            text = String.format(style, args);
        } catch (UnknownFormatConversionException ignore) {
        }
        fontRenderer.drawString(text, x, y, 0xffffff);
        y += 10;
        return y;
    }

    @Override
    public void updateScreen() {
        for (GuiTextField textField : textFields.values()) {
            textField.updateCursorCounter();
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        for (String name : textFields.keySet()) {
            GuiTextField guiTextField = textFields.get(name);
            if (onKeyTyped(name, guiTextField, typedChar, keyCode)) {
                guiTextField.textboxKeyTyped(typedChar, keyCode);
                afterKeyTyped(name, guiTextField, typedChar, keyCode);
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (GuiTextField textField : textFields.values()) {
            textField.mouseClicked(mouseX, mouseY, mouseButton);
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(@Nonnull GuiButton button) {
        if (button.id == 100) {
            mc.addScheduledTask(() -> mc.displayGuiScreen(new RoomGui(config)));
            return;
        }
        if (button.id == 101) {
            mc.addScheduledTask(() -> mc.displayGuiScreen(new DanmakuGui(config)));
            return;
        }
        if (button.id == 102) {
            mc.addScheduledTask(() -> mc.displayGuiScreen(new GiftGui(config)));
            return;
        }
        if (button.id == 103) {
            mc.addScheduledTask(() -> mc.displayGuiScreen(new EnterGui(config)));
            return;
        }
        if (button.id == 104) {
            mc.addScheduledTask(() -> mc.displayGuiScreen(new GuardGui(config)));
            return;
        }
        if (button.id == 105) {
            mc.addScheduledTask(() -> mc.displayGuiScreen(new ScGui(config)));
            return;
        }

        if (button.id == 943) {
            mc.addScheduledTask(() -> mc.displayGuiScreen(null));
            saveBilibiliConfig(config);
            OpenCloseDanmaku.closeDanmaku();
            OpenCloseDanmaku.openDanmaku();
            return;
        }

        if (button.id == 944) {
            mc.addScheduledTask(() -> mc.displayGuiScreen(null));
        }
    }
}
