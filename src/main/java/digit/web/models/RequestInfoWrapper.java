package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestInfoWrapper {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;
}
