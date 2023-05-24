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

import org.apache.maven.shared.verifier.Verifier;
import org.apache.maven.shared.verifier.util.ResourceExtractor;
import org.codehaus.plexus.util.Os;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.JRE;

import java.io.File;

/**
 * This is a test set for <a href="https://issues.apache.org/jira/browse/MNG-7737">MNG-7737</a>.
 * Simply verifies that various (expected) profiles are properly activated or not.
 *
 */
class MavenITmng7587Jsr330
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng7587Jsr330()
    {
        // affected Maven versions: 3.9.0
        super( ALL_MAVEN_VERSIONS );
    }

    /**
     * Verify components can be written using JSR330 on JDK 1.8.
     *
     * @throws Exception in case of failure
     */
    @Test
    @Disabled
    void testJdk8() throws Exception
    {
        testJdk( "1.8" );
    }

    /**
     * Verify components can be written using JSR330 on JDK 11.
     *
     * @throws Exception in case of failure
     */
    @Test
    @EnabledOnJre({JRE.JAVA_11, JRE.JAVA_17})
    @Disabled
    void testJdk11() throws Exception
    {
        testJdk( "11" );
    }


    /**
     * Verify components can be written using JSR330 on JDK 17.
     *
     * @throws Exception in case of failure
     */
    @Test
    @EnabledOnJre(JRE.JAVA_17)
    void testJdk17() throws Exception
    {
        testJdk( "17" );
    }

    void testJdk(String jdk) throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-7587-jsr330").getAbsoluteFile();

        final Verifier pluginVerifier = newVerifier( new File( testDir, "plugin" ).getPath() );
        pluginVerifier.setLogFileName( "log-" + jdk + ".txt" );
        pluginVerifier.addCliArgument( "clean" );
        pluginVerifier.addCliArgument( "install" );
        pluginVerifier.addCliArgument( "-V" );
        pluginVerifier.addCliArgument( "-Drunning-java-release-version=" + jdk );
        pluginVerifier.execute();
        pluginVerifier.verifyErrorFreeLog();

        final Verifier consumerVerifier = newVerifier( new File( testDir, "consumer" ).getPath() );
        consumerVerifier.setLogFileName( "log-" + jdk + ".txt" );
        consumerVerifier.addCliArgument( "clean" );
        consumerVerifier.addCliArgument( "verify" );
        consumerVerifier.addCliArgument( "-V" );
        consumerVerifier.addCliArgument( "-Drunning-java-release-version=" + jdk );
        consumerVerifier.execute();
        consumerVerifier.verifyErrorFreeLog();
    }

}
