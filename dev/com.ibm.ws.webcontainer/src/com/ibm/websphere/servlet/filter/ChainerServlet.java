/*******************************************************************************
 * Copyright (c) 1997, 2006 IBM Corporation and others.
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
package com.ibm.websphere.servlet.filter;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.ibm.ejs.ras.TraceNLS;

/**
 * 
 * 
 * Servlet that chains the responses of servlets together.
 * This servlet requires an init parameter 'chained.path.list'
 * that contains a space delimited list of servlet paths that
 * should be chained together.
 *
 * A servlet chain acts as a response pipe that allows servlets
 * to filter the output of the previous servlet in the chain.
 * When a servlet writes header or output stream data to the response,
 * this data is fed into a chained request object that will be passed
 * to the next servlet in the chain.  The next servlet can examine the
 * contents of the chained request to see the response that was
 * generated by the previous servlet. The data written by the last
 * servlet in the chain will be sent back to the client.
 *
 * To setup a servlet chain, an instance of this servlet must be
 * registered as the target servlet in the engine. When this servlet
 * is invoked, the response will be generated by chaining the response
 * of each servlet in the chainer.pathlist parameter.  The
 * response of the final servlet in the chain will be written to the
 * client (Deprecated since WebSphere 6.0).
 *
 * <BR><BR><B>Example usage:</B> Setup a servlet chain at the URI /servlet/upperCaseSnoop for /servlet/snoop-->/servlet/upperCaseFilter.
 * The result of this chain should force the output of snoop to become capitalized.
 * <UL>
 * <LI>Step 1: Register an instance of ChainerServlet and map it to URI /servlet/upperCaseSnoop
 * <LI>Step 2: Add an init parameter of 'chainer.pathlist=/servlet/snoop /servlet/upperCaseFilter'
 * <LI>Step 3: Request the URL: http://host/servlet/upperCaseSnoop
 * </UL>
 *
 *
 * <H3><I>Required init parameters</I></H3>
 * <UL>
 * <LI><B>chainer.pathlist:</B> space separated list of servlet paths to chain together
 * </UL>
 *
 * @deprecated Application developers requiring this functionality
 *  should implement this using javax.servlet.filter classes.
 *
 * @ibm-api
 */
public class ChainerServlet extends HttpServlet{
    /**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3257291335513092404L;
	/**
     * chainer.pathlist: the name of the parameter that specifies the chained servlet path list.
     */
    public static final String PARAM_SERVLET_PATHS = "chainer.pathlist";
    private static TraceNLS nls = TraceNLS.getTraceNLS(ChainerServlet.class, "com.ibm.ws.webcontainer.resources.Messages");

    // PQ47469 (ChainerServlet not thread-safe).
    // private ServletChain _chain;
    private String[] chainPath = null;

    /**
     * Initialize the servlet chainer.
     */
    @SuppressWarnings("unchecked")
    public void init() throws ServletException {
	// Method re-written for PQ47469
	String servlets = getRequiredInitParameter(PARAM_SERVLET_PATHS);
	StringTokenizer sTokenizer = new StringTokenizer(servlets);
	Vector servletChainPath = new Vector();

	while (sTokenizer.hasMoreTokens() == true) {
		String path = sTokenizer.nextToken().trim();
			
		if (path.length() > 0) {
			servletChainPath.addElement(path);
		}
	}

	int chainLength = servletChainPath.size();

	if (chainLength > 0) {
		this.chainPath = new String[chainLength];
			
		for (int index = 0; index < chainLength; ++index) {
			this.chainPath[index] = (String)servletChainPath.elementAt(index);
		}
	}

    }

   /**
    * Handle a servlet request by chaining the configured list of servlets.
    * Only the final response in the chain will be sent back to the client.
    * This servlet does not actual generate any content.  This servlet only
    * constructs and processes the servlet chain.
    * 
    * @param req HttpServletRequest
    * @param resp HttpServletResponse
    * @exception javax.servlet.ServletException
    * @exception java.io.IOException
    */
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	// Method re-written for PQ47469
	ServletChain chain = new ServletChain();
		
	try {
		String path = null;
			
		for (int index = 0; index < this.chainPath.length; ++index) {
			path = this.chainPath[index];
			RequestDispatcher rDispatcher = getServletContext().getRequestDispatcher(path);
			chain.addRequestDispatcher(rDispatcher);
		}
			
		chain.forward(new HttpServletRequestWrapper(request), new HttpServletResponseWrapper(response));
	} finally {
		if (chain != null) {
			chain.clear();
		}
	}
    }


    /**
     * Retrieve a required init parameter by name.
     * @exception javax.servlet.ServletException thrown if the required parameter is not present.
     */
    String getRequiredInitParameter(String name) throws ServletException {
        String value = getInitParameter(name);
        if (value == null) {
            throw new ServletException(MessageFormat.format(nls.getString("Missing.required.initialization.parameter","Missing required initialization parameter: {0}"), new Object[]{name}));
        }

        return value;
    }

    public void destroy() {
        // PQ47469
        // _chain.clear();
    }
}
