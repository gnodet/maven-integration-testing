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

import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;

import java.io.File;
import java.util.Properties;

/**
 * This is a test set for <a href="http://jira.codehaus.org/browse/MNG-294">MNG-294</a>.
 * 
 * @author John Casey
 * @version $Id$
 */
public class MavenITmng0294MergeGlobalAndUserSettingsTest
    extends AbstractMavenIntegrationTestCase
{
    public MavenITmng0294MergeGlobalAndUserSettingsTest()
    {
    }

    /**
     * Test merging of global- and user-level settings.xml files.
     */
    public void testitMNG294()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-0294" );
        Verifier verifier = new Verifier( testDir.getAbsolutePath() );
        Properties systemProperties = new Properties();
        systemProperties.put( "org.apache.maven.user-settings", "user-settings.xml" );
        systemProperties.put( "org.apache.maven.global-settings", "global-settings.xml" );
        verifier.setSystemProperties( systemProperties );
        if ( matchesVersionRange( "(2.99,)" ) )
        {
            verifier.getCliOptions().add( "--settings" );
            verifier.getCliOptions().add( new File( testDir, "user-settings.xml" ).getAbsolutePath() );
            verifier.getCliOptions().add( "--global-settings" );
            verifier.getCliOptions().add( new File( testDir, "global-settings.xml" ).getAbsolutePath() );
        }
        verifier.executeGoal( "org.apache.maven.its.plugins:maven-it-plugin-touch:touch" );
        verifier.assertFilePresent( "target/test.txt" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}