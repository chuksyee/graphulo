package edu.mit.ll.graphulo.apply;

import org.apache.accumulo.core.client.IteratorSetting;
import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Value;
import org.apache.accumulo.core.iterators.IteratorEnvironment;
import org.apache.hadoop.io.Text;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * For every entry passed in, emits <tt>knum</tt> entries,
 * each with the same row and with column qualifiers 1, 2, 3, ..., knum.
 * Values are random doubles between 0 and 1.
 */
public class RandomTopicApply implements ApplyOp {
  private static final Logger log = LogManager.getLogger(RandomTopicApply.class);

  public static final String KNUM = "knum";

  public static IteratorSetting iteratorSetting(int priority, int knum) {
    IteratorSetting itset = new IteratorSetting(priority, ApplyIterator.class);
    itset.addOption(ApplyIterator.APPLYOP, RandomTopicApply.class.getName());
    itset.addOption(ApplyIterator.APPLYOP+ApplyIterator.OPT_SUFFIX+KNUM, Integer.toString(knum));
    return itset;
  }

  private int knum;

  private void parseOptions(Map<String,String> options) {
    for (Map.Entry<String, String> entry : options.entrySet()) {
      String v = entry.getValue();
      switch (entry.getKey()) {
        case KNUM:
          knum = Integer.parseInt(v);
          break;
        default:
          log.warn("Unrecognized option: " + entry);
          break;
      }
    }
    if (knum <= 0)
      throw new IllegalArgumentException("Bad knum: "+knum);
  }

  @Override
  public void init(Map<String, String> options, IteratorEnvironment env) throws IOException {
    parseOptions(options);
  }

  private static final Text EMPTY_TEXT = new Text();

  @Override
  public Iterator<? extends Map.Entry<Key, Value>> apply(Key k, Value v) {
    Text row = k.getRow();
    SortedMap<Key,Value> map = new TreeMap<>();
    for (int i = 1; i <= knum; i++) {
      Key knew = new Key(row, EMPTY_TEXT, new Text(Integer.toString(i)), System.currentTimeMillis());
      Value vnew = new Value(Double.toString(Math.random()).getBytes());
      map.put(knew, vnew);
    }
    return map.entrySet().iterator();
  }

}