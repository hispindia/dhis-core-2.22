package org.hisp.dhis.trackedentity.action.program;

/*
 * Copyright (c) 2004-2014, University of Oslo
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.hisp.dhis.common.comparator.IdentifiableObjectNameComparator;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitLevel;
import org.hisp.dhis.program.Program;
import org.hisp.dhis.program.ProgramService;
import org.hisp.dhis.program.ProgramTrackedEntityAttribute;
import org.hisp.dhis.relationship.RelationshipType;
import org.hisp.dhis.relationship.RelationshipTypeService;
import org.hisp.dhis.trackedentity.TrackedEntity;
import org.hisp.dhis.trackedentity.TrackedEntityAttribute;
import org.hisp.dhis.trackedentity.TrackedEntityAttributeService;
import org.hisp.dhis.trackedentity.TrackedEntityService;
import org.hisp.dhis.user.UserGroup;
import org.hisp.dhis.user.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.Action;

/**
 * @author Chau Thu Tran
 * @version $Id$
 */
public class ShowUpdateProgramFormAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ProgramService programService;

    public void setProgramService( ProgramService programService )
    {
        this.programService = programService;
    }

    private TrackedEntityAttributeService attributeService;

    public void setAttributeService( TrackedEntityAttributeService attributeService )
    {
        this.attributeService = attributeService;
    }

    private UserGroupService userGroupService;

    public void setUserGroupService( UserGroupService userGroupService )
    {
        this.userGroupService = userGroupService;
    }

    private RelationshipTypeService relationshipTypeService;

    public void setRelationshipTypeService( RelationshipTypeService relationshipTypeService )
    {
        this.relationshipTypeService = relationshipTypeService;
    }

    @Autowired
    private TrackedEntityService trackedEntityService;

    // -------------------------------------------------------------------------
    // Input/Output
    // -------------------------------------------------------------------------

    private int id;

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    private Program program;

    public Program getProgram()
    {
        return program;
    }

    private List<OrganisationUnitLevel> levels;

    public List<OrganisationUnitLevel> getLevels()
    {
        return levels;
    }

    private List<OrganisationUnitGroup> groups;

    public List<OrganisationUnitGroup> getGroups()
    {
        return groups;
    }

    private Integer level;

    public Integer getLevel()
    {
        return level;
    }

    public void setLevel( Integer level )
    {
        this.level = level;
    }

    private Integer organisationUnitGroupId;

    public Integer getOrganisationUnitGroupId()
    {
        return organisationUnitGroupId;
    }

    public void setOrganisationUnitGroupId( Integer organisationUnitGroupId )
    {
        this.organisationUnitGroupId = organisationUnitGroupId;
    }

    private Collection<TrackedEntityAttribute> availableAttributes;

    public Collection<TrackedEntityAttribute> getAvailableAttributes()
    {
        return availableAttributes;
    }

    private List<UserGroup> userGroups;

    public List<UserGroup> getUserGroups()
    {
        return userGroups;
    }

    private List<RelationshipType> relationshipTypes;

    public List<RelationshipType> getRelationshipTypes()
    {
        return relationshipTypes;
    }

    private List<Program> programs;

    public List<Program> getPrograms()
    {
        return programs;
    }

    private List<TrackedEntity> trackedEntities;

    public List<TrackedEntity> getTrackedEntities()
    {
        return trackedEntities;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        program = programService.getProgram( id );

        availableAttributes = attributeService.getAllTrackedEntityAttributes();
        for ( ProgramTrackedEntityAttribute programAttribue : program.getAttributes() )
        {
            availableAttributes.remove( programAttribue.getAttribute() );
        }

        programs = new ArrayList<Program>( programService.getAllPrograms() );
        programs.removeAll( programService.getPrograms( Program.SINGLE_EVENT_WITHOUT_REGISTRATION ) );
        programs.remove( program );
        Collections.sort( programs, IdentifiableObjectNameComparator.INSTANCE );

        userGroups = new ArrayList<UserGroup>( userGroupService.getAllUserGroups() );

        relationshipTypes = new ArrayList<RelationshipType>( relationshipTypeService.getAllRelationshipTypes() );
        Collections.sort( relationshipTypes, IdentifiableObjectNameComparator.INSTANCE );

        trackedEntities = new ArrayList<TrackedEntity>( trackedEntityService.getAllTrackedEntity() );
        Collections.sort( trackedEntities, IdentifiableObjectNameComparator.INSTANCE );

        return SUCCESS;
    }
}
