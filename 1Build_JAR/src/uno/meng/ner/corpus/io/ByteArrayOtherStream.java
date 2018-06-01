package uno.meng.ner.corpus.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import uno.meng.ner.perceptron.instance.TextUtility;

public class ByteArrayOtherStream extends ByteArrayStream {
  InputStream is;
  static IIOAdapter IOAdapter;

  public ByteArrayOtherStream(byte[] bytes, int bufferSize) {
    super(bytes, bufferSize);
  }

  public ByteArrayOtherStream(byte[] bytes, int bufferSize, InputStream is) {
    super(bytes, bufferSize);
    this.is = is;
  }

  public static ByteArrayOtherStream createByteArrayOtherStream(String path) {
    try {
      InputStream is = IOAdapter == null ? new FileInputStream(path) : IOAdapter.open(path);
      return createByteArrayOtherStream(is);
    } catch (Exception e) {
      System.out.println(TextUtility.exceptionToString(e));
      return null;
    }
  }

  public static ByteArrayOtherStream createByteArrayOtherStream(InputStream is) throws IOException {
    if (is == null)
      return null;
    int size = is.available();
    size = Math.max(102400, size); // 有些网络InputStream实现会返回0，直到read的时候才知道到底是不是0
    int bufferSize = Math.min(1048576, size); // 最终缓冲区在100KB到1MB之间
    byte[] bytes = new byte[bufferSize];
    if (IOUtil.readBytesFromOtherInputStream(is, bytes) == 0) {
      throw new IOException("读取了空文件，或参数InputStream已经到了文件尾部");
    }
    return new ByteArrayOtherStream(bytes, bufferSize, is);
  }

  @Override
  protected void ensureAvailableBytes(int size) {
    if (offset + size > bufferSize) {
      try {
        int wantedBytes = offset + size - bufferSize; // 实际只需要这么多
        wantedBytes = Math.max(wantedBytes, is.available()); // 如果非阻塞IO能读到更多，那越多越好
        wantedBytes = Math.min(wantedBytes, offset); // 但不能超过脏区的大小
        byte[] bytes = new byte[wantedBytes];
        int readBytes = IOUtil.readBytesFromOtherInputStream(is, bytes);
        assert readBytes > 0 : "已到达文件尾部！";
        System.arraycopy(this.bytes, offset, this.bytes, offset - wantedBytes, bufferSize - offset);
        System.arraycopy(bytes, 0, this.bytes, bufferSize - wantedBytes, wantedBytes);
        offset -= wantedBytes;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void close() {
    super.close();
    if (is == null) {
      return;
    }
    try {
      is.close();
    } catch (IOException e) {
      System.out.println(TextUtility.exceptionToString(e));
    }
  }
}
