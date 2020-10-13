//package com.github.tartaricacid.bakadanmaku.gui.bilibili;
//
//import com.github.tartaricacid.bakadanmaku.config.BilibiliConfig;
//import com.github.tartaricacid.bakadanmaku.gui.ConfigGui;
//import net.minecraft.client.gui.GuiButton;
//import net.minecraft.client.gui.GuiButtonToggle;
//import net.minecraft.client.gui.GuiTextField;
//
//import javax.annotation.Nonnull;
//import java.util.HashMap;
//
//public class GuardGui extends ConfigGui {
//    private static final String GUARD1 = "guard1";
//    private static final String GUARD2 = "guard2";
//    private static final String GUARD3 = "guard3";
//    GuiButtonToggle show;
//
//    public GuardGui(BilibiliConfig config) {
//        super(config);
//    }
//
//    @Override
//    public void initGui() {
//        super.initGui();
//        show = new GuiButtonToggle(1, configStartX + configWidth - 10 - 16, configStartY + 10,
//                16, 16, config.getGuard().isShow());
//        show.initTextureValues(16, 0, -16, 16, ICON);
//
//        buttonList.add(show);
//    }
//
//    @Override
//    public boolean onKeyTyped(String textFieldName, GuiTextField guiTextField, char typedChar, int keyCode) {
//        return true;
//    }
//
//    @Override
//    public void afterKeyTyped(String textFieldName, GuiTextField guiTextField, char typedChar, int keyCode) {
//        if (GUARD1.equals(textFieldName)) {
//            config.getGuard().setGuardStyle1(guiTextField.getText());
//        }
//        if (GUARD2.equals(textFieldName)) {
//            config.getGuard().setGuardStyle2(guiTextField.getText());
//        }
//        if (GUARD3.equals(textFieldName)) {
//            config.getGuard().setGuardStyle3(guiTextField.getText());
//        }
//        config.deco();
//    }
//
//    @Override
//    public void addTextFields(HashMap<String, GuiTextField> textFields) {
//        GuiTextField guard1 = new GuiTextField(1, fontRenderer, configStartX + 10,
//                configStartY + 48, configWidth - 20, 15);
//        guard1.setMaxStringLength(256);
//        guard1.setText(config.getGuard().getGuardStyle1());
//
//        GuiTextField guard2 = new GuiTextField(2, fontRenderer, configStartX + 10,
//                configStartY + 79, configWidth - 20, 15);
//        guard2.setMaxStringLength(256);
//        guard2.setText(config.getGuard().getGuardStyle2());
//
//        GuiTextField guard3 = new GuiTextField(3, fontRenderer, configStartX + 10,
//                configStartY + 110, configWidth - 20, 15);
//        guard3.setMaxStringLength(256);
//        guard3.setText(config.getGuard().getGuardStyle3());
//
//        textFields.put(GUARD1, guard1);
//        textFields.put(GUARD2, guard2);
//        textFields.put(GUARD3, guard3);
//    }
//
//    @Override
//    public void drawConfigScreen(int mouseX, int mouseY, float partialTicks) {
//        fontRenderer.drawString("是否显示上舰提醒", configStartX + 10, configStartY + 15, 0xffffff, false);
//        fontRenderer.drawString("开通总督的提醒样式", configStartX + 10, configStartY + 36, 0xffffff, false);
//        fontRenderer.drawString("开通提督的提醒样式", configStartX + 10, configStartY + 67, 0xffffff, false);
//        fontRenderer.drawString("开通舰长的提醒样式", configStartX + 10, configStartY + 99, 0xffffff, false);
//
//        drawGradientRect(2, 21 + 15 * 4, 98, 36 + 15 * 4, 0xdd339966, 0xdd339966);
//    }
//
//    @Override
//    protected void actionPerformed(@Nonnull GuiButton button) {
//        super.actionPerformed(button);
//        if (button.id == 1) {
//            show.setStateTriggered(!show.isStateTriggered());
//            config.getGuard().setShow(show.isStateTriggered());
//        }
//    }
//}
