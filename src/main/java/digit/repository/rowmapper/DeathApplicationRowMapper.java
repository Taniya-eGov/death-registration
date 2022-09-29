package digit.repository.rowmapper;

import digit.web.models.*;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class DeathApplicationRowMapper implements ResultSetExtractor<List<DeathRegistrationApplication>> {
    public List<DeathRegistrationApplication> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<String,DeathRegistrationApplication> deathRegistrationApplicationMap = new LinkedHashMap<>();

        while (rs.next()){
            String uuid = rs.getString("applicationNumber");
            DeathRegistrationApplication deathRegistrationApplication = deathRegistrationApplicationMap.get(uuid);

            if(deathRegistrationApplication == null) {

                Long lastModifiedTime = rs.getLong("lastModifiedTime");
                if (rs.wasNull()) {
                    lastModifiedTime = null;
                }

                 Applicant applicant = Applicant.builder().id(Long.valueOf(rs.getString("applicantId"))).build();

                AuditDetails auditdetails = AuditDetails.builder()
                        .createdBy(rs.getString("createdBy"))
                        .createdTime(rs.getLong("createdTime"))
                        .lastModifiedBy(rs.getString("lastModifiedBy"))
                        .lastModifiedTime(lastModifiedTime)
                        .build();

                deathRegistrationApplication = DeathRegistrationApplication.builder()
                        .applicationNumber(rs.getString("applicationNumber"))
                        .tenantId(rs.getString("tenantId"))
                        .id(rs.getString("id"))
                        .applicantId(rs.getString("applicantId"))
                        .deceasedFirstName(rs.getString("deceasedFirstName"))
                        .deceasedLastName(rs.getString("deceasedLastName"))
                        .placeOfDeath(rs.getString("placeOfDeath"))
                        .timeOfDeath(rs.getInt("timeOfDeath"))
                        .auditDetails(auditdetails)
                        .build();
            }
            addChildrenToProperty(rs, deathRegistrationApplication);
            deathRegistrationApplicationMap.put(uuid, deathRegistrationApplication);
        }
        return new ArrayList<>(deathRegistrationApplicationMap.values());
    }

    private void addChildrenToProperty(ResultSet rs, DeathRegistrationApplication deathRegistrationApplication)
            throws SQLException {
        addAddressToApplication(rs, deathRegistrationApplication);
    }

    private void addAddressToApplication(ResultSet rs, DeathRegistrationApplication deathRegistrationApplication) throws SQLException {
        Address address = Address.builder()
//                .id(rs.getString("aid"))
                .tenantId(rs.getString("atenantId"))
                .doorNo(rs.getString("adoorNo"))
                .latitude(rs.getDouble("alatitude"))
                .longitude(rs.getDouble("alongitude"))
                .buildingName(rs.getString("abuildingName"))
                .addressId(rs.getString("aaddressId"))
                .addressNumber(rs.getString("aaddressNumber"))
                .type(rs.getString("atype"))
                .addressLine1(rs.getString("aaddressLine1"))
                .addressLine2(rs.getString("aaddressLine2"))
                .landmark(rs.getString("alandmark"))
                .street(rs.getString("astreet"))
                .city(rs.getString("acity"))
                .pincode(rs.getString("apincode"))
                .detail("adetail")
                .registrationId("aregistrationId")
                .build();

        deathRegistrationApplication.setAddress(address);

    }

}
