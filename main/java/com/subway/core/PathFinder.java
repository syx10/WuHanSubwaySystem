package com.subway.core;

import java.util.*;

public class PathFinder {
    private SubwayGraph graph;

    public PathFinder(SubwayGraph graph) {
        this.graph = graph;
    }

    /**
     * 查找所有无环路径（DFS实现）
     */
    public List<List<String>> findAllPaths(String start, String end) {
        List<List<String>> paths = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        dfs(start, end, new ArrayList<>(), visited, paths);
        return paths;
    }

    private void dfs(String current, String end, List<String> path, Set<String> visited, List<List<String>> paths) {
        if (current.equals(end)) {
            path.add(current);
            paths.add(new ArrayList<>(path));
            path.remove(path.size() - 1);
            return;
        }
        if (visited.contains(current)) return;

        visited.add(current);
        path.add(current);
        for (SubwayGraph.Edge edge : graph.getAdjacencyList().get(current)) {
            dfs(edge.target, end, path, visited, paths);
        }
        path.remove(path.size() - 1);
        visited.remove(current);
    }
}
