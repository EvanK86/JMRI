package jmri.jmrix.jmriclient.configurexml;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * JMRIClientTurnoutManagerXmlTest.java
 *
 * Description: tests for the JMRIClientTurnoutManagerXml class
 *
 * @author   Paul Bender  Copyright (C) 2016
 */
public class JMRIClientTurnoutManagerXmlTest {

    @Test
    public void testCtor(){
      Assert.assertNotNull("JMRIClientTurnoutManagerXml constructor",new JMRIClientTurnoutManagerXml());
    }

    // The minimal setup for log4J
    @Before
    public void setUp() {
        apps.tests.Log4JFixture.setUp();
    }

    @After
    public void tearDown() {
        apps.tests.Log4JFixture.tearDown();
    }

}

