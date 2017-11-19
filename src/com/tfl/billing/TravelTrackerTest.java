package com.tfl.billing;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.external.Customer;
import com.tfl.external.CustomerDatabase;
import com.tfl.underground.OysterReaderLocator;
import com.tfl.underground.Station;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.UUID;

import static org.junit.Assert.assertTrue;


public class TravelTrackerTest {

    private Mockery context1 = new Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};


    OysterCardReader oysterCardReader = context1.mock(OysterCardReader.class);

    @Test
    public void registeringOysterCardCallsOysterCardReader() {
        TravelTracker travelTracker = new TravelTracker();
        context1.checking(new Expectations() {{
            exactly(1).of(oysterCardReader).register(travelTracker);
            exactly(1).of(oysterCardReader).id();
            exactly(10).of(oysterCardReader).touch(new OysterCard());
        }});

        travelTracker.connect(oysterCardReader);


    }

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void cleanUpStreams() {
        System.setOut(null);
        System.setErr(null);
    }

    @Test
    public void getsCorrectPriceOf320ForOnePeakTravel() {
       // UUID cardID = CustomerDatabase.getInstance().getCustomers().get(0).cardId(); Fred Bloggs
        UUID originID = OysterReaderLocator.atStation(Station.PADDINGTON).id();
        UUID destID = OysterReaderLocator.atStation(Station.ALDGATE).id();
        String startTime = "2017/01/01 07:00:00";
        String endTime = "2017/01/01 07:30:00";

        CustomerDatabase.getInstance().getCustomers().clear();
        OysterCard oysterCard = new OysterCard();
        CustomerDatabase.getInstance().getCustomers().add(new Customer("Tom", oysterCard));

        TravelTracker travelTracker = new TravelTracker();
        travelTracker.cardScanned(oysterCard.id(), originID, startTime);
        travelTracker.cardScanned(oysterCard.id(), destID, endTime);

        travelTracker.chargeAccounts();
        assertTrue(outContent.toString().contains("3.20"));
    }

    @Test
    public void getsCorrectPriceOf240ForOneOffPeakTravel() {
        // UUID cardID = CustomerDatabase.getInstance().getCustomers().get(0).cardId(); Fred Bloggs
        UUID originID = OysterReaderLocator.atStation(Station.PADDINGTON).id();
        UUID destID = OysterReaderLocator.atStation(Station.ALDGATE).id();
        String startTime = "2017/01/01 12:00:00";
        String endTime = "2017/01/01 12:30:00";

        CustomerDatabase.getInstance().getCustomers().clear();
        OysterCard oysterCard = new OysterCard();
        CustomerDatabase.getInstance().getCustomers().add(new Customer("Tom", oysterCard));

        TravelTracker travelTracker = new TravelTracker();
        travelTracker.cardScanned(oysterCard.id(), originID, startTime);
        travelTracker.cardScanned(oysterCard.id(), destID, endTime);

        travelTracker.chargeAccounts();
        assertTrue(outContent.toString().contains("2.40"));
    }


}
