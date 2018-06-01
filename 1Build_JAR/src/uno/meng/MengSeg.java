package uno.meng;

import org.nlpcn.commons.lang.util.StringUtil;
import uno.meng.crf_seg.util.item;
import uno.meng.ner.domain.Term;
import uno.meng.ner.perceptron.PerceptronSegmenter;
import uno.meng.ner.recognition.real.FinanceRecognition;
import uno.meng.ner.recognition.real.LawRecognition;
import uno.meng.ner.recognition.real.MedicineRecognition;
import uno.meng.count.Count;
import uno.meng.crf_seg.Config;
import uno.meng.crf_seg.Model;
import uno.meng.crf_seg.util.CharTable;
import uno.meng.crf_seg.util.MatrixUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CRF分词
 */
public class MengSeg {
  private Model model = null;
  public PerceptronSegmenter segmenter = null;
  public MengSeg(Model model) throws IOException {
    segmenter = new PerceptronSegmenter(uno.meng.Constants.CWSMODEL);
    this.model = model;
  };

  public List<String> AnotherSeg(String str, List<String> model) throws IOException {
    char[] sentenceConverted = CharTable.convert(str.toCharArray());
    CharTable.normalization(sentenceConverted);
    // 字符正规化
    str = new String(sentenceConverted);
    if (!model.isEmpty()) {
      for (String s : model) {
        segmenter.learn(s);
      }
    }
    return segmenter.segment(str);

  }

  public List<String> Seg(char[] chars) {
    char[] sentenceConverted = CharTable.convert(chars);
    CharTable.normalization(sentenceConverted);
    return Seg(new String(sentenceConverted));
    // return Seg(new String(chars));
  }

  public List<String> Seg(String line) {
    if (StringUtil.isBlank(line)) {
      return Collections.emptyList();
    }
    char[] sentenceConverted = CharTable.convert(line.toCharArray());

    CharTable.normalization(sentenceConverted);
    // 字符正规化
    line = new String(sentenceConverted);

    List<item> items = vterbi(line);

    List<String> result = new ArrayList<String>();

    item e = null;
    int begin = 0;
    int end = 0;
    int size = items.size() - 1;
    for (int i = 0; i < items.size(); i++) {
      e = items.get(i);
      switch (e.getTag()) {
        case 0:
          end += e.len;
          result.add(line.substring(begin, end));
          begin = end;
          break;
        case 1:
          end += e.len;
          while (i < size && (e = items.get(++i)).getTag() != 3) {
            end += e.len;
          }
          end += e.len;
          result.add(line.substring(begin, end));
          begin = end;
        default:
          break;
      }
    }
    return result;
  }


  public List<String> AnotherSegRet(int flag, String line, List<String> model) throws IOException {
    List<String> ret = new ArrayList<String>();
    List<String> tmp = AnotherSeg(line,model);
    int len = line.length();
//    List<Term> fs = Count.FrequentStr(line);
//    ret = segTemp(ret, fs, len);
    switch (flag) {
      case 1:
        List<Term> finance = FinanceRecognition.extractFinance(line);
        ret = segTemp(tmp, finance, len);
        break;
      case 2:
        List<Term> law = LawRecognition.extractLaw(line);
        ret = segTemp(tmp, law, len);
        break;
      case 3:
        List<Term> medicine = MedicineRecognition.extractSick(line);
        ret = segTemp(tmp, medicine, len);
        break;
      default:
        ret = tmp;
        break;
    }
    return ret;
  }

  public List<String> SegRet(int flag, String line) {
    List<String> ret = new ArrayList<String>();
    List<String> tmp = Seg(line);
    int len = line.length();
//    List<Term> fs = Count.FrequentStr(line);
//    ret = segTemp(ret, fs, len);
    switch (flag) {
      case 1:
        List<Term> finance = FinanceRecognition.extractFinance(line);
        ret = segTemp(tmp, finance, len);
        break;
      case 2:
        List<Term> law = LawRecognition.extractLaw(line);
        ret = segTemp(tmp, law, len);
        break;
      case 3:
        List<Term> medicine = MedicineRecognition.extractSick(line);
        ret = segTemp(tmp, medicine, len);
        break;
      default:
        ret = tmp;
        break;
    }
    return ret;
  }

