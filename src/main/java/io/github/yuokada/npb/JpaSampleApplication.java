package io.github.yuokada.npb;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;

@OpenAPIDefinition(
    info = @org.eclipse.microprofile.openapi.annotations.info.Info(
        title = "JPA Sample Application",
        version = "1.0.0",
        license = @org.eclipse.microprofile.openapi.annotations.info.License(
            name = "MIT",
            url = "https://opensource.org/licenses/MIT"
        )
    ),
    servers = {
        @org.eclipse.microprofile.openapi.annotations.servers.Server(
            url = "http://localhost:8080",
            description = "Local server"
        )
    }
)
public class JpaSampleApplication extends Application {

}
