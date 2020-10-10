package com.github.tartaricacid.bakadanmaku.gui.bilibili;

import com.github.tartaricacid.bakadanmaku.config.BilibiliConfig;
import com.github.tartaricacid.bakadanmaku.gui.ConfigGui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonToggle;
import net.minecraft.client.gui.GuiTextField;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class ScGui extends ConfigGui {
    private static final String STYLE = "style";
    GuiButtonToggle show;

    public ScGui(BilibiliConfig config) {
        super(config);
    }

    @Override
    public void initGui() {
        super.initGui();
        show = new GuiButtonToggle(1, configStartX + configWidth - 10 - 16, configStartY + 10,
                16, 16, config.getSc().isShow());
        show.initTextureValues(16, 0, -16, 16, ICON);
        buttonList.add(show);
    }

    @Override
    public boolean onKeyTyped(String textFieldName, GuiTextField guiTextField, char typedChar, int keyCode) {
        return true;
    }

    @Override
    public void afterKeyTyped(String textFieldName, GuiTextField guiTextField, char typedChar, int keyCode) {
        if (STYLE.equals(textFieldName)) {
            config.getSc().setStyle(guiTextField.getText());
        }
        config.deco();
    }

    @Override
    public void addTextFields(HashMap<String, GuiTextField> textFields) {
        GuiTextField style = new GuiTextField(0, fontRenderer, configStartX + 10,
                configStartY + 47, configWidth - 20, 15);
        style.setMaxStringLength(256);
        style.setText(config.getSc().getStyle());
        textFields.put(STYLE, style);
    }

    @Override
    public void drawConfigScreen(int mouseX, int mouseY, float partialTicks) {
        fontRenderer.drawString("是否显示", configStartX + 10, configStartY + 15, 0xffffff, false);
        fontRenderer.drawString("显示样式", configStartX + 10, configStartY + 35, 0xffffff, false);
        drawGradientRect(2, 21 + 15 * 5, 98, 36 + 15 * 5, 0xdd339966, 0xdd339966);
    }

    @Override
    protected void actionPerformed(@Nonnull GuiButton button) {
        super.actionPerformed(button);
        if (button.id == 1) {
            show.setStateTriggered(!show.isStateTriggered());
            config.getSc().setShow(show.isStateTriggered());
        }
    }
}
