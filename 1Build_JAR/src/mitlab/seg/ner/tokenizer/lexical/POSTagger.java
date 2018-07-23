package mitlab.seg.ner.tokenizer.lexical;

import java.util.List;

public interface POSTagger {
  String[] tag(String... words);

  String[] tag(List<String> wordList);
}
