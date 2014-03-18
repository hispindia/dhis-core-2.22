package org.hisp.dhis.trackedentityattributevalue;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashSet;

import org.hisp.dhis.DhisSpringTest;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.trackedentity.TrackedEntityAttribute;
import org.hisp.dhis.trackedentity.TrackedEntityAttributeOption;
import org.hisp.dhis.trackedentity.TrackedEntityAttributeService;
import org.hisp.dhis.trackedentity.TrackedEntityInstance;
import org.hisp.dhis.trackedentity.TrackedEntityInstanceService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Chau Thu Tran
 * 
 * @version $ TrackedEntityAttributeValueStoreTest.java Nov 11, 2013 9:45:10 AM $
 */
public class TrackedEntityAttributeValueStoreTest
    extends DhisSpringTest
{
    @Autowired
    private TrackedEntityAttributeValueStore attributeValueStore;

    @Autowired
    private TrackedEntityInstanceService entityInstanceService;

    @Autowired
    private OrganisationUnitService organisationUnitService;

    @Autowired
    private TrackedEntityAttributeService attributeService;

    private TrackedEntityAttribute attributeA;

    private TrackedEntityAttribute attributeB;

    private TrackedEntityAttribute attributeC;

    private TrackedEntityAttributeOption attributeOpionA;

    private TrackedEntityAttributeOption attributeOpionB;

    private TrackedEntityInstance entityInstanceA;

    private TrackedEntityInstance entityInstanceB;

    private TrackedEntityInstance entityInstanceC;

    private TrackedEntityInstance entityInstanceD;

    private TrackedEntityAttributeValue attributeValueA;

    private TrackedEntityAttributeValue attributeValueB;

    private TrackedEntityAttributeValue attributeValueC;

    private TrackedEntityAttributeValue attributeValueD;

    private TrackedEntityAttributeValue attributeValueE;

    @Override
    public void setUpTest()
    {
        OrganisationUnit organisationUnit = createOrganisationUnit( 'A' );
        organisationUnitService.addOrganisationUnit( organisationUnit );

        entityInstanceA = createTrackedEntityInstance( 'A', organisationUnit );
        entityInstanceB = createTrackedEntityInstance( 'B', organisationUnit );
        entityInstanceC = createTrackedEntityInstance( 'C', organisationUnit );
        entityInstanceD = createTrackedEntityInstance( 'D', organisationUnit );

        entityInstanceService.addTrackedEntityInstance( entityInstanceA );
        entityInstanceService.addTrackedEntityInstance( entityInstanceB );
        entityInstanceService.addTrackedEntityInstance( entityInstanceC );
        entityInstanceService.addTrackedEntityInstance( entityInstanceD );

        attributeA = createTrackedEntityAttribute(  'A' );
        attributeB = createTrackedEntityAttribute( 'B' );
        attributeC = createTrackedEntityAttribute( 'C' );

        attributeService.addTrackedEntityAttribute( attributeA );
        attributeService.addTrackedEntityAttribute( attributeB );
        attributeService.addTrackedEntityAttribute( attributeC );

        attributeOpionA = createTrackedEntityAttributeOption( 'A', attributeC );
        attributeOpionB = createTrackedEntityAttributeOption( 'B', attributeC );

        attributeService.addTrackedEntityAttributeOption( attributeOpionA );
        attributeService.addTrackedEntityAttributeOption( attributeOpionB );

        attributeValueA = new TrackedEntityAttributeValue( attributeA, entityInstanceA, "A" );
        attributeValueB = new TrackedEntityAttributeValue( attributeB, entityInstanceA, "B" );
        attributeValueC = new TrackedEntityAttributeValue( attributeA, entityInstanceB, "C" );
        attributeValueD = new TrackedEntityAttributeValue( attributeC, entityInstanceC, "AttributeOptionA" );
        attributeValueD.setAttributeOption( attributeOpionA );
        attributeValueE = new TrackedEntityAttributeValue( attributeC, entityInstanceD, "AttributeOptionB" );
        attributeValueE.setAttributeOption( attributeOpionB );
    }

    @Test
    public void testSaveTrackedEntityAttributeValue()
    {
        attributeValueStore.saveVoid( attributeValueA );
        attributeValueStore.saveVoid( attributeValueB );

        assertNotNull( attributeValueStore.get( entityInstanceA, attributeA ) );
        assertNotNull( attributeValueStore.get( entityInstanceA, attributeA ) );
    }

    @Test
    public void testDeleteTrackedEntityAttributeValueByEntityInstance()
    {
        attributeValueStore.saveVoid( attributeValueA );
        attributeValueStore.saveVoid( attributeValueB );
        attributeValueStore.saveVoid( attributeValueC );

        assertNotNull( attributeValueStore.get( entityInstanceA, attributeA ) );
        assertNotNull( attributeValueStore.get( entityInstanceA, attributeB ) );
        assertNotNull( attributeValueStore.get( entityInstanceB, attributeA ) );

        attributeValueStore.deleteByTrackedEntityInstance( entityInstanceA );

        assertNull( attributeValueStore.get( entityInstanceA, attributeA ) );
        assertNull( attributeValueStore.get( entityInstanceA, attributeB ) );
        assertNotNull( attributeValueStore.get( entityInstanceB, attributeA ) );

        attributeValueStore.deleteByTrackedEntityInstance( entityInstanceB );
        assertNull( attributeValueStore.get( entityInstanceA, attributeA ) );
        assertNull( attributeValueStore.get( entityInstanceA, attributeB ) );
        assertNull( attributeValueStore.get( entityInstanceB, attributeA ) );
    }

    @Test
    public void testDeleteByAttribute()
    {
        attributeValueStore.saveVoid( attributeValueA );
        attributeValueStore.saveVoid( attributeValueB );
        attributeValueStore.saveVoid( attributeValueC );

        assertNotNull( attributeValueStore.get( entityInstanceA, attributeA ) );
        assertNotNull( attributeValueStore.get( entityInstanceA, attributeB ) );
        assertNotNull( attributeValueStore.get( entityInstanceB, attributeA ) );

        attributeValueStore.deleteByAttribute( attributeA );

        assertNull( attributeValueStore.get( entityInstanceA, attributeA ) );
        assertNull( attributeValueStore.get( entityInstanceB, attributeA ) );
        assertNotNull( attributeValueStore.get( entityInstanceA, attributeB ) );

        attributeValueStore.deleteByAttribute( attributeB );
        assertNull( attributeValueStore.get( entityInstanceA, attributeA ) );
        assertNull( attributeValueStore.get( entityInstanceA, attributeB ) );
        assertNull( attributeValueStore.get( entityInstanceB, attributeA ) );

    }

    @Test
    public void testGetTrackedEntityAttributeValue()
    {
        attributeValueStore.saveVoid( attributeValueA );
        attributeValueStore.saveVoid( attributeValueC );

        assertEquals( attributeValueA, attributeValueStore.get( entityInstanceA, attributeA ) );
        assertEquals( attributeValueC, attributeValueStore.get( entityInstanceB, attributeA ) );
    }

    @Test
    public void testGetByEntityInstance()
    {
        attributeValueStore.saveVoid( attributeValueA );
        attributeValueStore.saveVoid( attributeValueB );
        attributeValueStore.saveVoid( attributeValueC );

        Collection<TrackedEntityAttributeValue> attributeValues = attributeValueStore.get( entityInstanceA );

        assertEquals( 2, attributeValues.size() );
        assertTrue( equals( attributeValues, attributeValueA, attributeValueB ) );

        attributeValues = attributeValueStore.get( entityInstanceB );

        assertEquals( 1, attributeValues.size() );
        assertTrue( equals( attributeValues, attributeValueC ) );
    }

    @Test
    public void testGetTrackedEntityAttributeValuesbyAttribute()
    {
        attributeValueStore.saveVoid( attributeValueA );
        attributeValueStore.saveVoid( attributeValueB );
        attributeValueStore.saveVoid( attributeValueC );

        Collection<TrackedEntityAttributeValue> attributeValues = attributeValueStore.get( attributeA );
        assertEquals( 2, attributeValues.size() );
        assertTrue( attributeValues.contains( attributeValueA ) );
        assertTrue( attributeValues.contains( attributeValueC ) );

        attributeValues = attributeValueStore.get( attributeB );
        assertEquals( 1, attributeValues.size() );
        assertTrue( attributeValues.contains( attributeValueB ) );
    }

    @Test
    public void testGetTrackedEntityAttributeValuesbyEntityInstanceList()
    {
        attributeValueStore.saveVoid( attributeValueA );
        attributeValueStore.saveVoid( attributeValueB );
        attributeValueStore.saveVoid( attributeValueC );

        Collection<TrackedEntityInstance> entityInstances = new HashSet<TrackedEntityInstance>();
        entityInstances.add( entityInstanceA );
        entityInstances.add( entityInstanceB );

        Collection<TrackedEntityAttributeValue> attributeValues = attributeValueStore.get( entityInstances );
        assertEquals( 3, attributeValues.size() );
        assertTrue( equals( attributeValues, attributeValueA, attributeValueB, attributeValueC ) );
    }

    @Test
    public void testSearchTrackedEntityAttributeValue()
    {
        attributeValueStore.saveVoid( attributeValueA );
        attributeValueStore.saveVoid( attributeValueB );
        attributeValueStore.saveVoid( attributeValueC );

        Collection<TrackedEntityAttributeValue> attributeValues = attributeValueStore.searchByValue( attributeA, "A" );
        assertTrue( equals( attributeValues, attributeValueA ) );
    }

    @Test
    public void testGetEntityInstances()
    {
        attributeValueStore.saveVoid( attributeValueA );
        attributeValueStore.saveVoid( attributeValueB );
        attributeValueStore.saveVoid( attributeValueC );

        Collection<TrackedEntityInstance> entityInstances = attributeValueStore.getTrackedEntityInstances( attributeA, "A" );
        assertEquals( 1, entityInstances.size() );
        assertTrue( entityInstances.contains( entityInstanceA ) );
    }

    @Test
    public void testCountByTrackedEntityAttributeoption()
    {
        attributeValueStore.saveVoid( attributeValueA );
        attributeValueStore.saveVoid( attributeValueD );
        attributeValueStore.saveVoid( attributeValueE );

        int count = attributeValueStore.countByAttributeOption( attributeOpionA );
        assertEquals( 1, count );
    }

}
