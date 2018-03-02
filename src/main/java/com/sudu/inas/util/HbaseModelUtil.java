package com.sudu.inas.util;

import com.sudu.inas.beans.HbaseModel;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Created by J on  17-10-27.
 */

public class HbaseModelUtil {

        public static final String BASICTABLE ="Object";

    public static final String CF1 = "rawinfo";

    public static final String COLUMN1 = "realname";

    public static final String COLUMN2 = "rawtext";

    public static final String CF2 = "timeline";

    public static final String CONNTABLE = "Connection";

    public static final String DEFAULT = "default";

    public static HbaseModel kvToHbaseModel(KeyValue kv) {
        HbaseModel hbaseModel = new HbaseModel();
        hbaseModel.setRow(Bytes.toString(kv.getRow()));
        hbaseModel.setFamilyName(Bytes.toString(kv.getFamily()));
        hbaseModel.setQualifier(Bytes.toString(kv.getQualifier()));
        hbaseModel.setValue(Bytes.toString(kv.getValue()));
        return hbaseModel;
    }
}
