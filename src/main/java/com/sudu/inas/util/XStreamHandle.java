package com.sudu.inas.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Created by J on 2017/8/9.
 */
public class XStreamHandle {

    public static String toXml(Object obj){
        XStream xstream = new XStream(new DomDriver("utf8"));
        xstream.processAnnotations(obj.getClass()); // 识别obj类中的注解
        return xstream.toXML(obj);
    }

    public static <T> T toBean(String xmlStr, Class<T> cls){
        XStream xstream = new XStream(new DomDriver());
        xstream.processAnnotations(cls);
        @SuppressWarnings("unchecked")
        T t = (T) xstream.fromXML(xmlStr);
        return t;
    }

}
