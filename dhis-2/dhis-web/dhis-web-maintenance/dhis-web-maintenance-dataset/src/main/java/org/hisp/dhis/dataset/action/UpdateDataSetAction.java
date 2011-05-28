package org.hisp.dhis.dataset.action;

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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.PeriodType;

import com.opensymphony.xwork2.Action;

/**
 * @author Kristian
 * @version $Id: UpdateDataSetAction.java 6255 2008-11-10 16:01:24Z larshelg $
 */
public class UpdateDataSetAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataSetService dataSetService;

    public void setDataSetService( DataSetService dataSetService )
    {
        this.dataSetService = dataSetService;
    }

    private PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    private IndicatorService indicatorService;

    public void setIndicatorService( IndicatorService indicatorService )
    {
        this.indicatorService = indicatorService;
    }
    
    // -------------------------------------------------------------------------
    // Input & output
    // -------------------------------------------------------------------------

    private String name;

    public void setName( String name )
    {
        this.name = name;
    }

    private String shortName;

    public void setShortName( String shortName )
    {
        this.shortName = shortName;
    }

    private String code;

    public void setCode( String code )
    {
        this.code = code;
    }

    private String frequencySelect;

    public void setFrequencySelect( String frequencySelect )
    {
        this.frequencySelect = frequencySelect;
    }

    private int dataSetId;

    public void setDataSetId( int dataSetId )
    {
        this.dataSetId = dataSetId;
    }

    private Collection<String> dataElementsSelectedList = new HashSet<String>();

    public void setDataElementsSelectedList( Collection<String> dataElementsSelectedList )
    {
        this.dataElementsSelectedList = dataElementsSelectedList;
    }

    private Collection<String> indicatorsSelectedList = new HashSet<String>();

    public void setIndicatorsSelectedList( Collection<String> indicatorsSelectedList )
    {
        this.indicatorsSelectedList = indicatorsSelectedList;
    }

    // -------------------------------------------------------------------------
    // Action
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        // ---------------------------------------------------------------------
        // Prepare values
        // ---------------------------------------------------------------------

        if ( shortName != null && shortName.trim().length() == 0 )
        {
            shortName = null;
        }

        if ( code != null && code.trim().length() == 0 )
        {
            code = null;
        }

        Collection<DataElement> dataElements = new HashSet<DataElement>();

        for ( String id : dataElementsSelectedList )
        {
            dataElements.add( dataElementService.getDataElement( Integer.parseInt( id ) ) );
        }

        Set<Indicator> indicators = new HashSet<Indicator>();

        for ( String id : indicatorsSelectedList )
        {
            indicators.add( indicatorService.getIndicator( Integer.parseInt( id ) ) );
        }

        PeriodType periodType = periodService.getPeriodTypeByName( frequencySelect );

        DataSet dataSet = dataSetService.getDataSet( dataSetId );

        dataSet.setName( name );
        dataSet.setShortName( shortName );
        dataSet.setCode( code );
        dataSet.setPeriodType( periodService.getPeriodTypeByClass( periodType.getClass() ) );
        dataSet.setDataElements( dataElements );
        dataSet.setIndicators( indicators );

        if ( dataSet.isMobile() )
        {
            dataSet.setVersion( dataSet.getVersion() + 1 ); // TODO hack
        }

        dataSetService.updateDataSet( dataSet );

        return SUCCESS;
    }
}
