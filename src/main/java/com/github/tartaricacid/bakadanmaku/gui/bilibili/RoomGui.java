//package com.github.tartaricacid.bakadanmaku.gui.bilibili;
//
//import com.github.tartaricacid.bakadanmaku.config.BilibiliConfig;
//import com.github.tartaricacid.bakadanmaku.gui.ConfigGui;
//import net.minecraft.client.gui.widget.TextFieldWidget;
//import net.minecraft.client.gui.widget.ToggleWidget;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang3.math.NumberUtils;
//
//import java.util.HashMap;
//
//public class RoomGui extends ConfigGui {
//    private static final String ROOM_ID = "roomId";
//    ToggleWidget enable;
//
//    public RoomGui(BilibiliConfig config) {
//        super(config);
//    }
//
//    @Override
//    public void init() {
//        super.init();
//        enable = new ToggleWidget(configStartX + configWidth - 10 - 16, configStartY + 61,
//                16, 16, config.getRoom().isEnable());
//        enable.initTextureValues(16, 0, -16, 16, ICON);
//    }
//
//    @Override
//    public void addTextFields(HashMap<String, TextFieldWidget> textFields) {
//        TextFieldWidget roomId = new TextFieldWidget(font, configStartX + 10,
//                configStartY + 30, configWidth - 20, 15, "");
//        roomId.setMaxStringLength(10);
//        roomId.setText(String.valueOf(config.getRoom().getId()));
//        roomId.setValidator((s) -> NumberUtils.isDigits(s) || StringUtils.isEmpty(s));
//        roomId.setResponder((s -> {
//            try {
//                config.getRoom().setId(Integer.parseInt(s));
//            } catch (NumberFormatException ignore) {
//            }
//        }));
//        textFields.put(ROOM_ID, roomId);
//    }
//
//    @Override
//    public void drawConfigScreen(int mouseX, int mouseY, float partialTicks) {
//        font.drawString("直播房间号", configStartX + 10, configStartY + 15, 0xffffff);
//        font.drawString("启用弹幕机", configStartX + 10, configStartY + 65, 0xffffff);
//        fillGradient(2, 25 - 4, 98, 25 + 15 - 4, 0xdd339966, 0xdd339966);
//        enable.render(mouseX, mouseY, partialTicks);
//    }
//
//    @Override
//    public boolean mouseClicked(double mouseX, double mouseY, int mouse) {
//        if (enable.mouseClicked(mouseX, mouseY, mouse)) {
//            enable.setStateTriggered(!enable.isStateTriggered());
//            config.getRoom().setEnable(enable.isStateTriggered());
//            return true;
//        }
//        return super.mouseClicked(mouseX, mouseY, mouse);
//    }
//}
