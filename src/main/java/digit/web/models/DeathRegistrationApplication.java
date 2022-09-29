package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 * A Object holds the basic data for a Death Registration Application
 */
@ApiModel(description = "A Object holds the basic data for a Death Registration Application")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2022-09-22T13:20:01.205+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeathRegistrationApplication   {
        @JsonProperty("id")
        private String id = null;

        @JsonProperty("tenantId")
        private String tenantId = null;

        @JsonProperty("applicationNumber")
        private String applicationNumber = null;

        @JsonProperty("deceasedFirstName")
        private String deceasedFirstName = null;

        @JsonProperty("deceasedLastName")
        private String deceasedLastName = null;

        @JsonProperty("applicantMobileNumber")
        private String applicantMobileNumber = null;

        @JsonProperty("placeOfDeath")
        private String placeOfDeath = null;

        @JsonProperty("timeOfDeath")
        private Integer timeOfDeath = null;

        @JsonProperty("addressOfDeceased")
        private Address addressOfDeceased = null;

        @JsonProperty("applicant")
        private Applicant applicant = null;

        @JsonProperty("auditDetails")
        private AuditDetails auditDetails = null;

        @Valid
        @JsonProperty("workflow")
        private Workflow workflow = null;


}

