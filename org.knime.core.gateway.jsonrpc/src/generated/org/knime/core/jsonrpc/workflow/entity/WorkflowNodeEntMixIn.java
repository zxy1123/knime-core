/*
 * ------------------------------------------------------------------------
 *
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
 * ---------------------------------------------------------------------
 *
 */
package org.knime.core.jsonrpc.workflow.entity;

import java.util.List;
import java.util.Optional;
import org.knime.core.gateway.v0.workflow.entity.BoundsEnt;
import org.knime.core.gateway.v0.workflow.entity.JobManagerEnt;
import org.knime.core.gateway.v0.workflow.entity.NodeAnnotationEnt;
import org.knime.core.gateway.v0.workflow.entity.NodeEnt;
import org.knime.core.gateway.v0.workflow.entity.NodeInPortEnt;
import org.knime.core.gateway.v0.workflow.entity.NodeMessageEnt;
import org.knime.core.gateway.v0.workflow.entity.NodeOutPortEnt;
import org.knime.core.gateway.v0.workflow.entity.WorkflowNodeEnt;
import org.knime.core.gateway.v0.workflow.entity.impl.DefaultWorkflowNodeEnt;
import org.knime.core.gateway.v0.workflow.entity.impl.DefaultWorkflowNodeEntBuilder;


import org.knime.core.jsonrpc.JsonRpcUtil;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * MixIn class for entity implementations that adds jackson annotations for de-/serialization.
 *
 * @author Martin Horn, University of Konstanz
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = JsonRpcUtil.ENTITY_TYPE_KEY,
    defaultImpl = DefaultWorkflowNodeEnt.class)
@JsonSubTypes({
    @Type(value = DefaultWorkflowNodeEnt.class, name="WorkflowNodeEnt")
})
@JsonDeserialize(builder=DefaultWorkflowNodeEntBuilder.class)
// AUTO-GENERATED CODE; DO NOT MODIFY
public interface WorkflowNodeEntMixIn extends WorkflowNodeEnt {

	@Override
	@JsonProperty("WorkflowIncomingPorts")
    public List<NodeOutPortEnt> getWorkflowIncomingPorts();
    
	@Override
	@JsonProperty("WorkflowOutgoingPorts")
    public List<NodeInPortEnt> getWorkflowOutgoingPorts();
    
	@Override
	@JsonProperty("IsEncrypted")
    public boolean getIsEncrypted();
    
	@Override
	@JsonProperty("ParentNodeID")
    public Optional<String> getParentNodeID();
    
	@Override
	@JsonProperty("RootWorkflowID")
    public String getRootWorkflowID();
    
	@Override
	@JsonProperty("JobManager")
    public Optional<JobManagerEnt> getJobManager();
    
	@Override
	@JsonProperty("NodeMessage")
    public NodeMessageEnt getNodeMessage();
    
	@Override
	@JsonProperty("InPorts")
    public List<NodeInPortEnt> getInPorts();
    
	@Override
	@JsonProperty("OutPorts")
    public List<NodeOutPortEnt> getOutPorts();
    
	@Override
	@JsonProperty("Name")
    public String getName();
    
	@Override
	@JsonProperty("NodeID")
    public String getNodeID();
    
	@Override
	@JsonProperty("NodeType")
    public String getNodeType();
    
	@Override
	@JsonProperty("Bounds")
    public BoundsEnt getBounds();
    
	@Override
	@JsonProperty("IsDeletable")
    public boolean getIsDeletable();
    
	@Override
	@JsonProperty("NodeState")
    public String getNodeState();
    
	@Override
	@JsonProperty("HasDialog")
    public boolean getHasDialog();
    
	@Override
	@JsonProperty("NodeAnnotation")
    public NodeAnnotationEnt getNodeAnnotation();
    

}
