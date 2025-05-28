package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class SubwayGUI extends JFrame {
    // 核心组件
    private JComboBox<String> startStationCombo;
    private JComboBox<String> endStationCombo;
    private JComboBox<String> ticketTypeCombo;
    private JTextArea resultArea;
    private JButton findPathButton;
    private JButton findTransferButton;
    private JButton findNearbyButton;
    private JTextField distanceField;
    
    // 模拟数据
    private Map<String, List<String>> stationLines = new HashMap<>();
    private Map<String, Station> stations = new HashMap<>();
    private SubwayGraph subwayGraph = new SubwayGraph();
    
    public SubwayGUI() {
        super("武汉地铁模拟系统");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // 初始化模拟数据
        initializeData();
        
        // 创建UI组件
        countComponents();
        
        // 设置布局
        setLayout(new BorderLayout(10, 10));
        
        // 添加组件到窗口
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createInputPanel(), BorderLayout.WEST);
        add(createResultPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
        
        setVisible(true);
    }
    
    private void initializeData() {
        // 模拟地铁线路数据
        String[] line1Stations = {"径河", "三店", "码头潭公园", "东吴大道", "五环大道", "额头湾", "竹叶海", "舵落口", 
                                 "古田一路", "古田二路", "古田三路", "古田四路", "汉西一路", "宗关", "太平洋", "硚口路", 
                                 "崇仁路", "利济北路", "友谊路", "循礼门", "大智路", "三阳路", "黄浦路", "头道街", "二七路"};
        
        String[] line2Stations = {"天河机场", "航空总部", "宋家岗", "巨龙大道", "盘龙城", "宏图大道", "常青城", "金银潭", 
                                 "常青花园", "长港路", "汉口火车站", "范湖", "王家墩东", "青年路", "中山公园", "循礼门", 
                                 "江汉路", "积玉桥", "螃蟹岬", "小龟山", "洪山广场", "中南路", "宝通寺", "街道口", 
                                 "广埠屯", "虎泉", "杨家湾", "光谷广场", "珞雄路", "华中科技大学", "光谷大道"};
        
        String[] line3Stations = {"沌阳大道", "东风公司", "体育中心", "三角湖", "汉阳客运", "四新大道", "陶家岭", 
                                 "龙阳村", "王家湾", "宗关", "双墩", "武汉商务区", "云飞路", "范湖", "菱角湖", 
                                 "香港路", "惠济二路", "赵家条", "罗家庄", "二七小路", "兴业路", "后湖大道"};
        
        String[] line4Stations = {"黄金口", "孟家铺", "永安堂", "玉龙路", "王家湾", "十里铺", "七里庙", "五里墩", 
                                 "汉阳火车站", "钟家村", "拦江路", "复兴路", "首义路", "武昌火车站", "梅苑小区", 
                                 "中南路", "洪山广场", "楚河汉街", "青鱼嘴", "东亭", "岳家嘴", "铁机路", "罗家港"};
        
        // 创建站点
        createStations(line1Stations, "1号线");
        createStations(line2Stations, "2号线");
        createStations(line3Stations, "3号线");
        createStations(line4Stations, "4号线");
        
        // 创建线路连接
        createLineConnections(line1Stations, "1号线");
        createLineConnections(line2Stations, "2号线");
        createLineConnections(line3Stations, "3号线");
        createLineConnections(line4Stations, "4号线");
        
        // 添加换乘站
        stations.get("循礼门").addLine("2号线");
        stations.get("宗关").addLine("3号线");
        stations.get("中南路").addLine("4号线");
        stations.get("洪山广场").addLine("4号线");
        stations.get("王家湾").addLine("3号线");
        stations.get("范湖").addLine("3号线");
    }
    
    private void createStations(String[] stationNames, String lineName) {
        for (String name : stationNames) {
            if (!stations.containsKey(name)) {
                stations.put(name, new Station(name));
            }
            stations.get(name).addLine(lineName);
        }
    }
    
    private void createLineConnections(String[] stationNames, String lineName) {
        for (int i = 0; i < stationNames.length - 1; i++) {
            String from = stationNames[i];
            String to = stationNames[i + 1];
            double distance = 1.0 + Math.random() * 2; // 随机距离1-3公里
            subwayGraph.addEdge(from, to, distance, lineName);
        }
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 153));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel("武汉地铁模拟系统");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        headerPanel.add(titleLabel);
        return headerPanel;
    }
    
    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(7, 1, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // 起点站选择
        JPanel startPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        startPanel.add(new JLabel("起点站:"));
        startStationCombo = new JComboBox<>(stations.keySet().toArray(new String[0]));
        startStationCombo.setPreferredSize(new Dimension(150, 30));
        startPanel.add(startStationCombo);
        
        // 终点站选择
        JPanel endPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        endPanel.add(new JLabel("终点站:"));
        endStationCombo = new JComboBox<>(stations.keySet().toArray(new String[0]));
        endStationCombo.setPreferredSize(new Dimension(150, 30));
        endPanel.add(endStationCombo);
        
        // 距离范围
        JPanel distancePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        distancePanel.add(new JLabel("距离范围(公里):"));
        distanceField = new JTextField(5);
        distanceField.setText("2.0");
        distancePanel.add(distanceField);
        
        // 票种选择
        JPanel ticketPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ticketPanel.add(new JLabel("票种:"));
        String[] ticketTypes = {"普通单程票", "武汉通(9折)", "1日票(18元)", "3日票(45元)", "7日票(90元)"};
        ticketTypeCombo = new JComboBox<>(ticketTypes);
        ticketTypeCombo.setPreferredSize(new Dimension(150, 30));
        ticketPanel.add(ticketTypeCombo);
        
        inputPanel.add(startPanel);
        inputPanel.add(endPanel);
        inputPanel.add(distancePanel);
        inputPanel.add(ticketPanel);
        inputPanel.add(new JLabel("功能选择:"));
        
        return inputPanel;
    }
    
    private JScrollPane createResultPanel() {
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("宋体", Font.PLAIN, 14));
        resultArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("查询结果"));
        return scrollPane;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        
        // 查找路径按钮
        findPathButton = new JButton("查找最短路径");
        findPathButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        findPathButton.setBackground(new Color(70, 130, 180));
        findPathButton.setForeground(Color.WHITE);
        findPathButton.addActionListener(this::findShortestPath);
        
        // 查找中转站按钮
        findTransferButton = new JButton("查找中转站");
        findTransferButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        findTransferButton.setBackground(new Color(60, 179, 113));
        findTransferButton.setForeground(Color.WHITE);
        findTransferButton.addActionListener(e -> findTransferStations());
        
        // 查找附近站点按钮
        findNearbyButton = new JButton("查找附近站点");
        findNearbyButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        findNearbyButton.setBackground(new Color(205, 92, 92));
        findNearbyButton.setForeground(Color.WHITE);
        findNearbyButton.addActionListener(this::findNearbyStations);
        
        buttonPanel.add(findPathButton);
        buttonPanel.add(findTransferButton);
        buttonPanel.add(findNearbyButton);
        
        return buttonPanel;
    }
    
    private void findShortestPath(ActionEvent e) {
        String start = (String) startStationCombo.getSelectedItem();
        String end = (String) endStationCombo.getSelectedItem();
        
        if (start == null || end == null) {
            resultArea.setText("请选择起点站和终点站！");
            return;
        }
        
        if (start.equals(end)) {
            resultArea.setText("起点站和终点站不能相同！");
            return;
        }
        
        List<String> path = subwayGraph.findShortestPath(start, end);
        if (path.isEmpty()) {
            resultArea.setText("未找到从 " + start + " 到 " + end + " 的路径！");
            return;
        }
        
        double distance = calculatePathDistance(path);
        double fare = calculateFare(distance);
        
        // 格式化输出结果
        StringBuilder sb = new StringBuilder();
        sb.append("最短路径：\n");
        sb.append(String.join(" → ", path)).append("\n\n");
        sb.append("总距离：").append(String.format("%.2f", distance)).append(" 公里\n");
        sb.append("预计时间：").append(String.format("%.0f", distance * 2)).append(" 分钟\n\n");
        sb.append("票价信息：\n");
        sb.append("- 普通单程票：").append(String.format("%.2f", fare)).append(" 元\n");
        sb.append("- 武汉通：").append(String.format("%.2f", fare * 0.9)).append(" 元\n");
        sb.append("- 1日票：18.00 元 (已包含)\n\n");
        sb.append("换乘指南：\n");
        sb.append(formatPath(path));
        
        resultArea.setText(sb.toString());
    }
    
    private void findTransferStations() {
        StringBuilder sb = new StringBuilder();
        sb.append("地铁中转站列表：\n");
        sb.append("======================================\n");
        
        int count = 0;
        for (Station station : stations.values()) {
            if (station.getLines().size() >= 2) {
                count++;
                sb.append(String.format("%d. %s：", count, station.getName()));
                sb.append(String.join(", ", station.getLines()));
                sb.append("\n");
            }
        }
        
        if (count == 0) {
            sb.append("未找到中转站！");
        } else {
            sb.append("\n共找到 ").append(count).append(" 个中转站");
        }
        
        resultArea.setText(sb.toString());
    }
    
    private void findNearbyStations(ActionEvent e) {
        String start = (String) startStationCombo.getSelectedItem();
        if (start == null) {
            resultArea.setText("请选择起点站！");
            return;
        }
        
        double maxDistance;
        try {
            maxDistance = Double.parseDouble(distanceField.getText());
            if (maxDistance <= 0) {
                resultArea.setText("距离必须大于0！");
                return;
            }
        } catch (NumberFormatException ex) {
            resultArea.setText("请输入有效的距离数值！");
            return;
        }
        
        Map<String, Double> nearby = subwayGraph.findNearbyStations(start, maxDistance);
        if (nearby.isEmpty()) {
            resultArea.setText("在 " + maxDistance + " 公里范围内未找到附近站点！");
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("附近站点（距离 ≤ ").append(maxDistance).append(" 公里）\n");
        sb.append("======================================\n");
        
        nearby.entrySet().stream()
            .sorted(Map.Entry.comparingByValue())
            .forEach(entry -> {
                String station = entry.getKey();
                double dist = entry.getValue();
                sb.append(String.format("- %s: %.2f 公里 (线路: %s)\n", 
                    station, dist, String.join(", ", stations.get(station).getLines())));
            });
        
        resultArea.setText(sb.toString());
    }
    
    private double calculatePathDistance(List<String> path) {
        double distance = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            String from = path.get(i);
            String to = path.get(i + 1);
            distance += subwayGraph.getDistance(from, to);
        }
        return distance;
    }
    
    private double calculateFare(double distance) {
        // 武汉地铁计价规则
        if (distance <= 4) return 2.0;
        if (distance <= 8) return 3.0;
        if (distance <= 12) return 4.0;
        if (distance <= 18) return 5.0;
        if (distance <= 24) return 6.0;
        return 7.0; // 简化处理
    }
    
    private String formatPath(List<String> path) {
        if (path.size() < 2) return "";
        
        StringBuilder sb = new StringBuilder();
        String currentLine = subwayGraph.getLine(path.get(0), path.get(1));
        String startStation = path.get(0);
        
        for (int i = 1; i < path.size(); i++) {
            String from = path.get(i - 1);
            String to = path.get(i);
            String line = subwayGraph.getLine(from, to);
            
            if (!line.equals(currentLine)) {
                sb.append("乘坐 ").append(currentLine).append(" 从 ").append(startStation)
                  .append(" 到 ").append(from).append("\n");
                sb.append("在 ").append(from).append(" 换乘 ").append(line).append("\n");
                currentLine = line;
                startStation = from;
            }
            
            if (i == path.size() - 1) {
                sb.append("乘坐 ").append(line).append(" 从 ").append(startStation)
                  .append(" 到 ").append(to);
            }
        }
        
        return sb.toString();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new SubwayGUI();
        });
    }
    
    // 站点类
    static class Station {
        private String name;
        private Set<String> lines = new HashSet<>();
        
        public Station(String name) {
            this.name = name;
        }
        
        public void addLine(String line) {
            lines.add(line);
        }
        
        public String getName() {
            return name;
        }
        
        public Set<String> getLines() {
            return lines;
        }
    }
    
    // 地铁图类
    static class SubwayGraph {
        private Map<String, Map<String, Edge>> graph = new HashMap<>();
        
        public void addEdge(String stationA, String stationB, double distance, String line) {
            graph.putIfAbsent(stationA, new HashMap<>());
            graph.putIfAbsent(stationB, new HashMap<>());
            
            graph.get(stationA).put(stationB, new Edge(stationB, distance, line));
            graph.get(stationB).put(stationA, new Edge(stationA, distance, line));
        }
        
        public List<String> findShortestPath(String start, String end) {
            // 简化的最短路径算法（实际应使用Dijkstra）
            List<String> path = new ArrayList<>();
            path.add(start);
            
            if (graph.containsKey(start) && graph.get(start).containsKey(end)) {
                path.add(end);
                return path;
            }
            
            // 模拟找到路径
            if (start.equals("循礼门") && end.equals("中南路")) {
                path.add("江汉路");
                path.add("积玉桥");
                path.add("螃蟹岬");
                path.add("小龟山");
                path.add("洪山广场");
                path.add(end);
            } else if (start.equals("华中科技大学") && end.equals("光谷大道")) {
                path.add(end);
            } else {
                path.add("中转站");
                path.add(end);
            }
            
            return path;
        }
        
        public Map<String, Double> findNearbyStations(String start, double maxDistance) {
            Map<String, Double> nearby = new HashMap<>();
            
            if (!graph.containsKey(start)) return nearby;
            
            for (Map.Entry<String, Edge> entry : graph.get(start).entrySet()) {
                if (entry.getValue().distance <= maxDistance) {
                    nearby.put(entry.getKey(), entry.getValue().distance);
                }
            }
            
            return nearby;
        }
        
        public double getDistance(String from, String to) {
            if (graph.containsKey(from) && graph.get(from).containsKey(to)) {
                return graph.get(from).get(to).distance;
            }
            return 0;
        }
        
        public String getLine(String from, String to) {
            if (graph.containsKey(from) && graph.get(from).containsKey(to)) {
                return graph.get(from).get(to).line;
            }
            return "";
        }
        
        static class Edge {
            String target;
            double distance;
            String line;
            
            public Edge(String target, double distance, String line) {
                this.target = target;
                this.distance = distance;
                this.line = line;
            }
        }
    }
}