  public List<String> segTemp(List<String> tmp, List<Term> area, int len) {
    List<String> ret = new ArrayList<String>();
    int area_len = area.size();
    if (area_len == 0) {
      return tmp;
    }
    int index = 0;
    int tmp_index = 0;
    int tmp_i = 0;
    int i = 0;

    for (i = 0; i < tmp.size(); i++) {
      if (index == area.get(tmp_i).getOffe()) {
        ret.add(area.get(tmp_i).getRealName());
        index += area.get(tmp_i).getRealName().length();
        i--;
        if (tmp_i < area_len - 1) {
          tmp_i++;
          continue;
        } else {
          break;
        }

      } else if (index > tmp_index) {
        if (tmp_index + tmp.get(i).length() <= index) {
          tmp_index += tmp.get(i).length();
        } else {
          ret.add(tmp.get(i).substring(index - tmp_index));
          tmp_index += tmp.get(i).length();
          index = tmp_index;
        }
      } else if (index + tmp.get(i).length() <= area.get(tmp_i).getOffe()) {
        ret.add(tmp.get(i));
        index += tmp.get(i).length();
        tmp_index += tmp.get(i).length();

      } else if (index + tmp.get(i).length() > area.get(tmp_i).getOffe()
          && index < area.get(tmp_i).getOffe()) {
        ret.add(tmp.get(i).substring(0, area.get(tmp_i).getOffe() - index));
        index += area.get(tmp_i).getOffe() - index;
        tmp_index += tmp.get(i).length();
      }
    }
    // 从上一个位置break
    for (int j = i + 1; j < tmp.size(); j++) {
      if (index > tmp_index) {
        if (tmp_index + tmp.get(j).length() <= index) {
          tmp_index += tmp.get(j).length();
        } else {
          ret.add(tmp.get(j).substring(index - tmp_index));
          tmp_index += tmp.get(j).length();
          index = tmp_index;
        }
      } else {
        ret.add(tmp.get(j));
      }
    }
    return ret;
  }

  private List<item> vterbi(String line) {
    List<item> items = Config.wordAlert(line);

    int length = items.size();

    if (length == 0) { // 避免空list，下面get(0)操作越界
      return items;
    }
    if (length == 1) {
      items.get(0).updateTag(0);
      return items;
    }

    /**
     * 填充图
     */
    for (int i = 0; i < length; i++) {
      computeTagScore(items, i);
    }

    // 如果是开始不可能从 m，e开始 ，所以将它设为一个很小的值
    items.get(0).tagScore[2] = -1000;
    items.get(0).tagScore[3] = -1000;

    for (int i = 1; i < length; i++) {
      items.get(i).maxFrom(model, items.get(i - 1));
    }

    // 末位置只能从S,E开始
    // 末位置只能从0,3开始

    item next = items.get(items.size() - 1);

    item self = null;

    int maxStatus = next.tagScore[0] > next.tagScore[3] ? 0 : 3;

    next.updateTag(maxStatus);

    maxStatus = next.from[maxStatus];

    // 逆序寻找
    for (int i = items.size() - 2; i > 0; i--) {
      self = items.get(i);
      self.updateTag(maxStatus);
      maxStatus = self.from[self.getTag()];
      next = self;
    }
    items.get(0).updateTag(maxStatus);

    // printElements(elements) ;

    return items;

  }

  private void computeTagScore(List<item> items, int index) {

    char[][] feautres = model.getConfig().makeFeatureArr(items, index);

    // TODO: set 20 很大吧!
    float[] tagScore = new float[20]; // Config.TAG_NUM*Config.TAG_NUM+Config.TAG_NUM

    for (int i = 0; i < feautres.length; i++) {
      MatrixUtil.dot(tagScore, model.getFeature(feautres[i]));
    }

    items.get(index).tagScore = tagScore;
  }

  /**
   * 随便给一个词。计算这个词的内聚分值，可以理解为计算这个词的可信度
   * 
   * @param word
   */
  public float cohesion(String word) {

    if (word.length() == 0) {
      return Integer.MIN_VALUE;
    }

    List<item> items = Config.wordAlert(word);

    for (int i = 0; i < items.size(); i++) {
      computeTagScore(items, i);
    }

    float value = items.get(0).tagScore[1];

    int len = items.size() - 1;

    for (int i = 1; i < len; i++) {
      value += items.get(i).tagScore[2];
    }

    value += items.get(len).tagScore[3];

    if (value < 0) {
      return 1;
    } else {
      value += 1;
    }

    return value;
  }

}
