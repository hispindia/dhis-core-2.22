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
package org.hisp.dhis.patientattributevalue.hibernate;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.hibernate.HibernateGenericStore;
import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.patient.PatientAttribute;
import org.hisp.dhis.patient.PatientAttributeOption;
import org.hisp.dhis.patientattributevalue.PatientAttributeValue;
import org.hisp.dhis.patientattributevalue.PatientAttributeValueStore;

/**
 * @author Abyot Asalefew
 * @version $Id$
 */
public class HibernatePatientAttributeValueStore
    extends HibernateGenericStore<PatientAttributeValue>
    implements PatientAttributeValueStore
{
    public void saveVoid( PatientAttributeValue patientAttributeValue )
    {
        sessionFactory.getCurrentSession().save( patientAttributeValue );
    }

    public int deleteByAttribute( PatientAttribute patientAttribute )
    {
        Query query = getQuery( "delete from PatientAttributeValue where patientAttribute = :patientAttribute" );
        query.setEntity( "patientAttribute", patientAttribute );
        return query.executeUpdate();
    }

    public int deleteByPatient( Patient patient )
    {
        Query query = getQuery( "delete from PatientAttributeValue where patient = :patient" );
        query.setEntity( "patient", patient );
        return query.executeUpdate();
    }

    public PatientAttributeValue get( Patient patient, PatientAttribute patientAttribute )
    {
        return (PatientAttributeValue) getCriteria( Restrictions.eq( "patient", patient ),
            Restrictions.eq( "patientAttribute", patientAttribute ) ).uniqueResult();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<PatientAttributeValue> get( Patient patient )
    {
        return getCriteria( Restrictions.eq( "patient", patient ) ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<PatientAttributeValue> get( PatientAttribute patientAttribute )
    {
        return getCriteria( Restrictions.eq( "patientAttribute", patientAttribute ) ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<PatientAttributeValue> get( Collection<Patient> patients )
    {
        return getCriteria( Restrictions.in( "patient", patients ) ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<PatientAttributeValue> searchByValue( PatientAttribute patientAttribute, String searchText )
    {
        return getCriteria( Restrictions.eq( "patientAttribute", patientAttribute ),
            Restrictions.ilike( "value", "%" + searchText + "%" ) ).list();
    }

    public int delete( DataElementCategoryOptionCombo optionCombo )
    {
        Query query = getQuery( "delete from PatientDataValue where optionCombo = :optionCombo" );
        query.setEntity( "optionCombo", optionCombo );
        return query.executeUpdate();
    }

    public int countByPatientAttributeoption( PatientAttributeOption attributeOption )
    {
        Number rs = (Number) getCriteria( Restrictions.eq( "patientAttributeOption", attributeOption ) ).setProjection(
            Projections.rowCount() ).uniqueResult();
        return rs != null ? rs.intValue() : 0;
    }

    @SuppressWarnings( "unchecked" )
    public Collection<Patient> getPatient( PatientAttribute attribute, String value )
    {
        return getCriteria(
            Restrictions.and( Restrictions.eq( "patientAttribute", attribute ), Restrictions.eq( "value", value ) ) )
            .setProjection( Projections.property( "patient" ) ).list();
    }

    public int countSearchPatientAttributeValue( PatientAttribute patientAttribute, String searchText )
    {
        Number rs = (Number) getCriteria( Restrictions.eq( "patientAttribute", patientAttribute ),
            Restrictions.ilike( "value", "%" + searchText + "%" ) ).setProjection( Projections.rowCount() )
            .uniqueResult();

        return rs != null ? rs.intValue() : 0;
    }

    @SuppressWarnings( "unchecked" )
    public Collection<Patient> searchPatients( PatientAttribute patientAttribute, String searchText, int min, int max )
    {
        return getCriteria( Restrictions.eq( "patientAttribute", patientAttribute ),
            Restrictions.ilike( "value", "%" + searchText + "%" ) ).setProjection(
            Projections.distinct( Projections.property( "patient" ) ) ).setFirstResult( min ).setMaxResults( max )
            .list();

    }

    @SuppressWarnings( "unchecked" )
    public Collection<Patient> searchPatients( PatientAttribute patientAttribute, String searchText )
    {
        String hql = "select pav.patient from PatientAttributeValue pav where pav.patientAttribute = :patientAttribute and pav.value like '%"
            + searchText + "%'";

        Query query = getQuery( hql );
        query.setEntity( "patientAttribute", patientAttribute );

        return query.list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<Patient> searchPatients( List<Integer> patientAttributeIds, List<String> searchTexts, int min,
        int max )
    {
        String hql = "SELECT pav.patient FROM PatientAttributeValue as pav WHERE pav.patient in ";
        String end = "";

        int index = 0;
        for ( Integer patientAttributeId : patientAttributeIds )
        {
            end += ")";

            hql += createHQL( patientAttributeId, searchTexts.get( index ), index, patientAttributeIds.size() );

            index++;
        }

        hql += " ORDER BY pav.value ASC";

        Query query = getQuery( hql + end ).setFirstResult( min ).setMaxResults( max );

        return query.list();
    }

    public int countSearchPatients( List<Integer> patientAttributeIds, List<String> searchTexts )
    {
        String hql = "SELECT COUNT( pav.patient ) FROM PatientAttributeValue as pav WHERE pav.patient in ";
        String end = "";

        int index = 0;
        for ( Integer patientAttributeId : patientAttributeIds )
        {
            end += ")";

            hql += createHQL( patientAttributeId, searchTexts.get( index ), index, patientAttributeIds.size() );

            index++;
        }

        Query query = getQuery( hql + end );

        Number rs = (Number) query.uniqueResult();

        return (rs != null) ? rs.intValue() : 0;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------
    
    private String createHQL( Integer patientAttributeId, String searchText, int index, int noCondition )
    {
        String hql = "";
        boolean isSearchByAttribute = true;

        // ---------------------------------------------------------------------
        // search patients by name or identifier
        // ---------------------------------------------------------------------
        if ( patientAttributeId == null )
        {
            hql += " ( SELECT p" + index + " FROM Patient as p" + index + " JOIN p" + index
                + ".identifiers as identifier" + index + " " + "WHERE  lower(identifier" + index
                + ".identifier) LIKE lower('%" + searchText + "%') " + "OR lower(p" + index
                + ".firstName) LIKE lower('%" + searchText + "%') " + "OR lower(p" + index
                + ".middleName) LIKE lower('%" + searchText + "%') " + "OR lower(p" + index
                + ".lastName) LIKE lower('%" + searchText + "%') ";

            isSearchByAttribute = false;
        }
        // -----------------------------------------------------------------
        // search patients by program
        // -----------------------------------------------------------------
        else if ( patientAttributeId == 0 )
        {
            hql += " ( SELECT p" + index + " FROM Patient AS p" + index + " " + " JOIN p" + index
                + ".programs AS program" + index + " WHERE program" + index + ".id=" + searchText;

            isSearchByAttribute = false;
        }
        // -----------------------------------------------------------------
        // search patients by attribute
        // -----------------------------------------------------------------
        else
        {
            hql += " ( SELECT pav" + index + ".patient FROM PatientAttributeValue as pav" + index + " " + "WHERE pav"
                + index + ".patientAttribute.id=" + patientAttributeId + " AND lower(pav" + index
                + ".value) LIKE lower('%" + searchText + "%') ";
        }

        if ( index < noCondition - 1 )
        {

            if ( isSearchByAttribute )
            {
                hql += " AND pav" + index + ".patient in ";
            }
            else
            {
                hql += " AND p" + index + " in ";
            }
        }

        return hql;

    }
}
