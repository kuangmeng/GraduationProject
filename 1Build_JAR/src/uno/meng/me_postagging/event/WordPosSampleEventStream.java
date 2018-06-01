package uno.meng.me_postagging.event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import opennlp.tools.ml.model.Event;
import opennlp.tools.util.AbstractEventStream;
import opennlp.tools.util.ObjectStream;
import uno.meng.me_postagging.feature.WordPosContextGenerator;
import uno.meng.me_postagging.stream.WordPosSample;

/**
 * 根据上下文得到事件
 *
 */
public class WordPosSampleEventStream extends AbstractEventStream<WordPosSample> {

  private WordPosContextGenerator generator;

  /**
   * 构造
   * 
   * @param samples 样本流
   * @param generator 上下文产生器
   */
  public WordPosSampleEventStream(ObjectStream<WordPosSample> samples,
      WordPosContextGenerator generator) {
    super(samples);
    this.generator = generator;
  }

  /**
   * 创建事件
   * 
   * @param sample 样本流
   */
  @Override
  protected Iterator<Event> createEvents(WordPosSample sample) {
    String[] words = sample.getWords();
    String[] tagsAndposes = sample.getTagsAndPoses();
    String[] characters = sample.getCharacters();
    String[][] ac = sample.getAditionalContext();
    List<Event> events = generateEvents(characters, words, tagsAndposes, ac);
    return events.iterator();
  }

  /**
   * 产生事件
   * 
   * @param characters 字符
   * @param words 词语
   * @param tagsAndposes 词性
   * @param ac
   * @return
   */
  private List<Event> generateEvents(String[] characters, String[] words, String[] tagsAndposes,
      String[][] ac) {
    List<Event> events = new ArrayList<Event>(tagsAndposes.length);

    for (int i = 0; i < characters.length; i++) {
      // 产生事件的部分
      String[] context = generator.getContext(i, characters, tagsAndposes, words);

      events.add(new Event(tagsAndposes[i], context));
    }
    return events;
  }

}
