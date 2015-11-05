package org.hisp.dhis.query;

/*
 * Copyright (c) 2004-2015, University of Oslo
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

import com.google.common.collect.Lists;
import org.hisp.dhis.DhisSpringTest;
import org.hisp.dhis.common.IdentifiableObject;
import org.hisp.dhis.common.IdentifiableObjectManager;
import org.hisp.dhis.common.ValueType;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.query.operators.MatchMode;
import org.hisp.dhis.schema.Schema;
import org.hisp.dhis.schema.SchemaService;
import org.jfree.data.time.Year;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Morten Olav Hansen <mortenoh@gmail.com>
 */
public class CriteriaQueryEngineTest
    extends DhisSpringTest
{
    @Autowired
    private SchemaService schemaService;

    @Autowired
    private CriteriaQueryEngine<? extends IdentifiableObject> criteriaQueryEngine;

    @Autowired
    private IdentifiableObjectManager identifiableObjectManager;

    private void createDataElements()
    {
        DataElement dataElementA = createDataElement( 'A' );
        dataElementA.setValueType( ValueType.NUMBER );

        DataElement dataElementB = createDataElement( 'B' );
        dataElementB.setValueType( ValueType.BOOLEAN );

        DataElement dataElementC = createDataElement( 'C' );
        dataElementC.setValueType( ValueType.INTEGER );

        DataElement dataElementD = createDataElement( 'D' );
        dataElementD.setValueType( ValueType.NUMBER );

        DataElement dataElementE = createDataElement( 'E' );
        dataElementE.setValueType( ValueType.BOOLEAN );

        DataElement dataElementF = createDataElement( 'F' );
        dataElementF.setValueType( ValueType.INTEGER );

        dataElementA.setCreated( Year.parseYear( "2001" ).getStart() );
        dataElementB.setCreated( Year.parseYear( "2002" ).getStart() );
        dataElementC.setCreated( Year.parseYear( "2003" ).getStart() );
        dataElementD.setCreated( Year.parseYear( "2004" ).getStart() );
        dataElementE.setCreated( Year.parseYear( "2005" ).getStart() );
        dataElementF.setCreated( Year.parseYear( "2006" ).getStart() );

        identifiableObjectManager.save( dataElementB );
        identifiableObjectManager.save( dataElementE );
        identifiableObjectManager.save( dataElementA );
        identifiableObjectManager.save( dataElementC );
        identifiableObjectManager.save( dataElementF );
        identifiableObjectManager.save( dataElementD );
    }

    private boolean collectionContainsUid( Collection<? extends IdentifiableObject> collection, String uid )
    {
        for ( IdentifiableObject identifiableObject : collection )
        {
            if ( identifiableObject.getUid().equals( uid ) )
            {
                return true;
            }
        }

        return false;
    }

    @Test
    public void getAllQuery()
    {
        createDataElements();
        Query query = Query.from( schemaService.getDynamicSchema( DataElement.class ) );
        assertEquals( 6, criteriaQueryEngine.query( query ).size() );
    }

    @Test
    public void getMinMaxQuery()
    {
        createDataElements();
        Query query = Query.from( schemaService.getDynamicSchema( DataElement.class ) );
        query.setFirstResult( 2 );
        query.setMaxResults( 10 );

        assertEquals( 4, criteriaQueryEngine.query( query ).size() );

        query = Query.from( schemaService.getDynamicSchema( DataElement.class ) );
        query.setFirstResult( 2 );
        query.setMaxResults( 2 );

        assertEquals( 2, criteriaQueryEngine.query( query ).size() );
    }

    @Test
    public void getEqQuery()
    {
        createDataElements();
        Query query = Query.from( schemaService.getDynamicSchema( DataElement.class ) );
        query.add( Restrictions.eq( "id", "deabcdefghA" ) );
        List<? extends IdentifiableObject> objects = criteriaQueryEngine.query( query );

        assertEquals( 1, objects.size() );
        assertEquals( "deabcdefghA", objects.get( 0 ).getUid() );
    }

    @Test
    public void getNeQuery()
    {
        createDataElements();
        Query query = Query.from( schemaService.getDynamicSchema( DataElement.class ) );
        query.add( Restrictions.ne( "id", "deabcdefghA" ) );
        List<? extends IdentifiableObject> objects = criteriaQueryEngine.query( query );

        assertEquals( 5, objects.size() );

        assertFalse( collectionContainsUid( objects, "deabcdefghA" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghB" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghC" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghD" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghE" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghF" ) );
    }

    @Test
    public void getLikeQuery()
    {
        createDataElements();
        Query query = Query.from( schemaService.getDynamicSchema( DataElement.class ) );
        query.add( Restrictions.like( "name", "F", MatchMode.ANYWHERE ) );
        List<? extends IdentifiableObject> objects = criteriaQueryEngine.query( query );

        assertEquals( 1, objects.size() );
        assertEquals( "deabcdefghF", objects.get( 0 ).getUid() );
    }

    @Test
    public void getGtQuery()
    {
        createDataElements();
        Query query = Query.from( schemaService.getDynamicSchema( DataElement.class ) );
        query.add( Restrictions.gt( "created", Year.parseYear( "2003" ).getStart() ) );
        List<? extends IdentifiableObject> objects = criteriaQueryEngine.query( query );

        assertEquals( 3, objects.size() );

        assertTrue( collectionContainsUid( objects, "deabcdefghD" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghE" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghF" ) );
    }

    @Test
    public void getLtQuery()
    {
        createDataElements();
        Query query = Query.from( schemaService.getDynamicSchema( DataElement.class ) );
        query.add( Restrictions.lt( "created", Year.parseYear( "2003" ).getStart() ) );
        List<? extends IdentifiableObject> objects = criteriaQueryEngine.query( query );

        assertEquals( 2, objects.size() );

        assertTrue( collectionContainsUid( objects, "deabcdefghA" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghB" ) );
    }

    @Test
    public void getGeQuery()
    {
        createDataElements();
        Query query = Query.from( schemaService.getDynamicSchema( DataElement.class ) );
        query.add( Restrictions.ge( "created", Year.parseYear( "2003" ).getStart() ) );
        List<? extends IdentifiableObject> objects = criteriaQueryEngine.query( query );

        assertEquals( 4, objects.size() );

        assertTrue( collectionContainsUid( objects, "deabcdefghC" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghD" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghE" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghF" ) );
    }

    @Test
    public void getLeQuery()
    {
        createDataElements();
        Query query = Query.from( schemaService.getDynamicSchema( DataElement.class ) );
        query.add( Restrictions.le( "created", Year.parseYear( "2003" ).getStart() ) );
        List<? extends IdentifiableObject> objects = criteriaQueryEngine.query( query );

        assertEquals( 3, objects.size() );

        assertTrue( collectionContainsUid( objects, "deabcdefghA" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghB" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghC" ) );
    }

    @Test
    public void getBetweenQuery()
    {
        createDataElements();
        Query query = Query.from( schemaService.getDynamicSchema( DataElement.class ) );
        query.add( Restrictions.between( "created", Year.parseYear( "2003" ).getStart(), Year.parseYear( "2005" ).getStart() ) );
        List<? extends IdentifiableObject> objects = criteriaQueryEngine.query( query );

        assertEquals( 3, objects.size() );

        assertTrue( collectionContainsUid( objects, "deabcdefghC" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghD" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghE" ) );
    }

    @Test
    public void getInQuery()
    {
        createDataElements();
        Query query = Query.from( schemaService.getDynamicSchema( DataElement.class ) );
        query.add( Restrictions.in( "id", Lists.newArrayList( "deabcdefghD", "deabcdefghF" ) ) );
        List<? extends IdentifiableObject> objects = criteriaQueryEngine.query( query );

        assertEquals( 2, objects.size() );

        assertTrue( collectionContainsUid( objects, "deabcdefghD" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghF" ) );
    }

    @Test
    public void sortNameDesc()
    {
        createDataElements();
        Schema schema = schemaService.getDynamicSchema( DataElement.class );

        Query query = Query.from( schema );
        query.addOrder( new Order( schema.getProperty( "name" ), false ) );
        List<? extends IdentifiableObject> objects = criteriaQueryEngine.query( query );

        assertEquals( 6, objects.size() );

        assertEquals( "deabcdefghF", objects.get( 0 ).getUid() );
        assertEquals( "deabcdefghE", objects.get( 1 ).getUid() );
        assertEquals( "deabcdefghD", objects.get( 2 ).getUid() );
        assertEquals( "deabcdefghC", objects.get( 3 ).getUid() );
        assertEquals( "deabcdefghB", objects.get( 4 ).getUid() );
        assertEquals( "deabcdefghA", objects.get( 5 ).getUid() );
    }

    @Test
    public void sortNameAsc()
    {
        createDataElements();
        Schema schema = schemaService.getDynamicSchema( DataElement.class );

        Query query = Query.from( schema );
        query.addOrder( new Order( schema.getProperty( "name" ), true ) );
        List<? extends IdentifiableObject> objects = criteriaQueryEngine.query( query );

        assertEquals( 6, objects.size() );

        assertEquals( "deabcdefghA", objects.get( 0 ).getUid() );
        assertEquals( "deabcdefghB", objects.get( 1 ).getUid() );
        assertEquals( "deabcdefghC", objects.get( 2 ).getUid() );
        assertEquals( "deabcdefghD", objects.get( 3 ).getUid() );
        assertEquals( "deabcdefghE", objects.get( 4 ).getUid() );
        assertEquals( "deabcdefghF", objects.get( 5 ).getUid() );
    }

    @Test
    public void sortCreatedDesc()
    {
        createDataElements();
        Schema schema = schemaService.getDynamicSchema( DataElement.class );

        Query query = Query.from( schema );
        query.addOrder( new Order( schema.getProperty( "created" ), false ) );
        List<? extends IdentifiableObject> objects = criteriaQueryEngine.query( query );

        assertEquals( 6, objects.size() );

        assertEquals( "deabcdefghF", objects.get( 0 ).getUid() );
        assertEquals( "deabcdefghE", objects.get( 1 ).getUid() );
        assertEquals( "deabcdefghD", objects.get( 2 ).getUid() );
        assertEquals( "deabcdefghC", objects.get( 3 ).getUid() );
        assertEquals( "deabcdefghB", objects.get( 4 ).getUid() );
        assertEquals( "deabcdefghA", objects.get( 5 ).getUid() );
    }

    @Test
    public void sortCreatedAsc()
    {
        createDataElements();
        Schema schema = schemaService.getDynamicSchema( DataElement.class );

        Query query = Query.from( schema );
        query.addOrder( new Order( schema.getProperty( "created" ), true ) );
        List<? extends IdentifiableObject> objects = criteriaQueryEngine.query( query );

        assertEquals( 6, objects.size() );

        assertEquals( "deabcdefghA", objects.get( 0 ).getUid() );
        assertEquals( "deabcdefghB", objects.get( 1 ).getUid() );
        assertEquals( "deabcdefghC", objects.get( 2 ).getUid() );
        assertEquals( "deabcdefghD", objects.get( 3 ).getUid() );
        assertEquals( "deabcdefghE", objects.get( 4 ).getUid() );
        assertEquals( "deabcdefghF", objects.get( 5 ).getUid() );
    }

    @Test
    public void testDoubleEqConjunction()
    {
        createDataElements();
        Query query = Query.from( schemaService.getDynamicSchema( DataElement.class ) );

        Conjunction conjunction = query.conjunction();
        conjunction.add( Restrictions.eq( "id", "deabcdefghD" ) );
        conjunction.add( Restrictions.eq( "id", "deabcdefghF" ) );
        query.add( conjunction );

        List<? extends IdentifiableObject> objects = criteriaQueryEngine.query( query );

        assertEquals( 0, objects.size() );
    }

    @Test
    public void testDoubleEqDisjunction()
    {
        createDataElements();
        Query query = Query.from( schemaService.getDynamicSchema( DataElement.class ) );

        Disjunction disjunction = query.disjunction();
        disjunction.add( Restrictions.eq( "id", "deabcdefghD" ) );
        disjunction.add( Restrictions.eq( "id", "deabcdefghF" ) );
        query.add( disjunction );

        List<? extends IdentifiableObject> objects = criteriaQueryEngine.query( query );

        assertEquals( 2, objects.size() );

        assertTrue( collectionContainsUid( objects, "deabcdefghD" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghF" ) );
    }

    @Test
    public void testDateRange()
    {
        createDataElements();
        Query query = Query.from( schemaService.getDynamicSchema( DataElement.class ) );

        query.add( Restrictions.ge( "created", Year.parseYear( "2002" ).getStart() ) );
        query.add( Restrictions.le( "created", Year.parseYear( "2004" ).getStart() ) );

        List<? extends IdentifiableObject> objects = criteriaQueryEngine.query( query );

        assertEquals( 3, objects.size() );

        assertTrue( collectionContainsUid( objects, "deabcdefghB" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghC" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghD" ) );
    }

    @Test
    public void testDateRangeWithConjunction()
    {
        createDataElements();
        Query query = Query.from( schemaService.getDynamicSchema( DataElement.class ) );

        Conjunction conjunction = query.conjunction();
        conjunction.add( Restrictions.ge( "created", Year.parseYear( "2002" ).getStart() ) );
        conjunction.add( Restrictions.le( "created", Year.parseYear( "2004" ).getStart() ) );
        query.add( conjunction );

        List<? extends IdentifiableObject> objects = criteriaQueryEngine.query( query );

        assertEquals( 3, objects.size() );

        assertTrue( collectionContainsUid( objects, "deabcdefghB" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghC" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghD" ) );
    }

    @Test
    public void testIsNull()
    {
        createDataElements();
        Query query = Query.from( schemaService.getDynamicSchema( DataElement.class ) );
        query.add( Restrictions.isNull( "categoryCombo" ) );

        List<? extends IdentifiableObject> objects = criteriaQueryEngine.query( query );

        assertEquals( 6, objects.size() );

        assertTrue( collectionContainsUid( objects, "deabcdefghA" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghB" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghC" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghD" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghE" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghF" ) );
    }
}
