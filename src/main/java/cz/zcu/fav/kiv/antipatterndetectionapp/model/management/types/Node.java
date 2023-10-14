package cz.zcu.fav.kiv.antipatterndetectionapp.model.management.types;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;

import java.util.ArrayList;

public class Node {
    public Project project;
    public ArrayList<Node> children;
}