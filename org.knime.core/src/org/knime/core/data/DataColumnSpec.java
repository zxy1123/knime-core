/* 
 * --------------------------------------------------------------------- *
 * This source code, its documentation and all appendant files
 * are protected by copyright law. All rights reserved.
 *
 * Copyright, 2003 - 2006
 * University of Konstanz, Germany.
 * Chair for Bioinformatics and Information Mining
 * Prof. Dr. Michael R. Berthold
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
 * --------------------------------------------------------------------- *
 * 
 * History
 *    25.10.2006 (tg): cleanup
 *    31.10.2006 (tm, cs): reviewed
 */
package org.knime.core.data;

import org.knime.core.data.property.ColorHandler;
import org.knime.core.data.property.ShapeHandler;
import org.knime.core.data.property.SizeHandler;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.ConfigRO;
import org.knime.core.node.config.ConfigWO;

/**
 * A <code>DataColumnSpec</code> describes one column in a 
 * {@link org.knime.core.data.DataTable}.
 * It contains information about type, name, domain, data properties, and
 * optionally color/size/shape handling. This class can only be created using
 * the {@link org.knime.core.data.DataColumnSpecCreator} within this package.
 * 
 * @see DataColumnSpecCreator
 * @see DataTableSpec
 * 
 * @author Michael Berthold, University of Konstanz
 * 
 */
public final class DataColumnSpec {

    /** Keeps the column name. */
    private final String m_name;

    /** Keeps the column type. */
    private final DataType m_type;

    /** Keeps the column domain with additional meta-info. */
    private final DataColumnDomain m_domain;

    /** Contains column annotations. */
    private final DataColumnProperties m_properties;

    /** Holds the SizeHandler if one was set or null. */
    private final SizeHandler m_sizeHandler;

    /** Holds the ShapeHandler if one was set or null. */
    private final ShapeHandler m_shapeHandler;

    /** Holds the ColorHandler if one was set or null. */
    private final ColorHandler m_colorHandler;

    /** Config key for the column name. */
    private static final String CFG_COLUMN_NAME = "column_name";

    /** Config key for the column type. */
    private static final String CFG_COLUMN_TYPE = "column_type";

    /** Config key for the domain information. */
    private static final String CFG_COLUMN_DOMAIN = "column_domain";

    /** Config key for additional annotations. */
    private static final String CFG_COLUMN_PROPS = "column_properties";

    /** Config key for the ColorHandler. */
    private static final String CFG_COLORS = "color_handler";

    /** Config key for the SizeHandler. */
    private static final String CFG_SIZES = "size_handler";

    /** Config key for the ShapeHandler. */
    private static final String CFG_SHAPES = "shape_handler";

    /**
     * Constructor taking all properties of this column spec as arguments. It
     * creates a read-only <code>DataColumnSpec</code> and should only be
     * called from the {@link DataColumnSpecCreator} in this package.
     * 
     * @param name the name of the column, must not be <code>null</code>
     * @param type the type of the column, must not be <code>null</code>
     * @param domain the domain, must not be <code>null</code>
     * @param props additional properties, must not be <code>null</code>
     * @param sizeHdl the <code>SizeHandler</code> or <code>null</code>
     * @param colorHdl the <code>ColorHandler</code> or <code>null</code>
     * @param shapeHdl the <code>ShapeHandler</code> or <code>null</code>
     * @throws NullPointerException if either column name, type, domain, or
     *             properties are <code>null</code>
     */
    DataColumnSpec(final String name, final DataType type,
            final DataColumnDomain domain, final DataColumnProperties props,
            final SizeHandler sizeHdl, final ColorHandler colorHdl,
            final ShapeHandler shapeHdl) {
        if (name == null || type == null || domain == null || props == null) {
            throw new NullPointerException("Do not init DataColumnSpec with"
                    + " null arguments!");
        }
        m_name = name;
        m_type = type;
        m_domain = domain;
        m_properties = props;
        m_sizeHandler = sizeHdl;
        m_colorHandler = colorHdl;
        m_shapeHandler = shapeHdl;
    }

    /**
     * Returns the name of this column.
     * 
     * @return the column name
     */
    public String getName() {
        return m_name;
    }

    /**
     * Returns the column type which is a subclass of {@link DataType}.
     * 
     * @return the <code>DataType</code> of this column; all data cells of
     *         this column are type-castable to its native type
     * 
     * @see org.knime.core.data.DataType
     * @see org.knime.core.data.DataCell
     */
    public DataType getType() {
        return m_type;
    }

    /**
     * Returns the domain of this column spec including meta-information such as
     * bounds, possible values, etc.
     * 
     * @return the domain of the column spec; can be empty, but never
     *         <code>null</code>
     */
    public DataColumnDomain getDomain() {
        return m_domain;
    }

    /**
     * Returns the properties assigned to this column spec. These properties can
     * be seen as some sort of annotations to this column.
     * 
     * @return the column's annotation properties, never <code>null</code>
     */
    public DataColumnProperties getProperties() {
        return m_properties;
    }

    /**
     * Returns the <code>SizeHandler</code> defined on this column, if
     * available. Otherwise <code>null</code> will be returned.
     * 
     * @return attached <code>SizeHandler</code> or <code>null</code>
     */
    public SizeHandler getSizeHandler() {
        return m_sizeHandler;
    }

