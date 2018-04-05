package com.epam.training.app;

import com.epam.training.content.URLSource;

import java.util.concurrent.TimeoutException;

public class FrameworkApp {

    public static void main(String[] args){

        URLSource urlSource = new URLSource();
        try {
            urlSource.open("http://www.thebeautyoftravel.com/25-most-fascinating-castles-in-europe/2/", 5);
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        urlSource.checkLinkPresentByHref("http://www.thebeautyoftravel.com/tag/castles/");

        try {
            urlSource.readFromFile("src/test/resources/instructions.txt");
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        urlSource.checkPageTitle("25 Fascinating Castles In Europe That Will Surely Take Your Breath Away");

    }
}
