package org.ga4gh.testbed.api.utils;

public class SerializeView {

    public static class Always {};

    public static class Never {};

    public static class SpecificationSimple extends Always {};

    public static class SpecificationFull extends SpecificationSimple {};

    public static class TestbedSimple extends Always {};

    public static class TestbedFull extends TestbedSimple {};

    public static class PlatformSimple extends Always {};

    public static class PlatformFull extends PlatformSimple {};

    public static class OrganizationSimple extends Always {};

    public static class OrganizationFull extends OrganizationSimple {};
}
