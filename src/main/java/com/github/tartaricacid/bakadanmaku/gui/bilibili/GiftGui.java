package com.github.tartaricacid.bakadanmaku.gui.bilibili;

import com.github.tartaricacid.bakadanmaku.config.BilibiliConfig;
import com.github.tartaricacid.bakadanmaku.gui.ConfigGui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.GuiButtonToggle;
import net.minecraft.client.gui.GuiTextField;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Mouse;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.HashMap;

public class GiftGui extends ConfigGui {
    private static final String STYLE = "style";
    private static final String BLOCK = "block";
    GuiButtonToggle show;
    BlockListGui blockList;
    GuiTextField block;

    public GiftGui(BilibiliConfig config) {
        super(config);
    }

    @Override
    public void initGui() {
        super.initGui();
        show = new GuiButtonToggle(1, configStartX + configWidth - 10 - 16, configStartY + 10,
                16, 16, config.getGift().isShow());
        show.initTextureValues(16, 0, -16, 16, ICON);
        buttonList.add(show);
        buttonList.add(new GuiButtonImage(2, configStartX + 95, 116, configWidth - 105,
                20, 128, 128, 0, ICON));
        buttonList.add(new GuiButtonImage(3, configStartX + 95, 140, configWidth - 105,
                20, 128, 128, 0, ICON));
        blockList = new BlockListGui(mc, 80, 0, 95, configStartY + configHeight - 10,
                configStartX + 9, 12, width, height, config.getGift().getBlockGift());
    }

    @Override
    public boolean onKeyTyped(String textFieldName, GuiTextField guiTextField, char typedChar, int keyCode) {
        return true;
    }

    @Override
    public void afterKeyTyped(String textFieldName, GuiTextField guiTextField, char typedChar, int keyCode) {
        if (STYLE.equals(textFieldName)) {
            config.getGift().setStyle(guiTextField.getText());
        }
        config.deco();
    }

    @Override
    public void addTextFields(HashMap<String, GuiTextField> textFields) {
        GuiTextField style = new GuiTextField(0, fontRenderer, configStartX + 10,
                configStartY + 47, configWidth - 20, 15);
        style.setMaxStringLength(256);
        style.setText(config.getGift().getStyle());

        block = new GuiTextField(3, fontRenderer, configStartX + 95,
                configStartY + 91, configWidth - 20 - 85, 15);

        textFields.put(STYLE, style);
        textFields.put(BLOCK, block);
    }

    @Override
    public void drawConfigScreen(int mouseX, int mouseY, float partialTicks) {
        fontRenderer.drawString("是否显示礼物", configStartX + 10, configStartY + 15, 0xffffff, false);
        fontRenderer.drawString("礼物显示样式", configStartX + 10, configStartY + 35, 0xffffff, false);
        drawGradientRect(2, 21 + 15 * 2, 98, 36 + 15 * 2, 0xdd339966, 0xdd339966);
        fontRenderer.drawString("屏蔽礼物", configStartX + 10, configStartY + 79, 0xffffff, false);
        drawGradientRect(configStartX + 95, 116, configStartX + configWidth - 10, 136,
                0xff31343f, 0xff31343f);
        drawGradientRect(configStartX + 95, 140, configStartX + configWidth - 10, 160,
                0xff31343f, 0xff31343f);
        int middle = configStartX + 95 + (configWidth - 105) / 2 - fontRenderer.getStringWidth("两字") / 2;
        fontRenderer.drawString("添加", middle, 122, 0xffffff, false);
        fontRenderer.drawString("删除", middle, 146, 0xffffff, false);
        blockList.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(@Nonnull GuiButton button) {
        super.actionPerformed(button);
        if (button.id == 1) {
            show.setStateTriggered(!show.isStateTriggered());
            config.getGift().setShow(show.isStateTriggered());
            return;
        }
        if (button.id == 2) {
            String text = block.getText();
            if (StringUtils.isNotBlank(text)) {
                blockList.addBlockString(text);
                block.setText("");
                config.getGift().setBlockGift(blockList.getBlock());
            }
            return;
        }
        if (button.id == 3) {
            int removeIndex = blockList.getSelect();
            if (removeIndex < blockList.getSize()) {
                blockList.removeBlockString(removeIndex);
                config.getGift().setBlockGift(blockList.getBlock());
            }
            return;
        }
        blockList.actionPerformed(button);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        blockList.handleMouseInput(mouseX, mouseY);
    }
}
