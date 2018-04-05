package com.epam.training.content;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class URLSource extends Page{

    private String[] fileInstructions;
    private static int passedTests;
    private static int failedTests;
    private final Logger logger = LogManager.getRootLogger();

    public Logger getLogger() {
        return logger;
    }

    public int getPassedTests() {
        return passedTests;
    }

    public int getFailedTests() {
        return failedTests;
    }

    public URLSource() {

        super();
        passedTests = 0;
        failedTests = 0;
    }

    public boolean open(String urlString, long timeout) throws TimeoutException {

        long endTime = System.currentTimeMillis() + timeout*1000;
        try
        {
            url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection connection = null;
            if(urlConnection instanceof HttpURLConnection)
            {
                connection = (HttpURLConnection) urlConnection;
            }
            else
            {
                System.out.println("Please enter an HTTP URL.");
                return false;
            }
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            String current;
            while((current = in.readLine()) != null)
            {
                content.append(current);
                content.append(System.lineSeparator());
            }
        }catch(IOException e)
        {
            failedTests++;
            logger.info("! [open \""+urlString+"\" \""+timeout+"\"] - IOException (reading from url)");
            return false;
        }
        if (System.currentTimeMillis() < endTime) {
            passedTests++;
            logger.info("+ [open \""+urlString+"\" \""+timeout+"\"]");
            return true;
        }else {
            passedTests++;
            logger.info("+ [open \""+urlString+"\" \""+timeout+"\"] - TimeoutException");
            throw new TimeoutException();
        }
    }

    public boolean checkLinkPresentByHref(String href){

        linksByHref = new ArrayList<>();

        findTextByAttribute(href,"href", "<a", "</a>", String.valueOf(content), linksByHref);

        if (linksByHref.size() == 0){
            failedTests++;
            logger.info("! [checkLinkPresentByHref \""+href+"\"]");
            return false;
        }
        passedTests++;
        logger.info("+ [checkLinkPresentByHref \""+href+"\"]");
        return true;


    }

    public boolean checkLinkPresentByName(String linkname){

        linksByName = new ArrayList<>();

        findTextByAttribute(linkname,"name", "<a", "</a>", String.valueOf(content), linksByName);

        if (linksByName.size() == 0){
            failedTests++;
            logger.info("! [checkLinkPresentByName \""+linkname+"\"]");
            return false;
        }
        passedTests++;
        logger.info("+ [checkLinkPresentByName \""+linkname+"\"]");
        return true;

    }

    public boolean checkPageTitle(String text){

        linksByName = new ArrayList<>();

        findTitleText(text, "<title>", "</title>", String.valueOf(content));

        if (title.equals("")){
            failedTests++;
            logger.info("! [checkPageTitle \""+text+"\"]");
            return false;
        }
        passedTests++;
        logger.info("+ [checkPageTitle \""+text+"\"]");
        return true;

    }

    public boolean checkPageContains(String text){

        timesTextIsPresent = 0;

        findText(text, ">", "<", String.valueOf(content));

        if (timesTextIsPresent == 0){
            failedTests++;
            logger.info("! [checkPageContains \""+text+"\"]");
            return false;
        }
        passedTests++;
        logger.info("+ [checkPageContains \""+text+"\"]");
        return true;

    }

    public void readFromFile(String path) throws TimeoutException {

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            String currentLine;

            while ((currentLine = br.readLine()) != null) {
                fileInstructions = currentLine.split("\"");
                if (currentLine.contains("open")){
                    open(fileInstructions[1], Long.parseLong(fileInstructions[3]));

                }else if (currentLine.contains("checkLinkPresentByHref")){
                    checkLinkPresentByHref(fileInstructions[1]);

                }else if (currentLine.contains("checkLinkPresentByName")){
                    checkLinkPresentByName(fileInstructions[1]);

                }else if (currentLine.contains("checkPageTitle")){
                    checkPageTitle(fileInstructions[1]);

                }else if (currentLine.contains("checkPageContains")){
                    checkPageContains(fileInstructions[1]);

                }
            }

        } catch (IOException e) {
            logger.info("! ["+fileInstructions[0]+" \""+fileInstructions[1]+"] - IOException (reading from file)");
        }

    }
}
