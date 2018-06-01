package uno.meng.crf_pp.learn;

public abstract class Model {

  public boolean open(String[] args) {
    return true;
  }

  public boolean open(String arg) {
    return true;
  }

  public boolean close() {
    return true;
  }

  public Tagger createTagger() {
    return null;
  }
}
