package org.hisp.dhis.dashboard;

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

import java.util.ArrayList;
import java.util.List;

import org.hisp.dhis.chart.Chart;
import org.hisp.dhis.common.BaseIdentifiableObject;
import org.hisp.dhis.common.DxfNamespaces;
import org.hisp.dhis.common.IdentifiableObject;
import org.hisp.dhis.document.Document;
import org.hisp.dhis.mapping.Map;
import org.hisp.dhis.report.Report;
import org.hisp.dhis.reporttable.ReportTable;
import org.hisp.dhis.user.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * @author Lars Helge Overland
 */
@JacksonXmlRootElement( localName = "dashboardItem", namespace = DxfNamespaces.DXF_2_0)
public class DashboardItem
    extends BaseIdentifiableObject
{
    public static final int MAX_CONTENT = 9;
    
    public static final String TYPE_CHART = "chart";
    public static final String TYPE_MAP = "map";
    public static final String TYPE_USERS = "users";
    public static final String TYPE_REPORT_TABLES = "reportTables";
    public static final String TYPE_REPORTS = "reports";
    public static final String TYPE_RESOURCES = "resources";
    
    private Chart chart;
    
    private Map map;
    
    private List<User> users = new ArrayList<User>();
    
    private List<ReportTable> reportTables = new ArrayList<ReportTable>();
    
    private List<Report> reports = new ArrayList<Report>();

    private List<Document> resources = new ArrayList<Document>();

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public DashboardItem()
    {
    }

    public DashboardItem( String uid )
    {
        this.uid = uid;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    @JsonProperty
    public String getType()
    {
        if ( chart != null )
        {
            return TYPE_CHART;
        }
        else if ( map != null )
        {
            return TYPE_MAP;
        }
        else if ( !users.isEmpty() )
        {
            return TYPE_USERS;
        }
        else if ( !reportTables.isEmpty() )
        {
            return TYPE_REPORT_TABLES;
        }
        else if ( !reports.isEmpty() )
        {
            return TYPE_REPORTS;
        }
        else if ( !resources.isEmpty() )
        {
            return TYPE_RESOURCES;
        }
        
        return null;
    }
    
    public int getContentCount()
    {
        int count = 0;
        count += users.size();
        count += reportTables.size();
        count += reports.size();
        count += resources.size();
        return count;
    }
    
    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    public Chart getChart()
    {
        return chart;
    }

    public void setChart( Chart chart )
    {
        this.chart = chart;
    }
    
    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    public Map getMap()
    {
        return map;
    }

    public void setMap( Map map )
    {
        this.map = map;
    }

    @JsonProperty( value = "users" )
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "users", namespace = DxfNamespaces.DXF_2_0)
    @JacksonXmlProperty( localName = "user", namespace = DxfNamespaces.DXF_2_0)
    public List<User> getUsers()
    {
        return users;
    }

    public void setUsers( List<User> users )
    {
        this.users = users;
    }

    @JsonProperty( value = "reportTables" )
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "reportTables", namespace = DxfNamespaces.DXF_2_0)
    @JacksonXmlProperty( localName = "reportTable", namespace = DxfNamespaces.DXF_2_0)
    public List<ReportTable> getReportTables()
    {
        return reportTables;
    }

    public void setReportTables( List<ReportTable> reportTables )
    {
        this.reportTables = reportTables;
    }

    @JsonProperty( value = "reports" )
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "reports", namespace = DxfNamespaces.DXF_2_0)
    @JacksonXmlProperty( localName = "report", namespace = DxfNamespaces.DXF_2_0)
    public List<Report> getReports()
    {
        return reports;
    }

    public void setReports( List<Report> reports )
    {
        this.reports = reports;
    }

    @JsonProperty( value = "resources" )
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "resources", namespace = DxfNamespaces.DXF_2_0)
    @JacksonXmlProperty( localName = "resource", namespace = DxfNamespaces.DXF_2_0)
    public List<Document> getResources()
    {
        return resources;
    }

    public void setResources( List<Document> resources )
    {
        this.resources = resources;
    }

    // -------------------------------------------------------------------------
    // Merge with
    // -------------------------------------------------------------------------

    @Override
    public void mergeWith( IdentifiableObject other )
    {
        super.mergeWith( other );

        if ( other.getClass().isInstance( this ) )
        {
            DashboardItem item = (DashboardItem) other;
            
            chart = item.getChart() == null ? chart : item.getChart();
            map = item.getMap() == null ? map : item.getMap();
            users = item.getUsers() == null ? users : item.getUsers();
            reportTables = item.getReportTables() == null ? reportTables : item.getReportTables();
            reports = item.getReports() == null ? reports : item.getReports();
            resources = item.getResources() == null ? resources : item.getResources();
        }
    }
}
