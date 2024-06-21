package edu.mit.ll.graphulo.skvi;

import org.apache.accumulo.core.iterators.TypedValueCombiner;
import org.apache.accumulo.core.client.lexicoder.Encoder;

/**
 * A lexicoder for preserving the native Java sort order of Double values.
 *
 * Temporary stand-in for Accumulo server 1.6.0 compatibility
 */
public class LongLexicoderTemp implements Encoder<Long> {

  @Override
  public byte[] encode(Long l) {
    return encodeLong(l);
  }

  static byte[] encodeLong(Long l) {
    int shift = 56;
    int index;
    int prefix = l < 0 ? 0xff : 0x00;

    for (index = 0; index < 8; index++) {
      if (((l >>> shift) & 0xff) != prefix)
        break;

      shift -= 8;
    }

    byte ret[] = new byte[9 - index];
    ret[0] = (byte) (8 - index);
    for (index = 1; index < ret.length; index++) {
      ret[index] = (byte) (l >>> shift);
      shift -= 8;
    }

    if (l < 0)
      ret[0] = (byte) (16 - ret[0]);

    return ret;
  }

  @Override
  public Long decode(byte[] b) {
    // This concrete implementation is provided for binary compatibility with 1.6; it can be removed in 2.0. See ACCUMULO-3789.
    return decodeUnchecked(b, 0, b.length);
  }

  protected Long decodeUnchecked(byte[] data, int offset, int len) {
    return decodeLongUnchecked(data, offset, len);
  }

  static long decodeLongUnchecked(byte[] data, int offset, int len) {
      long l = 0;
      int shift = 0;

      if (data[offset] < 0 || data[offset] > 16)
        throw new IllegalArgumentException("Unexpected length " + (0xff & data[offset]));

      for (int i = (offset + len) - 1; i >= offset + 1; i--) {
        l += (data[i] & 0xffl) << shift;
        shift += 8;
      }

      // fill in 0xff prefix
      if (data[offset] > 8)
        l |= -1l << ((16 - data[offset]) << 3);

      return l;
    }
}
