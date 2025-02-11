/*******************************************************************************
 * Copyright (c) 2009, 2018 IBM Corporation and others.
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
package com.ibm.ws.ejbcontainer.interceptor.aroundTimeout_exc.ejb;

import java.util.concurrent.CountDownLatch;

public interface ATNoExInterface {
    public static final String AUTO_TIMER_INFO = "automaticTimerNoEx";

    public CountDownLatch getAutoTimerLatch();

    public CountDownLatch createSingleActionTimer(String info);
}
