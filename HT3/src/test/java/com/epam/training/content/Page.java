package com.epam.training.content;

import java.net.URL;
import java.util.ArrayList;

public class Page {

    protected URL url;
    protected StringBuilder content;
    protected ArrayList<String> linksByHref;
    protected ArrayList<String> linksByName;
    protected String title;
    protected int timesTextIsPresent;

    public Page() {

        content = new StringBuilder();
        title = "";
        timesTextIsPresent = 0;
    }

    //ищет текст по заданному атрибуту "by"
    public void findTextByAttribute(String text, String by, String firstChars, String lastChars, String subcontent, ArrayList list){

        int start = subcontent.indexOf(firstChars, 0);
        int end = subcontent.indexOf(lastChars, start);

        if (start<=0 || end<=0){
            return;
        }

        String whereToFind = subcontent.substring(start, end);

        if (whereToFind.contains(by+"=\""+text+"\"")){
            list.add(whereToFind);
        }

        subcontent = subcontent.substring(end, subcontent.length());

        findTextByAttribute(text, by, firstChars, lastChars, subcontent, list);

    }

    //ищет title и проверяет на совпадение
    public void findTitleText(String text, String firstChars, String lastChars, String subcontent){

        //да, это не очень красиво, но по-другому не получилось(
        int start = subcontent.indexOf(firstChars, 0)+7;
        int end = subcontent.indexOf(lastChars, start);

        if (start<=0 || end<=0){
            return;
        }

        String whereToFind = subcontent.substring(start, end);

        if (whereToFind.equals(text)){
            title = text;
        }

    }

    //ищет присутствие заданного текста во всём документе
    public void findText(String text, String firstChars, String lastChars, String subcontent){

        int start = subcontent.indexOf(firstChars, 0);
        int end = subcontent.indexOf(lastChars, start);

        if (start<=0 || end<=0){
            return;
        }

        String whereToFind = subcontent.substring(start, end);

        if (whereToFind.contains(text)){
            timesTextIsPresent++;
        }

        subcontent = subcontent.substring(end, subcontent.length());

        findText(text, firstChars, lastChars, subcontent);

    }


}
