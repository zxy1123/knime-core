/* @(#)$RCSfile$ 
 * $Revision$ $Date$ $Author$
 * 
 * -------------------------------------------------------------------
 * This source code, its documentation and all appendant files
 * are protected by copyright law. All rights reserved.
 * 
 * Copyright, 2003 - 2006
 * Universitaet Konstanz, Germany.
 * Lehrstuhl fuer Angewandte Informatik
 * Prof. Dr. Michael R. Berthold
 * 
 * You may not modify, publish, transmit, transfer or sell, reproduce,
 * create derivative works from, distribute, perform, display, or in
 * any way exploit any of the content, in whole or in part, except as
 * otherwise expressly permitted in writing by the copyright owner.
 * -------------------------------------------------------------------
 * 
 * History
 *   12.01.2005 (Florian Georg): created
 */
package de.unikn.knime.workbench.ui.wizards.file;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

/**
 * Page to enter the settings for the creation of a new workflow file.
 * 
 * @author Florian Georg, University of Konstanz
 */
public class NewWorkflowPage extends WizardPage {
    private Text m_containerText;

    private Text m_fileText;

    private ISelection m_selection;

    /**
     * Constructor for NewWorkflowPage.
     * 
     * @param selection The initial selection
     */
    public NewWorkflowPage(final ISelection selection) {
        super("wizardPage");
        setTitle("New Hades Workflow File");
        setDescription("This wizard creates a new Hades workflow file.");
        this.m_selection = selection;
    }

    /**
     * @see org.eclipse.jface.dialogs.IDialogPage
     *      #createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(final Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        container.setLayout(layout);
        layout.numColumns = 3;
        layout.verticalSpacing = 9;
        Label label = new Label(container, SWT.NULL);
        label.setText("Select the &containing project:");

        m_containerText = new Text(container, SWT.BORDER | SWT.SINGLE);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        m_containerText.setLayoutData(gd);
        m_containerText.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                dialogChanged();
            }
        });

        Button button = new Button(container, SWT.PUSH);
        button.setText("Select...");
        button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                handleBrowse();
            }
        });
        label = new Label(container, SWT.NULL);
        label.setText("&File name for the new workflow (*.hflow):");

        m_fileText = new Text(container, SWT.BORDER | SWT.SINGLE);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        m_fileText.setLayoutData(gd);
        m_fileText.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                dialogChanged();
            }
        });
        initialize();
        dialogChanged();
        setControl(container);
    }

    /**
     * Tests if the current workbench selection is a suitable container to use.
     */
    private void initialize() {

        if (m_selection instanceof IStructuredSelection
                && (!m_selection.isEmpty())) {
            IStructuredSelection ssel = (IStructuredSelection) m_selection;
            if (ssel.size() > 1) {
                return;
            }
            Object obj = ssel.getFirstElement();
            if (obj instanceof IResource) {
                IContainer container;
                if (obj instanceof IContainer) {
                    container = (IContainer) obj;
                } else {
                    container = ((IResource) obj).getParent();
                }

                m_containerText.setText(container.getFullPath().toString());
            }
        }
        m_fileText.setText("new_file.hflow");
    }

    /**
     * Uses the standard container selection dialog to choose the new value for
     * the container field.
     */

    private void handleBrowse() {
        ContainerSelectionDialog dialog = new ContainerSelectionDialog(
                getShell(), ResourcesPlugin.getWorkspace().getRoot(), false,
                "Select container project");
        if (dialog.open() == Window.OK) {
            Object[] result = dialog.getResult();
            if (result.length == 1) {
                m_containerText.setText(((Path) result[0]).toString());
            }
        }
    }

    /**
     * Ensures that both text fields are set.
     */

    private void dialogChanged() {
        IResource container = ResourcesPlugin.getWorkspace().getRoot()
                .findMember(new Path(getContainerName()));
        String fileName = getFileName();

        if (getContainerName().length() == 0) {
            updateStatus("File container must be specified");
            return;
        }
        boolean f = false;
        f = (container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0;
        if (container == null || f) {
            updateStatus("File container must exist");
            return;
        }
        if (!container.isAccessible()) {
            updateStatus("Project must be writable");
            return;
        }
        if (fileName.length() == 0) {
            updateStatus("File name must be specified");
            return;
        }
        if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
            updateStatus("File name must be valid");
            return;
        }
        int dotLoc = fileName.lastIndexOf('.');
        if (dotLoc != -1) {
            String ext = fileName.substring(dotLoc + 1);
            if (!ext.equalsIgnoreCase("hflow")) {
                updateStatus("File extension must be \"hflow\"");
                return;
            }
        }
        updateStatus(null);
    }

    private void updateStatus(final String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }

    /**
     * @return The container (=project) name
     */
    public String getContainerName() {
        return m_containerText.getText();
    }

    /**
     * @return The file name
     */
    public String getFileName() {
        return m_fileText.getText();
    }
}
