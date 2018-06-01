package uno.meng.ner.corpus.io;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public abstract class LineHandler {
  String delimiter = "\t";

  public LineHandler(String delimiter) {
    this.delimiter = delimiter;
  }

  public LineHandler() {}

  public void handle(String line) throws Exception {
    List<String> tokenList = new LinkedList<String>();
    int start = 0;
    int end;
    while ((end = line.indexOf(delimiter, start)) != -1) {
      tokenList.add(line.substring(start, end));
      start = end + 1;
    }
    tokenList.add(line.substring(start, line.length()));
    handle(tokenList.toArray(new String[0]));
  }

  public void done() throws IOException {
    // do noting
  }

  public abstract void handle(String[] params) throws IOException;
}
