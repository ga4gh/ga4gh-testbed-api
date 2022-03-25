package org.ga4gh.testbed.api.utils.requesthandler.report;

import org.apache.commons.codec.digest.DigestUtils;
import org.ga4gh.starterkit.common.exception.ConflictException;
import org.ga4gh.starterkit.common.exception.ResourceNotFoundException;
import org.ga4gh.starterkit.common.hibernate.exception.EntityDoesntExistException;
import org.ga4gh.starterkit.common.hibernate.exception.EntityExistsException;
import org.ga4gh.starterkit.common.requesthandler.BasicDeleteRequestHandler;
import org.ga4gh.testbed.api.exception.UnauthorizedException;
import org.ga4gh.testbed.api.model.Report;
import org.ga4gh.testbed.api.model.ReportSeries;
import org.ga4gh.testbed.api.utils.hibernate.TestbedApiHibernateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeleteReportHandler extends BasicDeleteRequestHandler<String, Report> {

    private String reportId;
    private String reportSeriesId;
    private String reportSeriesToken;

    @Autowired
    TestbedApiHibernateUtil hibernateUtil;

    public DeleteReportHandler() {
        super(Report.class);
    }

    public DeleteReportHandler prepare(String reportId, String reportSeriesId, String reportSeriesToken) {
        setReportId(reportId);
        setReportSeriesId(reportSeriesId);
        setReportSeriesToken(reportSeriesToken);
        return this;
    }

    @Override
    public Report handleRequest() {
        ReportSeries reportSeries = hibernateUtil.readEntityObject(ReportSeries.class, reportSeriesId, false);
        if (reportSeries == null) {
            throw new ResourceNotFoundException("No ReportSeries by id: " + reportSeriesId);
        }

        String tokenSaltPlaintextAttempt = reportSeriesToken + reportSeries.getTokenSalt();
        String hashAttempt = DigestUtils.sha256Hex(tokenSaltPlaintextAttempt);
        if (! hashAttempt.equals(reportSeries.getTokenHash())) {
            throw new UnauthorizedException("Cannot delete Report: invalid token");
        }

        try {
            hibernateUtil.deleteEntityObject(Report.class, getReportId());
            return hibernateUtil.readEntityObject(Report.class, getReportId(), false);
        } catch (EntityDoesntExistException ex) {
            throw new ConflictException("No report exists at id: " + getReportId());
        } catch (EntityExistsException ex) {
            throw new ConflictException("Could not delete report with id: " + getReportId());
        }
    }
}
