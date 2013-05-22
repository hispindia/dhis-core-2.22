package org.hisp.dhis.chart;

/*
 * Copyright (c) 2004-2012, University of Oslo
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

import java.util.Iterator;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.system.deletion.DeletionHandler;
import org.hisp.dhis.user.User;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class ChartDeletionHandler
    extends DeletionHandler
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ChartService chartService;

    public void setChartService( ChartService chartService )
    {
        this.chartService = chartService;
    }

    // -------------------------------------------------------------------------
    // DeletionHandler implementation
    // -------------------------------------------------------------------------

    @Override
    public String getClassName()
    {
        return Chart.class.getSimpleName();
    }

    @Override
    public String allowDeletePeriod( Period period )
    {
        for ( Chart chart : chartService.getAllCharts() )
        {
            if ( chart.getPeriods().contains( period ) )
            {
                return chart.getName();
            }
        }
        
        return null;
    }

    @Override
    public void deleteIndicator( Indicator indicator )
    {
        for ( Chart chart : chartService.getAllCharts() )
        {
            if ( chart.getIndicators().remove( indicator ) )
            {
                chartService.updateChart( chart );
            }
        }
    }

    @Override
    public void deleteDataElement( DataElement dataElement )
    {
        for ( Chart chart : chartService.getAllCharts() )
        {
            if ( chart.getDataElements().remove( chart ) )
            {
                chartService.updateChart( chart );
            }
        }
    }

    @Override
    public void deleteDataSet( DataSet dataSet )
    {
        for ( Chart chart : chartService.getAllCharts() )
        {
            if ( chart.getDataSets().remove( chart ) )
            {
                chartService.updateChart( chart );
            }
        }
    }
    
    @Override
    public void deleteOrganisationUnit( OrganisationUnit unit )
    {
        for ( Chart chart : chartService.getAllCharts() )
        {
            if ( chart.getOrganisationUnits().remove( unit ) )
            {
                chartService.updateChart( chart );
            }
        }
    }
    
    @Override
    public void deleteUser( User user )
    {
        Iterator<Chart> iterator = chartService.getChartsByUser( user ).iterator();
        
        while ( iterator.hasNext() )
        {
            Chart chart = iterator.next();
            iterator.remove();
            chartService.deleteChart( chart );
        }
    }

    @Override
    public void deleteDataElementGroup( DataElementGroup group )
    {
        for ( Chart chart : chartService.getAllCharts() )
        {
            if ( chart.getDataElementGroups().remove( group ) )
            {
                chartService.updateChart( chart );
            }
        }
    }
    
    @Override
    public void deleteOrganisationUnitGroup( OrganisationUnitGroup group )
    {
        for ( Chart chart : chartService.getAllCharts() )
        {
            if ( chart.getOrganisationUnitGroups().remove( group ) )
            {
                chartService.updateChart( chart );
            }
        }
    }
}
