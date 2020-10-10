package com.github.tartaricacid.bakadanmaku.gui.bilibili;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;

import java.util.Arrays;

public class BlockListGui extends GuiScrollingList {
    private String[] block;
    private int select;

    public BlockListGui(Minecraft client, int width, int height, int top, int bottom, int left, int entryHeight, int screenWidth, int screenHeight, String[] block) {
        super(client, width, height, top, bottom, left, entryHeight, screenWidth, screenHeight);
        this.block = block;
    }

    @Override
    protected int getSize() {
        return block.length;
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {
        if (index < block.length) {
            select = index;
        }
    }

    @Override
    protected boolean isSelected(int index) {
        return select == index;
    }

    @Override
    protected void drawBackground() {
    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
        if (slotIdx < block.length) {
            FontRenderer renderer = Minecraft.getMinecraft().fontRenderer;
            renderer.drawString(String.valueOf(slotIdx + 1), 120, slotTop, 0xffffff, false);
            renderer.drawString(block[slotIdx], 135, slotTop, 0xffffff, false);
        }
    }

    public void addBlockString(String text) {
        if (Arrays.asList(block).contains(text)) {
            return;
        }
        String[] after = new String[block.length + 1];
        System.arraycopy(block, 0, after, 0, block.length);
        after[block.length] = text;
        block = after;
    }

    public void removeBlockString(int removeIndex) {
        String[] after = new String[block.length - 1];
        int afterIndex = 0;
        for (int i = 0; i < block.length; i++) {
            if (i != removeIndex) {
                after[afterIndex] = block[i];
                afterIndex++;
            }
        }
        block = after;
    }

    public int getSelect() {
        return select;
    }

    public String[] getBlock() {
        return block;
    }
}
