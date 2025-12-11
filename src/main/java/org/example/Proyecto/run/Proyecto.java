package org.example.Proyecto.run;

import org.openxava.util.*;

public class Proyecto {
    public static void main(String[] args) throws Exception {
        DBServer.start("Proyecto");
        AppServer.run("Proyecto");
    }
}