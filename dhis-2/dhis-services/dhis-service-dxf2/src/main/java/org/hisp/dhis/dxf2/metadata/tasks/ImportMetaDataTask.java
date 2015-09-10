package org.hisp.dhis.dxf2.metadata.tasks;

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

import org.hisp.dhis.security.SecurityContextRunnable;
import org.hisp.dhis.dxf2.common.ImportOptions;
import org.hisp.dhis.dxf2.metadata.ImportService;
import org.hisp.dhis.dxf2.metadata.MetaData;
import org.hisp.dhis.scheduling.TaskId;

/**
 * @author Morten Olav Hansen <mortenoh@gmail.com>
 */
public class ImportMetaDataTask
    extends SecurityContextRunnable
{
    private String userUid;

    private final ImportService importService;

    private final ImportOptions importOptions;

    private final TaskId taskId;

    private final MetaData metaData;

    public ImportMetaDataTask( String userUid, ImportService importService, ImportOptions importOptions, TaskId taskId, MetaData metaData )
    {
        super();
        this.userUid = userUid;
        this.importService = importService;
        this.importOptions = importOptions;
        this.taskId = taskId;
        this.metaData = metaData;
    }

    @Override
    public void call()
    {
        importService.importMetaData( userUid, metaData, importOptions, taskId );
    }
}
