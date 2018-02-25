package com.sudu.inas.util;

import com.sudu.inas.beans.Arg;
import com.sudu.inas.beans.SyntaxResult;
import com.sudu.inas.beans.Word;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ParseXml {

    @SuppressWarnings("unchecked")
    public static SyntaxResult parseSyntax(String xml) throws DocumentException {

        HashMap<Integer, Word> wordsMap = new HashMap<>();
        ArrayList<Word> verbs = new ArrayList<>();
        ArrayList<Integer> pos = new ArrayList<>();
        ArrayList<Integer> nh = new ArrayList<>();
        Document document = DocumentHelper.parseText(xml);
        Element rootElement = document.getRootElement();
        List<Element> words = rootElement.element("doc").element("para").element("sent").elements("word");
        Iterator wordsIterator = words.iterator();
        SyntaxResult syntaxResult = new SyntaxResult();

        while (wordsIterator.hasNext()){
            Element word = (Element) wordsIterator.next();
            int id = Integer.parseInt(word.attribute("id").getText());
            Word singleWord = new Word();
            singleWord.setId(id);
            singleWord.setCont(word.attribute("cont").getText());
            singleWord.setPos(word.attribute("pos").getText());
            singleWord.setNe(word.attribute("ne").getText());
            singleWord.setParent(Integer.parseInt(word.attribute("parent").getText()));
            singleWord.setRelate(word.attribute("relate").getText());
            singleWord.setSemparent(Integer.parseInt(word.attribute("semparent").getText()));
            singleWord.setSemrelate(word.attribute("semrelate").getText());
            if ("HED".equals(word.attribute("relate").getText())){
                parseForVerb(singleWord,word);
                verbs.add(singleWord);
            }
            if ("COO".equals(word.attribute("relate").getText())){
                Word verb = parseForVerb(singleWord,word);
                verbs.add(verb);
            }
            if ("ns".equals(word.attribute("pos").getText())){
                pos.add(id);
            }

            if ("nh".equals(word.attribute("pos").getText())){
                nh.add(id);
            }

            wordsMap.put(id,singleWord);
        }

        syntaxResult.setVerbs(verbs);
        syntaxResult.setWords(wordsMap);
        syntaxResult.setPos(pos);
        syntaxResult.setNh(nh);
        return syntaxResult;

    }

    public static Word parseForVerb(Word verb,Element word){
        List argsList = word.elements("arg");
        if (argsList.size()>0){
            Iterator argsIterator = argsList.iterator();
            ArrayList<Arg> args = new ArrayList<>();
            while (argsIterator.hasNext()){
                Element arg = (Element) argsIterator.next();
                Arg arg1 = new Arg();
                arg1.setId(Integer.parseInt(arg.attribute("id").getText()));
                arg1.setBegin(Integer.parseInt(arg.attribute("beg").getText()));
                arg1.setEnd(Integer.parseInt(arg.attribute("end").getText()));
                arg1.setType(arg.attribute("type").getText());
                args.add(arg1);
            }
            verb.setArgs(args);
        }
        return verb;
    }

}
