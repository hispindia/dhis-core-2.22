package org.hisp.dhis.cache;

/*
 * Copyright (c) 2004-2007, University of Oslo
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

import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.stat.Statistics;

/**
 * @author Lars Helge Overland
 * @version $Id: CrossTabDataValue.java 5514 2008-08-04 10:48:07Z larshelg $
 */
public class DefaultHibernateCacheManager
    implements HibernateCacheManager
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private SessionFactory sessionFactory;

    public void setSessionFactory( SessionFactory sessionFactory )
    {
        this.sessionFactory = sessionFactory;
    }

    // -------------------------------------------------------------------------
    // HibernateCacheManager implementation
    // -------------------------------------------------------------------------

    @SuppressWarnings( "unchecked" )
    public void clearObjectCache()
    {
        Map<String, ClassMetadata> classMetaData = sessionFactory.getAllClassMetadata();
        
        for ( String entityName : classMetaData.keySet() )
        {
            sessionFactory.evictEntity( entityName );
        }

        Map<String, ClassMetadata> collectionMetaData = sessionFactory.getAllCollectionMetadata();
        
        for ( String roleName : collectionMetaData.keySet() )
        {
            sessionFactory.evictCollection( roleName );
        }
    }
    
    public void clearQueryCache()
    {
        sessionFactory.evictQueries();
    }
    
    public void clearCache()
    {
        clearObjectCache();
        
        clearQueryCache();
    }
    
    public Statistics getStatistics()
    {
        return sessionFactory.getStatistics();
    }
}
