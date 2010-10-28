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
package org.hisp.dhis.dashboard.ga.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataelement.comparator.DataElementGroupNameComparator;
import org.hisp.dhis.dataelement.comparator.DataElementNameComparator;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.period.DailyPeriodType;
import org.hisp.dhis.period.MonthlyPeriodType;
import org.hisp.dhis.period.OnChangePeriodType;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.period.QuarterlyPeriodType;
import org.hisp.dhis.period.SixMonthlyPeriodType;
import org.hisp.dhis.period.TwoYearlyPeriodType;
import org.hisp.dhis.period.WeeklyPeriodType;
import org.hisp.dhis.period.YearlyPeriodType;
import org.hisp.dhis.period.comparator.PeriodComparator;

import com.opensymphony.xwork2.Action;

/**
 * @author Mithilesh Kumar Thakur
 *
 * @version GenerateGraphicalAnalyserDataElementsFormAction.java Oct 20, 2010 4:38:56 PM
 */
public class GenerateGraphicalAnalyserDataElementsFormAction implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    
    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }
    
    private PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }
    
    private OrganisationUnitGroupService organisationUnitGroupService;

    public void setOrganisationUnitGroupService( OrganisationUnitGroupService organisationUnitGroupService )
    {
        this.organisationUnitGroupService = organisationUnitGroupService;
    }
    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------
    
    private List<DataElement> dataElements;
    
    public List<DataElement> getDataElements()
    {
        return dataElements;
    }
    
    private List<DataElementGroup> dataElementGroups;
    
    public List<DataElementGroup> getDataElementGroups()
    {
        return dataElementGroups;
    }
    
    private List<PeriodType> periodTypes;

    public List<PeriodType> getPeriodTypes()
    {
        return periodTypes;
    }
   
    private List<Period> monthlyPeriods;

    public List<Period> getMonthlyPeriods()
    {
        return monthlyPeriods;
    }
    
    private List<String> periodNameList;

    public List<String> getPeriodNameList()
    {
        return periodNameList;
    }
    
    private SimpleDateFormat simpleDateFormat;

    public SimpleDateFormat getSimpleDateFormat()
    {
        return simpleDateFormat;
    }
    
    private String monthlyPeriodTypeName;

    public String getMonthlyPeriodTypeName()
    {
        return monthlyPeriodTypeName;
    }
    
    private String quarterlyPeriodTypeName;

    public String getQuarterlyPeriodTypeName()
    {
        return quarterlyPeriodTypeName;
    }

    private String sixMonthPeriodTypeName;

    public String getSixMonthPeriodTypeName()
    {
        return sixMonthPeriodTypeName;
    }

    private String yearlyPeriodTypeName;

    public String getYearlyPeriodTypeName()
    {
        return yearlyPeriodTypeName;
    }
    
    private List<Period> yearlyPeriods;

    public List<Period> getYearlyPeriods()
    {
        return yearlyPeriods;
    }
    
    private List<OrganisationUnitGroup> orgUnitGroups;

    public List<OrganisationUnitGroup> getOrgUnitGroups()
    {
        return orgUnitGroups;
    }

    
    // -------------------------------------------------------------------------
    // Action Implementation
    // -------------------------------------------------------------------------
    

    public String execute() throws Exception
    {
    
        /* DataElements and Groups */
        dataElements = new ArrayList<DataElement>( dataElementService.getAllDataElements() );
        dataElementGroups = new ArrayList<DataElementGroup>( dataElementService.getAllDataElementGroups() );
        Collections.sort( dataElements, new DataElementNameComparator() );
        Collections.sort( dataElementGroups, new DataElementGroupNameComparator() );
        
        /* Periods Type */
        periodTypes = new ArrayList<PeriodType>( periodService.getAllPeriodTypes() );
        
        Iterator<PeriodType> ptIterator = periodTypes.iterator();
        while ( ptIterator.hasNext() )
        {
            String pTName = ptIterator.next().getName();
            if ( pTName.equalsIgnoreCase( DailyPeriodType.NAME ) || pTName.equalsIgnoreCase( TwoYearlyPeriodType.NAME )
                || pTName.equalsIgnoreCase( OnChangePeriodType.NAME )
                || pTName.equalsIgnoreCase( WeeklyPeriodType.NAME ) )
            {
                ptIterator.remove();
            }
        }

        
        monthlyPeriods = new ArrayList<Period>( periodService.getPeriodsByPeriodType( new MonthlyPeriodType() ) );
        periodNameList = new ArrayList<String>();
        Collections.sort( monthlyPeriods, new PeriodComparator() );
        simpleDateFormat = new SimpleDateFormat( "MMM-yyyy" );
        monthlyPeriodTypeName = MonthlyPeriodType.NAME;
        quarterlyPeriodTypeName = QuarterlyPeriodType.NAME;
        sixMonthPeriodTypeName = SixMonthlyPeriodType.NAME;
        yearlyPeriodTypeName = YearlyPeriodType.NAME;

        yearlyPeriods = new ArrayList<Period>( periodService.getPeriodsByPeriodType( new YearlyPeriodType() ) );
        Iterator<Period> periodIterator = yearlyPeriods.iterator();
        while( periodIterator.hasNext() )
        {
            Period p1 = periodIterator.next();
            
            if ( p1.getStartDate().compareTo( new Date() ) > 0 )
            {
                periodIterator.remove( );
            }
            
        }
        Collections.sort( yearlyPeriods, new PeriodComparator() );
        simpleDateFormat = new SimpleDateFormat( "yyyy" );
        //System.out.println( monthlyPeriodTypeName );
        int year;
        for ( Period p1 : yearlyPeriods )
        {
            year = Integer.parseInt( simpleDateFormat.format( p1.getStartDate() ) ) + 1;
            periodNameList.add( simpleDateFormat.format( p1.getStartDate() ) + "-" + year );
        }
        
        /* Organisationunit Group */
       
        orgUnitGroups = new ArrayList<OrganisationUnitGroup>( organisationUnitGroupService.getAllOrganisationUnitGroups() );

        
    return SUCCESS;
    }
}

