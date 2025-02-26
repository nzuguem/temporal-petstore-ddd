package me.nzuguem.petstore.configurations.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.UUID;

@Component
public class RequestIdFilters extends OncePerRequestFilter {

    public static final String REQUEST_ID_MDC_KEY = "X-Request-Id";
    public static final String REQUEST_IP_MDC_KEY = "X-Request-Address";
    public static final String REQUEST_USER_MDC_KEY = "X-Remote-User";
    public static final String REQUEST_HOSTNAME_MDC_KEY = "X-Hostname";

    private static String hostname;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            var requestId = UUID.randomUUID().toString();
            var ipAddress = request.getRemoteAddr();
            var loggedInUser = request.getRemoteUser() != null ? request.getRemoteUser() : "anonymous";
            String hostName = getHostname();

            MDC.put(REQUEST_ID_MDC_KEY, requestId);
            MDC.put(REQUEST_IP_MDC_KEY, ipAddress);
            MDC.put(REQUEST_USER_MDC_KEY, loggedInUser);
            MDC.put(REQUEST_HOSTNAME_MDC_KEY, hostName);

            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

    private static String getHostname() {
        if (hostname != null && !hostname.isEmpty()) {
            return hostname;
        }
        var name = ManagementFactory.getRuntimeMXBean().getName();
        hostname = name.split("@")[1];
        return hostname;
    }
}