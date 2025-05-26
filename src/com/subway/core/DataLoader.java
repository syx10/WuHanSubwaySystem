package com.subway.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.subway.model.Station;

public class DataLoader {
    public static Map<String, Station> loadStations(String filePath) throws IOException {
        Map<String, Station> stations = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                String lineName = parts[0];
                String stationA = parts[1];
                String stationB = parts[2];

                // 更新站点信息
                Station sA = stations.computeIfAbsent(stationA, Station::new);
                sA.addLine(lineName);
                Station sB = stations.computeIfAbsent(stationB, Station::new);
                sB.addLine(lineName);
            }
        }
        return stations;
    }

    public static SubwayGraph buildGraph(String filePath) throws IOException {
        SubwayGraph graph = new SubwayGraph();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                String lineName = parts[0];
                String stationA = parts[1];
                String stationB = parts[2];
                double distance = Double.parseDouble(parts[3]);
                graph.addEdge(stationA, stationB, distance, lineName);
             // 在 DataLoader.buildGraph() 中
                if (parts.length < 4) {
                    throw new IOException("文件格式错误：" + line);
                }
            }
            
        }
        return graph;
        
    }
}