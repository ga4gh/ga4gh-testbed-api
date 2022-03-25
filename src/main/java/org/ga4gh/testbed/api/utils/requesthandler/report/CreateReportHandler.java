package org.ga4gh.testbed.api.utils.requesthandler.report;

import java.util.UUID;
import org.apache.commons.codec.digest.DigestUtils;
import org.ga4gh.starterkit.common.exception.ConflictException;
import org.ga4gh.starterkit.common.exception.ResourceNotFoundException;
import org.ga4gh.starterkit.common.hibernate.exception.EntityExistsException;
import org.ga4gh.starterkit.common.requesthandler.BasicCreateRequestHandler;
import org.ga4gh.testbed.api.exception.UnauthorizedException;
import org.ga4gh.testbed.api.model.Report;
import org.ga4gh.testbed.api.model.ReportSeries;
import org.ga4gh.testbed.api.utils.hibernate.TestbedApiHibernateUtil;
import org.ga4gh.testbed.api.utils.secretmanager.AwsSecretManagerUtil;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateReportHandler extends BasicCreateRequestHandler<String, Report> {

    private String reportSeriesId;
    private String reportSeriesToken;
    private Report report;
    // private static final String TESTBED_TOKENS_SECRET_NAME = "ga4gh-testbed-reportseries-tokens";

    @Autowired
    TestbedApiHibernateUtil hibernateUtil;

    @Autowired
    AwsSecretManagerUtil awsSecretManagerUtil;

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

        // first, look up the db for token salt and hash
        String tokenSalt = reportSeries.getTokenSalt();
        String tokenHash = reportSeries.getTokenHash();

        /*
        if (tokenSalt == null || tokenHash == null) {
            // AWS SECRET MANAGER
            // get the token salt and token hash from AWS SM
            HashMap<String, String> tokenMap;
            try  {
                tokenMap = awsSecretManagerUtil.retrieveSecret(TESTBED_TOKENS_SECRET_NAME);
            } catch(Exception ex){
                // TODO: log the original exception message
                throw new ResourceNotFoundException(String.format("Retrieval of token salt and hash failed for report series id: %s",reportSeriesId));
            }
            tokenSalt = tokenMap.get("token-salt-"+reportSeriesId);
            tokenHash = tokenMap.get("token-hash-"+reportSeriesId);
            if (tokenSalt == null || tokenHash == null){
                // TODO: log this
                System.err.println(String.format("token salt or token hash is not available in the secret manager for report series id: %s",reportSeriesId) );
            }
        }
        */

        // calculate the input token hash based on input token and salt
        // compare the input token hash to the report series's token hash in the AWS SM
        String tokenSaltPlaintextAttempt = reportSeriesToken + tokenSalt;
        String hashAttempt = DigestUtils.sha256Hex(tokenSaltPlaintextAttempt);
        if (! hashAttempt.equals(tokenHash)) {
            throw new UnauthorizedException("Cannot add Report to ReportSeries: invalid token");
        }

        // assign a random UUIDv4 to the report
        report.setId(UUID.randomUUID().toString());

        // create object
        try {
            hibernateUtil.createEntityObject(Report.class, report);
        } catch (EntityExistsException ex) {
            throw new ConflictException(ex.getMessage());
        }

        return hibernateUtil.readFullReport(report.getId());
    }
}