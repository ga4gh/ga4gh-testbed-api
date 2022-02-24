package org.ga4gh.testbed.api.utils.webserver;

import org.apache.catalina.connector.RequestFacade;
import org.apache.catalina.connector.ResponseFacade;
import org.ga4gh.starterkit.common.util.webserver.AdminEndpointsFilter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

import static org.ga4gh.starterkit.common.constant.StarterKitConstants.ADMIN;

public class TestbedAdminEndpointsFilter extends AdminEndpointsFilter {

    private int adminPort;

    public TestbedAdminEndpointsFilter(int adminPort) {
        super(adminPort);
        this.adminPort = adminPort;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        if (adminPort != 0) {

            // if a request is made to an admin endpoint via a non-admin port, disallow the request
            if (isRequestForAdminEndpoint(servletRequest) && servletRequest.getLocalPort() != adminPort) {
                ((ResponseFacade) servletResponse).setStatus(404);
                servletResponse.getOutputStream().close();
                return;
            }

            // if a request is made to a non-admin endpoint via the admin port, disallow the request
            if (!isRequestForAdminEndpoint(servletRequest) && servletRequest.getLocalPort() == adminPort) {
                ((ResponseFacade) servletResponse).setStatus(404);
                servletResponse.getOutputStream().close();
                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void init(FilterConfig filterConfig) {

    }

    public void destroy() {

    }

    private boolean isRequestForAdminEndpoint(ServletRequest servletRequest) {
        // This method checks if the string "admin" is present in the request URI.
        // If the input servletRequest can not be casted to type "RequestFacade", then skip the check.
        // TODO: handle this appropriately - maybe add extensive logs or a different filter logic
        //  so that we are not hardcoding the return value to false.
        boolean containsAdmin;
        try {
            containsAdmin = ((RequestFacade) servletRequest).getRequestURI().contains(ADMIN);
        } catch(Exception ex){
            System.err.println(ex.getMessage());
            containsAdmin = false;
        }
        return containsAdmin;
    }

}