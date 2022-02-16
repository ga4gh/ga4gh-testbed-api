package org.ga4gh.testbed.api.utils;

public class SerializeView {

    public static class Always {};

    public static class Never {};

    public static class TestbedSimple extends Always {};

    public static class TestbedFull extends TestbedSimple {};
    
}
