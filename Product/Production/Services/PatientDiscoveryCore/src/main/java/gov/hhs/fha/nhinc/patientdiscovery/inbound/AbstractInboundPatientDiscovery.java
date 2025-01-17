/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.patientdiscovery.inbound;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryException;
import gov.hhs.fha.nhinc.patientdiscovery.audit.PatientDiscoveryAuditLogger;
import java.util.Properties;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;

public abstract class AbstractInboundPatientDiscovery implements InboundPatientDiscovery {

    abstract PRPAIN201306UV02 process(PRPAIN201305UV02 body, AssertionType assertion, Properties webContextProperties) 
        throws PatientDiscoveryException;

    abstract PatientDiscoveryAuditLogger getAuditLogger();

    /**
     * Method that processes the Patient Discovery request
     *
     * @param body the body of the PD request
     * @param assertion the assertion of the PD request
     * @param webContextProperties
     * @return PRPAIN201306UV02 response message
     * @throws gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryException
     */
    @Override
    public PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(PRPAIN201305UV02 body, AssertionType assertion,
        Properties webContextProperties) throws PatientDiscoveryException {

        PRPAIN201306UV02 response = process(body, assertion, webContextProperties);
        auditResponseToNhin(body, response, assertion, webContextProperties);
        return response;
    }

    protected void auditResponseToNhin(PRPAIN201305UV02 request, PRPAIN201306UV02 response, AssertionType assertion,
        Properties webContextProperties) {

        getAuditLogger().auditResponseMessage(request, response, assertion, null, 
            NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, Boolean.FALSE, 
            webContextProperties, NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME); 
    }
}
