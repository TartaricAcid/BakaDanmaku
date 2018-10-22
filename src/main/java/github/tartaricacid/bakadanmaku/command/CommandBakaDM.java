package github.tartaricacid.bakadanmaku.command;

import github.tartaricacid.bakadanmaku.api.thread.BaseDanmakuThread;
import github.tartaricacid.bakadanmaku.api.thread.DanmakuThreadFactory;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.IClientCommand;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandBakaDM extends CommandBase implements IClientCommand {
    public static final String commandBakaDanmaku = "bakadm"; // 指令头
    public static final String commandHelpText = "/bakadm [子命令] [参数]\n子命令：start，stop，restart，running"; // 帮助

    @Override
    public String getName() {
        return commandBakaDanmaku;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return commandHelpText;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0; // 让任意玩家可以执行此指令
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        // 参数为空
        if (args.length == 0) {
            BaseDanmakuThread.sendChatMessage(TextFormatting.RED + "用法：" + commandHelpText);
            return;
        }

        // 具体的参数分析
        switch (args[0]) {
            // 重载指令
            case "restart": {
                BaseDanmakuThread.sendChatMessage("§8§l正在重启中……");
                DanmakuThreadFactory.restartThreads();
                break;
            }

            // 开始指令
            case "start": {
                if (args.length == 1) {
                    // 启动配置文件中指定的全部 DanmakuThread
                    if (!DanmakuThreadFactory.getRunningDanmakuThread().isEmpty()) {
                        BaseDanmakuThread.sendChatMessage(TextFormatting.RED + "弹幕姬已处于运行状态！");
                    } else {
                        DanmakuThreadFactory.restartThreads();
                    }
                } else {
                    // 启动指定的 DanmakuThread
                    for (int i = 1; i < args.length; i++) {
                        if (!DanmakuThreadFactory.isDanmakuThreadAvailable(args[i])) {
                            BaseDanmakuThread.sendChatMessage(TextFormatting.RED + "弹幕线程\"" + args[i] + "\"不存在！");
                            continue;
                        }
                        if (DanmakuThreadFactory.isThreadRunning(args[i])) {
                            BaseDanmakuThread.sendChatMessage(TextFormatting.RED + "弹幕线程\"" + args[i] + "\"正在运行！");
                            continue;
                        }

                        BaseDanmakuThread.sendChatMessage("§8§l正在启动弹幕线程\"" + args[i] + "\"……");
                        DanmakuThreadFactory.runThread(args[i]);
                        BaseDanmakuThread.sendChatMessage("§8§l弹幕线程\"" + args[i] + "\"启动成功！");
                    }
                }
                break;
            }

            // 停止指令
            case "stop": {
                if (args.length == 1) {
                    if (DanmakuThreadFactory.getRunningDanmakuThread().isEmpty()) {
                        BaseDanmakuThread.sendChatMessage(TextFormatting.RED + "弹幕姬已停止！");
                    } else {
                        BaseDanmakuThread.sendChatMessage("§8§l正在停止中……");
                        DanmakuThreadFactory.stopAllThreads();
                        BaseDanmakuThread.sendChatMessage("§8§l弹幕姬已停止。");
                    }
                } else {
                    // 停止指定的 DanmakuThread
                    for (int i = 1; i < args.length; i++) {
                        if (!DanmakuThreadFactory.isDanmakuThreadAvailable(args[i])) {
                            BaseDanmakuThread.sendChatMessage(TextFormatting.RED + "弹幕线程\"" + args[i] + "\"不存在！");
                            continue;
                        }
                        if (!DanmakuThreadFactory.isThreadRunning(args[i])) {
                            BaseDanmakuThread.sendChatMessage(TextFormatting.RED + "弹幕线程\"" + args[i] + "\"已停止！");
                            continue;
                        }

                        BaseDanmakuThread.sendChatMessage("§8§l正在停止弹幕线程\"" + args[i] + "\"……");
                        DanmakuThreadFactory.stopThread(args[i]);
                        BaseDanmakuThread.sendChatMessage("§8§l弹幕线程\"" + args[i] + "\"已停止。");
                    }
                }
                break;
            }

            // 列出正在运行的所有弹幕线程
            case "running": {
                BaseDanmakuThread.sendChatMessage("正在运行的弹幕线程：" + String.join("，", DanmakuThreadFactory.getRunningDanmakuThread()));
                break;
            }

            // 帮助指令
            case "help": {
                BaseDanmakuThread.sendChatMessage(TextFormatting.RED + "用法：" + commandHelpText);
                break;
            }

            default:
                BaseDanmakuThread.sendChatMessage(TextFormatting.RED + "用法：" + commandHelpText);
                break;
        }
    }

    /**
     * Tab 补全具体实现
     */
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        // 存入当前可用指令，这个是给遍历用的
        List<String> listIn = new ArrayList<>(Arrays.asList("restart", "start", "stop", "running", "help"));

        // 如果没写参数，返回整个列表
        if (args.length == 0) {
            return listIn;
        }

        // 写入第一个参数，依据输入内容头部检索，返回符合的
        if (args.length == 1) {
            // 因为不能同时遍历 list 时删除 list 元素，故需要创建一个 list 存储输入出的 list
            List<String> listOut = new ArrayList<>();

            // 遍历参数
            for (String str : listIn) {
                // 不是的部分，剔除
                if (str.indexOf(args[0]) == 0) listOut.add(str);
            }

            return listOut;
        }

        return null;
    }

    @Override
    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
        return false;
    }
}
