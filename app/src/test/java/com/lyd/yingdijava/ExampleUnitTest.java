package com.lyd.yingdijava;

import static com.lyd.yingdijava.Utils.TextUtils.unicodeStr2String;

import org.junit.Test;

import static org.junit.Assert.*;


import com.lyd.yingdijava.Utils.TextUtils;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void myTest(){
        String img = "{\\\"build\\\":9940,\\\"clazz\\\":2,\\\"clazzCount\\\":\\\"{\\\\\\\"装备\\\\\\\":2,\\\\\\\"法术\\\\\\\":15,\\\\\\\"随从\\\\\\\":11,\\\\\\\"地标\\\\\\\":2}\\\",\\\"code\\\":\\\"AAEBAaIHBrIC+g6CtALtgAT13QT23QQM7QKGCfW7AqrLA+fdA\\u002FPdA875A72ABLezBPTdBPztBMGDBQA=\\\",\\\"collect\\\":0,\\\"collectDeckId\\\":0,\\\"color\\\":\\\"\\\",\\\"deckImg\\\":\\\"https:\\u002F\\u002Fpic.iyingdi.com\\u002Fbefore825java\\u002Fcommon\\u002F2022\\u002F10\\u002F21\\u002Fca5c8a32-ed12-47f7-a81c-ae1ee37de291.png\\\",\\\"deckType\\\":\\\"hearthstone\\\",\\\"faction\\\":\\\"rogue\\\",\\\"format\\\":\\\"狂野\\\",\\\"goldPrice\\\":33600,\\\"hates\\\":0,\\\"id\\\":7453176,\\\"img\\\":\\\"https:\\u002F\\u002Fpic.iyingdi.com\\u002Fbefore825java\\u002Fcard\\u002Fhearthstone\\u002Fseries\\u002Fbasic\\u002Fthumb\\u002F83.png\\\",\\\"imgCard\\\":\\\"艾德温·范克里夫\\\",\\\"latestSeries\\\":51,\\\"likes\\\":7,\\\"name\\\":\\\"邮箱奇迹贼\\\",\\\"open\\\":1,\\\"openTime\\\":1665897500,\\\"pageview\\\":3650,\\\"player\\\":\\\"济往繁星\\\",\\\"price\\\":9940,\\\"rank\\\":\\\"\\\",\\\"rarityInfo\\\":\\\"{\\\\\\\"普通\\\\\\\":6,\\\\\\\"稀有\\\\\\\":15,\\\\\\\"传说\\\\\\\":4,\\\\\\\"史诗\\\\\\\":5}\\\",\\\"relatedRes\\\":\\\"\\\",\\\"remark\\\":\\\"\\\",\\\"reply\\\":0,\\\"resource\\\":2,\\\"score\\\":0,\\\"sequence\\\":100,\\\"setId\\\":754095,\\\"setName\\\":\\\"cjxc的套牌\\\",\\\"statistic\\\":\\\"{\\\\\\\"0\\\\\\\":6,\\\\\\\"1\\\\\\\":10,\\\\\\\"12\\\\\\\":1,\\\\\\\"2\\\\\\\":7,\\\\\\\"3\\\\\\\":1,\\\\\\\"4\\\\\\\":2,\\\\\\\"7+\\\\\\\":1,\\\\\\\"5\\\\\\\":1,\\\\\\\"6\\\\\\\":2}\\\",\\\"tags\\\":\\\"[狂野模式][潜行者套牌]\\\",\\\"time\\\":1665545482,\\\"updated\\\":1666311156,\\\"user\\\":11397603,\\\"visible\\\":1}";
        String imgs = "https:\\u002F\\u002Fpic.iyingdi.com\\u002Fyingdiapp\\u002F202210\\u002F11690211\\u002F16664612427027_w_720_h_1584.jpeg`http:\\u002F\\u002Fwspic.iyingdi.cn\\u002Fexpression\\u002Fhs20201023\\u002F082.png`http:\\u002F\\u002Fwspic.iyingdi.cn\\u002Fexpression\\u002Fhs20201023\\u002F082.png";
        try {
//            StringBuffer sb = new StringBuffer();
//            String[] hex = img.split("\\\\u");
//            for (String s : hex) {
//                int data = Integer.parseInt(s, 16);
//                sb.append((char) data);
//            }
//            System.out.println(sb);
            StringBuffer temp = new StringBuffer(unicodeStr2String(img));
            String result = unicodeStr2String(img).replaceAll("\\\\","");
            System.out.println(result);
            System.out.println("{\"build\":9940,\"clazz\":2,\"clazzCount\":{\"装备\":2,\"法术\":15,\"随从\":11,\"地标\":2},\"code\":\"AAEBAaIHBrIC+g6CtALtgAT13QT23QQM7QKGCfW7AqrLA+fdA/PdA875A72ABLezBPTdBPztBMGDBQA=\",\"collect\":0,\"collectDeckId\":0,\"color\":\"\",\"deckImg\":\"https://pic.iyingdi.com/before825java/common/2022/10/21/ca5c8a32-ed12-47f7-a81c-ae1ee37de291.png\",\"deckType\":\"hearthstone\",\"faction\":\"rogue\",\"format\":\"狂野\",\"goldPrice\":33600,\"hates\":0,\"id\":7453176,\"img\":\"https://pic.iyingdi.com/before825java/card/hearthstone/series/basic/thumb/83.png\",\"imgCard\":\"艾德温·范克里夫\",\"latestSeries\":51,\"likes\":7,\"name\":\"邮箱奇迹贼\",\"open\":1,\"openTime\":1665897500,\"pageview\":3650,\"player\":\"济往繁星\",\"price\":9940,\"rank\":\"\",\"rarityInfo\":{\"普通\":6,\"稀有 \":15,\"传说\":4,\"史诗\":5},\"relatedRes\":\"\",\"remark\":\"\",\"reply\":0,\"resource\":2,\"score\":0,\"sequence\":100,\"setId\":754095,\"setName\":\"cjxc的套牌\",\"statistic\":{\"0 \":6,\"1 \":10,\"12 \":1,\"2 \":7,\"3 \":1,\"4 \":2,\"7 \":1,\"5 \":1,\"6 \":2},\"tags\":\"[狂野模式][潜行者套牌]\",\"time\":1665545482,\"updated\":1666311156,\"user\":11397603,\"visible\":1}");
//                删除所有 { 前面的双引号
            System.out.println(unicodeStr2String(img));
//            String[] resList = imgs.split("`");
//            for (int i = 0; i < resList.length; i++) {
//                resList[i] = TextUtils.unicodeStr2String(resList[i]);
//                System.out.println(resList[i]);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}