package uno.meng.ner.perceptron.feature;

import uno.meng.ner.perceptron.tagset.TagSet;

public abstract class ImmutableFeatureMap extends FeatureMap {
  public ImmutableFeatureMap() {}

  public ImmutableFeatureMap(TagSet tagSet) {
    super(tagSet);
  }
}
