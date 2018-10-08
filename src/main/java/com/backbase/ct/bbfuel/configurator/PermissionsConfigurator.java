package com.backbase.ct.bbfuel.configurator;

import com.backbase.ct.bbfuel.client.accessgroup.AccessGroupIntegrationRestClient;
import com.backbase.ct.bbfuel.client.accessgroup.AccessGroupPresentationRestClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionsConfigurator {

    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionsConfigurator.class);

    private final AccessGroupIntegrationRestClient accessGroupIntegrationRestClient;
    private final AccessGroupPresentationRestClient accessGroupPresentationRestClient;

    public void assignAllFunctionDataGroupsToUserAndServiceAgreement(String externalUserId,
        String internalServiceAgreementId) {
        List<String> functionGroupIds = accessGroupPresentationRestClient
            .retrieveFunctionGroupIdsByServiceAgreement(internalServiceAgreementId);
        List<String> dataGroupIds = accessGroupPresentationRestClient
            .retrieveDataGroupIdsByServiceAgreement(internalServiceAgreementId);

        functionGroupIds.forEach(functionGroupId -> {
            accessGroupIntegrationRestClient.assignPermissions(
                externalUserId,
                internalServiceAgreementId,
                functionGroupId,
                dataGroupIds);

            LOGGER
                .info("Permission assigned for service agreement [{}], user [{}], function group [{}], data groups {}",
                    internalServiceAgreementId, externalUserId, functionGroupId, dataGroupIds);
        });
    }

    public void assignPermissions(String externalUserId, String internalServiceAgreementId, String functionGroupId,
        List<String> dataGroupIds) {
        accessGroupIntegrationRestClient.assignPermissions(
            externalUserId,
            internalServiceAgreementId,
            functionGroupId,
            dataGroupIds);

        LOGGER.info("Permission assigned for service agreement [{}], user [{}], function group [{}], data groups {}",
            internalServiceAgreementId, externalUserId, functionGroupId, dataGroupIds);
    }
}