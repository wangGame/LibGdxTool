package com.libGdx.test.moni;

import com.badlogic.gdx.scenes.scene2d.ui.List;

import java.util.ArrayList;

public class CalUtils {
    public static void mainTest(float x1,float y1,float x2,float y2,float a1,float b1,float a2,float b2,float width,float height) {
        // 定义墙上的洞的坐标
        double[] A_wall = {x1, y1};
        double[] B_wall = {x2, y2};
//
//        // 定义矩形的尺寸
//        double width = 4;
//        double height = 2;

        // 定义矩形上的孔的坐标（相对于矩形左下角）
        double[] A_rect = {a1, b1}; // 第一个孔
        double[] B_rect = {a2, b2}; // 第二个孔

        // 转换矩形上的孔的坐标为相对于矩形中心
        double[] center_offset = {width / 2, height / 2};
        double[] A_rect_centered = {A_rect[0] - center_offset[0], A_rect[1] - center_offset[1]};
        double[] B_rect_centered = {B_rect[0] - center_offset[0], B_rect[1] - center_offset[1]};

        // 计算墙上两个洞的向量
        double[] vector_wall = {B_wall[0] - A_wall[0], B_wall[1] - A_wall[1]};

        // 计算矩形上两个孔的向量
        double[] vector_rect = {B_rect_centered[0] - A_rect_centered[0], B_rect_centered[1] - A_rect_centered[1]};

        // 计算旋转角度
        double angle_wall = Math.atan2(vector_wall[1], vector_wall[0]);
        double angle_rect = Math.atan2(vector_rect[1], vector_rect[0]);
        double theta = angle_wall - angle_rect;

        // 计算旋转矩阵
        double[][] rotation_matrix = {
                {Math.cos(theta), -Math.sin(theta)},
                {Math.sin(theta), Math.cos(theta)}
        };

        // 旋转矩形上的孔坐标
        double[] A_rect_rotated = rotatePoint(A_rect_centered, rotation_matrix);
        double[] B_rect_rotated = rotatePoint(B_rect_centered, rotation_matrix);

        // 计算矩形中心坐标
        double[] center = {A_wall[0] - A_rect_rotated[0], A_wall[1] - A_rect_rotated[1]};

        System.out.println("矩形的中心坐标: (" + center[0] + ", " + center[1] + ")");
        System.out.println("旋转角度（弧度）: " + theta);
        System.out.println("旋转角度（度）: " + Math.toDegrees(theta));
    }

    private static double[] rotatePoint(double[] point, double[][] matrix) {
        double x = point[0] * matrix[0][0] + point[1] * matrix[0][1];
        double y = point[0] * matrix[1][0] + point[1] * matrix[1][1];
        return new double[]{x, y};
    }

    public static void main(String[] args) {
        CalUtils utils = new CalUtils();
        utils.xx();
    }

    public void xx() {
        // 圆心坐标
        double centerX = 0.0;
        double centerY = 0.0;

        // 弧上的两个坐标
        double startX = 1.0;
        double startY = 1.0;
        double endX = -1.0;
        double endY = 1.0;

        // 计算半径
        double radius = Math.sqrt(Math.pow(startX - centerX, 2) + Math.pow(startY - centerY, 2));

        // 计算起点和终点的角度
        double startAngle = Math.atan2(startY - centerY, startX - centerX);
        double endAngle = Math.atan2(endY - centerY, endX - centerX);

        // 确保起点角度小于终点角度
        if (startAngle > endAngle) {
            endAngle += 2 * Math.PI;
        }

        // 插值角度以获取圆弧上的点
        int numPoints = 100; // 圆弧上的点的数量
        ArrayList<double[]> arcPoints = new ArrayList<double[]>();

        for (int i = 0; i <= numPoints; i++) {
            double t = (double) i / numPoints;
            double angle = startAngle + t * (endAngle - startAngle);
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);
            arcPoints.add(new double[]{x, y});
        }

        // 打印圆弧上的点
        for (double[] point : arcPoints) {
            System.out.printf("(%.2f, %.2f)\n", point[0], point[1]);
        }
    }
}
