/* ------------------------------------------------------------------
 * This source code, its documentation and all appendant files
 * are protected by copyright law. All rights reserved.
 *
 * Copyright by KNIME.com, Zurich, Switzerland
 *
 * You may not modify, publish, transmit, transfer or sell, reproduce,
 * create derivative works from, distribute, perform, display, or in
 * any way exploit any of the content, in whole or in part, except as
 * otherwise expressly permitted in writing by the copyright owner or
 * as specified in the license file distributed with this product.
 *
 * If you have any questions please contact the copyright holder:
 * website: www.knime.com
 * email: contact@knime.com
 * ---------------------------------------------------------------------
 *
 * History
 *   Created on 27.08.2014 by koetter
 */
package org.knime.core.node.port.database.aggregation.function.postgresql;

import org.knime.core.data.DataType;
import org.knime.core.data.DoubleValue;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.node.port.database.StatementManipulator;
import org.knime.core.node.port.database.aggregation.DBAggregationFunction;
import org.knime.core.node.port.database.aggregation.function.column.AbstractColumnDBAggregationFunction;

/**
 * Aggregation function.
 * @author Tobias Koetter, KNIME.com, Zurich, Switzerland
 * @since 2.11
 */
public class RegrSXYDBAggregationFunction extends AbstractColumnDBAggregationFunction {

    /**
     * Constructor.
     */
    public RegrSXYDBAggregationFunction() {
        super("X column: ", null, DoubleValue.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataType getType(final DataType originalType) {
        return DoubleCell.TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSQLFragment(final StatementManipulator manipulator, final String tableName,
        final String colName) {
        return getLabel() + "(" + manipulator.quoteIdentifier(tableName) + "." + manipulator.quoteIdentifier(colName)
                + ", " + manipulator.quoteIdentifier(tableName) + "."
                + manipulator.quoteIdentifier(getSelectedColumnName()) + ")";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DBAggregationFunction createInstance() {
        return new RegrSXYDBAggregationFunction();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return "REGR_SXY";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return getLabel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCompatible(final DataType type) {
        return type.isCompatible(DoubleValue.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return "The function regr_sxy(Y, X) returns the 'sum of products' of  of independent times dependent variable "
                + "(sum(X*Y) - sum(X) * sum(Y)/N).";
    }
}
