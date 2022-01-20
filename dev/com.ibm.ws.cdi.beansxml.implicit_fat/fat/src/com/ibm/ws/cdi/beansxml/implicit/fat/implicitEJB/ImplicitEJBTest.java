/*******************************************************************************
 * Copyright (c) 2015, 2022 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.ws.cdi.beansxml.implicit.fat.implicitEJB;

import static componenttest.rules.repeater.EERepeatTests.EEVersion.EE7_FULL;
import static componenttest.rules.repeater.EERepeatTests.EEVersion.EE9;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.runner.RunWith;

import com.ibm.websphere.simplicity.CDIArchiveHelper;
import com.ibm.websphere.simplicity.ShrinkHelper;
import com.ibm.websphere.simplicity.ShrinkHelper.DeployOptions;
import com.ibm.ws.cdi.beansxml.implicit.fat.implicitEJB.war.ImplicitEJBServlet;
import com.ibm.ws.cdi.beansxml.implicit.fat.implicitEJB.war.SimpleEJB;
import com.ibm.ws.cdi.beansxml.implicit.fat.utils.SimpleAbstract;

import componenttest.annotation.Server;
import componenttest.annotation.TestServlet;
import componenttest.annotation.TestServlets;
import componenttest.custom.junit.runner.FATRunner;
import componenttest.custom.junit.runner.Mode;
import componenttest.custom.junit.runner.Mode.TestMode;
import componenttest.rules.repeater.EERepeatTests;
import componenttest.rules.repeater.RepeatTests;
import componenttest.topology.impl.LibertyServer;
import componenttest.topology.utils.FATServletClient;

/**
 * Test that wars can be implicit bean archives.
 */
@Mode(TestMode.FULL)
@RunWith(FATRunner.class)
public class ImplicitEJBTest extends FATServletClient {

    public static final String SERVER_NAME = "cdi12ImplicitServer";

    public static final String IMPLICIT_EJB_IN_WAR_APP_NAME = "implicitEJBInWar";

    //not bothering to repeat with EE8 ... the EE9 version is mostly a transformed version of the EE8 code
    @ClassRule
    public static RepeatTests r = EERepeatTests.with(SERVER_NAME, EE9, EE7_FULL);

    @Server(SERVER_NAME)
    @TestServlets({
                    @TestServlet(servlet = ImplicitEJBServlet.class, contextRoot = IMPLICIT_EJB_IN_WAR_APP_NAME) //FULL
    })
    public static LibertyServer server;

    @BeforeClass
    public static void buildShrinkWrap() throws Exception {

        JavaArchive utilLib = ShrinkWrap.create(JavaArchive.class, "utilLib.jar");
        utilLib.addClass(SimpleAbstract.class);
        CDIArchiveHelper.addBeansXML(utilLib, SimpleAbstract.class.getPackage(), "empty-beans.xml");

        WebArchive implicitEJBInWar = ShrinkWrap.create(WebArchive.class, IMPLICIT_EJB_IN_WAR_APP_NAME + ".war")
                                                .addClass(ImplicitEJBServlet.class)
                                                .addClass(SimpleEJB.class)
                                                .addAsLibrary(utilLib);

        ShrinkHelper.exportDropinAppToServer(server, implicitEJBInWar, DeployOptions.SERVER_ONLY);
        server.startServer();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        if (server != null) {
            server.stopServer();
        }
    }

}