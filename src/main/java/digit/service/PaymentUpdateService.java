package digit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import digit.repository.DeathRegistrationRepository;
import digit.web.models.*;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class PaymentUpdateService {

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private DeathRegistrationRepository repository;

    public void process(HashMap<String, Object> record) {

        try {

            PaymentRequest paymentRequest = mapper.convertValue(record, PaymentRequest.class);
            RequestInfo requestInfo = paymentRequest.getRequestInfo();

            List<PaymentDetail> paymentDetails = paymentRequest.getPayment().getPaymentDetails();
            String tenantId = paymentRequest.getPayment().getTenantId();

            for (PaymentDetail paymentDetail : paymentDetails) {
                updateWorkflowForDeathRegistrationPayment(requestInfo, tenantId, paymentDetail);
            }
        } catch (Exception e) {
            log.error("KAFKA_PROCESS_ERROR:", e);
        }

    }

    private void updateWorkflowForDeathRegistrationPayment(RequestInfo requestInfo, String tenantId, PaymentDetail paymentDetail) {

        Bill bill  = paymentDetail.getBill();

        DeathApplicationSearchCriteria criteria = DeathApplicationSearchCriteria.builder()
                .applicationNumber(bill.getConsumerCode())
                .tenantId(tenantId)
                .build();

        List<DeathRegistrationApplication> deathRegistrationApplicationList = repository.getApplications(criteria);

        if (CollectionUtils.isEmpty(deathRegistrationApplicationList))
            throw new CustomException("INVALID RECEIPT",
                    "No applications found for the consumerCode " + criteria.getApplicationNumber());

        Role role = Role.builder().code("SYSTEM_PAYMENT").tenantId(tenantId).build();
        requestInfo.getUserInfo().getRoles().add(role);

        deathRegistrationApplicationList.forEach( application -> {

            DeathRegistrationRequest updateRequest = DeathRegistrationRequest.builder().requestInfo((digit.web.models.RequestInfo) requestInfo)
                    .deathRegistrationApplications(Collections.singletonList(application)).build();

            ProcessInstanceRequest wfRequest = workflowService.getProcessInstanceForDeathRegistrationPayment(updateRequest);

            State state = workflowService.callWorkFlow(wfRequest);

        });
    }

}
