/*
 * reserved comment block
 * DO NOT REMOVE OR ALTER!
 */
/*
 * Copyright 1999-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// $Id: JAXPVariableStack.java,v 1.1.2.1 2005/08/01 01:30:17 jeffsuttor Exp $

package com.sun.apache.xpath.internal.jaxp;

import org.openjdk.javax.xml.transform.TransformerException;
import org.openjdk.javax.xml.xpath.XPathVariableResolver;

import com.sun.apache.xalan.internal.res.XSLMessages;
import com.sun.apache.xpath.internal.objects.XObject;
import com.sun.apache.xml.internal.utils.QName;
import com.sun.apache.xpath.internal.VariableStack;
import com.sun.apache.xpath.internal.XPathContext;

import com.sun.apache.xpath.internal.res.XPATHErrorResources;


/**
 * Overrides {@link VariableStack} and delegates the call to
 * {@link org.openjdk.javax.xml.xpath.XPathVariableResolver}.
 *
 * @author Ramesh Mandava ( ramesh.mandava@sun.com )
 */
public class JAXPVariableStack extends VariableStack {

    private final XPathVariableResolver resolver;

    public JAXPVariableStack(XPathVariableResolver resolver) {
        this.resolver = resolver;
    }

    public XObject getVariableOrParam(XPathContext xctxt, QName qname)
        throws TransformerException,IllegalArgumentException {
        if ( qname == null ) {
            //JAXP 1.3 spec says that if variable name is null then
            // we need to through IllegalArgumentException
            String fmsg = XSLMessages.createXPATHMessage(
                XPATHErrorResources.ER_ARG_CANNOT_BE_NULL,
                new Object[] {"Variable qname"} );
            throw new IllegalArgumentException( fmsg );
        }
        org.openjdk.javax.xml.namespace.QName name =
            new org.openjdk.javax.xml.namespace.QName(
                qname.getNamespace(),
                qname.getLocalPart());
        Object varValue = resolver.resolveVariable( name );
        if ( varValue == null ) {
            String fmsg = XSLMessages.createXPATHMessage(
                XPATHErrorResources.ER_RESOLVE_VARIABLE_RETURNS_NULL,
                new Object[] { name.toString()} );
            throw new TransformerException( fmsg );
        }
        return XObject.create( varValue, xctxt );
    }

}