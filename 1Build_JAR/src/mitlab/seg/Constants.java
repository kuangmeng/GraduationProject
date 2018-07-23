package mitlab.seg;

public class Constants {
  public static final Integer MAX = 100;
  public static final Integer ONE = 1;
  public static final String SPACES = " ";
  public static final double DOUBLEONE = 1.0;
  public static final String START = "<s>";
  public static final String END = "</s>";
  public static final String SEGMENT = " / ";
  public static final String CWSMODEL = "data/model/perceptron/cws.model";
  public static final String POSMODEL = "data/model/perceptron/pos.model";
  public static final String NERMODEL = "data/model/perceptron/ner.model";
  public static final String PartOfSpeechTagDictionary = "data/dictionary/other/TagPKU98.csv";
  public static final String CoreDictionaryPath = "data/dictionary/CoreNatureDictionary.txt";
  public static final String CustomDictionaryPath[] =
      new String[] {"data/dictionary/custom/CustomDictionary.txt"};
  public static final boolean Normalization = false;
  public static final String BiGramDictionaryPath =
      "data/dictionary/CoreNatureDictionary.ngram.txt";
  public static final String CoreDictionaryTransformMatrixDictionaryPath =
      "data/dictionary/CoreNatureDictionary.tr.txt";
  public static final double dSmoothingPara = 0.1;
  public static final int MAX_FREQUENCY = 25146057; // 现在总词频25146057
  public static final double dTemp = (double) 1 / MAX_FREQUENCY + 0.00001;

}
