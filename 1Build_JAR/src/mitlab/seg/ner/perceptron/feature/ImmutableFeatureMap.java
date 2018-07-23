package mitlab.seg.ner.perceptron.feature;

import mitlab.seg.ner.perceptron.tagset.TagSet;

public abstract class ImmutableFeatureMap extends FeatureMap {
  public ImmutableFeatureMap() {}

  public ImmutableFeatureMap(TagSet tagSet) {
    super(tagSet);
  }
}
