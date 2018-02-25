package com.sudu.inas.beans;

import com.google.common.base.MoreObjects;
import lombok.Data;


@Data
public class TransElement {


    private int sentenceId;
    /**
     * 内容
     */
    private String text;
    /**
     * 格式
     */
    private String format;
    /**
     * 数据
     */
    private String body;

    public TransElement(String text, String format, String body) {
        this.text = text;
        this.format = format;
        this.body = body;
    }

    public TransElement() {

    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("text", text)
                .add("format", format)
                .add("body", body)
                .toString();
    }
}
