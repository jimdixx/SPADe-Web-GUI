package cz.zcu.fav.kiv.antipatterndetectionapp.model.project.management.types;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.dto.ProjectDto;

import java.util.ArrayList;

public class Node {
    public ProjectDto project;
    public ArrayList<Node> children;
}