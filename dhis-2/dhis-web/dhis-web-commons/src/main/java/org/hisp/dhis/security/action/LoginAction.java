package org.hisp.dhis.security.action;

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

import com.opensymphony.xwork2.Action;
import org.apache.struts2.ServletActionContext;
import org.hisp.dhis.i18n.resourcebundle.ResourceBundleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author mortenoh
 */
public class LoginAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DeviceResolver deviceResolver;

    public void setDeviceResolver( DeviceResolver deviceResolver )
    {
        this.deviceResolver = deviceResolver;
    }

    @Autowired
    private ResourceBundleManager resourceBundleManager;

    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------

    private Boolean failed = false;

    public void setFailed( Boolean failed )
    {
        this.failed = failed;
    }

    public Boolean getFailed()
    {
        return failed;
    }

    private List<Locale> availableLocales;

    public List<Locale> getAvailableLocales()
    {
        return availableLocales;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
        throws Exception
    {
        Device device = deviceResolver.resolveDevice( ServletActionContext.getRequest() );

        ServletActionContext.getResponse().addHeader( "Login-Page", "true" );

        if ( device.isMobile() || device.isTablet() )
        {
            return "mobile";
        }

        availableLocales = new ArrayList<Locale>( resourceBundleManager.getAvailableLocales() );

        return "standard";
    }
}
