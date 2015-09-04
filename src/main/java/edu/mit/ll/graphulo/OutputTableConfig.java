package edu.mit.ll.graphulo;

import com.google.common.base.Preconditions;
import edu.mit.ll.graphulo.apply.ApplyOp;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static edu.mit.ll.graphulo.InputTableConfig.DEFAULT_ITERS_MAP;

/**
 * Immutable class representing a table used as output from an iterator stack via RemoteWriteIterator.
 */
public final class OutputTableConfig implements Serializable {

  public static final int DEFAULT_COMBINER_PRIORITY = 6;

  private static final long serialVersionUID = 1L;

  private final TableConfig tableConfig;
  private final Class<? extends ApplyOp> applyLocal;  // allow null
  private final Map<String,String> applyLocalOptions;
  private final Map<String,String> tableItersRemote;

  public OutputTableConfig(TableConfig tableConfig) {
    Preconditions.checkNotNull(tableConfig);
    this.tableConfig = tableConfig;
    applyLocal = null;
    applyLocalOptions = Collections.emptyMap();
    tableItersRemote = DEFAULT_ITERS_MAP;
  }

  private OutputTableConfig(TableConfig tableConfig, Class<? extends ApplyOp> applyLocal,
                            Map<String, String> applyLocalOptions, Map<String, String> tableItersRemote) {
    this.tableConfig = tableConfig;
    this.tableItersRemote = tableItersRemote;
    this.applyLocalOptions = applyLocalOptions;
    this.applyLocal = applyLocal;
  }

  public OutputTableConfig withTableConfig(TableConfig tableConfig) {
    Preconditions.checkNotNull(tableConfig);
    return new OutputTableConfig(tableConfig, applyLocal, applyLocalOptions, tableItersRemote);
  }
  public OutputTableConfig withApplyLocal(Class<? extends ApplyOp> applyLocal, Map<String,String> applyLocalOptions) {
    return new OutputTableConfig(tableConfig, applyLocal,
        applyLocal == null || applyLocalOptions == null ? Collections.<String,String>emptyMap() : new HashMap<>(applyLocalOptions),
        tableItersRemote);
  }
  public OutputTableConfig withTableItersRemote(DynamicIteratorSetting tableItersRemote) {
    return new OutputTableConfig(tableConfig, applyLocal, applyLocalOptions, tableItersRemote == null ? DEFAULT_ITERS_MAP : tableItersRemote.buildSettingMap());
  }

  // will enable these shortcut methods if determined to be a safe, common use case
//  public InputTableConfig withRowFilter(String rowFilter) {
//    DynamicIteratorSetting dis = DynamicIteratorSetting.fromMap(tableItersRemote);
//    dis.prepend(D4mRangeFilter.iteratorSetting(1, D4mRangeFilter.KeyPart.ROW, rowFilter));
//    return withItersRemote(dis);
//  }
//  public InputTableConfig withColFilter(String colFilter) {
//    DynamicIteratorSetting dis = DynamicIteratorSetting.fromMap(tableItersRemote);
//    dis.prepend(D4mRangeFilter.iteratorSetting(1, D4mRangeFilter.KeyPart.COLQ, colFilter));
//    return withItersRemote(dis);
//  }

  public TableConfig getTableConfig() {
    return tableConfig;
  }
  public DynamicIteratorSetting getTableItersRemote() {
    return DynamicIteratorSetting.fromMap(tableItersRemote);
  }
  public Class<? extends ApplyOp> getApplyLocal() {
    return applyLocal;
  }
  public Map<String, String> getApplyLocalOptions() {
    return Collections.unmodifiableMap(applyLocalOptions);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    OutputTableConfig that = (OutputTableConfig) o;

    if (!tableConfig.equals(that.tableConfig)) return false;
    if (applyLocal != null ? !applyLocal.equals(that.applyLocal) : that.applyLocal != null) return false;
    if (!applyLocalOptions.equals(that.applyLocalOptions)) return false;
    return tableItersRemote.equals(that.tableItersRemote);

  }

  @Override
  public int hashCode() {
    int result = tableConfig.hashCode();
    result = 31 * result + (applyLocal != null ? applyLocal.hashCode() : 0);
    result = 31 * result + applyLocalOptions.hashCode();
    result = 31 * result + tableItersRemote.hashCode();
    return result;
  }
}