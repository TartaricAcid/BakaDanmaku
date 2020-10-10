package com.github.tartaricacid.bakadanmaku.gui.bilibili;

import com.github.tartaricacid.bakadanmaku.config.BilibiliConfig;
import com.github.tartaricacid.bakadanmaku.gui.ConfigGui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonToggle;
import net.minecraft.client.gui.GuiTextField;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class EnterGui extends ConfigGui {
    private static final String NORMAL = "normal";
    private static final String GUARD1 = "guard1";
    private static final String GUARD2 = "guard2";
    private static final String GUARD3 = "guard3";
    GuiButtonToggle showNormal;
    GuiButtonToggle showGuard;

    public EnterGui(BilibiliConfig config) {
        super(config);
    }

    @Override
    public void initGui() {
        super.initGui();
        showNormal = new GuiButtonToggle(1, configStartX + configWidth - 10 - 16, configStartY + 10,
                16, 16, config.getEnter().isShowNormal());
        showNormal.initTextureValues(16, 0, -16, 16, ICON);

        showGuard = new GuiButtonToggle(2, configStartX + configWidth - 10 - 16, configStartY + 30,
                16, 16, config.getEnter().isShowGuard());
        showGuard.initTextureValues(16, 0, -16, 16, ICON);

        buttonList.add(showNormal);
        buttonList.add(showGuard);
    }

    @Override
    public boolean onKeyTyped(String textFieldName, GuiTextField guiTextField, char typedChar, int keyCode) {
        return true;
    }

    @Override
    public void afterKeyTyped(String textFieldName, GuiTextField guiTextField, char typedChar, int keyCode) {
        if (NORMAL.equals(textFieldName)) {
            config.getEnter().setNormalStyle(guiTextField.getText());
        }
        if (GUARD1.equals(textFieldName)) {
            config.getEnter().setGuardStyle1(guiTextField.getText());
        }
        if (GUARD2.equals(textFieldName)) {
            config.getEnter().setGuardStyle2(guiTextField.getText());
        }
        if (GUARD3.equals(textFieldName)) {
            config.getEnter().setGuardStyle3(guiTextField.getText());
        }
        config.deco();
    }

    @Override
    public void addTextFields(HashMap<String, GuiTextField> textFields) {
        GuiTextField normal = new GuiTextField(0, fontRenderer, configStartX + 10,
                configStartY + 67, configWidth - 20, 15);
        normal.setMaxStringLength(256);
        normal.setText(config.getEnter().getNormalStyle());

        GuiTextField guard1 = new GuiTextField(1, fontRenderer, configStartX + 10,
                configStartY + 98, configWidth - 20, 15);
        guard1.setMaxStringLength(256);
        guard1.setText(config.getEnter().getGuardStyle1());

        GuiTextField guard2 = new GuiTextField(2, fontRenderer, configStartX + 10,
                configStartY + 129, configWidth - 20, 15);
        guard2.setMaxStringLength(256);
        guard2.setText(config.getEnter().getGuardStyle2());

        GuiTextField guard3 = new GuiTextField(3, fontRenderer, configStartX + 10,
                configStartY + 160, configWidth - 20, 15);
        guard3.setMaxStringLength(256);
        guard3.setText(config.getEnter().getGuardStyle3());

        textFields.put(NORMAL, normal);
        textFields.put(GUARD1, guard1);
        textFields.put(GUARD2, guard2);
        textFields.put(GUARD3, guard3);
    }

    @Override
    public void drawConfigScreen(int mouseX, int mouseY, float partialTicks) {
        fontRenderer.drawString("是否显示普通进房提醒", configStartX + 10, configStartY + 15, 0xffffff, false);
        fontRenderer.drawString("是否显示上舰进房提醒", configStartX + 10, configStartY + 35, 0xffffff, false);
        fontRenderer.drawString("普通用户进房提醒样式", configStartX + 10, configStartY + 55, 0xffffff, false);
        fontRenderer.drawString("总督进房提醒样式", configStartX + 10, configStartY + 86, 0xffffff, false);
        fontRenderer.drawString("提督进房提醒样式", configStartX + 10, configStartY + 117, 0xffffff, false);
        fontRenderer.drawString("舰长进房提醒样式", configStartX + 10, configStartY + 149, 0xffffff, false);

        drawGradientRect(2, 21 + 15 * 3, 98, 36 + 15 * 3, 0xdd339966, 0xdd339966);
    }

    @Override
    protected void actionPerformed(@Nonnull GuiButton button) {
        super.actionPerformed(button);
        if (button.id == 1) {
            showNormal.setStateTriggered(!showNormal.isStateTriggered());
            config.getEnter().setShowNormal(showNormal.isStateTriggered());
            return;
        }
        if (button.id == 2) {
            showGuard.setStateTriggered(!showGuard.isStateTriggered());
            config.getEnter().setShowGuard(showGuard.isStateTriggered());
        }
    }
}
