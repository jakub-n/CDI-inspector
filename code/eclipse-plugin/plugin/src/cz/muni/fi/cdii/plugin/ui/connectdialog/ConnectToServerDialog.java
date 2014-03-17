package cz.muni.fi.cdii.plugin.ui.connectdialog;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
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
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.model.IURLProvider;
import org.jboss.ide.eclipse.as.core.server.internal.v7.Wildfly8Server;

import com.fasterxml.jackson.databind.ObjectMapper;

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
        GridLayout gridLayout = (GridLayout) container.getLayout();
        
        directRadioButton = new Button(container, SWT.RADIO);
        directRadioButton.setText("URL of application root context:");
        
        
        comboViewer = new ComboViewer(container, SWT.NONE);
        Combo combo = comboViewer.getCombo();
        combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        comboViewer.setContentProvider(new ArrayContentProvider());
        comboViewer.setLabelProvider(new LabelProvider());
        comboViewer.setInput(new String[] {"ahoj", "svete"});
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
            String text = this.comboViewer.getCombo().getText();
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
    private void setTreeViewerInput(Server[] input) {
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
            // TODO remove exception
            throw new RuntimeException("unknown element");
        }
        
    }
    
    private static class AsyncModelComputation implements Runnable {
        
        final ConnectToServerDialog dialog;
        
        public AsyncModelComputation(ConnectToServerDialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void run() {
            Server[] model = this.computeModel();
            this.setModel(model);
        }

        private Server[] computeModel() {
            IServer[] servers = ServerCore.getServers();
            List<IServer> wildflyServers = filterWildflyServers(servers);
            List<IServer> runningServers = filterRunningServers(wildflyServers);
            List<ServerAndModules> modules = extractModules(runningServers);
            List<ServerAndModules> runningModules = filterRunningModuls(modules);
            List<ServerAndModules> cdiiModules = filterCdiiModules(runningModules);
            final Server[] input = createDialogModel(cdiiModules);
            return input;
        }

        private Server[] createDialogModel(List<ServerAndModules> serversAndModules) {
            List<Server> servers = new ArrayList<>();
            for (ServerAndModules serverAndModules : serversAndModules) {
                Server server = createServerModel(serverAndModules);
                servers.add(server);
            }
            return servers.toArray(new Server[0]);
        }

        private Server createServerModel(ServerAndModules serverAndModules) {
            Server server = new Server(serverAndModules.getServer().getName());
            for (IModule iModule : serverAndModules.getModules()) {
                String moduleName = iModule.getName();
                URL moduleUrl = createCdiiStatusUrl(serverAndModules.getServer(), iModule);
                if (moduleUrl == null) {
                    continue;
                }
                Module module = new Module(moduleName, moduleUrl, server);
                server.getModules().add(module);
            }
            return server;
        }

        private List<ServerAndModules> filterCdiiModules(List<ServerAndModules> serversAndModules) {
            List<ServerAndModules> nonEmptyServers = new ArrayList<>();
            List<IsCdiiEnabled> callables = new ArrayList<>();
            for (ServerAndModules serverAndModules : serversAndModules) {
                if (serverAndModules.getModules().isEmpty()) {
                    continue;
                }
                URL cdiiStatusUrl = createCdiiStatusUrl(serverAndModules.getServer(), 
                        serverAndModules.getModules().get(0));
                if (cdiiStatusUrl == null) {
                    continue;
                }
                callables.add(new IsCdiiEnabled(cdiiStatusUrl));
                nonEmptyServers.add(serverAndModules);
                
            }
            if (nonEmptyServers.isEmpty()) {
                return Collections.emptyList();
            }
            
            ExecutorService threadPool = Executors.newFixedThreadPool(
                    Math.min(8, callables.size()));
            List<Future<Boolean>> futureResults;
            try {
                futureResults = threadPool.invokeAll(callables, 5, TimeUnit.SECONDS);
            } catch (InterruptedException e1) {
                return Collections.emptyList();
            }
            threadPool.shutdownNow();
            List<ServerAndModules> result = new ArrayList<>();
            for (int i = 0; i < nonEmptyServers.size(); i++) {
                try {
                    if (futureResults.get(i).get()) {
                        result.add(nonEmptyServers.get(i));
                    }
                } catch (InterruptedException | ExecutionException e) {
                    // nothing
                }
            }
            return result;
        }
        
        /**
         * @return null if creation fail 
         */
        private URL createCdiiStatusUrl(IServer server, IModule module) {
            Wildfly8Server wildfly8Server = getWildfly8Server(server);
            URL moduleUrl = wildfly8Server.getModuleRootURL(module);
            String cdiiStatusUrlString = moduleUrl.toString() + "/cdii/status";
            try {
                URL cdiiStatusUrl = new URL(cdiiStatusUrlString);
                return cdiiStatusUrl;
            } catch (MalformedURLException e) {
                return null;
            }
        }
        
        private Wildfly8Server getWildfly8Server(IServer server) {
            IURLProvider urlProvider = (IURLProvider) server.getAdapter(IURLProvider.class);
            if (urlProvider instanceof Wildfly8Server) {
                return (Wildfly8Server) urlProvider;
            }
            throw new RuntimeException("Unexpected " + IURLProvider.class.getName() + ": " 
                    + (urlProvider == null ? "null" : urlProvider.getClass().getCanonicalName()));
        }

        private List<ServerAndModules> filterRunningModuls(List<ServerAndModules> modules) {
            List<ServerAndModules> result = new ArrayList<>();
            for (ServerAndModules serverAndModules : modules) {
                ServerAndModules serverAndRunningModules = 
                        new ServerAndModules(serverAndModules.getServer());
                serverAndRunningModules.getModules().addAll(filterRunningModules(serverAndModules));
                result.add(serverAndRunningModules);
            }
            return result;
        }

        private List<IModule> filterRunningModules(ServerAndModules serverAndModules) {
            List<IModule> result = new ArrayList<>();
            for (IModule module : serverAndModules.getModules()) {
                if (serverAndModules.getServer().getModuleState(new IModule[] {module}) 
                        == IServer.STATE_STARTED) {
                    result.add(module);
                }
            }
            return result;
        }

        private List<ServerAndModules> extractModules(List<IServer> servers) {
            List<ServerAndModules> result = new ArrayList<>();
            for (IServer server : servers) {
                ServerAndModules serverAndModules = new ServerAndModules(server);
                serverAndModules.getModules().addAll(Arrays.asList(server.getModules()));
                result.add(serverAndModules);
            }
            return result;
        }

        private List<IServer> filterRunningServers(List<IServer> servers) {
            List<IServer> result = new ArrayList<>();
            for (IServer server : servers) {
                if (server.getServerState() == IServer.STATE_STARTED) {
                    result.add(server);
                }
            }
            return result;
        }

        private List<IServer> filterWildflyServers(IServer[] servers) {
            List<IServer> result = new ArrayList<>();
            for (IServer server : servers) {
                IURLProvider urlProvider = (IURLProvider) server.getAdapter(IURLProvider.class);
                if (urlProvider != null && urlProvider instanceof Wildfly8Server) {
                    result.add(server);
                }
            }
            return result;
        }

        private void setModel(final Server[] input) {
            if (input.length == 0) {
                return;
            }
            Display.getDefault().asyncExec(new Runnable() {
            
            @Override
            public void run() {
                dialog.setTreeViewerInput(input);
            }
        });
            
        }
        
        private static class IsCdiiEnabled implements Callable<Boolean> {
            
            private final URL cdiiEnabledUrl;
            
            public IsCdiiEnabled(URL cdiiEnabledUrl) {
                this.cdiiEnabledUrl = cdiiEnabledUrl;
            }

            @Override
            public Boolean call() throws Exception {
                InputStream inputStream = this.cdiiEnabledUrl.openStream();
                ObjectMapper mapper = new ObjectMapper();
                boolean isEnabled = mapper.readTree(inputStream).get("status").asText()
                        .equals("enabled");
                return isEnabled;
            }
            
        }
        
        private static class ServerAndModules {
            
            final private IServer server;
            final private List<IModule> modules = new ArrayList<>();
            
            public ServerAndModules(IServer server) {
                this.server = server;
            }
            public IServer getServer() {
                return server;
            }
            public List<IModule> getModules() {
                return modules;
            }
            
            
        }
    }
    
    private static class Server {
        private String name;
        private List<Module> modules = new ArrayList<>();
        
        public Server(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public List<Module> getModules() {
            return modules;
        }
    }
    
    private static class Module {
        private String name;
        private URL url;
        private Server server;
        
        public Module(String name, URL url, Server server) {
            this.name = name;
            this.url = url;
            this.server = server;
        }
        
        public String getName() {
            return name;
        }
        
        public URL getUrl() {
            return url;
        }

        public Server getServer() {
            return server;
        }
    }
    
    private static enum SelectionType {
        TREE, DIRECT
    }
    
}
