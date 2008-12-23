package org.apache.maven.it;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.maven.it.util.ResourceExtractor;

/**
 * This is a test set for <a href="http://jira.codehaus.org/browse/MNG-1830">MNG-1830</a>.
 * 
 * @author Brett Porter
 * @version $Id: MavenITmng3906MergedPluginClassPathOrderingTest.java 726432 2008-12-14 13:20:02Z bentmann $
 */
public class MavenITmng1830ShowVersionTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng1830ShowVersionTest()
    {
        // TODO: reinstate for 3.0
        super( "(2.0.10,2.1.0-M1),(2.1.0-M1,2.999)" ); // Maven 2.0.11 + , 2.1.0-M2 +
    }

    /**
     * Test that the version format
     */
    public void testVersion()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-1830" );

        Verifier verifier = new Verifier( testDir.getAbsolutePath() );
        List cliOptions = Collections.singletonList( "-X" );
        verifier.setCliOptions( cliOptions );
        verifier.executeGoal( "clean" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        String line = (String) verifier.loadFile( verifier.getBasedir(), verifier.getLogFileName(), false ).get( 1 );
        assertTrue( line, line.matches( "^Apache Maven (.*?) \\(r[0-9]+; .*\\)$" ) );

        // check timestamp parses
        String timestamp = line.substring( line.lastIndexOf( ';' ) + 1, line.length() - 1 ).trim();
        SimpleDateFormat fmt = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ssZ" );
        Date date = fmt.parse( timestamp );
        assertEquals( timestamp, fmt.format( date ) );
    }
}