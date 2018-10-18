package github.tartaricacid.bakadanmaku.thread;

import com.google.common.io.ByteStreams;
import github.tartaricacid.bakadanmaku.BakaDanmaku;
import github.tartaricacid.bakadanmaku.api.event.DanmakuEvent;
import github.tartaricacid.bakadanmaku.api.event.GiftEvent;
import github.tartaricacid.bakadanmaku.api.event.PopularityEvent;
import github.tartaricacid.bakadanmaku.api.event.WelcomeEvent;
import github.tartaricacid.bakadanmaku.api.thread.BaseDanmakuThread;
import github.tartaricacid.bakadanmaku.config.BakaDanmakuConfig;
import net.minecraftforge.common.MinecraftForge;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DouyuDanmakuThread extends BaseDanmakuThread {
    private static final String LIVE_URL = "openbarrage.douyutv.com"; // 斗鱼弹幕地址
    private static final int PORT = 8601; // Websocket 端口

    private static Pattern readHotValue = Pattern.compile("\"online\":(\\d+),"); // 读取弹幕发送者

    private static Pattern readDanmakuUser = Pattern.compile("nn@=(.*?)/"); // 读取弹幕发送者
    private static Pattern readDanmakuInfo = Pattern.compile("txt@=(.*?)/"); // 读取弹幕文本

    private static Pattern readGiftUser = Pattern.compile("nn@=(.*?)/"); // 读取礼物发送者
    private static Pattern readGiftName = Pattern.compile("gfid@=(.*?)/"); // 读取礼物名称
    private static Pattern readGiftNum = Pattern.compile("gfcnt@=(.*?)/"); // 读取礼物发送者

    private static Pattern readWelcomeUser = Pattern.compile("nn@=(.*?)/"); // 读取欢迎的用户名

    private DataOutputStream dataOutputStream; // 等会读取数据流的

    // 斗鱼的礼物都是数字，需要这个映射将其改为对应名字
    private static HashMap<String, String> giftMap = new HashMap<String, String>() {{
        put("192", "赞");
        put("193", "弱鸡");
        put("194", "666");
        put("195", "飞机");
        put("196", "火箭");
        put("374", "风暴要火");
        put("375", "中国队强无敌");
        put("519", "呵呵");
        put("520", "稳");
        put("545", "觀心覺悟火箭");
        put("600", "手游666");
        put("607", "清香白莲火箭");
        put("622", "熊大炫酷飞机");
        put("640", "超管火箭");
        put("659", "GG");
        put("660", "H1Z1");
        put("672", "鱼购飞天神器");
        put("673", "鱼购冲天神器");
        put("674", "鱼购外星神器");
        put("689", "御甲飞机");
        put("690", "王牌火箭");
        put("704", "黄金马车");
        put("712", "棒棒哒");
        put("713", "辣眼睛");
        put("714", "怂");
        put("750", "办卡");
        put("804", "铜牌");
        put("805", "银牌");
        put("806", "金牌");
        put("824", "粉丝荧光棒");
        put("947", "狼抓手");
        put("1005", "超级火箭");
        put("1075", "这都不叫事");
        put("1093", "前进100");
        put("1094", "前进200");
        put("1095", "前进300");
        put("1096", "前进500");
        put("1114", "呆萌喵");
        put("1116", "般若波若蜜");
        put("1126", "恭贺新年");
        put("1130", "五毛钱");
        put("1131", "一发");
        put("1132", "三周年快乐");
        put("1138", "飞龙在天");
        put("1140", "超级大鹅");
        put("1142", "鱼小弟");
        put("1144", "闪电超级火箭");
        put("1149", "基本操作");
        put("1157", "冲动是魔鬼");
        put("1166", "落地成盒");
        put("1169", "樱桃8");
        put("1176", "冰河世纪");
        put("1191", "盛典星光");
        put("1192", "仙鱼散花");
        put("1193", "飞鱼破浪");
        put("1194", "鱼跃龙门");
        put("1195", "鱼翔九天");
        put("1197", "温柔宝宝");
        put("1208", "安娜爱总裁 ");
        put("1210", "觀心覺悟");
        put("1211", "格子火箭");
        put("1214", "哈士奇");
        put("1225", "一顿操作");
        put("1228", "金色明灯");
        put("1229", "银色明灯");
        put("1230", "幺幺火箭");
        put("1231", "抬一手");
        put("1232", "打Call");
        put("1234", "为爱击掌");
        put("1235", "双十一火箭");
        put("1236", "967up");
        put("1237", "赞");
        put("1238", "背锅");
        put("1239", "红蓝buff");
        put("1240", "槟榔火箭");
        put("1244", "超级幸运");
        put("1246", "秋");
        put("1247", "魔幻之箭");
        put("1248", "1.8亿老婆");
        put("1250", "超级热游蒸汽");
        put("1254", "丘比特之箭");
        put("1258", "超级大头豆糕");
        put("1272", "抽奖辩非欧");
        put("1273", "骚骚骚骚");
        put("1287", "剑舞红颜笑");
        put("1288", "星光财团");
        put("1289", "阿姆斯特朗");
        put("1290", "菁伦超级火箭");
        put("1291", "枸杞槟榔");
        put("1292", "莹yin69");
        put("1293", "一支穿云剑");
        put("1294", "老干爹辣酱");
        put("1301", "泰兰德的果子");
        put("1302", "伊利丹的刀");
        put("1304", "十八专用超火");
        put("1327", "平安果");
        put("1328", "十三少");
        put("1329", "我养你");
        put("1330", "思念星");
        put("1331", "瑞雪丰年");
        put("1332", "鱼福降临");
        put("1344", "一口毒奶");
        put("1345", "一杯毒奶");
        put("1346", "一瓶毒奶");
        put("1347", "一箱毒奶");
        put("1350", "盛典巅峰火箭");
        put("1351", "大胸超级火箭");
        put("1352", "平胸超级火箭");
        put("1353", "爱你一万年");
        put("1355", "说谎");
        put("1362", "道皇火箭");
        put("1363", "豹豹豹");
        put("1364", "大融婷儿");
        put("1365", "绿鞋找妈妈");
        put("1367", "一口毒奶");
        put("1369", "灌篮高手");
        put("1370", "兽兽号超火");
        put("1371", "幸运火箭");
        put("1373", "飞机");
        put("1374", "火箭");
        put("1375", "超级火箭");
        put("1380", "微笑");
        put("1382", "KSG");
        put("1388", "火箭");
        put("1392", "弥大远洋号");
        put("1394", "伴风眠");
        put("1395", "IRONM");
        put("1397", "小温心");
        put("1398", "告白气球");
        put("1421", "烟花易冷");
        put("1424", "南豆豆");
        put("1425", "裤裆里家族");
        put("1426", "办卡");
        put("1427", "赞");
        put("1428", "弱鸡");
        put("1429", "药丸");
        put("1430", "药丸");
        put("1431", "有排面");
        put("1432", "没排面");
        put("1433", "医疗箱");
        put("1434", "吃鸡");
        put("1435", "皇冠");
        put("1437", "玫瑰花");
        put("1438", "小香蕉");
        put("1439", "猫耳");
        put("1440", "女仆装");
        put("1444", "散出一片天");
        put("1454", "飞鸡送福");
        put("1455", "福瑞火箭");
        put("1456", "阳春贺岁超火");
        put("1457", "香瓣");
        put("1458", "旺旺旺");
        put("1459", "二珂的旅行");
        put("1461", "莫莫哒");
        put("1464", "一起看日出");
        put("1465", "魔鬼中的天使");
        put("1473", "星光点点");
        put("1477", "办卡");
        put("1479", "素右右补给包");
        put("1484", "能量饮料");
        put("1485", "肾上腺素");
        put("1486", "平底锅");
        put("1489", "办卡");
        put("1507", "至尊水果导弹");
        put("1509", "神龙见首");
        put("1510", "争锋火箭");
        put("1511", "狂欢集结超火");
        put("1519", "应援飞机");
        put("1530", "寂寞灵魂");
        put("1531", "女朋友");
        put("1534", "LPL加油");
        put("1537", "心里只有你");
        put("1541", "一根星光棒");
        put("1542", "一束星光棒");
        put("1543", "一捆星光棒");
        put("1544", "一堆星光棒");
        put("1545", "礼物buff");
        put("1546", "鱼丸buff");
        put("1553", "筑梦之鲲");
        put("1554", "星空火箭");
        put("1562", "福特翼搏");
        put("1572", "EJ小脑斧");
        put("1573", "688专属");
        put("1574", "银子么么哒");
        put("1575", "天南地北");
        put("1576", "冰心葑");
        put("1577", "月光飞船");
        put("1578", "天使纪元");
        put("1581", "28号技师");
        put("1582", "小怪兽");
        put("1585", "小西瓜");
        put("1593", "小温心");
        put("1596", "安慕希真好喝");
        put("1598", "鸡皇驾到");
        put("1599", "小心大融");
        put("1600", "呆妹儿大大大");
        put("1602", "明日之星");
        put("1603", "对A皇后号");
        put("1604", "恋爱ING");
        put("1605", "心悦猫咪");
        put("1615", "挚爱告白");
        put("1616", "甜蜜情书");
        put("1617", "浪漫密语");
        put("1620", "天圆地方");
        put("1634", "一期一会");
        put("1635", "梦幻之羽");
        put("1636", "爱是一道光");
        put("1637", "金宝专属火箭");
        put("1655", "我不好色");
        put("1661", "Aslank");
        put("1666", "京东优惠券");
        put("1679", "c字超火");
        put("1685", "银河特快");
        put("1686", "定制版儿童节");
        put("1699", "一周年快乐");
        put("1700", "金榜题名");
        put("1701", "第二杯半价");
        put("1702", "枫哥很喜欢");
        put("1715", "凌仕净油飞机");
        put("1716", "抽奖辩非欧");
        put("1722", "07专属火箭");
        put("1737", "旗开得胜");
        put("1738", "旗开得胜");
        put("1743", "冷小兔一周年");
        put("1744", "俗");
        put("1748", "今生护你周全");
        put("1749", "梦回TI2");
        put("1750", "皇家同花顺");
        put("1751", "幻想乡快旅");
        put("1752", "草莓vv呀");
        put("1753", "骚白喵喵喵");
        put("1754", "国民姑姑");
        put("1755", "意大利炮");
        put("1756", "六元带你上天");
        put("1757", "办卡");
        put("1761", "生日烟花");
        put("1773", "小浛最漂亮");
        put("1791", "Rola专属");
        put("1792", "鲸鱼欧尼");
        put("1795", "一起哈啤");
        put("1796", "观花赏月");
        put("1797", "我们生日快乐");
        put("1798", "Rola加油");
        put("1800", "69式饺子");
        put("1801", "突击纵队");
        put("1802", "装甲兵团");
        put("1803", "轰炸机群");
        put("1804", "帝国舰队");
        put("1805", "可爱羡羡火箭");
        put("1806", "粉丝荧光棒");
        put("1813", "石西叽");
        put("1814", "一箭丹心");
        put("1823", "赞");
        put("1824", "弱鸡");
        put("1825", "突击纵队");
        put("1826", "装甲兵团");
        put("1827", "轰炸机群");
        put("1828", "帝国舰队");
        put("1838", "粉丝荧光棒");
        put("1844", "英嘤英");
        put("1850", "一只狗");
        put("1852", "穷逼火箭");
        put("1853", "凌仕净油飞机");
        put("1855", "星星");
        put("1856", "0.2测");
        put("1857", "星空棒棒糖");
        put("1858", "太空旅行卡");
        put("1868", "烟花礼赞");
        put("1869", "烟花雨");
        put("1870", "烟花束");
        put("1884", "MIKU西");
        put("1886", "苏恩小猫咪");
        put("1888", "超级火箭");
        put("1889", "火箭");
        put("1890", "宇宙飞船");
        put("1891", "亿大佬超火");
        put("1897", "大马猴火箭");
        put("1912", "岁月静好");
        put("1913", "吃鸡");
        put("1914", "粉丝荧光棒");
        put("1923", "伊C");
        put("1924", "装逼王超火");
        put("1925", "超级达克宁");
        put("1930", "林七周年火箭");
        put("1931", "钱胖胖来辣");
        put("1933", "一路向南");
        put("1934", "OS白鹄");
        put("1938", "女神驾到");
        put("1944", "东部upup");
        put("1945", "西部upup");
        put("1946", "KPL加油！");
        put("1947", "C位出道");
        put("1959", "苏菲弹力贴身");
        put("1961", "人间大炮");
        put("1962", "天生是王者");
        put("1965", "花洒");
        put("1966", "水桶");
        put("1967", "灭火器");
        put("1968", "高压水枪");
        put("1976", "音乐火箭");
        put("1977", "我的林可爱");
        put("1978", "招行联名卡");
        put("1979", "鲨鱼公仔");
        put("1980", "鲨鱼娘");
        put("1981", "呆头鹅专属");
        put("1982", "同舟共济");
        put("1984", "米宝超火");
        put("1985", "抽象火箭");
        put("1986", "苏菲最美");
        put("1987", "我佳");
        put("1991", "摩卡的冰淇淋");
        put("1992", "超级巡航导弹");
        put("2021", "唯爱瑾宝");
        put("2022", "安排一下");
        put("2026", "套你猴子");
        put("2031", "球王战衣");
    }};

    @Override
    public boolean preRunCheck() {
        boolean check = super.preRunCheck();
        // 处理直播房间未设置的问题
        if (BakaDanmakuConfig.livePlatform.douyuRoom.liveRoom == 0) {
            sendChatMessage("§8§l直播房间 ID 未设置，弹幕机已停止工作！ ");
            check = false;
        }

        return check;
    }

    @Override
    public void doRun() {
        // 获取真实房间 ID
        String roomID = String.valueOf(BakaDanmakuConfig.livePlatform.douyuRoom.liveRoom);

        // 提示，相关房间信息已经获取
        sendChatMessage("§8§l直播房间 ID 已经获取，ID 为 " + roomID);

        try {
            // 连接
            Socket socket = new Socket(LIVE_URL, PORT);

            // 获取数据输出流
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            // 获取输出流
            InputStream inputStream = socket.getInputStream();

            // 发送加入信息
            sendJoinMsg(roomID);

            // 提示，已经连接
            sendChatMessage("§8§l弹幕姬已经连接");

            // 直播热度值获取，Post PopularityEvent
            MinecraftForge.EVENT_BUS.post(new PopularityEvent(BakaDanmakuConfig.livePlatform.douyuRoom.platformDisplayName, getHotValue()));

            // 创建定时器
            Timer timer = new Timer();
            // 利用 timer 模块定时发送心跳包，同时定期更新直播间的热度值，周期为 45 秒
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    // 心跳包
                    sendHeartBeat();

                    // 直播热度值更新，Post PopularityEvent
                    MinecraftForge.EVENT_BUS.post(new PopularityEvent(BakaDanmakuConfig.livePlatform.douyuRoom.platformDisplayName, getHotValue()));
                }
            }, 45000, 45000);

            // 等待验证
            while (keepRunning) {
                // 开始读取数据流，先开辟缓存区，因为头部为 8 字长，还有 4 字长消息长度
                byte[] buf = new byte[8 + 4];
                inputStream.read(buf);
                ByteBuffer byteBuffer = ByteBuffer.wrap(buf);

                int length = LittleToBig(byteBuffer.getInt()); // 长度数据

                // 如果长度小于等于 8
                if (length <= 8) continue;

                // 剔除头部，进行读取
                byte[] bodyByte = new byte[length - 8];
                inputStream.read(bodyByte);

                // 如果长度大于 8，说明是有数据的
                String bodyStringFirst = new String(bodyByte, StandardCharsets.UTF_8);

                // 看下是不是登陆信息
                if (bodyStringFirst.indexOf("type@=loginres/") == 0) {
                    sendChatMessage("§8§l斗鱼弹幕姬登陆成功");
                    break;
                }
            }

            // 发送分组信息
            sendDataPack(String.format("type@=joingroup/rid@=%s/gid@=-9999", roomID));

            // 轮询接收弹幕
            while (keepRunning) {
                try {
                    // 开始读取数据流，先开辟缓存区，因为头部为 8 字长，还有 4 字长消息长度
                    byte[] buf = new byte[8 + 4];
                    inputStream.read(buf);
                    ByteBuffer byteBuffer = ByteBuffer.wrap(buf);

                    int length = LittleToBig(byteBuffer.getInt()); // 长度数据

                    // 如果长度小于等于 8，或者超出 String 上限，这种情况很常见
                    if (length <= 16 || length > 65534) continue;

                    // 剔除头部，进行读取
                    byte[] bodyByte = new byte[length - 8];
                    inputStream.read(bodyByte);

                    // 如果长度大于 8，说明是有数据的
                    String bodyString = new String(bodyByte, StandardCharsets.UTF_8);

                    // BakaDanmaku.logger.info(bodyString);

                    // 开始判断，普通弹幕
                    if (bodyString.indexOf("type@=chatmsg") == 0) {
                        // 正则匹配
                        Matcher mDanmaku = readDanmakuInfo.matcher(bodyString);
                        Matcher mUser = readDanmakuUser.matcher(bodyString);

                        // 两者均匹配到才进行事件触发
                        if (mDanmaku.find() && mUser.find()) {
                            String danmuMsg = mDanmaku.group(1); // 弹幕信息
                            String user = mUser.group(1); // 发送者

                            // Post DanmakuEvent
                            MinecraftForge.EVENT_BUS.post(new DanmakuEvent(BakaDanmakuConfig.livePlatform.douyuRoom.platformDisplayName, user, danmuMsg));

                            continue;
                        }
                    }

                    // 开始判断，送礼弹幕
                    if (bodyString.indexOf("type@=dgb") == 0) {
                        // 正则匹配
                        Matcher mUser = readGiftUser.matcher(bodyString);
                        Matcher mGiftName = readGiftName.matcher(bodyString);
                        Matcher mNum = readGiftNum.matcher(bodyString);

                        // 两者均匹配到才进行事件触发
                        if (mGiftName.find() && mUser.find() && mNum.find()) {
                            String giftName = giftMap.containsKey(mGiftName.group(1)) ? giftMap.get(mGiftName.group(1)) : mGiftName.group(1); // 礼物信息
                            String user = mUser.group(1); // 发送者
                            int num = Integer.valueOf(mNum.group(1)); // 礼物数量

                            // Post GiftEvent
                            MinecraftForge.EVENT_BUS.post(new GiftEvent(BakaDanmakuConfig.livePlatform.douyuRoom.platformDisplayName, giftName, num, user));

                            continue;
                        }
                    }

                    // 开始判断，玩家进入
                    if (bodyString.indexOf("type@=uenter") == 0) {
                        // 正则匹配
                        Matcher mUser = readWelcomeUser.matcher(bodyString);

                        // 两者均匹配到才进行事件触发
                        if (mUser.find()) {
                            String user = mUser.group(1); // 玩家名称

                            // Post WelcomeEvent
                            MinecraftForge.EVENT_BUS.post(new WelcomeEvent(BakaDanmakuConfig.livePlatform.douyuRoom.platformDisplayName, user));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            sendDataPack("type@=logout/"); // 发送断开包
            timer.cancel(); // 关闭心跳包线程的定时器
            socket.close(); // 关闭 socket
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * 清除部分
     */
    @Override
    public void clear() {
    }

    /**
     * 首次连接之前测试网络连通性
     *
     * @return 能否找到斗鱼弹幕服务器
     */
    private boolean isReachable() {
        try {
            return InetAddress.getByName(LIVE_URL).isReachable(BakaDanmakuConfig.network.timeout);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 首次连接发送的认证数据
     *
     * @param roomId 直播房间 ID
     */
    private void sendJoinMsg(String roomId) {
        // 发送验证包
        sendDataPack(String.format("type@=loginreq/roomid@=%s/", roomId));
    }

    /**
     * 客户端发送的心跳包，45 秒发送一次
     */
    private void sendHeartBeat() {
        sendDataPack("type@=mrkl/");
    }

    /**
     * 读取直播热度值
     *
     * @return 直播热度值
     */
    private int getHotValue() {
        // 初始化
        int hotValue = 0;

        try {
            // B 站提供的获取直播 id 的 api
            URL url = new URL("http://open.douyucdn.cn/api/RoomApi/room/" + BakaDanmakuConfig.livePlatform.douyuRoom.liveRoom);

            // 获取网络数据流
            InputStream con = url.openStream();

            // 按照 UTF-8 编码解析
            String data = new String(ByteStreams.toByteArray(con), StandardCharsets.UTF_8);

            // 关闭数据流
            con.close();

            // 简单的 JSON 不需要用 Gson 解析，正则省事
            Matcher matcher = readHotValue.matcher(data);
            if (matcher.find()) {
                hotValue = Integer.valueOf(matcher.group(1));
            } else {
                // 日志记录
                BakaDanmaku.logger.fatal("Cannot get live hot value.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return hotValue;
    }

    /**
     * 固定的发包方法
     *
     * @param body 发送的数据本体部分
     */
    private void sendDataPack(String body) {
        try {
            // 数据部分，要求以 \0 结尾，同时以 UTF-8 编码解析成 Byte
            body = body + '\0';
            byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);

            // 封包总长度，因为头部固定为 8 字长，故加上 8
            int length = bodyBytes.length + 8;

            // 为缓冲区分配长度，再来个 4 字长的长度数据
            ByteBuffer byteBuffer = ByteBuffer.allocate(length + 4);

            // 存两遍（鬼知道斗鱼在干啥） 4 字长的封包总大小数据，所以为 int
            // 别忘了斗鱼是该死的小端模式
            byteBuffer.putInt(BigToLittle(length));
            byteBuffer.putInt(BigToLittle(length));

            // 存入 2 字长的消息类型，消息类型有 689（C->S） 和 690（S->C）
            byteBuffer.putShort(BigToLittle((short) 689));

            // 存入加密字段和保留字段，目前都为 0，总共 2 字长
            byteBuffer.putShort((short) 0);

            // 存入数据
            byteBuffer.put(bodyBytes);

            // 写入输出数据流中
            dataOutputStream.write(byteBuffer.array());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 4 字节大端模式数据 -> 小端模式数据
     *
     * @param big 大端模式数据
     * @return 小端模式数据
     */
    private int BigToLittle(int big) {
        // 利用 ByteBuffer 把 int 转 byte[]
        byte[] bytes = ByteBuffer.allocate(4).putInt(big).array();

        // 倒置数组，然后处理成 int，返回
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    /**
     * 2 字节大端模式数据 -> 小端模式数据
     *
     * @param big 大端模式数据
     * @return 小端模式数据
     */
    private short BigToLittle(short big) {
        // 利用 ByteBuffer 把 short 转 byte[]
        byte[] bytes = ByteBuffer.allocate(2).putShort(big).array();

        // 倒置数组，然后处理成 int，返回
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }

    /**
     * 4 字节小端模式数据 -> 大端模式数据
     *
     * @param little 小端模式数据
     * @return 大端模式数据
     */
    private int LittleToBig(int little) {
        // 利用 ByteBuffer 把 int 转 byte[]
        byte[] bytes = ByteBuffer.allocate(4).putInt(little).array();

        // 倒置数组，然后处理成 int，返回
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }
}
