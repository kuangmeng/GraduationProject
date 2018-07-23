package mitlab.seg.ner.perceptron.instance;

import mitlab.seg.ner.corpus.document.sentence.Sentence;

public interface InstanceHandler {
  boolean process(Sentence instance);
}
