package com.subway.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.subway.model.Station;

public class DataLoader {
	public static Map<String, Station> loadStations(InputStreamReader reader) throws IOException {
	    Map<String, Station> stations = new HashMap<>();
	    try (BufferedReader br = new BufferedReader(reader)) {
	        String line;
	        while ((line = br.readLine()) != null) {
	            String[] parts = line.split("\t");
	            if (parts.length < 4) {
	                throw new IOException("文件格式错误：每行至少需要包含线路名、站点A、站点B和距离，实际数据：" + line);
	            }
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

    public static SubwayGraph buildGraph(InputStreamReader reader) throws IOException {
        SubwayGraph graph = new SubwayGraph();
        try (BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                // 分割每行数据
                String[] parts = line.split("\t");
                if (parts.length < 4) {
                    // 检查数据格式是否正确
                    throw new IOException("文件格式错误：每行需要包含线路名、站点A、站点B和距离，实际数据: " + line);
                }
                String lineName = parts[0];
                String stationA = parts[1];
                String stationB = parts[2];
                double distance;
                try {
                    // 尝试将距离字符串转换为double类型
                    distance = Double.parseDouble(parts[3]);
                } catch (NumberFormatException e) {
                    // 处理距离转换失败的情况
                    throw new IOException("文件格式错误：距离必须是有效的数字，实际数据: " + line, e);
                }
                graph.addEdge(stationA, stationB, distance, lineName);
            }
        }
        return graph;
    }
}