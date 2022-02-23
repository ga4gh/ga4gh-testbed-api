package org.ga4gh.testbed.api.utils.requesthandler.report;

import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.ga4gh.starterkit.common.exception.ResourceNotFoundException;
import org.ga4gh.starterkit.common.requesthandler.BasicCreateRequestHandler;
import org.ga4gh.testbed.api.exception.UnauthorizedException;
import org.ga4gh.testbed.api.model.Report;
import org.ga4gh.testbed.api.model.ReportSeries;
import org.ga4gh.testbed.api.utils.hibernate.TestbedApiHibernateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateReportHandler extends BasicCreateRequestHandler<String, Report> {

    private String reportSeriesId;
    private String reportSeriesToken;
    private Report report;

    @Autowired
    TestbedApiHibernateUtil hibernateUtil;

    public CreateReportHandler() {
        super(Report.class);
    }

    public CreateReportHandler prepare(String reportSeriesId, String reportSeriesToken, Report report) {
        setReportSeriesId(reportSeriesId);
        setReportSeriesToken(reportSeriesToken);
        setReport(report);
        return this;
    }

    @Override
    public Report handleRequest() {
        // Get the report series associated with this report
        ReportSeries reportSeries = hibernateUtil.readEntityObject(ReportSeries.class, reportSeriesId, false);
        if (reportSeries == null) {
            throw new ResourceNotFoundException("No ReportSeries by id: " + reportSeriesId);
        }
        report.setReportSeries(reportSeries);

        // calculate the input token hash based on input token and salt
        // compare the input token hash to the report series's token hash in db
        String tokenSaltPlaintextAttempt = reportSeriesToken + reportSeries.getTokenSalt();
        String hashAttempt = DigestUtils.sha256Hex(tokenSaltPlaintextAttempt);
        if (! hashAttempt.equals(reportSeries.getTokenHash())) {
            throw new UnauthorizedException("Cannot add Report to ReportSeries: invalid token");
        }

        // assign a random UUIDv4 to the report
        report.setId(UUID.randomUUID().toString());
        
        // create object
        hibernateUtil.createEntityObject(Report.class, report);
        return hibernateUtil.readFullReport(report.getId());
    }
}
