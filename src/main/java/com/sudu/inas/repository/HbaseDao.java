package com.sudu.inas.repository;


import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.TableCallback;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J on  17-10-23.
 */

@Repository
public class HbaseDao {
    @Autowired
    private HbaseTemplate hbaseTemplate;

    private final String encoding = "utf-8";



    /**
     * insert cell according to tableName,rowKey,familyName,qualifer,value
     * @param tableName 表名
     * @param rowKey 行键
     * @param familyName 列蔟
     * @param qualifier 列名
     * @param value 列值
     * @return boolean
     */
    public Boolean insertData(String tableName, String rowKey, String familyName,String qualifier,String value,TableCallback<Boolean> action) {
        return hbaseTemplate.execute(tableName, table -> {
            boolean flag = false;
            try {
                byte[] rowkey = rowKey.getBytes();
                Put put = new Put(rowkey);
                put.add(Bytes.toBytes(familyName), Bytes.toBytes(qualifier), Bytes.toBytes(value));
                table.put(put);
                flag = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return flag;
        });
    }

    /**
     * get result according to tableName,rowKey
     * @param tableName 表名
     * @param rowKey 行键
     * @return 对应列值的一行数据
     */
    public Result getDataFromRowkey(String tableName, String rowKey) {
        return hbaseTemplate.execute(tableName, new TableCallback<Result>() {
            @Override
            public Result doInTable(HTableInterface table) throws Throwable {
                Get get = new Get(rowKey.getBytes(encoding));
                return table.get(get);
            }

        });
    }

    /**
     * get value of cell according to tableName,rowKey,familyName,qualifer
     * @param tableName 表名
     * @param rowName 行键
     * @param familyName 列蔟
     * @param qualifier 列名
     * @return string 列值
     */
    public String getDataFromQualifier(String tableName ,String rowName, String familyName, String qualifier) {
        return hbaseTemplate.get(tableName, rowName,familyName,qualifier , (result, rowNum) -> {
            List<Cell> ceList =   result.listCells();
            String res = "";
            if(ceList!=null&&ceList.size()>0){
                for(Cell cell:ceList){
                    res = Bytes.toString( cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                }
            }
            return res;
        });
    }



    /**
     * get results according to tableName,begin part of rowKey,
     * @param tableName 表名
     * @param keyBegin 行键的前缀
     * @return 符合要求的多行结果
     */

    public List<Result> getDataWithSameBegining(String tableName,String keyBegin){
        return hbaseTemplate.execute(tableName, new TableCallback<List<Result>>() {
            List<Result> list = new ArrayList<>();
            @Override
            public List<Result> doInTable(HTableInterface hTableInterface) throws Throwable {
                Filter pf = new PrefixFilter(Bytes.toBytes(keyBegin));
                Scan scan = new Scan();
                scan.setFilter(pf);
                ResultScanner rs = hTableInterface.getScanner(scan);
                for(Result result:rs){
                    list.add(result);
                }
                return list;
            }
        });
    }

    /**
     * delete column according to tableName,rowKey,familyName,qualifier,
     * @param tableName 表名
     * @param rowKey 行键
     * @param cf 列蔟
     * @param qualifier 列名
     */
    public void delColumnByQualifier(String tableName,String rowKey,String cf,String qualifier) {
        hbaseTemplate.execute(tableName, new TableCallback<String>() {
            @Override
            public String doInTable(HTableInterface table) throws Throwable {
                Delete deleteColumn = new Delete(Bytes.toBytes(rowKey));
                deleteColumn.deleteColumns(Bytes.toBytes(cf),
                        Bytes.toBytes(qualifier));
                table.delete(deleteColumn);
                return "ok";
            }

        });
    }

}
