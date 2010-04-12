package org.hisp.dhis.jdbc.statementbuilder;

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

import org.amplecode.quick.JdbcConfiguration;
import org.amplecode.quick.StatementDialect;
import org.hisp.dhis.jdbc.StatementBuilder;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class StatementBuilderFactoryBean
    implements FactoryBean
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    
    private JdbcConfiguration jdbcConfiguration;

    public void setJdbcConfiguration( JdbcConfiguration jdbcConfiguration )
    {
        this.jdbcConfiguration = jdbcConfiguration;
    }
    
    private StatementBuilder statementBuilder;

    // -------------------------------------------------------------------------
    // Initialisation
    // -------------------------------------------------------------------------
    
    public void init()
    {
        StatementDialect dialect = jdbcConfiguration.getDialect();
        
        if ( dialect.equals( StatementDialect.MYSQL ) )
        {
            this.statementBuilder = new MySQLStatementBuilder();
        }
        else if ( dialect.equals( StatementDialect.POSTGRESQL ) )
        {
            this.statementBuilder = new PostgreSQLStatementBuilder();
        }
        else if ( dialect.equals( StatementDialect.H2 ) )
        {
            this.statementBuilder = new H2StatementBuilder();
        }
        else if ( dialect.equals( StatementDialect.DERBY ) )
        {
            this.statementBuilder = new DerbyStatementBuilder();
        }
        else
        {
            throw new RuntimeException( "Unsupported dialect: " + dialect.toString() );
        }
    }

    // -------------------------------------------------------------------------
    // FactoryBean implementation
    // -------------------------------------------------------------------------
    
    public Object getObject()
        throws Exception
    {
        return statementBuilder;
    }

    public Class<?> getObjectType()
    {
        return StatementBuilder.class;
    }

    public boolean isSingleton()
    {
        return true;
    }
}
