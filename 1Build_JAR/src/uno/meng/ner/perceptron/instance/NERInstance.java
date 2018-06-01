package uno.meng.ner.perceptron.instance;

import uno.meng.ner.corpus.document.sentence.Sentence;
import uno.meng.ner.corpus.document.sentence.word.CompoundWord;
import uno.meng.ner.corpus.document.sentence.word.IWord;
import uno.meng.ner.corpus.document.sentence.word.Word;
import uno.meng.ner.perceptron.feature.FeatureMap;
import uno.meng.ner.perceptron.feature.MutableFeatureMap;
import uno.meng.ner.perceptron.tagset.NERTagSet;
import uno.meng.ner.perceptron.utility.Utility;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class NERInstance extends Instance {
  public NERInstance(String[] wordArray, String[] posArray, String[] nerArray, NERTagSet tagSet,
      FeatureMap featureMap) {
    this(wordArray, posArray, featureMap);

    tagArray = new int[wordArray.length];
    for (int i = 0; i < wordArray.length; i++) {
      tagArray[i] = tagSet.add(nerArray[i]);
    }
  }

  public NERInstance(String[] wordArray, String[] posArray, FeatureMap featureMap) {
    initFeatureMatrix(wordArray, posArray, featureMap);
  }

  private void initFeatureMatrix(String[] wordArray, String[] posArray, FeatureMap featureMap) {
    featureMatrix = new int[wordArray.length][];
    for (int i = 0; i < featureMatrix.length; i++) {
      featureMatrix[i] = extractFeature(wordArray, posArray, featureMap, i);
    }
  }

  /**
   * 提取特征，override此方法来拓展自己的特征模板
   *
   * @param wordArray 词语
   * @param posArray 词性
   * @param featureMap 储存特征的结构
   * @param position 当前提取的词语所在的位置
   * @return 特征向量
   */
  protected int[] extractFeature(String[] wordArray, String[] posArray, FeatureMap featureMap,
      int position) {
    boolean create = featureMap instanceof MutableFeatureMap;
    List<Integer> featVec = new ArrayList<Integer>();

    String pre2Word = position >= 2 ? wordArray[position - 2] : "_B_";
    String preWord = position >= 1 ? wordArray[position - 1] : "_B_";
    String curWord = wordArray[position];
    String nextWord = position <= wordArray.length - 2 ? wordArray[position + 1] : "_E_";
    String next2Word = position <= wordArray.length - 3 ? wordArray[position + 2] : "_E_";

    String pre2Pos = position >= 2 ? posArray[position - 2] : "_B_";
    String prePos = position >= 1 ? posArray[position - 1] : "_B_";
    String curPos = posArray[position];
    String nextPos = position <= posArray.length - 2 ? posArray[position + 1] : "_E_";
    String next2Pos = position <= posArray.length - 3 ? posArray[position + 2] : "_E_";

    StringBuilder sb = new StringBuilder();
    addFeatureThenClear(sb.append(pre2Word).append('1'), featVec, featureMap, create);
    addFeatureThenClear(sb.append(preWord).append('2'), featVec, featureMap, create);
    addFeatureThenClear(sb.append(curWord).append('3'), featVec, featureMap, create);
    addFeatureThenClear(sb.append(nextWord).append('4'), featVec, featureMap, create);
    addFeatureThenClear(sb.append(next2Word).append('5'), featVec, featureMap, create);
    // addFeatureThenClear(sb.append(pre2Word).append(preWord).append('6'), featVec, featureMap,
    // create);
    // addFeatureThenClear(sb.append(preWord).append(curWord).append('7'), featVec, featureMap,
    // create);
    // addFeatureThenClear(sb.append(curWord).append(nextWord).append('8'), featVec, featureMap,
    // create);
    // addFeatureThenClear(sb.append(nextWord).append(next2Word).append('9'), featVec, featureMap,
    // create);

    addFeatureThenClear(sb.append(pre2Pos).append('A'), featVec, featureMap, create);
    addFeatureThenClear(sb.append(prePos).append('B'), featVec, featureMap, create);
    addFeatureThenClear(sb.append(curPos).append('C'), featVec, featureMap, create);
    addFeatureThenClear(sb.append(nextPos).append('D'), featVec, featureMap, create);
    addFeatureThenClear(sb.append(next2Pos).append('E'), featVec, featureMap, create);
    addFeatureThenClear(sb.append(pre2Pos).append(prePos).append('F'), featVec, featureMap, create);
    addFeatureThenClear(sb.append(prePos).append(curPos).append('G'), featVec, featureMap, create);
    addFeatureThenClear(sb.append(curPos).append(nextPos).append('H'), featVec, featureMap, create);
    addFeatureThenClear(sb.append(nextPos).append(next2Pos).append('I'), featVec, featureMap,
        create);

    return toFeatureArray(featVec);
  }

  public static NERInstance create(String segmentedTaggedNERSentence, FeatureMap featureMap) {
    return create(Sentence.create(segmentedTaggedNERSentence), featureMap);
  }

  public static NERInstance create(Sentence sentence, FeatureMap featureMap) {
    if (sentence == null || featureMap == null)
      return null;

    NERTagSet tagSet = (NERTagSet) featureMap.tagSet;
    List<String[]> collector = Utility.convertSentenceToNER(sentence, tagSet);
    String[] wordArray = new String[collector.size()];
    String[] posArray = new String[collector.size()];
    String[] tagArray = new String[collector.size()];
    int i = 0;
    for (String[] tuple : collector) {
      wordArray[i] = tuple[0];
      posArray[i] = tuple[1];
      tagArray[i] = tuple[2];
      ++i;
    }
    return new NERInstance(wordArray, posArray, tagArray, tagSet, featureMap);
  }
}
