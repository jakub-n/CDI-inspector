package cz.muni.fi.cdii.ecilpse.ui.connectdialog;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;

public class ConnectToServerDialog extends Dialog {
    
    private static final Server[] EMPTY_INPUT = new Server[] {
        new Server("no suitable application")};
    
    private TreeViewer treeViewer;
    private Button treeRadioButton;
    private Button directRadioButton;
    private ComboViewer comboViewer;
    private URL selectedUrl = null;

    /**
     * Create the dialog.
     * @param parentShell
     */
    public ConnectToServerDialog(Shell parentShell) {
        super(parentShell);
        setShellStyle(SWT.RESIZE);
    }

    /**
     * Create contents of the dialog.
     * @param parent
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        //GridLayout gridLayout = (GridLayout) container.getLayout();
        
        directRadioButton = new Button(container, SWT.RADIO);
        directRadioButton.setText("URL of application root context:");
        
        
        comboViewer = new ComboViewer(container, SWT.NONE);
        Combo combo = comboViewer.getCombo();
        combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        comboViewer.setContentProvider(new ArrayContentProvider());
        comboViewer.setLabelProvider(new LabelProvider());
        comboViewer.setInput(ConnectToServerHistoryUtil.getHistory());
        selectFirstComboElementIfExists();
        combo.setFocus();
        combo.addFocusListener(new SelectionTypeFocusListener(this, SelectionType.DIRECT));
        
        treeRadioButton = new Button(container, SWT.RADIO);
        treeRadioButton.setText("Eclipse managed application:");
        treeRadioButton.setEnabled(false);
        
        this.treeViewer = new TreeViewer(container, SWT.BORDER);
        Tree tree = treeViewer.getTree();
        tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        tree.setLinesVisible(true);
        tree.addFocusListener(new SelectionTypeFocusListener(this, SelectionType.TREE));
        treeViewer.addSelectionChangedListener(new SelectModulesOnlyListener(this.treeViewer));
        treeViewer.addDoubleClickListener(new SelectModulesOnlyListener(treeViewer));
        treeViewer.setContentProvider(new ServersTreeContentProvider());
        treeViewer.setLabelProvider(new ServersLabelProvider());
        tree.setEnabled(false);
        treeViewer.setInput(ConnectToServerDialog.EMPTY_INPUT);
        new Thread(new AsyncModelComputation(this)).start();
        
        this.setSelectionType(SelectionType.DIRECT);
        
        return container;
    }

    private void selectFirstComboElementIfExists() {
        Object firstElement = comboViewer.getElementAt(0);
        if (firstElement != null) {
            comboViewer.setSelection(new StructuredSelection(firstElement));
            int textLength = comboViewer.getCombo().getText().length();
            comboViewer.getCombo().setSelection(new Point(0, textLength - 1));
        }
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Inspect running application");
    }

    /**
     * Create contents of the button bar.
     * @param parent
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
        createButton(parent, IDialogConstants.OK_ID, "Inspect", true);
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(450, 350);
    }
    
    @Override
    protected void okPressed() {
        this.selectedUrl = retrieveSelectedUrl();
        if (this.selectedUrl == null) {
            return;
        }
        ConnectToServerHistoryUtil.saveLast(selectedUrl.toString());
        super.okPressed();
    };

    public void setSelectionType(SelectionType type) {
        switch (type) {
        case TREE:
            this.treeRadioButton.setSelection(true);
            this.directRadioButton.setSelection(false);
            break;
        case DIRECT:
            this.treeRadioButton.setSelection(false);
            this.directRadioButton.setSelection(true);
            break;
        }
    }

    private URL retrieveSelectedUrl() {
        if (this.directRadioButton.getSelection() && ! this.treeRadioButton.getSelection()) {
            String text = this.comboViewer.getCombo().getText().trim();
            try {
                URL url = new URL(text);
                return url;
            } catch (MalformedURLException e) {
                showWarningMessageBox("Malformed URL", "Selected URL is not valid.");
                return null;
            }
        }
        if (! this.directRadioButton.getSelection() && this.treeRadioButton.getSelection()) {
            ITreeSelection selection = (ITreeSelection) this.treeViewer.getSelection();
            if (selection.isEmpty()) {
                showWarningMessageBox("No application selected", "No application selected");
                return null;
            }
            Module module = (Module) selection.getFirstElement();
            return module.getUrl();
        }
        throw new RuntimeException("Unexpected dialog selection state");
    }

    private void showWarningMessageBox(String title, String message) {
        MessageBox dialog = new MessageBox(this.getShell(), SWT.ICON_WARNING | SWT.OK );
        dialog.setText(title);
        dialog.setMessage(message);
        dialog.open();
    }

    public URL getSelectedUrl() {
        return selectedUrl;
    }
    
    /**
     * It has to be run in SWT UI thread.
     */
    void setTreeViewerInput(Server[] input) {
        this.treeViewer.setInput(input);
        this.treeViewer.getTree().setEnabled(true);
        this.treeViewer.expandAll();
        this.treeRadioButton.setEnabled(true);
    }
    
