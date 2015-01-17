package org.hisp.dhis.webapi.controller;

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

import javax.servlet.http.HttpServletResponse;

import org.hisp.dhis.email.Email;
import org.hisp.dhis.email.EmailService;
import org.hisp.dhis.setting.SystemSettingManager;
import org.hisp.dhis.user.CurrentUserService;
import org.hisp.dhis.webapi.utils.ContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Halvdan Hoem Grelland <halvdanhg@gmail.com>
 */
@Controller
@RequestMapping( value = EmailController.RESOURCE_PATH )
public class EmailController
{
    public static final String RESOURCE_PATH = "/email";

    //--------------------------------------------------------------------------
    // Dependencies
    //--------------------------------------------------------------------------

    @Autowired
    private EmailService emailService;

    @Autowired
    private CurrentUserService currentUserService;
    
    @Autowired
    private SystemSettingManager systemSettingManager;

    @RequestMapping( value = "/test", method = RequestMethod.POST )
    public void sendTestEmail( HttpServletResponse response )
    {
        String userEmail = currentUserService.getCurrentUser().getEmail();
        boolean smtpConfigured = emailService.emailEnabled();
        boolean userEmailConfigured = userEmail != null && !userEmail.isEmpty();

        if ( !smtpConfigured )
        {
            ContextUtils.conflictResponse( response, "Could not send test email, SMTP server not configured" );
            return;
        }
        
        if ( !userEmailConfigured )
        {
            ContextUtils.conflictResponse( response, "Could not send test email, no email configured for current user" );
            return;
        }
        
        emailService.sendTestEmail();

        ContextUtils.okResponse( response, "Test email was sent to " + userEmail );
    }
    
    @RequestMapping( value = "/notification", method = RequestMethod.POST )
    public void sendSystemNotificationEmail( @RequestBody Email email, HttpServletResponse response )
    {
        boolean smtpConfigured = emailService.emailEnabled();
        boolean systemNotificationEmailValid = systemSettingManager.systemNotificationEmailValid();

        if ( !smtpConfigured )
        {
            ContextUtils.conflictResponse( response, "Could not send email, SMTP server not configured" );
            return;
        }
        
        if ( !systemNotificationEmailValid )
        {
            ContextUtils.conflictResponse( response, "Could not send email, system notification email address not set or not valid" );
            return;
        }
        
        emailService.sendSystemEmail( email );
        
        ContextUtils.okResponse( response, "System notification email sent" );
    }
}
