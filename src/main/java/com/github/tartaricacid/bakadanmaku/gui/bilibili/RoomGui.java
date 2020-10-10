package com.github.tartaricacid.bakadanmaku.gui.bilibili;

import com.github.tartaricacid.bakadanmaku.config.BilibiliConfig;
import com.github.tartaricacid.bakadanmaku.gui.ConfigGui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonToggle;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class RoomGui extends ConfigGui {
    private static final String ROOM_ID = "roomId";
    GuiButtonToggle enable;

    public RoomGui(BilibiliConfig config) {
        super(config);
    }

    @Override
    public void initGui() {
        super.initGui();
        enable = new GuiButtonToggle(1, configStartX + configWidth - 10 - 16, configStartY + 61,
                16, 16, config.getRoom().isEnable());
        enable.initTextureValues(16, 0, -16, 16, ICON);
        buttonList.add(enable);
    }

    @Override
    public boolean onKeyTyped(String textFieldName, GuiTextField guiTextField, char typedChar, int keyCode) {
        if (ROOM_ID.equals(textFieldName)) {
            return Character.isDigit(typedChar) || keyCode == Keyboard.KEY_BACK || keyCode == Keyboard.KEY_DELETE
                    || keyCode == Keyboard.KEY_LEFT || keyCode == Keyboard.KEY_RIGHT
                    || GuiScreen.isKeyComboCtrlA(keyCode) || GuiScreen.isKeyComboCtrlC(keyCode)
                    || GuiScreen.isKeyComboCtrlV(keyCode) || GuiScreen.isKeyComboCtrlX(keyCode);
        }
        return true;
    }

    @Override
    public void afterKeyTyped(String textFieldName, GuiTextField guiTextField, char typedChar, int keyCode) {
        try {
            int room = Integer.parseInt(guiTextField.getText());
            config.getRoom().setId(room);
        } catch (NumberFormatException ignore) {
        }
    }

    @Override
    public void addTextFields(HashMap<String, GuiTextField> textFields) {
        GuiTextField roomId = new GuiTextField(1, fontRenderer, configStartX + 10,
                configStartY + 30, configWidth - 20, 15);
        roomId.setMaxStringLength(10);
        roomId.setText(String.valueOf(config.getRoom().getId()));
        textFields.put(ROOM_ID, roomId);
    }

    @Override
    public void drawConfigScreen(int mouseX, int mouseY, float partialTicks) {
        fontRenderer.drawString("直播房间号", configStartX + 10, configStartY + 15, 0xffffff, false);
        fontRenderer.drawString("启用弹幕机", configStartX + 10, configStartY + 65, 0xffffff, false);
        drawGradientRect(2, 25 - 4, 98, 25 + 15 - 4, 0xdd339966, 0xdd339966);
    }

    @Override
    protected void actionPerformed(@Nonnull GuiButton button) {
        super.actionPerformed(button);
        if (button.id == 1) {
            enable.setStateTriggered(!enable.isStateTriggered());
            config.getRoom().setEnable(enable.isStateTriggered());
        }
    }
}