    private static final class SelectionTypeFocusListener implements FocusListener {
        
        private SelectionType selectionType;
        private ConnectToServerDialog dialog;

        public SelectionTypeFocusListener(ConnectToServerDialog dialog, 
                SelectionType selectionType) {
            this.selectionType = selectionType;
            this.dialog = dialog;
        }

        @Override
        public void focusGained(FocusEvent e) {
            dialog.setSelectionType(this.selectionType);
        }

        @Override
        public void focusLost(FocusEvent e) {
            // nothing
        }
        
    }
    
    private static final class SelectModulesOnlyListener implements ISelectionChangedListener, 
            IDoubleClickListener {
        
        private final TreeViewer treeViewer;
        
        public SelectModulesOnlyListener(TreeViewer treeViewer) {
            this.treeViewer = treeViewer;
        }
        
        private void handleSelection(IStructuredSelection selection) {
            if (selection.isEmpty()) {
                return;
            }
            boolean isModuleSelected = selection.getFirstElement() instanceof Module;
            if (!isModuleSelected) {
                Display.getDefault().asyncExec(new Runnable() {
                    
                    @Override
                    public void run() {
                        treeViewer.setSelection(StructuredSelection.EMPTY);
                    }
                });
            }
        }

        @Override
        public void selectionChanged(SelectionChangedEvent event) {
            IStructuredSelection selection = (IStructuredSelection) event.getSelection();
           this.handleSelection(selection);
        }

        @Override
        public void doubleClick(DoubleClickEvent event) {
            IStructuredSelection selection = (IStructuredSelection) event.getSelection();
            this.handleSelection(selection);
        }
    }
    
    private static class ServersLabelProvider extends StyledCellLabelProvider {
        
        @Override
        public String getToolTipText(Object element) {
            if (element instanceof Module) {
                final Module module = (Module) element;
                return module.getUrl().toString();
            }
            return null;
        }

        @Override
        public void update(ViewerCell cell) {
            Object element = cell.getElement();
            if (element instanceof Server) {
                final Server server = (Server) element;
                final String text = server.getName();
                cell.setText(text);
            }
            if (element instanceof Module) {
                final Module module = (Module) element;
                final String text = module.getName();
                final StyledString styledString = new StyledString();
                styledString.append(text)
                    .append(" [" + module.getUrl().toString() + "]", 
                            StyledString.COUNTER_STYLER);
                cell.setText(styledString.getString());
                cell.setStyleRanges(styledString.getStyleRanges());
            }
        }
        
    }
    
    /**
     * Content provider for {@link TreeViewer} to select module to inspect.
     * It expects {@link Server}[] as input.
     */
    private static class ServersTreeContentProvider implements ITreeContentProvider {

        @Override
        public void dispose() {
            // do nothing
        }

        @Override
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            // do nothing
        }

        @Override
        public Object[] getElements(Object inputElement) {
            final Server[] treeRoot = (Server[]) inputElement;
            return treeRoot;
        }

        @Override
        public Object[] getChildren(Object parentElement) {
            if (parentElement instanceof Server) {
                final Server server = (Server) parentElement;
                return server.getModules().toArray();
            }
            return new Object[0];
        }

        @Override
        public Object getParent(Object element) {
            if (element instanceof Module) {
                final Module module = (Module) element;
                return module.getServer();
            }
            return null;
        }

        @Override
        public boolean hasChildren(Object element) {
            if (element instanceof Server) {
                return true;
            }
            if (element instanceof Module) {
                return false;
            }
            throw new RuntimeException("Unknown parent element.");
        }
        
    }
    
    private static enum SelectionType {
        TREE, DIRECT
    }
    
}
