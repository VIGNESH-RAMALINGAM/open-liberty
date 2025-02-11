/*******************************************************************************
 * Copyright (c) 2018 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.ws.springboot.support.fat;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import componenttest.annotation.MaximumJavaLevel;
import componenttest.custom.junit.runner.FATRunner;

@RunWith(FATRunner.class)
@MaximumJavaLevel(javaLevel = 8)
public class CommonWebServerTests15 extends CommonWebServerTests {
    @AfterClass
    public static void stopTestServer() throws Exception {
        if (!javaVersion.startsWith("1.")) {
            server.stopServer("CWWKC0265W");
        }
    }

    @Test
    public void testBasicSpringBootApplication15() throws Exception {
        testBasicSpringBootApplication();
    }

    @Override
    public Set<String> getFeatures() {
        return new HashSet<>(Arrays.asList("springBoot-1.5", "servlet-3.1"));
    }

    @Override
    public String getApplication() {
        return SPRING_BOOT_15_APP_BASE;
    }

    @Test
    public void expectWarningWhenHigherThanJava8IsUsedWithSpringBoot15() throws Exception {
        List<String> logMessages = server.findStringsInLogs("CWWKC0265W");
        if (!javaVersion.startsWith("1.")) {
            assertTrue("Expected warning message CWWKC0265W not found", !logMessages.isEmpty() && logMessages.size() == 1);
        } else {
            assertTrue("CWWKC0265W warning message should not appear when java versions below 9 is used with Spring Boot 1.5.x and below", logMessages.isEmpty());
        }
    }
}