    /**
     * Returns the <code>ShapeHandler</code> defined on this column, if
     * available. Otherwise <code>null</code> will be returned.
     * 
     * @return atached <code>ShapeHandler</code> or <code>null</code>
     */
    public ShapeHandler getShapeHandler() {
        return m_shapeHandler;
    }

    /**
     * Returns the <code>ColorHandler</code> defined on this column, if
     * available. Otherwise <code>null</code> will be returned.
     * 
     * @return attached <code>ColorHandler</code> or <code>null</code>
     */
    public ColorHandler getColorHandler() {
        return m_colorHandler;
    }

    /**
     * Two <code>DataColumnSpec</code>s are equal if they are the same
     * instance, that is <code>this==obj</code>. 
     * 
     * @param obj another object to compare this column to
     * @return <code>true</code> if both have the same reference,
     *          otherwise <code>false</code>
     * 
     * @see #equalStructure(DataColumnSpec)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        return (this == obj);
    }
    
    /**
     * Two <code>DataColumnSpec</code>s are equal if they have the same
     * column name and type. Domain info, properties, and handlers are not
     * considered during the comparison. 
     * 
     * @param cspec another <code>DataColumnSpec</code> to compare this column 
     *              to
     * @return <code>true</code> if both have the same column name and type,
     *          otherwise <code>false</code>
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equalStructure(final DataColumnSpec cspec) {
        if (cspec == this) {
            return true;
        }
        if (cspec == null) {
            return false;
        }
        return getName().equals(cspec.getName()) 
                && getType().equals(cspec.getType());
    }
    
    /**
     * Two <code>DataColumnSpec</code>s are equal with domain if the column 
     * name, type, domain, and properties match. Does not check if the handler
     * matches.
     * 
     * @param cspec the <code>DataColumnSpec</code> to check equality
     * @return <code>true</code> if name, type, domain, and properties match
     * @see #equals(Object)
     * @deprecated use {@link #equalStructure(DataColumnSpec)} and check if 
     *             domain and properties matches by yourself
     */
    @Deprecated
    public boolean equalsWithDomain(final DataColumnSpec cspec) {
        if (cspec == this) {
            return true;
        }
        if (cspec == null) {
            return false;
        }
        return getDomain().equals(cspec.getDomain())
                && getProperties().equals(cspec.getProperties())
                && getName().equals(cspec.getName())
                && getType().equals(cspec.getType());
    }

    /**
     * The hash code is computed based on the hash code of column name and type.
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return getName().hashCode() ^ getType().hashCode();
    }

    /**
     * Returns a string summary of this column spec including name and type.
     * 
     * @return a string summary of this column spec with column name and type
     */
    @Override
    public String toString() {
        return "name=" + getName() + ",type=" + getType();
    }

    /**
     * Saves name, type, domain, and properties and - if available - color,
     * size, and shape handler to the given <code>ConfigWO</code>.
     * 
     * @param config write properties into
     * @throws NullPointerException if the config object is <code>null</code>
     */
    public void save(final ConfigWO config) {
        config.addString(CFG_COLUMN_NAME, m_name);
        m_type.save(config.addConfig(CFG_COLUMN_TYPE));
        m_domain.save(config.addConfig(CFG_COLUMN_DOMAIN));
        m_properties.save(config.addConfig(CFG_COLUMN_PROPS));
        if (m_colorHandler != null) {
            m_colorHandler.save(config.addConfig(CFG_COLORS));
        }
        if (m_sizeHandler != null) {
            m_sizeHandler.save(config.addConfig(CFG_SIZES));
        }
        if (m_shapeHandler != null) {
            m_shapeHandler.save(config.addConfig(CFG_SHAPES));
        }
    }

    /**
     * Reads name, type, domain, and properties from the given
     * <code>ConfigRO</code> and - if available - size, shape, and color
     * handler. Returns a new <code>DataColumnSpec</code> object initialized
     * with the information read.
     * 
     * @param config to read properties from
     * @return a new column spec object
     * @throws InvalidSettingsException if one of the non-optional properties is
     *             not available or can't be initialized
     * @throws NullPointerException if the config object is <code>null</code>
     */
    public static DataColumnSpec load(final ConfigRO config)
            throws InvalidSettingsException {
        String name = config.getString(CFG_COLUMN_NAME);
        DataType type = DataType.load(config.getConfig(CFG_COLUMN_TYPE));
        DataColumnDomain domain =
                DataColumnDomain.load(config.getConfig(CFG_COLUMN_DOMAIN));
        DataColumnProperties properties =
                DataColumnProperties.load(config.getConfig(CFG_COLUMN_PROPS));
        ColorHandler color = null;
        if (config.containsKey(CFG_COLORS)) {
            color = ColorHandler.load(config.getConfig(CFG_COLORS));
        }
        SizeHandler size = null;
        if (config.containsKey(CFG_SIZES)) {
            size = SizeHandler.load(config.getConfig(CFG_SIZES));
        }
        ShapeHandler shape = null;
        if (config.containsKey(CFG_SHAPES)) {
            shape = ShapeHandler.load(config.getConfig(CFG_SHAPES));
        }
        return new DataColumnSpec(name, type, domain, properties, size, color,
                shape);
    }

} // DataColumnSpec
