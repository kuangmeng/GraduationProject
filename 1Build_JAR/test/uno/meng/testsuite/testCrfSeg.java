package uno.meng.testsuite;

import java.util.List;
import org.junit.Test;
import uno.meng.MengSeg;
import uno.meng.crf_seg.library.CrfLibrary;

public class testCrfSeg{

  @Test
  public void test() {
    String[] sentenceArray =
        new String[] {"高锰酸钾是一种强氧化剂，紫红色晶体，可溶于水，遇乙醇即被还原。常用作消毒剂、水净化剂、氧化剂、漂白剂、毒气吸收剂、二氧化碳精制剂等。", // 专业名词有一定辨识能力
            "《夜晚的骰子》通过描述浅草的舞女在暗夜中扔骰子的情景,寄托了作者对庶民生活区的情感", // 非新闻语料
            "这个像是真的[委屈]前面那个打扮太江户了，一点不上品..", "鼎泰丰的小笼一点味道也没有...每样都淡淡的...淡淡的，哪有食堂2A的好次",
            "克里斯蒂娜·克罗尔说：不，我不是虎妈。我全家都热爱音乐，我也鼓励他们这么做。", "今日APPS：Sago Mini Toolbox培养孩子动手能力",
            "财政部副部长王保安调任国家统计局党组书记", "2.34米男子娶1.53米女粉丝 称夫妻生活没问题", "你看过穆赫兰道吗",
            "国办发布网络提速降费十四条指导意见 鼓励流量不清零", "乐视超级手机能否承载贾布斯的生态梦，这个研究生会五种语言,硕士研究生鱼片与苹果",
            "来一段:工信部女处长每月经过下属科室都要亲口交代24口交换机等技术性器件的安装工作", "大四的匡盟盟来到了哈工大学习计算机科学。",
            "既往史：往体健，否认有高血压病及糖尿病病史。无肝炎、结核等传染病及密切接触史。无重大外伤、手术史及输血史。无食物及药物过敏史，预防接种史不祥。"};
    MengSeg crfSplitWord = CrfLibrary.get();
     for (String s : sentenceArray) {
     List<String> termList = crfSplitWord.Seg(s);
     for (int i = 0; i < termList.size() - 1; i++) {
       System.out.print(termList.get(i) + " ");
     }
     System.out.println(termList.get(termList.size() - 1));
     }
  }
}
