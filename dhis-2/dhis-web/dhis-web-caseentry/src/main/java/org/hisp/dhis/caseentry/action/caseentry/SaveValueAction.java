/*
 * Copyright (c) 2004-2009, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of the HISP project nor the names of its contributors may
 *   be used to endorse or promote products derived from this software without
 *   specific prior written permission.
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

package org.hisp.dhis.caseentry.action.caseentry;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryService;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.patient.PatientService;
import org.hisp.dhis.patientdatavalue.PatientDataValue;
import org.hisp.dhis.patientdatavalue.PatientDataValueService;
import org.hisp.dhis.program.ProgramInstance;
import org.hisp.dhis.program.ProgramInstanceService;
import org.hisp.dhis.program.ProgramStage;
import org.hisp.dhis.program.ProgramStageInstance;
import org.hisp.dhis.program.ProgramStageInstanceService;
import org.hisp.dhis.program.ProgramStageService;

import com.opensymphony.xwork2.Action;

/**
 * @author Abyot Asalefew Gizaw
 * @version $Id$
 */
public class SaveValueAction
    implements Action
{

    private static final Log LOG = LogFactory.getLog( SaveValueAction.class );

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    private PatientService patientService;

    public void setPatientService( PatientService patientService )
    {
        this.patientService = patientService;
    }

    private ProgramStageService programStageService;

    public void setProgramStageService( ProgramStageService programStageService )
    {
        this.programStageService = programStageService;
    }

    private ProgramInstanceService programInstanceService;

    public void setProgramInstanceService( ProgramInstanceService programInstanceService )
    {
        this.programInstanceService = programInstanceService;
    }

    private ProgramStageInstanceService programStageInstanceService;

    public void setProgramStageInstanceService( ProgramStageInstanceService programStageInstanceService )
    {
        this.programStageInstanceService = programStageInstanceService;
    }

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    private PatientDataValueService patientDataValueService;

    public void setPatientDataValueService( PatientDataValueService patientDataValueService )
    {
        this.patientDataValueService = patientDataValueService;
    }

    private DataElementCategoryService dataElementCategoryService;

    public void setDataElementCategoryService( DataElementCategoryService dataElementCategoryService )
    {
        this.dataElementCategoryService = dataElementCategoryService;
    }

    // -------------------------------------------------------------------------
    // Input/Output
    // -------------------------------------------------------------------------

    private boolean providedByAnotherFacility;

    public void setProvidedByAnotherFacility( boolean providedByAnotherFacility )
    {
        this.providedByAnotherFacility = providedByAnotherFacility;
    }

    private String value;

    public void setValue( String value )
    {
        this.value = value;
    }

    private int dataElementId;

    public void setDataElementId( int dataElementId )
    {
        this.dataElementId = dataElementId;
    }

    public int getDataElementId()
    {
        return dataElementId;
    }

    private int statusCode;

    public int getStatusCode()
    {
        return statusCode;
    }

    private int optionComboId;

    public int getOptionComboId()
    {
        return optionComboId;
    }

    public void setOptionComboId( int optionComboId )
    {
        this.optionComboId = optionComboId;
    }

    private Integer orgunitId;

    public void setOrgunitId( Integer orgunitId )
    {
        this.orgunitId = orgunitId;
    }

    private Integer patientId;

    public void setPatientId( Integer patientId )
    {
        this.patientId = patientId;
    }

    private Integer programStageId;

    public void setProgramStageId( Integer programStageId )
    {
        this.programStageId = programStageId;
    }

    // -------------------------------------------------------------------------
    // Implementation Action
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        // ---------------------------------------------------------------------
        // Get program-stage-instance
        // ---------------------------------------------------------------------

        OrganisationUnit organisationUnit = organisationUnitService.getOrganisationUnit( orgunitId );

        Patient patient = patientService.getPatient( patientId );

        ProgramStage programStage = programStageService.getProgramStage( programStageId );

        Collection<ProgramInstance> progamInstances = programInstanceService.getProgramInstances( patient, programStage
            .getProgram(), false );

        ProgramInstance programInstance = progamInstances.iterator().next();

        ProgramStageInstance programStageInstance = programStageInstanceService.getProgramStageInstance(
            programInstance, programStage );
        
        // ---------------------------------------------------------------------
        // Save value
        // ---------------------------------------------------------------------

        DataElement dataElement = dataElementService.getDataElement( dataElementId );

        if ( value != null && value.trim().length() == 0 )
        {
            value = null;
        }

        if ( value != null )
        {
            value = value.trim();
        }

        DataElementCategoryOptionCombo optionCombo = null;

        if ( optionComboId == 0 )
        {
            optionCombo = dataElement.getCategoryCombo().getOptionCombos().iterator().next();
        }
        else
        {
            Set<DataElementCategoryOptionCombo> options = dataElement.getCategoryCombo().getOptionCombos();
            if ( options != null )
            {
                for ( DataElementCategoryOptionCombo tmpOption : options )
                {
                    if ( tmpOption.getId() == optionComboId )
                    {
                        optionCombo = tmpOption;
                        break;
                    }
                }
            }
        }

        PatientDataValue patientDataValue = patientDataValueService.getPatientDataValue( programStageInstance,
            dataElement, organisationUnit );

        if ( dataElement.getType().equalsIgnoreCase( DataElement.VALUE_TYPE_STRING )
            && dataElement.isMultiDimensional() )
        {
            if ( value != null )
            {
                optionCombo = dataElementCategoryService.getDataElementCategoryOptionCombo( Integer.parseInt( value ) );
            }
        }

        if ( programStageInstance.getExecutionDate() == null )
        {
            programStageInstance.setExecutionDate( new Date() );
            programStageInstanceService.updateProgramStageInstance( programStageInstance );
        }
        
        if ( patientDataValue == null )
        {
            LOG.debug( "Adding PatientDataValue, value added" );

            patientDataValue = new PatientDataValue( programStageInstance, dataElement, optionCombo, organisationUnit,
                new Date(), value, providedByAnotherFacility );

            patientDataValueService.savePatientDataValue( patientDataValue );
        }
        else
        {
            LOG.debug( "Updating PatientDataValue, value added/changed" );

            patientDataValue.setValue( value );
            patientDataValue.setOptionCombo( optionCombo );
            patientDataValue.setProvidedByAnotherFacility( providedByAnotherFacility );
            patientDataValue.setTimestamp( new Date() );

            patientDataValueService.updatePatientDataValue( patientDataValue );
        }

        return SUCCESS;
    }
}
