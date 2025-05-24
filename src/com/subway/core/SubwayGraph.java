package com.subway.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class SubwayGraph {
    private Map<String, List<Edge>> adjacencyList = new HashMap<>();

    public void addEdge(String stationA, String stationB, double distance, String line) {
        adjacencyList.putIfAbsent(stationA, new ArrayList<>());
        adjacencyList.putIfAbsent(stationB, new ArrayList<>());
        adjacencyList.get(stationA).add(new Edge(stationB, distance, line));
        adjacencyList.get(stationB).add(new Edge(stationA, distance, line));
    }

    // Dijkstra 算法实现（核心逻辑）
    public List<String> findShortestPath(String start, String end) {
        PriorityQueue<Node> queue = new PriorityQueue<>();
        Map<String, Double> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();

        for (String station : adjacencyList.keySet()) {
            distances.put(station, Double.MAX_VALUE);
        }
        distances.put(start, 0.0);
        queue.add(new Node(start, 0.0));

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            if (current.station.equals(end)) break;

            for (Edge edge : adjacencyList.get(current.station)) {
                double newDist = distances.get(current.station) + edge.distance;
                if (newDist < distances.get(edge.target)) {
                    distances.put(edge.target, newDist);
                    previous.put(edge.target, current.station);
                    queue.add(new Node(edge.target, newDist));
                }
            }
        }

       
        List<String> path = new ArrayList<>();
        for (String at = end; at != null; at = previous.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }

    // 内部类：边和节点
    private static class Edge {
        String target;
        double distance;
        String line;

        public Edge(String target, double distance, String line) {
            this.target = target;
            this.distance = distance;
            this.line = line;
        }
    }

    private static class Node implements Comparable<Node> {
        String station;
        double distance;

        public Node(String station, double distance) {
            this.station = station;
            this.distance = distance;
        }

        @Override
        public int compareTo(Node other) {
            return Double.compare(this.distance, other.distance);
        }
    }
}
