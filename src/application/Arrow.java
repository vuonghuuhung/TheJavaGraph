package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

/*
 * Đây là lớp giúp thể hiện được hướng của cạnh, lớp tạo ra mũi tên.
 * Thông tin về lớp Path: extends từ Shape, Path đại diện cho một hình dạng đơn giản và cung cấp các
 * phương tiện cần thiết để xây dựng cơ bản và quản lý 1 đường hình học
 */
public class Arrow extends Path{
    private static final double defaultArrowHeadSize = 7; // Mặc định để cỡ mũi tên là 7
    public Arrow(double startX, double startY, double endX, double endY, double arrowHeadSize){
        // Khởi tạo một đối tượng Path
        super();

        // Thiết lập đường viền có cùng màu với nội dung bên trong
        strokeProperty().bind(fillProperty());
        setFill(Color.BLACK); // Màu của đường là màu đen -> viền cũng là màu đen

        // Tạo một đường bắt đầu từ điểm start -> End: giúp thêm cạnh có hướng
        getElements().add(new MoveTo(startX, startY));
        getElements().add(new LineTo(endX, endY));

        // Sử dụng toán để vẽ mũi tên, xác định góc angle = 1/2 góc tạo bởi 2 cạnh mũi tên, để tạo mũi tên ta tạo thêm 2 điểm + điểm End
        double angle = Math.atan2((endY - startY), (endX - startX)) - Math.PI / 2.0;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        //point1
        double x1 = (- 1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowHeadSize + endX;
        double y1 = (- 1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowHeadSize + endY;
        //point2
        double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowHeadSize + endX;
        double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowHeadSize + endY;

        // từ End -> point1 -> point2 -> về End -> tạo hình tam giác giống mũi tên
        getElements().add(new LineTo(x1, y1));
        getElements().add(new LineTo(x2, y2));
        getElements().add(new LineTo(endX, endY));
    }

    // Constructor khởi tạo arrow size mặc định
    public Arrow(double startX, double startY, double endX, double endY){
        this(startX, startY, endX, endY, defaultArrowHeadSize);
    }
}
