package mitlab.seg.ner.corpus.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class ByteArrayStream extends ByteArray {
  /**
   * 每次读取1mb
   */
  protected int bufferSize;
  static IIOAdapter IOAdapter;

  public ByteArrayStream(byte[] bytes, int bufferSize) {
    super(bytes);
    this.bufferSize = bufferSize;
  }

  public static ByteArrayStream createByteArrayStream(String path) {
    if (IOAdapter == null)
      return ByteArrayFileStream.createByteArrayFileStream(path);

    try {
      InputStream is = IOAdapter.open(path);
      if (is instanceof FileInputStream)
        return ByteArrayFileStream.createByteArrayFileStream((FileInputStream) is);
      return ByteArrayOtherStream.createByteArrayOtherStream(is);
    } catch (IOException e) {
      System.out.println("打开失败：" + path);
      return null;
    }
  }

  @Override
  public int nextInt() {
    ensureAvailableBytes(4);
    return super.nextInt();
  }

  @Override
  public char nextChar() {
    ensureAvailableBytes(2);
    return super.nextChar();
  }

  @Override
  public double nextDouble() {
    ensureAvailableBytes(8);
    return super.nextDouble();
  }

  @Override
  public byte nextByte() {
    ensureAvailableBytes(1);
    return super.nextByte();
  }

  @Override
  public float nextFloat() {
    ensureAvailableBytes(4);
    return super.nextFloat();
  }

  protected abstract void ensureAvailableBytes(int size);
}
