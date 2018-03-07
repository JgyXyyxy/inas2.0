package com.sudu.inas.beans;

import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class SyntaxResult {

    private Map<Integer, Word> words = new HashMap<>();
    private List<Word> verbs = new ArrayList<>();
    private List<Integer> pos = new ArrayList<>();
    private List<Integer> nh = new ArrayList<>();
    private List<String> relate = new ArrayList<>();

    public SyntaxResult() {
    }

    public List<Action>  dotrans() throws ParseException {
        ArrayList<Action> actions = new ArrayList<>();
        for (Word verb:verbs){
            Map<String, Timenode> timenodeMap = transVerbToTimenodes(verb);
            Iterator<Map.Entry<String, Timenode>> entryIterator = timenodeMap.entrySet().iterator();
            while (entryIterator.hasNext()){
                Action action = new Action();
                Map.Entry<String, Timenode> timenodeEntry = entryIterator.next();
                action.setEntityName(timenodeEntry.getKey());
                if (timenodeEntry.getValue().getInfo().getLocation().equals("")){
                    action.setLocation("未知");
                }else {
                    action.setLocation(timenodeEntry.getValue().getInfo().getLocation());
                }
                action.setTimepoint(timenodeEntry.getValue().getTimePoint());
                action.setDescription(timenodeEntry.getValue().getInfo().getDescription());
                actions.add(action);
            }
        }

        return actions;
    }

    public Map<String, Timenode> transVerbToTimenodes(Word verb) throws ParseException {

        HashMap<String, Timenode> nodeMap = new HashMap<>();
        List<Arg> args = verb.getArgs();
        Iterator<Arg> iterator = args.iterator();
        StringBuilder time = new StringBuilder();
        StringBuilder position = new StringBuilder();
        ArrayList<Integer> a1 = new ArrayList<>();
        ArrayList<Integer> a0 = new ArrayList<>();
        ArrayList<String> nameList = new ArrayList<>();

        while (iterator.hasNext()) {
            Arg arg = iterator.next();
            if ("TMP".equals(arg.getType())) {
                int begin = arg.getBegin();
                int end = arg.getEnd();
                for (int i = begin; i <= end; i++) {
                    time.append(words.get(i).getCont());
                }
            }
            if ("LOC".equals(arg.getType())) {
                int begin = arg.getBegin();
                int end = arg.getEnd();
                for (int i = begin; i <= end; i++) {
                    position.append(words.get(i).getCont());
                }
            }
            if ("A1".equals(arg.getType())) {
                int begin = arg.getBegin();
                int end = arg.getEnd();
                for (int i = begin;i<=end; i++){
                    a1.add(i);
                }
            }

            if ("A0".equals(arg.getType())) {
                int begin = arg.getBegin();
                int end = arg.getEnd();
                for (int i = begin; i <= end; i++) {
//                    name.append(words.get(i).getCont());
                    a0.add(i);
                    nameList = findEntityNameList(a0,verb.getId());
                }
            }
        }

        String s = transTimeStr(time.toString());
        time.setLength(0);
        if ("Not Found".equals(s)) {
            String timeInSem = findTimeInSem(verb.getId());
            if (!"No timeTag".equals(timeInSem)) {
                time.append(timeInSem);
            }
        } else {
            time.append(s);
        }

        a1.add(verb.getId());
        Collections.sort(a1);
        StringBuffer des = new StringBuffer();
        for (int i = 0;i<a1.size();i++){
            des.append(words.get(a1.get(i)).getCont());
        }

        if (time.length()>0){
            for (String entityName: nameList){
                DetailedInfo detailedInfo = new DetailedInfo();
                detailedInfo.setLocation(position.toString());
                detailedInfo.setDescription(des.toString());
                Timenode timenode = new Timenode();
                timenode.setTimePoint(time.toString());
                timenode.setInfo(detailedInfo);
                nodeMap.put(entityName,timenode);
            }
        }

        if(nodeMap.size()>0){
            Iterator<Map.Entry<String, Timenode>> nodeIterator = nodeMap.entrySet().iterator();
            while (nodeIterator.hasNext()){
                Map.Entry<String, Timenode> node = nodeIterator.next();
                System.out.println(node.getKey()+node.getValue());
            }


        }

        return nodeMap;
    }

    private ArrayList<String> findEntityNameList(ArrayList<Integer> a0, int id) {
        ArrayList<String> nameList = new ArrayList<>();
        for (int i :a0){
            Word sbvWord = words.get(i);
            if (("SBV".equals(sbvWord.getRelate()))&&(id == sbvWord.getParent())) {
                nameList.add(sbvWord.getCont());
                for (int j :a0){
                    Word cooWord = words.get(j);
                    if (("COO".equals(cooWord.getRelate()))&&(sbvWord.getId() == cooWord.getParent())){
                        nameList.add(cooWord.getCont());
                    }
                }
            }
        }
        return  nameList;
    }


    private String findTimeInSem(int verbId) throws ParseException {
        StringBuilder timeInSem = new StringBuilder();
        for (Word word :words.values()){
            if ((word.getSemparent() == verbId)&&("Time".equals(word.getSemrelate()))){
                ArrayList<Integer> tmodList = new ArrayList<>();
                tmodList.add(word.getId());
                findTmodForParent(word.getId(),tmodList);
                Collections.sort(tmodList);
                for (int i : tmodList){
                    timeInSem.append(words.get(i).getCont());
                }
            }
        }
        String transTimeStr = transTimeStr(timeInSem.toString());
        if ("Not Found".equals(transTimeStr)){
            return "No timeTag";
        }
        return transTimeStr;
    }

    private void findTmodForParent(int timeId,List tmodList) {
        for (Word word :words.values()){
            if ((word.getSemparent() == timeId)&&("Tmod".equals(word.getSemrelate()))){
                tmodList.add(word.getId());
                findTmodForParent(word.getId(),tmodList);
            }
        }
    }


    public String transTimeStr(String timeStr) throws ParseException {
        String regex1 = "\\d{1,4}[-|/|年]\\d{1,2}[-|/|月]\\d{1,2}[-|/|日]";
        String regex2 = "\\d{1,4}[-|/|年]\\d{1,2}[-|/|月]";
        String regex3 = "\\d{1,4}[-|/|年]";
        StringBuilder time = new StringBuilder();
        Matcher matcher1 = Pattern.compile(regex1).matcher(timeStr);
        if (matcher1.find()) {
            String s = matcher1.group();
            Date d1 = new SimpleDateFormat("yyyy年MM月dd日").parse(s);//定义起始日期
            SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy");
            SimpleDateFormat sdf1 = new SimpleDateFormat("MM");
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd");
            time.append(sdf0.format(d1)).append("-").append(sdf1.format(d1)).append("-").append(sdf2.format(d1));
        } else {
            Matcher matcher2 = Pattern.compile(regex2).matcher(timeStr);
            if (matcher2.find()) {
                String s = matcher2.group();
                Date d1 = new SimpleDateFormat("yyyy年MM月").parse(s);//定义起始日期
                SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy");
                SimpleDateFormat sdf1 = new SimpleDateFormat("MM");
                time.append(sdf0.format(d1)).append("-").append(sdf1.format(d1));
            } else {
                Matcher matcher3 = Pattern.compile(regex3).matcher(timeStr);
                if (matcher3.find()) {
                    String s = matcher3.group();
                    Date d1 = new SimpleDateFormat("yyyy年").parse(s);//定义起始日期
                    SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy");
                    time.append(sdf0.format(d1));
                } else {
                    time.append("Not Found");
                }
            }
        }
        return time.toString();
    }




    public static void main(String[] args) throws ParseException {
        SyntaxResult syntaxResult = new SyntaxResult();
        String[] strings = {"2018年5月4日", "2018年5月", "2018年", "DDDD"};
        for (String s : strings) {
            System.out.println(syntaxResult.transTimeStr(s));
        }
    }


}
