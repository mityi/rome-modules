/*
 * SlashModuleGeneratorTest.java
 * JUnit based test
 *
 * Created on November 19, 2005, 10:13 PM
 */

package org.rometools.feed.module.yahooweather.io;

import java.io.File;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.junit.Assert;
import org.rometools.feed.module.AbstractTestCase;
import org.rometools.feed.module.yahooweather.YWeatherModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.SyndFeedOutput;

/**
 *
 * @author <a href="mailto:cooper@screaming-penguin.com">Robert "kebernet" Cooper</a>
 */
public class WeatherGeneratorTest extends AbstractTestCase {

    private static final Logger LOG = LoggerFactory.getLogger(WeatherGeneratorTest.class);

    public WeatherGeneratorTest(final String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(WeatherGeneratorTest.class);
    }

    /**
     * Test of generate method, of class com.totsp.xml.syndication.base.io.SlashGenerator.
     */
    public void testGenerate() throws Exception {
        LOG.debug("testGenerate");
        final SyndFeedInput input = new SyndFeedInput();
        final SyndFeedOutput output = new SyndFeedOutput();
        final File testDir = new File(super.getTestFile("xml"));
        final File[] testFiles = testDir.listFiles();
        for (int h = 0; h < testFiles.length; h++) {
            if (!testFiles[h].getName().endsWith(".xml")) {
                continue;
            }
            LOG.debug("processing" + testFiles[h]);
            final SyndFeed feed = input.build(testFiles[h]);
            output.output(feed, new File("target/" + testFiles[h].getName()));
            final SyndFeed feed2 = input.build(new File("target/" + testFiles[h].getName()));
            {
                final YWeatherModule weather = (YWeatherModule) feed.getModule(YWeatherModule.URI);
                final YWeatherModule weather2 = (YWeatherModule) feed2.getModule(YWeatherModule.URI);
                Assert.assertEquals(testFiles[h].getName(), weather, weather2);

            }
            for (int i = 0; i < feed.getEntries().size(); i++) {
                final SyndEntry entry = feed.getEntries().get(i);
                final SyndEntry entry2 = feed2.getEntries().get(i);
                final YWeatherModule weather = (YWeatherModule) entry.getModule(YWeatherModule.URI);
                final YWeatherModule weather2 = (YWeatherModule) entry2.getModule(YWeatherModule.URI);
                Assert.assertEquals(testFiles[h].getName(), weather, weather2);
            }
        }

    }

}