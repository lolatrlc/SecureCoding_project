package hr.algebra.booknook.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Set;

@Service
public class SafeUrlValidator {

    private static final Logger log = LoggerFactory.getLogger(SafeUrlValidator.class);

    private static final Set<String> ALLOWED_HOSTS = Set.of(
            "images.unsplash.com",
            "raw.githubusercontent.com"
    );

    public URI validateOrThrow(String rawUrl) {
        if (rawUrl == null || rawUrl.isBlank()) {
            throw new IllegalArgumentException("URL must not be blank");
        }

        URI uri;
        try {
            uri = URI.create(rawUrl).normalize();
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Malformed URL");
        }

        String scheme = uri.getScheme();
        if (scheme == null || !"https".equalsIgnoreCase(scheme)) {
            throw new IllegalArgumentException("Only https URLs are permitted");
        }

        String host = uri.getHost();
        if (host == null || !ALLOWED_HOSTS.contains(host.toLowerCase())) {
            throw new IllegalArgumentException("Host '" + host + "' is not on the allow-list");
        }

        try {
            InetAddress[] addresses = InetAddress.getAllByName(host);
            for (InetAddress addr : addresses) {
                if (addr.isLoopbackAddress()
                        || addr.isSiteLocalAddress()
                        || addr.isLinkLocalAddress()
                        || addr.isAnyLocalAddress()
                        || addr.isMulticastAddress()) {
                    log.warn("Blocked SSRF attempt — host {} resolved to private IP {}",
                            host, addr.getHostAddress());
                    throw new IllegalArgumentException("Host resolves to a private network");
                }
            }
        } catch (UnknownHostException ex) {
            throw new IllegalArgumentException("Could not resolve host");
        }

        return uri;
    }
}