package mitlab.seg.crf_seg.util;

public class MengSegMap<K, V> {

  private K k;
  private V v;

  private MengSegMap(K k, V v) {
    this.k = k;
    this.v = v;
  }

  public static <K, V> MengSegMap<K, V> with(K k, V v) {
    return new MengSegMap<K, V>(k, v);
  }

  public void setK(K k) {
    this.k = k;
  }

  public void setV(V v) {
    this.v = v;
  }

  public K getK() {
    return k;
  }

  public V getV() {
    return v;
  }
}
