package jmri.jmris.simpleserver;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the jmri.jmris.simpleserver.SimpleServerManager class 
 *
 * @author Paul Bender
 */
public class SimpleServerManagerTest {

    @Test
    public void testGetInstance() {
        SimpleServerManager a = SimpleServerManager.getInstance();
        assertThat(a).isNotNull();
    }

    @Test
    public void testGetPreferences(){
        SimpleServerManager a = SimpleServerManager.getInstance();
        assertThat(a.getPreferences()).isNotNull().withFailMessage("preferences not created");
    }

    @BeforeEach
    public void setUp() throws Exception {
        jmri.util.JUnitUtil.setUp();
        jmri.util.JUnitUtil.resetProfileManager();
    }

    @AfterEach
    public void tearDown() throws Exception {
        jmri.util.JUnitUtil.tearDown();

    }

}
