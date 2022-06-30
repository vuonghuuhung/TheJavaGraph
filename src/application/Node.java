package application;

import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node> {

    public String name;// tên của node
    public List<Edge> adjacents = new ArrayList<Edge>();// chứa các cạnh kề với node này
    public List<Edge> revAdjacents = new ArrayList<Edge>();// chứa các cạnh ko kề với node này
    //    public Node previous;// tạo ra 1 node
    public NodeFX circle;// tạo ra 1 nodefx hình tròn
    public double minDistance = Double.POSITIVE_INFINITY;// khoảng cách nhỏ nhất giữa 2 node và được cho mặc đinh là vô
    // cùng
    public boolean visited, isArticulationPoint;// xác nhận đồ thị đã đi qua node này chưa, và nó có phải là điểm khớp
    // nối ko
    public int visitTime = 0, lowTime = 0;// thời gian đi qua, thời gian nhỏ nhất
    public int DAGColor;
    public Node previous;

    public Node(String argName) {// khởi tạo đối tượng có tên argName
        this.name = argName;
        visited = false;// node này chưa được đi qua
    }

    public Node(String argName, NodeFX c) {// tạo ra đối tượng node chưa có hình tròn
        name = argName;
        circle = c;
        visited = false;
    }

    @Override
    public int compareTo(Node o) {// so sánh khoảng cách của 2 node
        return Double.compare(minDistance, o.minDistance);
    }
}

