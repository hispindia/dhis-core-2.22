package org.hisp.dhis.importexport.dxf.converter;

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
import java.util.Map;

import org.amplecode.quick.BatchHandler;
import org.amplecode.staxwax.reader.XMLReader;
import org.amplecode.staxwax.writer.XMLWriter;
import org.hisp.dhis.importexport.ExportParams;
import org.hisp.dhis.importexport.GroupMemberType;
import org.hisp.dhis.importexport.ImportObjectService;
import org.hisp.dhis.importexport.ImportParams;
import org.hisp.dhis.importexport.XMLConverter;
import org.hisp.dhis.importexport.converter.AbstractGroupSetConverter;
import org.hisp.dhis.importexport.mapping.NameMappingUtil;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupSet;

/**
 * @author Lars Helge Overland
 * @version $Id: GroupSetConverter.java 6455 2008-11-24 08:59:37Z larshelg $
 */
public class GroupSetConverter
    extends AbstractGroupSetConverter implements XMLConverter
{
    public static final String COLLECTION_NAME = "groupSets";
    public static final String ELEMENT_NAME = "groupSet";
    
    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_COMPULSORY = "compulsory";
    
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Constructor for write operations.
     */
    public GroupSetConverter( OrganisationUnitGroupService organisationUnitGroupService )
    {   
        this.organisationUnitGroupService = organisationUnitGroupService;
    }    

    /**
     * Constructor for read operations.
     * 
     * @param batchHandler the batchHandler to use.
     * @param organisationUnitGroupService the organisationUnitGroupService to use.
     * @param importObjectService the importObjectService to use.
     */
    public GroupSetConverter( BatchHandler<OrganisationUnitGroupSet> batchHandler, 
        ImportObjectService importObjectService, 
        OrganisationUnitGroupService organisationUnitGroupService )
    {
        this.batchHandler = batchHandler;
        this.importObjectService = importObjectService;
        this.organisationUnitGroupService = organisationUnitGroupService;
    }
    
    // -------------------------------------------------------------------------
    // XMLConverter implementation
    // -------------------------------------------------------------------------

    public void write( XMLWriter writer, ExportParams params )
    {
        Collection<OrganisationUnitGroupSet> groupSets = 
            organisationUnitGroupService.getOrganisationUnitGroupSets( params.getOrganisationUnitGroupSets() );
        
        if ( groupSets != null && groupSets.size() > 0 )
        {
            writer.openElement( COLLECTION_NAME );
            
            for ( OrganisationUnitGroupSet groupSet : groupSets )
            {
                writer.openElement( ELEMENT_NAME );
                
                writer.writeElement( FIELD_ID, String.valueOf( groupSet.getId() ) );
                writer.writeElement( FIELD_NAME, groupSet.getName() );
                writer.writeElement( FIELD_DESCRIPTION, groupSet.getDescription() );
                writer.writeElement( FIELD_COMPULSORY, String.valueOf( groupSet.isCompulsory() ) );
                
                writer.closeElement();
            }
            
            writer.closeElement();
        }
    }
    
    public void read( XMLReader reader, ImportParams params )
    {
        while ( reader.moveToStartElement( ELEMENT_NAME, COLLECTION_NAME ) )
        {
            final Map<String, String> values = reader.readElements( ELEMENT_NAME );
            
            final OrganisationUnitGroupSet groupSet = new OrganisationUnitGroupSet();

            groupSet.setId( Integer.parseInt( values.get( FIELD_ID ) ) );
            groupSet.setName( values.get( FIELD_NAME ) );
            groupSet.setDescription( values.get( FIELD_DESCRIPTION ) );
            groupSet.setCompulsory( Boolean.parseBoolean( values.get( FIELD_COMPULSORY ) ) );
            
            NameMappingUtil.addGroupSetMapping( groupSet.getId(), groupSet.getName() );
            
            read( groupSet, GroupMemberType.NONE, params );
        }
    }
}
