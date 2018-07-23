package mitlab.seg.ner.tokenizer.lexical;

import mitlab.seg.ner.perceptron.tagset.NERTagSet;

/**
 * 命名实体识别接口
 *
 */
public interface NERecognizer {
  String[] recognize(String[] wordArray, String[] posArray);

  NERTagSet getNERTagSet();
}
