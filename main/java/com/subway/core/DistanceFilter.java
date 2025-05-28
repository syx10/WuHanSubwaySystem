package com.subway.core;

import java.util.*;
import java.util.stream.Collectors;
import com.subway.model.Station;

public class DistanceFilter {
    private SubwayGraph graph;

    public DistanceFilter(SubwayGraph graph) {
        this.graph = graph;
    }

    /**
     * 查找距离小于等于 maxDistance 的所有站点
     */
    public List<DistanceResult> findNearbyStations(String start, double maxDistance) {
        Map<String, Double> distances = new HashMap<>();
        PriorityQueue<SubwayGraph.Node> queue = new PriorityQueue<>();
        distances.put(start, 0.0);
        queue.add(new SubwayGraph.Node(start, 0.0));

        while (!queue.isEmpty()) {
            SubwayGraph.Node current = queue.poll();
            for (SubwayGraph.Edge edge : graph.getAdjacencyList().get(current.station)) {
                double newDist = current.distance + edge.distance;
                if (newDist <= maxDistance && 
                    (!distances.containsKey(edge.target) || newDist < distances.get(edge.target))) {
                    distances.put(edge.target, newDist);
                    queue.add(new SubwayGraph.Node(edge.target, newDist));
                }
            }
        }

        // 过滤掉起点自身，并封装结果
        return distances.entrySet().stream()
                .filter(e -> !e.getKey().equals(start))
                .map(e -> new DistanceResult(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    /** 结果封装类 */
    public static class DistanceResult {
        public String station;
        public double distance;

        public DistanceResult(String station, double distance) {
            this.station = station;
            this.distance = distance;
        }
    }
}
