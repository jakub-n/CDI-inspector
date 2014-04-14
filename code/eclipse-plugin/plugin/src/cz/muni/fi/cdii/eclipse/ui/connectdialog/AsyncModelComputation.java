package cz.muni.fi.cdii.eclipse.ui.connectdialog;

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

import org.eclipse.swt.widgets.Display;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.model.IURLProvider;
import org.jboss.ide.eclipse.as.core.server.internal.v7.Wildfly8Server;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AsyncModelComputation implements Runnable {
    
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
        List<AsyncModelComputation.ServerAndModules> modules = extractModules(runningServers);
        List<AsyncModelComputation.ServerAndModules> runningModules = filterRunningModuls(modules);
        List<AsyncModelComputation.ServerAndModules> cdiiModules = filterCdiiModules(runningModules);
        final Server[] input = createDialogModel(cdiiModules);
        return input;
    }

    private Server[] createDialogModel(List<AsyncModelComputation.ServerAndModules> serversAndModules) {
        List<Server> servers = new ArrayList<>();
        for (AsyncModelComputation.ServerAndModules serverAndModules : serversAndModules) {
            Server server = createServerModel(serverAndModules);
            servers.add(server);
        }
        return servers.toArray(new Server[0]);
    }

    private Server createServerModel(AsyncModelComputation.ServerAndModules serverAndModules) {
        Server server = new Server(serverAndModules.getServer().getName());
        for (IModule iModule : serverAndModules.getModules()) {
            String moduleName = iModule.getName();
            URL moduleUrl = getModuleUrl(serverAndModules.getServer(), iModule);
            if (moduleUrl == null) {
                continue;
            }
            Module module = new Module(moduleName, moduleUrl, server);
            server.getModules().add(module);
        }
        return server;
    }

    private List<AsyncModelComputation.ServerAndModules> filterCdiiModules(List<AsyncModelComputation.ServerAndModules> serversAndModules) {
        List<AsyncModelComputation.ServerAndModules> nonEmptyServers = new ArrayList<>();
        List<AsyncModelComputation.IsCdiiEnabled> callables = new ArrayList<>();
        for (AsyncModelComputation.ServerAndModules serverAndModules : serversAndModules) {
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
        List<AsyncModelComputation.ServerAndModules> result = new ArrayList<>();
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
        URL moduleUrl = getModuleUrl(server, module);
        String cdiiStatusUrlString = moduleUrl.toString() + "/cdii/status";
        try {
            URL cdiiStatusUrl = new URL(cdiiStatusUrlString);
            return cdiiStatusUrl;
        } catch (MalformedURLException e) {
            return null;
        }
    }
    
    private static URL getModuleUrl(IServer server, IModule module) {
        Wildfly8Server wildfly8Server = getWildfly8Server(server);
        URL moduleUrl = wildfly8Server.getModuleRootURL(module);
        return moduleUrl;
    }
    
    private static Wildfly8Server getWildfly8Server(IServer server) {
        IURLProvider urlProvider = (IURLProvider) server.getAdapter(IURLProvider.class);
        if (urlProvider instanceof Wildfly8Server) {
            return (Wildfly8Server) urlProvider;
        }
        throw new RuntimeException("Unexpected " + IURLProvider.class.getName() + ": " 
                + (urlProvider == null ? "null" : urlProvider.getClass().getCanonicalName()));
    }

    private List<AsyncModelComputation.ServerAndModules> filterRunningModuls(List<AsyncModelComputation.ServerAndModules> modules) {
        List<AsyncModelComputation.ServerAndModules> result = new ArrayList<>();
        for (AsyncModelComputation.ServerAndModules serverAndModules : modules) {
            AsyncModelComputation.ServerAndModules serverAndRunningModules = 
                    new ServerAndModules(serverAndModules.getServer());
            serverAndRunningModules.getModules().addAll(filterRunningModules(serverAndModules));
            result.add(serverAndRunningModules);
        }
        return result;
    }

    private List<IModule> filterRunningModules(AsyncModelComputation.ServerAndModules serverAndModules) {
        List<IModule> result = new ArrayList<>();
        for (IModule module : serverAndModules.getModules()) {
            if (serverAndModules.getServer().getModuleState(new IModule[] {module}) 
                    == IServer.STATE_STARTED) {
                result.add(module);
            }
        }
        return result;
    }

    private List<AsyncModelComputation.ServerAndModules> extractModules(List<IServer> servers) {
        List<AsyncModelComputation.ServerAndModules> result = new ArrayList<>();
        for (IServer server : servers) {
            AsyncModelComputation.ServerAndModules serverAndModules = new ServerAndModules(server);
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