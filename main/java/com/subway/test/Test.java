package com.subway.test;

import com.subway.core.*;
import com.subway.model.Station;
import com.subway.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Test {
    public static void main(String[] args) {
        try {
            // 使用 FileInputStream 读取文件
            File file = new File("main/resources/subway.txt");
            System.out.println("文件存在: " + file.exists());
            FileInputStream inputStream1 = new FileInputStream(file);
            InputStreamReader reader1 = new InputStreamReader(inputStream1);

            // 加载站点信息
            Map<String, Station> stations = DataLoader.loadStations(reader1);

            // 重新打开文件输入流
            FileInputStream inputStream2 = new FileInputStream(file);
            InputStreamReader reader2 = new InputStreamReader(inputStream2);

            // 构建地铁图
            SubwayGraph graph = DataLoader.buildGraph(reader2);

            // 2. 功能1：识别所有中转站
            Set<Station> transferStations = new HashSet<>();
            for (Station station : stations.values()) {
                if (station.getLines().size() >= 2) {
                    transferStations.add(station);
                }
            }
            System.out.println("=== 功能1：中转站列表 ===");
            transferStations.forEach(s -> System.out.println(s.getName() + " - 线路：" + s.getLines()));

            // 3. 功能2：查找距离小于n的站点
            DistanceFilter distanceFilter = new DistanceFilter(graph);
            List<DistanceFilter.DistanceResult> nearbyStations = 
                distanceFilter.findNearbyStations("华中科技大学", 2.0);
            System.out.println("\n=== 功能2：距离小于2公里的站点 ===");
            nearbyStations.forEach(s -> System.out.println(
                s.station + " - 距离：" + s.distance + "公里"
            ));

            // 4. 功能3：查找所有无环路径（示例：华中科技大学 → 湖口）
            PathFinder pathFinder = new PathFinder(graph);
            List<List<String>> allPaths = 
                pathFinder.findAllPaths("华中科技大学", "湖口");
            System.out.println("\n=== 功能3：所有无环路径 ===");
            allPaths.forEach(path -> System.out.println("路径：" + path));

            // 5. 功能4：查找最短路径
            List<String> shortestPath = graph.findShortestPath("华中科技大学", "湖口");
            System.out.println("\n=== 功能4：最短路径 ===");
            System.out.println("路径：" + shortestPath);

            // 6. 功能5：格式化输出路径
            System.out.println("\n=== 功能5：路径格式化输出 ===");
            PathFormatter.printPath(shortestPath, graph);

            // 7. 功能6/7：计算票价
            double totalDistance = calculatePathDistance(shortestPath, graph);
            System.out.println("\n=== 功能6/7：票价计算 ===");
            System.out.println("总距离：" + totalDistance + "公里");
            System.out.println("普通票价：" + FareCalculator.calculateFare(totalDistance) + "元");
            System.out.println("武汉通票价：" + FareCalculator.calculateWuhanTongFare(totalDistance) + "元");
            System.out.println("日票票价：0元（已购买日票）");

        } catch (IOException e) {
            System.err.println("错误：文件加载失败，请检查 subway.txt 路径和格式！");
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("错误：输入的站点不存在！");
        }
    }

    /**
     * 辅助方法：计算路径总距离
     */
    private static double calculatePathDistance(List<String> path, SubwayGraph graph) {
        double distance = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            String from = path.get(i);
            String to = path.get(i + 1);
            for (SubwayGraph.Edge edge : graph.getAdjacencyList().get(from)) {
                if (edge.target.equals(to)) {
                    distance += edge.distance;
                    break;
                }
            }
        }
        return distance;
    }
}