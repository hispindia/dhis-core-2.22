package org.hisp.dhis.reportexcel.datasetcompleted.action;

/*
 * Copyright (c) 2004-2010, University of Oslo
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategory;
import org.hisp.dhis.dataset.CompleteDataSetRegistration;
import org.hisp.dhis.dataset.CompleteDataSetRegistrationService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.comparator.OrganisationUnitNameComparator;
import org.hisp.dhis.ouwt.manager.OrganisationUnitSelectionManager;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.comparator.PeriodComparator;
import org.hisp.dhis.reportexcel.status.DataEntryStatus;

import com.opensymphony.xwork2.Action;

/**
 * @author Tran Thanh Tri
 * @version $Id$
 */

public class ViewCompletedReportByPeriodsAction
    implements Action
{

    // -------------------------------------------
    // Dependency
    // -------------------------------------------

    private PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }

    private OrganisationUnitSelectionManager organisationUnitSelectionManager;

    public void setOrganisationUnitSelectionManager( OrganisationUnitSelectionManager organisationUnitSelectionManager )
    {
        this.organisationUnitSelectionManager = organisationUnitSelectionManager;
    }

    private DataSetService dataSetService;

    public void setDataSetService( DataSetService dataSetService )
    {
        this.dataSetService = dataSetService;
    }

    private CompleteDataSetRegistrationService completeDataSetRegistrationService;

    public void setCompleteDataSetRegistrationService(
        CompleteDataSetRegistrationService completeDataSetRegistrationService )
    {
        this.completeDataSetRegistrationService = completeDataSetRegistrationService;
    }

    public DataValueService dataValueService;

    public void setDataValueService( DataValueService dataValueService )
    {
        this.dataValueService = dataValueService;
    }
    
    // -------------------------------------------
    // Input
    // -------------------------------------------

    private Integer id;

    public void setId( Integer id )
    {
        this.id = id;
    }

    protected Integer[] columns;

    public void setColumns( Integer[] columns )
    {
        this.columns = columns;
    }

    // -------------------------------------------
    // Output
    // -------------------------------------------

    private List<OrganisationUnit> organisationUnits;

    public List<OrganisationUnit> getOrganisationUnits()
    {
        return organisationUnits;
    }

    private List<Period> periods;

    public List<Period> getPeriods()
    {
        return periods;
    }

    private DataSet dataSet;

    public DataSet getDataSet()
    {
        return dataSet;
    }

    private Map<String, Integer> completedValues;

    public Map<String, Integer> getCompletedValues()
    {
        return completedValues;
    }

    @Override
    public String execute()
        throws Exception
    {
        dataSet = dataSetService.getDataSet( id );

        organisationUnits = new ArrayList<OrganisationUnit>( organisationUnitSelectionManager
            .getSelectedOrganisationUnit().getChildren() );

        periods = new ArrayList<Period>();

        Period period = null;

        CompleteDataSetRegistration completeDataSetRegistration = null;

        completedValues = new HashMap<String, Integer>();

        for ( Integer id : columns )
        {
            period = periodService.getPeriod( id );

            for ( OrganisationUnit o : organisationUnits )
            {
                 if ( o.getDataSets().contains( dataSet ) )
                {
                    Collection<DataElement> dataElements = dataSet.getDataElements();
                    Collection<DataValue> values = dataValueService.getDataValues( o, period, dataElements );
                    int count = 0;
                    for(DataElement de : dataElements)
                    {
                        int opCount = 1;
                        for ( DataElementCategory ca : de.getCategoryCombo().getCategories()){
                            opCount *= ca.getCategoryOptions().size();
                        }
                        count += opCount;
                    }
                    int percent = (values.size() * 100 ) / count ;
                    
                    completeDataSetRegistration = completeDataSetRegistrationService.getCompleteDataSetRegistration(
                        dataSet, period, o );

                    if ( completeDataSetRegistration != null )
                    {
                        completedValues.put( o.getId() + ":" + id, percent );
                    }else{
                        percent = (percent == 0 ) ? 900 : ( 1000 * percent ) ;
                        completedValues.put( o.getId() + ":" + id, percent );
                    }
                }
                else
                {
                    completedValues.put( o.getId() + ":" + id, null );
                }
            }

            periods.add( period );

        }

        Collections.sort( periods, new PeriodComparator() );

        Collections.sort( organisationUnits, new OrganisationUnitNameComparator() );

        return SUCCESS;
    }

}
