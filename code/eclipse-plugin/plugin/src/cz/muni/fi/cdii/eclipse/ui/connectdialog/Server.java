package cz.muni.fi.cdii.eclipse.ui.connectdialog;

import java.util.ArrayList;
import java.util.List;

public class Server {
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