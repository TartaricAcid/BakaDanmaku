//package com.github.tartaricacid.bakadanmaku.gui.bilibili;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.FontRenderer;
//import net.minecraft.client.renderer.Tessellator;
//import net.minecraft.util.text.ITextComponent;
//import net.minecraftforge.client.gui.ScrollPanel;
//import net.minecraftforge.fml.client.gui.screen.ModListScreen;
//
//import java.util.Arrays;
//
//public class BlockListGui extends ScrollPanel {
//    private String[] block;
//    private int select;
//    private FontRenderer font;
//
//    public BlockListGui(Minecraft client, int width, int height, int top, int left, String[] block) {
//        super(client, width, height, top, left);
//        this.block = block;
//        this.font = Minecraft.getInstance().fontRenderer;
//    }
//
//    @Override
//    public int getContentHeight() {
//        return Math.max(height, block.length * font.FONT_HEIGHT);
//    }
//
//    @Override
//    protected int getScrollAmount() {
//        return font.FONT_HEIGHT * 3;
//    }
//
//    @Override
//    public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
//        final ITextComponent component = findTextLine((int) mouseX, (int) mouseY);
//        if (component != null) {
//            ModListScreen.this.handleComponentClicked(component);
//            return true;
//        }
//        return super.mouseClicked(mouseX, mouseY, button);
//    }
//
//    @Override
//    protected void drawPanel(int entryRight, int relativeY, Tessellator tess, int mouseX, int mouseY) {
//        if (slotIdx < block.length) {
//            FontRenderer renderer = Minecraft.getMinecraft().fontRenderer;
//            renderer.drawString(String.valueOf(slotIdx + 1), 120, slotTop, 0xffffff, false);
//            renderer.drawString(block[slotIdx], 135, slotTop, 0xffffff, false);
//        }
//    }
//
//    @Override
//    protected void drawBackground() {
//    }
//
//    public void addBlockString(String text) {
//        if (Arrays.asList(block).contains(text)) {
//            return;
//        }
//        String[] after = new String[block.length + 1];
//        System.arraycopy(block, 0, after, 0, block.length);
//        after[block.length] = text;
//        block = after;
//    }
//
//    public void removeBlockString(int removeIndex) {
//        String[] after = new String[block.length - 1];
//        int afterIndex = 0;
//        for (int i = 0; i < block.length; i++) {
//            if (i != removeIndex) {
//                after[afterIndex] = block[i];
//                afterIndex++;
//            }
//        }
//        block = after;
//    }
//
//    public int getSelect() {
//        return select;
//    }
//
//    public String[] getBlock() {
//        return block;
//    }
//}
