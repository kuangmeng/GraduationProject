package uno.meng.ner.perceptron.instance;

import uno.meng.ner.corpus.document.sentence.Sentence;

public interface InstanceHandler {
  boolean process(Sentence instance);
}
