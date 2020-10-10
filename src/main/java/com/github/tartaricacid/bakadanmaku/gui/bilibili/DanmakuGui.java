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

public class DanmakuGui extends ConfigGui {
    private static final String NORMAL = "normal";
    private static final String GUARD = "guard";
    private static final String ADMIN = "admin";
    private static final String BLOCK = "block";
    GuiButtonToggle show;
    BlockListGui blockList;
    GuiTextField block;

    public DanmakuGui(BilibiliConfig config) {
        super(config);
    }

    @Override
    public void initGui() {
        super.initGui();
        show = new GuiButtonToggle(1, configStartX + configWidth - 10 - 16, configStartY + 10,
                16, 16, config.getDanmaku().isShow());
        show.initTextureValues(16, 0, -16, 16, ICON);
        buttonList.add(show);
        buttonList.add(new GuiButtonImage(2, configStartX + 95, 166, configWidth - 105,
                20, 128, 128, 0, ICON));
        buttonList.add(new GuiButtonImage(3, configStartX + 95, 190, configWidth - 105,
                20, 128, 128, 0, ICON));
        blockList = new BlockListGui(mc, 80, 0, 145, configStartY + configHeight - 10,
                configStartX + 9, 12, width, height, config.getDanmaku().getBlockWord());
    }

    @Override
    public boolean onKeyTyped(String textFieldName, GuiTextField guiTextField, char typedChar, int keyCode) {
        return true;
    }

    @Override
    public void afterKeyTyped(String textFieldName, GuiTextField guiTextField, char typedChar, int keyCode) {
        if (NORMAL.equals(textFieldName)) {
            config.getDanmaku().setNormalStyle(guiTextField.getText());
        }
        if (GUARD.equals(textFieldName)) {
            config.getDanmaku().setGuardStyle(guiTextField.getText());
        }
        if (ADMIN.equals(textFieldName)) {
            config.getDanmaku().setAdminStyle(guiTextField.getText());
        }
        config.deco();
    }

    @Override
    public void addTextFields(HashMap<String, GuiTextField> textFields) {
        GuiTextField normal = new GuiTextField(0, fontRenderer, configStartX + 10,
                configStartY + 47, configWidth - 20, 15);
        normal.setMaxStringLength(256);
        normal.setText(config.getDanmaku().getNormalStyle());

        GuiTextField guard = new GuiTextField(1, fontRenderer, configStartX + 10,
                configStartY + 78, configWidth - 20, 15);
        guard.setMaxStringLength(256);
        guard.setText(config.getDanmaku().getGuardStyle());

        GuiTextField admin = new GuiTextField(2, fontRenderer, configStartX + 10,
                configStartY + 109, configWidth - 20, 15);
        admin.setMaxStringLength(256);
        admin.setText(config.getDanmaku().getAdminStyle());

        block = new GuiTextField(3, fontRenderer, configStartX + 95,
                configStartY + 141, configWidth - 20 - 85, 15);

        textFields.put(NORMAL, normal);
        textFields.put(GUARD, guard);
        textFields.put(ADMIN, admin);
        textFields.put(BLOCK, block);
    }

    @Override
    public void drawConfigScreen(int mouseX, int mouseY, float partialTicks) {
        fontRenderer.drawString("是否显示弹幕", configStartX + 10, configStartY + 15, 0xffffff, false);
        fontRenderer.drawString("普通用户弹幕样式", configStartX + 10, configStartY + 35, 0xffffff, false);
        fontRenderer.drawString("舰长弹幕样式", configStartX + 10, configStartY + 66, 0xffffff, false);
        fontRenderer.drawString("房管弹幕样式", configStartX + 10, configStartY + 97, 0xffffff, false);
        fontRenderer.drawString("弹幕屏蔽词", configStartX + 10, configStartY + 129, 0xffffff, false);

        drawGradientRect(2, 21 + 15, 98, 36 + 15, 0xdd339966, 0xdd339966);
        drawGradientRect(configStartX + 95, 166, configStartX + configWidth - 10, 186,
                0xff31343f, 0xff31343f);
        drawGradientRect(configStartX + 95, 190, configStartX + configWidth - 10, 210,
                0xff31343f, 0xff31343f);
        int middle = configStartX + 95 + (configWidth - 105) / 2 - fontRenderer.getStringWidth("两字") / 2;
        fontRenderer.drawString("添加", middle, 172, 0xffffff, false);
        fontRenderer.drawString("删除", middle, 196, 0xffffff, false);
        blockList.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(@Nonnull GuiButton button) {
        super.actionPerformed(button);
        if (button.id == 1) {
            show.setStateTriggered(!show.isStateTriggered());
            config.getDanmaku().setShow(show.isStateTriggered());
            return;
        }
        if (button.id == 2) {
            String text = block.getText();
            if (StringUtils.isNotBlank(text)) {
                blockList.addBlockString(text);
                block.setText("");
                config.getDanmaku().setBlockWord(blockList.getBlock());
            }
            return;
        }
        if (button.id == 3) {
            int removeIndex = blockList.getSelect();
            if (removeIndex < blockList.getSize()) {
                blockList.removeBlockString(removeIndex);
                config.getDanmaku().setBlockWord(blockList.getBlock());
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
