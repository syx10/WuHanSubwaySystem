package com.subway.util;

import com.subway.core.SubwayGraph;
import java.util.List;

public class PathFormatter {
    /**
     * 格式化输出路径（含换乘信息）
     */
    public static void printPath(List<String> path, SubwayGraph graph) {
        if (path.isEmpty()) {
            System.out.println("未找到路径！");
            return;
        }

        String currentLine = null;
        String startStation = path.get(0);
        for (int i = 0; i < path.size() - 1; i++) {
            String from = path.get(i);
            String to = path.get(i + 1);
            String line = getLineBetweenStations(from, to, graph);

            if (currentLine == null) {
                // 起始线路
                System.out.printf("乘坐 %s 从 %s", line, from);
                currentLine = line;
            } else if (!line.equals(currentLine)) {
                // 换乘提示
                System.out.printf(" 到 %s\n在 %s 换乘 %s", from, from, line);
                currentLine = line;
            }

            // 终点站处理
            if (i == path.size() - 2) {
                System.out.printf(" 到 %s\n", to);
            }
        }
    }

    /** 获取两站之间的线路 */
    private static String getLineBetweenStations(String a, String b, SubwayGraph graph) {
        return graph.getAdjacencyList().get(a).stream()
                .filter(edge -> edge.target.equals(b))
                .map(edge -> edge.line)
                .findFirst().orElse("未知线路");
    }
}

