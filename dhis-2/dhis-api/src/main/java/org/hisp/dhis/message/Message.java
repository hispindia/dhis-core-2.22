package org.hisp.dhis.message;

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

import java.util.Date;
import java.util.UUID;

import org.hisp.dhis.user.User;

/**
 * @author Lars Helge Overland
 */
public class Message
{
    /**
     * The message internal identifier.
     */
    private int id;

    /**
     * The unique key for the message. Will be auto-generated by the constructors.
     */
    private String key;
    
    /**
     * The message text.
     */
    private String text;
    
    /**
     * The message meta data, like user agent and OS of sender.
     */
    private String metaData;

    /**
     * The message sender.
     */
    private User sender;
    
    /**
     * The date the message was sent.
     */
    private Date sentDate;

    public Message()
    {
        this.key = UUID.randomUUID().toString();
        this.sentDate = new Date();
    }
    
    public Message( String text, String metaData, User sender )
    {
        this.key = UUID.randomUUID().toString();
        this.text = text;
        this.metaData = metaData;
        this.sender = sender;
        this.sentDate = new Date();
    }
    
    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey( String key )
    {
        this.key = key;
    }

    public String getText()
    {
        return text;
    }

    public void setText( String text )
    {
        this.text = text;
    }

    public String getMetaData()
    {
        return metaData;
    }

    public void setMetaData( String metaData )
    {
        this.metaData = metaData;
    }

    public User getSender()
    {
        return sender;
    }

    public void setSender( User sender )
    {
        this.sender = sender;
    }

    public Date getSentDate()
    {
        return sentDate;
    }

    public void setSentDate( Date sentDate )
    {
        this.sentDate = sentDate;
    }
    
    @Override
    public int hashCode()
    {
        return key.hashCode();
    }

    @Override
    public boolean equals( Object object )
    {
        if ( this == object )
        {
            return true;
        }
        
        if ( object == null )
        {
            return false;
        }
        
        if ( getClass() != object.getClass() )
        {
            return false;
        }
        
        final Message other = (Message) object;
        
        return key.equals( other.key );
    }
    
    @Override
    public String toString()
    {
        return "[" + text + "]";
    }
}
