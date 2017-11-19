package com.tfl.billing;

import com.oyster.OysterCardReader;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class MockTest {
    private Mockery context = new JUnit4Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};

    OysterCardReader oysterCardReader = context.mock(OysterCardReader.class);
    TravelTracker travelTracker = new TravelTracker();

    @Test
    public void registeringOysterCardCallsOysterCardReader() {
        context.checking(new Expectations() {{
            exactly(1).of(oysterCardReader).register(travelTracker);
        }});
        travelTracker.connect(oysterCardReader);
    }

}