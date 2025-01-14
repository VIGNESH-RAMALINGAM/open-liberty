/*******************************************************************************
 * Copyright (c) 2019 IBM Corporation and others.
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

package com.ibm.ws.kernel.feature.fat;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.junit.Assert;

import componenttest.topology.impl.LibertyServer;

/**
 *
 */
public class TestUtils {

    /**
     * This method loads the feature.cache file as properties.
     *
     * @param server TODO
     * @param cacheFile - The cache file to read.
     *
     * @return - A properties object containing the properties from the feature.cache file.
     * @throws Exception
     */
    public static Properties getCacheProperties(LibertyServer server, String cacheFile) throws Exception {
        Properties cacheProps = new Properties();
        InputStream cacheStream = null;
        try {
            cacheStream = server.getFileFromLibertyServerRoot(cacheFile).openForReading();
            cacheProps.load(new BufferedReader(new InputStreamReader(cacheStream, "UTF-8")));

            // Race with the server replacing this file
            if (cacheProps.isEmpty()) {
                Thread.currentThread().sleep(1000);
                cacheStream = server.getFileFromLibertyServerRoot(cacheFile).openForReading();
                cacheProps.load(new BufferedReader(new InputStreamReader(cacheStream, "UTF-8")));
            }

            Assert.assertFalse("feature.bundle.cache should not be empty unless there was a race reading it", cacheProps.isEmpty());
        } finally {
            tryToClose(cacheStream);
        }

        return cacheProps;
    }

    /**
     * This method finds the installed features from the trace.log file
     *
     * @param server - Liberty Server
     * @return The installed features list as a string
     * @throws Exception
     */
    public static String getInstalledFeatures(LibertyServer server) throws Exception {
        String installedFeatures = server.waitForStringInTraceUsingLastOffset("all installed features \\[");
        return installedFeatures;
    }

    public static void tryToClose(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException ioe) {
            }
        }
    }

    public static void makeConfigUpdateSetMark(LibertyServer server, String newServerXml) throws Exception {
        // set a new mark
        server.setMarkToEndOfLog(server.getDefaultLogFile());

        // make the configuration update
        server.setServerConfigurationFile(newServerXml);

        // wait for configuration and feature update to complete
        // these two messages can happen in any order!
        server.waitForStringInLogUsingMark("CWWKG0017I");
        server.waitForStringInLogUsingMark("CWWKF0008I");
    }

}
