package org.ga4gh.testbed.api.utils.DTO;

/**
 * @author dashrath
 */
public class ReportCountDTO {
    private String testbedName;
    private int totalReports;

    public ReportCountDTO() {}

    public ReportCountDTO(String testbed, int totalReports) {
        this.testbedName = testbed;
        this.totalReports = totalReports;
    }

    public String getTestbedName() {
        return testbedName;
    }

    public void setTestbedName(String testbedName) {
        this.testbedName = testbedName;
    }

    public int getTotalReports() {
        return totalReports;
    }

    public void setTotalReports(int totalReports) {
        this.totalReports = totalReports;
    }
}
