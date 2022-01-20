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
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import org.apache.maven.it.util.ResourceExtractor;

public class MavenITmng7003TransitiveDependencyManagement
        extends AbstractMavenIntegrationTestCase
{
    public MavenITmng7003TransitiveDependencyManagement()
    {
        super( "[3.8.4,)" );
    }

    public void testIt() throws IOException, VerificationException
    {
        final File projectDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-7003" );

        final Verifier depsVerifier = newVerifier( new File( projectDir, "deps" ).getAbsolutePath() );
        depsVerifier.executeGoal( "install" );
        depsVerifier.verifyErrorFreeLog();

        final Verifier parentVerifier = newVerifier( new File( projectDir, "parent" ).getAbsolutePath() );
        parentVerifier.executeGoal( "install" );
        parentVerifier.verifyErrorFreeLog();

        final Verifier usageVerifier = newVerifier( new File( projectDir, "usage" ).getAbsolutePath() );
        usageVerifier.executeGoal( "dependency:tree" );
        usageVerifier.verifyErrorFreeLog();
        usageVerifier.verifyTextInLog( "org.apache.maven.its.mng7003:slf4j-api:jar:1.7:compile" );
    }

}
