package uno.meng.testsuite;

import java.util.List;
import org.junit.Test;
import uno.meng.MengSeg;
import uno.meng.crf_seg.library.CrfLibrary;
import uno.meng.ner.MedicineDicExtractNER;
import uno.meng.ner.domain.Term;
import uno.meng.ner.recognition.real.FinanceRecognition;
import uno.meng.ner.recognition.real.LawRecognition;
import uno.meng.ner.recognition.real.MedicineRecognition;

public class testAreaSeg {

  @Test
  public void test() {
    String text = "既往史：往体健，否认有高血压病及糖尿病病史。无肝炎、结核等传染病及密切接触史。无重大外伤、手术史及输血史。无食物及药物过敏史，预防接种史不祥。";
    MengSeg crfSplitWord = CrfLibrary.get();
    List<String> ret = crfSplitWord.SegRet(3, text);
    for (String r : ret) {
      System.out.print(r + "/");
    }
  }
  
  @Test
  public void testMedicine() {
      String text = "既往史：往体健，否认有高血压病及糖尿病病史。无肝炎、结核等传染病及密切接触史。无重大外伤、手术史及输血史。无食物及药物过敏史，预防接种史不祥。";
      List<Term> list = MedicineRecognition.extractSick(text);
      System.out.println("医疗NER：");
      for (Term t : list) {
        System.out.println(t.getName() + ":" + t.getOffe());
      }
  }
  
  @Test
  public void testLaw() {
    String text = "被告人宋皓受贿一案，贵州省高级人民法院2010年4月2日作出（2010）黔高刑二终字第4号刑事判决，维持贵州省六盘水市中级人民法院（2009）黔六中刑三初字第32号刑事判决第一项，即：被告人宋皓犯受贿罪，判处无期徒刑，剥夺政治权利终身，并处没收个人财产人民币10万元；撤销贵州省六盘水市中级人民法院（2009）黔六中刑三初字第32号刑事判决第二项，改判上诉人宋皓受贿所得赃款赃物房屋及现金予以追缴，上缴国库。上述裁判发生法律效力后，宋皓以“其与黄某某是合作关系，借款与担保是两人之间约定的特殊合作方式，其参与了公司的日常经营管理活动；原判认定事实不清，适用法律错误”为由，向贵州省高级人民法院申诉，该院于2011年12月16日作出（2011）黔高调刑监字第25号通知驳回申诉。宋皓不服向本院申诉，本院于2013年3月14日作出（2012）刑监字第182号指令再审决定，指令贵州省高级人民法院再审本案。贵州省高级人民法院经再审于2013年12月25日作出（2013）黔高刑再终字第2号刑事裁定，驳回申诉，维持该院（2010）黔高刑二终字第4号刑事判决。宋皓不服，再次提出申诉。";
    List<Term> list = LawRecognition.extractLaw(text);
    System.out.println("法律NER：");
    for (Term t : list) {
      System.out.println(t.getName() + ":" + t.getOffe());
    }
  }

  @Test
  public void testFinance() {
    String text = "";
    List<Term> list = FinanceRecognition.extractFinance(text);
    System.out.println("金融NER：");
    for (Term t : list) {
      System.out.println(t.getName() + ":" + t.getOffe());
    }
  }
}
