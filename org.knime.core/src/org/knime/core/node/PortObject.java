/* ------------------------------------------------------------------
 * This source code, its documentation and all appendant files
 * are protected by copyright law. All rights reserved.
 *
 * Copyright, 2003 - 2008
 * University of Konstanz, Germany
 * Chair for Bioinformatics and Information Mining (Prof. M. Berthold)
 * and KNIME GmbH, Konstanz, Germany
 *
 * You may not modify, publish, transmit, transfer or sell, reproduce,
 * create derivative works from, distribute, perform, display, or in
 * any way exploit any of the content, in whole or in part, except as
 * otherwise expressly permitted in writing by the copyright owner or
 * as specified in the license file distributed with this product.
 *
 * If you have any questions please contact the copyright holder:
 * website: www.knime.org
 * email: contact@knime.org
 * ---------------------------------------------------------------------
 * 
 */
package org.knime.core.node;

import java.io.File;
import java.io.IOException;

import org.knime.core.internal.SerializerMethodLoader.Serializer;
import org.knime.core.node.portobject.AbstractPortObject;
import org.knime.core.node.portobject.AbstractSimplePortObject;


/**
 * General interface for objects that are passed along node 
 * connections. Most prominent example of such an object is 
 * {@link org.knime.core.node.BufferedDataTable}. 
 * <code>PortObjects</code> contain the actual data or models, which are used
 * during a node's 
 * {@link GenericNodeModel#execute(PortObject[], ExecutionContext) execution}.
 * 
 * <p><b>Important:</b> Implementors of this interface must also provide a 
 * {@link PortObjectSerializer}, which is used to save and load instances. The
 * framework will try to invoke a static method defined in the implementation
 * with the following signature: 
 * <pre>
 *  public static PortObjectSerializer&lt;FooPortObject&gt; 
 *          getPortObjectSerializer();
 * </pre>
 * If the class does not have such a static method (or it has the wrong 
 * signature), an exception will be thrown at runtime. There are two exceptions
 * to this rule: Objects of class {@link BufferedDataTable} and 
 * {@link ModelContent} are treated separately. As such, they do not define
 * this method (or their implementations throw an exception as the method is
 * not called by the framework). However, if you do not extend either of these
 * two classes, you do need to implement the method mentioned above.  
 * @see org.knime.core.node.BufferedDataTable
 * @see PortObjectSpec
 * @see PortType
 * @see AbstractPortObject
 * @see AbstractSimplePortObject
 * @author Bernd Wiswedel & Michael Berthold, University of Konstanz
 */
public interface PortObject {
    
    /** Factory class that's used for writing and loading objects of class 
     * denoted by <code>T</code>. See description of class {@link PortObject}
     * for details.
     * @param <T> class of the object to save or load. */
    abstract static class PortObjectSerializer<T extends PortObject> 
        implements Serializer<T> {
        
        /** Saves the portObject to a directory location. There is no need
         * to also save the {@link PortObjectSpec} associated with the port
         * object as the framework will save both in different places and
         * will provide the spec when {@link #loadPortObject(
         * File, PortObjectSpec, ExecutionMonitor)} is called.
         * @param portObject The object to save.
         * @param directory Where to save to
         * @param exec To report progress to and to check for cancelation.
         * @throws IOException If that fails for IO problems.
         * @throws CanceledExecutionException If canceled.
         */
        protected abstract void savePortObject(final T portObject,
                final File directory, final ExecutionMonitor exec)
        throws IOException, CanceledExecutionException;
        
        /** Load a portObject from a directory location.
         * @param directory Where to load from
         * @param spec The spec that was associated with the object. It can
         * safely be cast to the expected PortObjectSpec class.
         * @param exec To report progress to and to check for cancelation.
         * @return The restored object.
         * @throws IOException If that fails for IO problems.
         * @throws CanceledExecutionException If canceled.
         */
        protected abstract T loadPortObject(final File directory, 
                final PortObjectSpec spec, final ExecutionMonitor exec)
        throws IOException, CanceledExecutionException;
    }
    
    /**
     * Get specification to this port object. That is, the corresponding
     * {@link PortObjectSpec} which is used to configure any successor node
     * after execution, e.g. a <code>BufferedDataTable</code> can return a
     * <code>DataTableSpec</code>.
     * 
     * <p>Subclasses should narrow the return type if possible.
     * 
     * @return underlying <code>PortObjectSpec</code> or any derived spec,
     *         never <code>null</code>.
     */
    PortObjectSpec getSpec();
    
}
