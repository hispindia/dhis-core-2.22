package org.hisp.dhis.security.oauth2;

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

import org.hisp.dhis.oauth2.OAuth2Client;
import org.hisp.dhis.oauth2.OAuth2ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Morten Olav Hansen <mortenoh@gmail.com>
 */
public class DefaultClientDetailsService implements ClientDetailsService
{
    @Autowired
    private OAuth2ClientService oAuth2ClientService;

    @Override
    public ClientDetails loadClientByClientId( String clientId ) throws ClientRegistrationException
    {
        ClientDetails clientDetails = clientDetails( oAuth2ClientService.getOAuth2ClientByClientId( clientId ) );

        if ( clientDetails == null )
        {
            throw new ClientRegistrationException( "Invalid client_id" );
        }

        return clientDetails;
    }

    private ClientDetails clientDetails( OAuth2Client client )
    {
        if ( client == null )
        {
            return null;
        }

        Set<String> grantTypes = new HashSet<>();
        grantTypes.add( "password" );
        grantTypes.add( "authorization_code" );
        grantTypes.add( "refresh_token" );
        grantTypes.add( "client_credentials" );
        grantTypes.add( "implicit" );

        Set<String> scopes = new HashSet<>();
        scopes.add( "ALL" );

        BaseClientDetails clientDetails = new BaseClientDetails();
        clientDetails.setClientId( client.getCid() );
        clientDetails.setClientSecret( client.getSecret() );
        clientDetails.setAuthorizedGrantTypes( grantTypes );
        clientDetails.setScope( scopes );

        return clientDetails;
    }
}