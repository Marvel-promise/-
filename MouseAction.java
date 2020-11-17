package Geometric.MyAction;
import Geometric.GeometricParent.*;
import Geometric.GeometricParent.Point;
import Geometric.GeometricParent.Polygon;
import Geometric.GeometricParent.Rectangle;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.sqrt;

public class MouseAction extends MouseAdapter implements ActionListener {
    private int x1, y1, x2, y2;
    private int new_x1, new_y1, new_x2, new_y2;
    private ArrayList<Point> points;
    DrawingGraphics drawingGraphics = new DrawingGraphics();
    private int num;
    private Graphics2D g;
    private DrawJFrame df;
    private boolean flag = false;
    String shape = "直线";
    Color color;
    private int temp = 0;
    public MouseAction(DrawJFrame df) {
        this.df = df;
    }
//    鼠标按键在组件上单击（按下并释放）时调用。
    @Override
    public void mouseClicked(MouseEvent e) {
        if(shape.equals("三角形") && flag){
            x2 = e.getX();
            y2 = e.getY();
            if (e.getClickCount() == 2) {
                g.drawLine(new_x1, new_y1, x2, y2);
                g.drawLine(new_x2, new_y2, x2, y2);
                drawingGraphics.addList(new Triangle(line_long(new_x1,new_y1,new_x2,new_y2),
                        line_long(x2,y2,new_x1,new_y1),line_long(new_x2,new_y2,x1,y1)));
                flag = false;
            }
        }else if(shape.equals("多边形")&&flag){
            x2 = e.getX();
            y2 = e.getY();
            if (e.getClickCount() == 2) {
                g.drawLine(new_x1, new_y1, new_x2, new_y2);
                g.drawLine(new_x2, new_y2, x2, y2);
                points.add(new Point(x2,y2));
                temp++;
                flag = false;
                drawingGraphics.addList((new Polygon(points,temp)));
                return;
            }
            g.drawLine(new_x2, new_y2, x2, y2);
            new_x2 = x2;
            new_y2 = y2;
            points.add(new Point(x2,y2));
            temp++;
        }
        }
    //鼠标按下时执行操作
    @Override
    public void mousePressed(MouseEvent e) {
        g = (Graphics2D) df.getGraphics();
        g.setColor(color);
        x1 = e.getX();
        y1 = e.getY();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        num = 0;
    }
//    鼠标按钮在组件上释放时调用。
    @Override
    public void mouseReleased(MouseEvent e) {
        x2 = e.getX();
        y2 = e.getY();
        if(flag == false && shape.equals("多边形")){
            points = new ArrayList<>();
        }
        switch (shape){
            case "直线":
                g.drawLine(x1,y1,x2,y2);
                break;
            case "三角形":
                if(!flag){
                    g.drawLine(x1,y1,x2,y2);
                    new_x1=x1;new_x2=x2;new_y1=y1;new_y2=y2;
                    temp = 2;
                    flag = true;
                }
                break;
            case "多边形":
                if(!flag){
                    g.drawLine(x1,y1,x2,y2);
                    points.add(new Point(x1,y1));
                    points.add(new Point(x2,y2));
                    new_x1=x1;new_x2=x2;new_y1=y1;new_y2=y2;
                    temp = 2;
                    flag = true;
                }
                break;
            case "圆":
                g.drawOval(x1,y1, line_long(x1,y1,x2,y2), line_long(x1,y1,x2,y2));
                    drawingGraphics.addList(new Circle(line_long(x1,y1,x2,y2)/2));
                break;
            case "矩形":
                g.drawRect(x1, y1, Math.abs(x2 - x1), Math.abs(y2 - y1));
                drawingGraphics.addList(new Rectangle(Math.abs(x2 - x1), Math.abs(y2 - y1)));
                break;
        }
    }
    private int line_long(int x1,int y1,int x2,int y2){
        return  (int)sqrt(square(x1,x2)+square(y1,y2));
    }
    private double square(int a,int b){
        return (a-b)*(a-b);
    }
//    鼠标进入到组件上时调用。
    @Override
    public void mouseEntered(MouseEvent e) {

    }
    //鼠标离开组件时调用。
    @Override
    public void mouseExited(MouseEvent e) {

    }
//鼠标按键在组件上按下并拖动时调用。
    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        x2 = e.getX();
        y2 = e.getY();
        if (shape.equals("橡皮擦")) {
            g.setStroke(new BasicStroke(80));
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(Color.WHITE);
            g.drawLine(x1, y1, x2, y2);
            x1 = x2;
            y1 = y2;
        }
    }
//鼠标光标移动到组件上但无按键按下时调用。
    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("")) {
            JButton button = (JButton) e.getSource();
            color = button.getBackground();
            System.out.println("color = " + color);
        } else {
            JButton button = (JButton) e.getSource();
            shape = button.getActionCommand();
            System.out.println("String = " + shape);
        }
        if(shape.equals("读取文件中的图形")){

        }else if(shape.equals("将图形分类排序")){
            drawingGraphics.setClassification();
            drawingGraphics.sort();
        }else if(shape.equals("将图形写入文件")){
            File file = new File("Geometric.txt");
            FileWriter writer = null;
            try {
                writer = new FileWriter(file);
                writer.write(drawingGraphics.toString());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }finally {
                if(writer != null) {
                    try {
                        writer.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        }else if(shape.equals("显示已有图形")){
            List<GeometricObject> list = drawingGraphics.getList();
            for (GeometricObject g:list
                 ) {
                System.out.println(g);
            }
        }
    }
}
