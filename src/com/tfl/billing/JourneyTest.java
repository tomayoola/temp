package com.tfl.billing;

import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class JourneyTest {
    @Test
    public void getsCorrectOriginReaderID() {
        UUID cardID = UUID.fromString("123e4567-e89b-12d3-a456-426655440001");
        UUID originID = UUID.fromString("123e4567-e89b-12d3-a456-426655440000");
        UUID destID = UUID.fromString("123e4567-e89b-12d3-a456-426655440002");

        Journey journey = new Journey(new JourneyStart(cardID, originID),
                new JourneyEnd(cardID,destID));
        assertThat(journey.originId(), is(originID));
    }

    @Test
    public void getsCorrectDestinationReaderID() {
        UUID cardID = UUID.fromString("123e4567-e89b-12d3-a456-426655440001");
        UUID originID = UUID.fromString("123e4567-e89b-12d3-a456-426655440000");
        UUID destID = UUID.fromString("123e4567-e89b-12d3-a456-426655440002");

        Journey journey = new Journey(new JourneyStart(cardID, originID),
                new JourneyEnd(cardID,destID));
        assertThat(journey.destinationId(), is(destID));
    }

    @Test
    public void startTimeIsBeforeEndTime() {
        UUID cardID = UUID.fromString("123e4567-e89b-12d3-a456-426655440001");
        UUID originID = UUID.fromString("123e4567-e89b-12d3-a456-426655440000");
        UUID destID = UUID.fromString("123e4567-e89b-12d3-a456-426655440002");

        JourneyStart journeyStart = new JourneyStart(cardID, originID);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JourneyEnd journeyEnd = new JourneyEnd(cardID, destID);

        Journey journey = new Journey(journeyStart,
                journeyEnd);
        assertTrue(journey.startTime().before(journey.endTime()));
    }

}
