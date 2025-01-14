/*******************************************************************************
 * Copyright (c) 2017 IBM Corporation and others.
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
package app.injection.beans;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Named;

/**
 * Since EJB instances are managed by the EJB container rather than CDI, it does not really
 * make sense to inject a JobContext. So we include this test class, but purposely do NOT
 * have it extend {@link AbstractScopedBean}, to show we considered but rejected this use case.
 */
@Stateless
@LocalBean
@Dependent
@Named("DependentStatelessLocalEJB")
// No-op, essentially this class isn't used
public class DependentStatelessLocalEJB extends AbstractBean {

}
