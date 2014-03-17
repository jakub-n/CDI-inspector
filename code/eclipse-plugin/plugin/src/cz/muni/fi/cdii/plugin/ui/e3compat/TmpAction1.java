package cz.muni.fi.cdii.plugin.ui.e3compat;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.action.Action;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.model.IURLProvider;
import org.jboss.ide.eclipse.as.core.server.internal.v7.Wildfly8Server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import cz.muni.fi.cdii.plugin.Activator;


public class TmpAction1 extends Action {

    public TmpAction1() {
        super();
        this.setImageDescriptor(Activator.getImageDescriptor("icons/eclipse/action1.gif"));
        this.setToolTipText("tmp action 1");
    }
    
    @Override
    public void run() {
        System.out.println("ahoj");
        IServer[] servers = ServerCore.getServers();
        for (IServer server : servers) {
            System.out.println(server);
            System.out.println("> name " + server.getName());
            System.out.println("> server type " + server.getServerType());
            System.out.println(server.getClass().getCanonicalName());
            
            //IServer server = ...
            IURLProvider urlProvider = (IURLProvider) server.getAdapter(IURLProvider.class);
            if (urlProvider instanceof Wildfly8Server) {
                final Wildfly8Server wildfly8Server = (Wildfly8Server) urlProvider;
                System.out.println("> management port " + wildfly8Server.getManagementPort());
                printServerModules(server, wildfly8Server);
                checkCdiiSubModule(wildfly8Server);
            }
        }
    }

    private void checkCdiiSubModule(Wildfly8Server wildfly8Server) {
        try {
            URL url = new URL(wildfly8Server.getModuleRootURL(
                    wildfly8Server.getServer().getModules()[0]).toString()
                    + "cdii/status");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String result = "";
            while (reader.ready()) {
                result += reader.readLine();
            }
            ObjectMapper mapper = new ObjectMapper();
            boolean enabled = mapper.readTree(result).get("status").asText().equals("enabled");
            System.out.println("enabled: " + enabled);
        } catch (IOException ex) {
            ex.printStackTrace();
//            throw new RuntimeException(ex);
        }
        
//        String urlString = new StringBuilder().append("http://")
//                .append(wildfly8Server.getUsername())
//                .append(":")
//                .append(wildfly8Server.getPassword())
//                .append("@")
//                .append(wildfly8Server.getHost())
//                .append("/management/subsystem/cdii")
//                .toString();
//        try {
//            URL url = new URL(urlString);
//            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
//            String result = "";
//            while (reader.ready()) {
//                result += reader.readLine();
//            }
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode jsonTree = mapper.readTree(result);
//            jsonTree.get("outcome");
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }
    }

    private void printServerModules(IServer server, Wildfly8Server w8server) {
        System.out.println("> modules:");
        for (IModule module : server.getModules()) {
            System.out.println("* module name" + module.getName());
            System.out.println("* module type" + module.getModuleType());
            System.out.println("* module state" + server.getModuleState(new IModule[]{module}));
            System.out.println("* module class" + module.getClass().getCanonicalName());
            System.out.println("* module url" + w8server.getModuleRootURL(module));
        }
        
    }
}
