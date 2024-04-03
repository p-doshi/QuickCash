package dal.cs.quickcash3.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static dal.cs.quickcash3.test.ExampleJobList.GOOGLEPLEX;
import static dal.cs.quickcash3.test.ExampleJobList.JOBS;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.util.Range;

public class SearchFiltersTest {
    @Test
    public void jobDuration() {
        NumericRangeSearchFilter<AvailableJob> filter = new NumericRangeSearchFilter<>("duration");
        filter.setRange(new Range<>(24.0, Double.POSITIVE_INFINITY));

        List<AvailableJob> passed = new ArrayList<>();
        for (AvailableJob job : JOBS.values()) {
            if (filter.isValid(job)) {
                passed.add(job);
            }
        }

        List<String> expectedJobTitles = Collections.singletonList(
            "Landscaping"
        );

        assertEquals(expectedJobTitles.size(), passed.size());
        for (AvailableJob job : passed) {
            assertTrue(expectedJobTitles.contains(job.getTitle()));
        }
    }

    @Test
    public void locationBased() {
        LocationSearchFilter<AvailableJob> filter =
            new LocationSearchFilter<>("latitude", "longitude");
        filter.setLocation(GOOGLEPLEX);
        filter.setMaxDistance(100.0);

        List<AvailableJob> passed = new ArrayList<>();
        for (AvailableJob job : JOBS.values()) {
            if (filter.isValid(job)) {
                passed.add(job);
            }
        }

        List<String> expectedJobTitles = Collections.singletonList(
            "Coding problem"
        );

        assertEquals(expectedJobTitles.size(), passed.size());
        for (AvailableJob job : passed) {
            assertTrue(expectedJobTitles.contains(job.getTitle()));
        }
    }

    @Test
    public void durationBasedChained() {
        LocationSearchFilter<AvailableJob> locationFilter =
            new LocationSearchFilter<>("latitude", "longitude");
        locationFilter.setLocation(GOOGLEPLEX);
        locationFilter.setMaxDistance(300000);

        NumericRangeSearchFilter<AvailableJob> salaryRangeFilter =
            new NumericRangeSearchFilter<>("salary");
        salaryRangeFilter.setRange(new Range<>(0.0, Double.POSITIVE_INFINITY));

        NumericRangeSearchFilter<AvailableJob> durationFilter =
            new NumericRangeSearchFilter<>("duration");
        durationFilter.setRange(new Range<>(24.0, Double.POSITIVE_INFINITY));

        locationFilter.addNext(salaryRangeFilter).addNext(durationFilter);

        List<AvailableJob> passed = new ArrayList<>();
        for (AvailableJob job : JOBS.values()) {
            if (locationFilter.isValid(job)) {
                passed.add(job);
            }
        }

        List<String> expectedJobTitles = Collections.singletonList(
            "Landscaping"
        );

        assertEquals(expectedJobTitles.size(), passed.size());
        for (AvailableJob job : passed) {
            assertTrue(expectedJobTitles.contains(job.getTitle()));
        }
    }
}
