package com.epam.training;

import com.epam.training.content.URLSource;
import com.sun.source.tree.AssertTree;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Date;
import java.util.concurrent.TimeoutException;

public class SampleTest {

    private URLSource urlSource;
    private double time;

    @BeforeClass()
    public void setUp() throws TimeoutException {
        urlSource = new URLSource();
        urlSource.open("http://www.thebeautyoftravel.com/25-most-fascinating-castles-in-europe/2/", 5);
        time = System.currentTimeMillis();
        urlSource.getLogger().info("Start testing...");
    }

    @Test()
    public void checkLinkByHrefTest() {
        Assert.assertTrue(urlSource.checkLinkPresentByHref("http://www.thebeautyoftravel.com/tag/castles/"));
    }

    @Test()
    public void checkLinkByNameTest() {
        Assert.assertTrue(urlSource.checkLinkPresentByName("castle"));
    }

    @Test()
    public void checkPageTitleTest() {
        Assert.assertTrue(urlSource.checkPageTitle("25 Fascinating Castles In Europe That Will Surely Take Your Breath Away"));
    }

    @Test()
    public void checkPageContainsTest() {
        Assert.assertTrue(urlSource.checkPageContains("It was built in 1800 and it attracts thousands of visitors"));
    }

    @Test(expectedExceptions = TimeoutException.class)
    public void timeTest() throws TimeoutException {
        Assert.assertTrue(urlSource.open("http://www.thebeautyoftravel.com/25-most-fascinating-castles-in-europe/2/", (long) 0.001));
    }


    @AfterClass()
    public void setDown()
    {
        urlSource.getLogger().info("Total tests: "+(urlSource.getFailedTests()+urlSource.getPassedTests()));
        urlSource.getLogger().info("Passed/Failed: "+urlSource.getPassedTests()+"/"+urlSource.getFailedTests());
        urlSource.getLogger().info("Finish testing...");
        urlSource.getLogger().info("Total time: "+(((double)System.currentTimeMillis()-time)/1000));
    }
}
