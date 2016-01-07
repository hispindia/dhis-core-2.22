package org.hisp.dhis.trackedentityattributevalue;

/*
 * Copyright (c) 2004-2016, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import org.hisp.dhis.common.AuditType;
import org.hisp.dhis.external.conf.DhisConfigurationProvider;
import org.hisp.dhis.trackedentity.TrackedEntityAttribute;
import org.hisp.dhis.trackedentity.TrackedEntityInstance;
import org.hisp.dhis.user.CurrentUserService;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;

/**
 * @author Abyot Asalefew
 */
@Transactional
public class DefaultTrackedEntityAttributeValueService
    implements TrackedEntityAttributeValueService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private TrackedEntityAttributeValueStore attributeValueStore;

    public void setAttributeValueStore( TrackedEntityAttributeValueStore attributeValueStore )
    {
        this.attributeValueStore = attributeValueStore;
    }

    @Autowired
    private TrackedEntityAttributeValueAuditService trackedEntityAttributeValueAuditService;

    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private DhisConfigurationProvider dhisConfigurationProvider;

    // -------------------------------------------------------------------------
    // Implementation methods
    // -------------------------------------------------------------------------

    @Override
    public void deleteTrackedEntityAttributeValue( TrackedEntityAttributeValue attributeValue )
    {
        TrackedEntityAttributeValueAudit trackedEntityAttributeValueAudit = new TrackedEntityAttributeValueAudit( attributeValue,
            attributeValue.getValue(), currentUserService.getCurrentUsername(), AuditType.DELETE );

        trackedEntityAttributeValueAuditService.addTrackedEntityAttributeValueAudit( trackedEntityAttributeValueAudit );
        attributeValueStore.delete( attributeValue );
    }

    @Override
    public TrackedEntityAttributeValue getTrackedEntityAttributeValue( TrackedEntityInstance instance,
        TrackedEntityAttribute attribute )
    {
        return attributeValueStore.get( instance, attribute );
    }

    @Override
    public List<TrackedEntityAttributeValue> getTrackedEntityAttributeValues( TrackedEntityInstance instance )
    {
        return attributeValueStore.get( instance );
    }

    @Override
    public List<TrackedEntityAttributeValue> getTrackedEntityAttributeValues( TrackedEntityAttribute attribute )
    {
        return attributeValueStore.get( attribute );
    }

    @Override
    public List<TrackedEntityAttributeValue> getTrackedEntityAttributeValues(
        Collection<TrackedEntityInstance> instances )
    {
        if ( instances != null && instances.size() > 0 )
        {
            return attributeValueStore.get( instances );
        }

        return null;
    }

    @Override
    public void addTrackedEntityAttributeValue( TrackedEntityAttributeValue attributeValue )
    {
        if(attributeValue.getAttribute().isConfidential() && !dhisConfigurationProvider.isEncryptionConfigured().isOk())
        {
            throw new EncryptionOperationNotPossibleException( "Unable to encrypt data. Encryption is not correctly configured." );
        }

        attributeValue.setAutoFields();

        if ( attributeValue.getValue() != null )
        {
            attributeValueStore.saveVoid( attributeValue );
        }
    }

    @Override
    public void updateTrackedEntityAttributeValue( TrackedEntityAttributeValue attributeValue )
    {
        attributeValue.setAutoFields();

        if ( StringUtils.isEmpty( attributeValue.getValue() ) )
        {
            attributeValueStore.delete( attributeValue );
        }
        else
        {
            TrackedEntityAttributeValueAudit trackedEntityAttributeValueAudit = new TrackedEntityAttributeValueAudit( attributeValue,
                attributeValue.getValue(), currentUserService.getCurrentUsername(), AuditType.UPDATE );

            trackedEntityAttributeValueAuditService.addTrackedEntityAttributeValueAudit( trackedEntityAttributeValueAudit );
            attributeValueStore.update( attributeValue );
        }
    }

    @Override
    public List<TrackedEntityAttributeValue> searchTrackedEntityAttributeValue( TrackedEntityAttribute attribute,
        String searchText )
    {
        return attributeValueStore.searchByValue( attribute, searchText );
    }

    @Override
    public void copyTrackedEntityAttributeValues( TrackedEntityInstance source, TrackedEntityInstance destination )
    {
        attributeValueStore.deleteByTrackedEntityInstance( destination );

        for ( TrackedEntityAttributeValue attributeValue : getTrackedEntityAttributeValues( source ) )
        {
            TrackedEntityAttributeValue value = new TrackedEntityAttributeValue(
                attributeValue.getAttribute(), destination, attributeValue.getValue() );

            addTrackedEntityAttributeValue( value );
        }
    }
}
