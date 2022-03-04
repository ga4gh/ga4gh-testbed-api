package org.ga4gh.testbed.api.utils;

import org.testng.annotations.Test;

public class SerializeViewTest {

    @Test
    public void testSerializeView() {
        new SerializeView();
        new SerializeView.Always();
        new SerializeView.Never();
        new SerializeView.SpecificationSimple();
        new SerializeView.SpecificationFull();
        new SerializeView.TestbedSimple();
        new SerializeView.TestbedFull();
        new SerializeView.PlatformSimple();
        new SerializeView.PlatformFull();
        new SerializeView.OrganizationSimple();
        new SerializeView.OrganizationFull();
        new SerializeView.ReportSeriesSimple();
        new SerializeView.ReportSeriesFull();
        new SerializeView.ReportSimple();
        new SerializeView.ReportFull();
    }
}
