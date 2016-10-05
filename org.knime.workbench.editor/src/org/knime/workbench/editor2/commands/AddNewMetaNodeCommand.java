/*
 * ------------------------------------------------------------------------
 *  Copyright by KNIME GmbH, Konstanz, Germany
 *  Website: http://www.knime.org; Email: contact@knime.org
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License, Version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 *  Additional permission under GNU GPL version 3 section 7:
 *
 *  KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
 *  Hence, KNIME and ECLIPSE are both independent programs and are not
 *  derived from each other. Should, however, the interpretation of the
 *  GNU GPL Version 3 ("License") under any applicable laws result in
 *  KNIME and ECLIPSE being a combined program, KNIME GMBH herewith grants
 *  you the additional permission to use and propagate KNIME together with
 *  ECLIPSE with only the license terms in place for ECLIPSE applying to
 *  ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
 *  license terms of ECLIPSE themselves allow for the respective use and
 *  propagation of ECLIPSE together with KNIME.
 *
 *  Additional permission relating to nodes for KNIME that extend the Node
 *  Extension (and in particular that are based on subclasses of NodeModel,
 *  NodeDialog, and NodeView) and that only interoperate with KNIME through
 *  standard APIs ("Nodes"):
 *  Nodes are deemed to be separate and independent programs and to not be
 *  covered works.  Notwithstanding anything to the contrary in the
 *  License, the License does not apply to Nodes, you are not required to
 *  license Nodes under the License, and you are granted a license to
 *  prepare and propagate Nodes, in each case even if such Nodes are
 *  propagated with or for interoperation with KNIME.  The owner of a Node
 *  may freely choose the license terms applicable to such Node, including
 *  when such Node is propagated with or for interoperation with KNIME.
 * -------------------------------------------------------------------
 *
 * History
 *   21.06.2012 (Peter Ohl): created
 */
package org.knime.workbench.editor2.commands;

import org.eclipse.draw2d.geometry.Point;
import org.knime.core.api.node.workflow.INodeContainer;
import org.knime.core.api.node.workflow.NodeUIInformation;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.port.PortType;
import org.knime.core.node.util.UseImplUtil;
import org.knime.core.node.workflow.NodeID;
import org.knime.core.node.workflow.WorkflowManager;
import org.knime.workbench.editor2.WorkflowEditor;

/**
 * GEF command for adding a new empty metanode (called from the new metanode wizard).
 *
 * @author Peter Ohl, KNIME.com, Zurich, Switzerland
 */
public class AddNewMetaNodeCommand extends AbstractKNIMECommand {

    private static final NodeLogger LOGGER = NodeLogger.getLogger(AddNewMetaNodeCommand.class);

    private PortType[] m_inPorts;

    private PortType[] m_outPorts;

    private String m_name;

    private Point m_location;

    // for undo
    private NodeID m_metanodeID;

    /**
     * Creates a new command.
     *
     * @param workflowManager hostWFM
     * @param inPorts
     * @param outPorts
     * @param name
     * @param location
     */
    public AddNewMetaNodeCommand(final WorkflowManager workflowManager, final PortType[] inPorts,
            final PortType[] outPorts, final String name, final Point location) {
        super(workflowManager);
        m_inPorts = inPorts.clone();
        m_outPorts = outPorts.clone();
        m_name = name;
        m_location = location;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canExecute() {
        if (!super.canExecute()) {
            return false;
        }
        return (m_inPorts != null && m_outPorts != null && m_name != null && m_location != null);
    }

    /** {@inheritDoc} */
    @Override
    public void execute() {
        m_metanodeID = UseImplUtil.getWFMImplOf(getHostWFM()).createAndAddSubWorkflow(m_inPorts, m_outPorts, m_name).getID();
        // create extra info and set it
        INodeContainer cont = getHostWFM().getNodeContainer(m_metanodeID);
        NodeUIInformation.Builder infoBuilder = NodeUIInformation.builder().setNodeLocation(m_location.x, m_location.y, -1, -1);
        if (WorkflowEditor.getActiveEditorSnapToGrid()) {
            infoBuilder.setSnapToGrid(true);
        }
        cont.setUIInformation(infoBuilder.build());
    }

    /** {@inheritDoc} */
    @Override
    public boolean canUndo() {
        return m_metanodeID != null && getHostWFM().canRemoveNode(m_metanodeID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void undo() {
        try {
            getHostWFM().removeNode(m_metanodeID);
        } catch (Exception e) {
            LOGGER.error("Undo failed! Removal of metanode " + m_metanodeID + " failed: " + e.getMessage(), e);
            // prevent a redo from happening (and inserting yet another metanode).
            m_name = null;
            m_inPorts = null;
            m_outPorts = null;
            throw new RuntimeException(e);
        }
    }

}
