/*******************************************************************************
 * Copyright (c) 2007, 2023 IBM Corporation and others.
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
package com.ibm.tx.jta.util.alarm;

import java.util.concurrent.ScheduledFuture;

import com.ibm.tx.util.alarm.Alarm;

public class AlarmImpl implements Alarm {
    private final ScheduledFuture<?> _future;

    public AlarmImpl(ScheduledFuture<?> future) {
        _future = future;
    }

    @Override
    public boolean cancel() {
        return _future.cancel(false);
    }
}