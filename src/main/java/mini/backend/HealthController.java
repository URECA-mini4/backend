package mini.backend;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Health Check API", description = "health check api 입니다.")
@RestController
@RequestMapping("/api/v1")
public class HealthController {

    @Tag(name = "Health Check API")
    @Operation(summary = "Health Check", description = "Ping And Pong")
    @GetMapping("/health")
    public String checkHealthWithPing() {
        return "Pong";
    }
}
