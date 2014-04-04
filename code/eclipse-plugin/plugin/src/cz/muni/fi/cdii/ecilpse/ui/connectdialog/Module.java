package cz.muni.fi.cdii.ecilpse.ui.connectdialog;

import java.net.URL;

public class Module {
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