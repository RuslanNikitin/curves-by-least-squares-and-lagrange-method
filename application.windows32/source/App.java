import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import Jama.Matrix; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class App extends PApplet {

Grid grid;
Point currentPoint;
ArrayList<Point> allPoints;
MNK mnk;
Lagrange lagrange;

public void setup() {
      
  grid = new Grid();
  allPoints = new ArrayList<Point>();
  mnk = new MNK();
  lagrange = new Lagrange();
}

public void draw() {
  background(180);
  grid.update();
  drawPoints();
  checkCursor();
  mnk.drawPolynom(allPoints);   
  lagrange.drawPolynom(allPoints);
}

public void mouseDragged() {
  if(currentPoint != null && grid.isAcceptableArea()) {
    currentPoint.move();
  } 
}

public void mousePressed() {  
  if(!isMouseOverPoint() && grid.isAcceptableArea() && mouseButton == LEFT) {    
    allPoints.add(new Point());
  } else if(isMouseOverPoint() && mouseButton == RIGHT) {
    allPoints.remove(currentPoint);
  }
}

public void checkCursor() {
  if(isMouseOverPoint()) {
    cursor(HAND);
  } else {
    cursor(ARROW);
  }
}

public void drawPoints() {
  for(Point point : allPoints) {
    point.drawPoint();
  }
}

public boolean isMouseOverPoint() {
  boolean condition = false;
  
  for(Point point : allPoints) {
    if(point.isMouseOver()) {
      condition = true;
      currentPoint = point;
      break;
    } else {
      currentPoint = null;
    }
  }
  
  return condition;
}
class Gauss{

    private double[][] augmentedMatrix;

    Gauss(double[][] matrix) {
        augmentedMatrix = matrix;
    }

    public void eliminate() {
        int startColumn = 0;
        for (int row = 0; row < augmentedMatrix.length; row++) {
            //if the number in the start column is 0, try to switch with another
            while (augmentedMatrix[row][startColumn] == 0.0f){
                boolean switched = false;
                int i = row;
                while (!switched && i < augmentedMatrix.length) {
                    if(augmentedMatrix[i][startColumn] != 0.0f){
                        double[] temp = augmentedMatrix[i];
                        augmentedMatrix[i] = augmentedMatrix[row];
                        augmentedMatrix[row] = temp;
                        switched = true;
                    }
                    i++;
                }
                //if after trying to switch, it is still 0, increase column
                if (augmentedMatrix[row][startColumn] == 0.0f) {
                    startColumn++;
                }
            }
            //if the number isn't one, reduce to one
            if(augmentedMatrix[row][startColumn] != 1.0f) {
                double divisor = augmentedMatrix[row][startColumn];
                for (int i = startColumn; i < augmentedMatrix[row].length; i++) {
                    augmentedMatrix[row][i] = augmentedMatrix[row][i] / divisor;
                }
            }
            //make sure the number in the start column of all other rows is 0
            for (int i = 0; i < augmentedMatrix.length; i++) {
                if (i != row && augmentedMatrix[i][startColumn] != 0) {
                    double multiple = 0 - augmentedMatrix[i][startColumn];
                    for (int j = startColumn; j < augmentedMatrix[row].length; j++){
                        augmentedMatrix[i][j] += multiple*augmentedMatrix[row][j];
                    }
                }
            }
            startColumn++;
        }
    }
    
    public double[][] getMatrix() {
      return this.augmentedMatrix;
    }
}
class Grid {
  
  private int axisGap = 30;
  private int axisNameGap = 20;  
  private int valueOfDivision = 50;
  private int smallTextSize = 12;
  private int bigTextSize = 20; 
  private int arrowSize = 30;
  
  public void update() {
    strokeWeight(2);
    stroke(0);
    fill(0); 
  
    // Y axis
    textSize(bigTextSize);
    text("y", axisNameGap, axisNameGap);
    line(axisGap, axisGap, axisGap, height - axisGap);
    triangle(arrowSize - 5, arrowSize + 10,
             arrowSize, arrowSize, arrowSize + 5,
             arrowSize + 10);
    textSize(smallTextSize);
    for(int i = valueOfDivision; i < (height - valueOfDivision); i += valueOfDivision) {
      text(i, 3, height - i - axisGap + smallTextSize / 2);
    }  
  
    // X axis
    textSize(bigTextSize);
    text("x", width - axisNameGap, height - axisNameGap);
    line(axisGap, height - axisGap, width - axisGap, height - axisGap);
    triangle(width - arrowSize - 10, height - arrowSize - 5,
             width - arrowSize, height - arrowSize,
             width - arrowSize - 10, height - arrowSize + 5);
    textSize(smallTextSize);
    for(int i = valueOfDivision; i <= (width - valueOfDivision); i += valueOfDivision) {
      text(i, i + axisGap * 2/3, height - axisGap / 2 + smallTextSize / 2);
    }
  
    // vertical mesh
    strokeWeight(0);
    for(int i = (axisGap + valueOfDivision); i <= width; i += valueOfDivision) {
      line(i, valueOfDivision, i, height - axisGap);
    }
  
    // horizontal mesh
    strokeWeight(0);
    for(int i = height - (axisGap + valueOfDivision); i > 0; i -= valueOfDivision) {
      line(axisGap, i, width - valueOfDivision, i);
    }
  }
  
  public boolean isAcceptableArea() {
    return (mouseX >= axisGap
            && mouseX <= width - valueOfDivision
            && mouseY >= valueOfDivision
            && mouseY <= height - axisGap);
  }
  
  //public int getAxisGap() {
  //  return this.axisGap;
  //}
}


class Lagrange {
  
  public void drawPolynom(ArrayList<Point> points) {
    try{    
      tryToDraw(points);
    } catch (RuntimeException e) {
      text("координаты точек по оси \"Х\" не должны совпадать", 130, 40);
    }
  }

  private void tryToDraw(ArrayList<Point> points) {
    noFill();
    strokeWeight(2);
    stroke(50, 170, 150);

    if(points != null && !points.isEmpty() && points.size() > 2) {  
      double[] abcd = findPolynomialFactors(points);
      
      beginShape();  
      int y = 0;
      for(int x = 30; x <= 530; x++) {        
        for(int i = 0; i < abcd.length; i++) {         
          y += Math.pow(x, i) * abcd[abcd.length - i - 1];
        }
        curveVertex(x, y);
        y = 0;
      }
      endShape();
  
      line(420, 35, 440, 35);
      text("Лагранж", 450, 40);
    }
  }

  private double[] findPolynomialFactors (ArrayList<Point> list) {

        double[] x = new double[list.size()];
        double[] y = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            x[i] = list.get(i).getX();
            y[i] = list.get(i).getY();
        }

        int n = x.length;

        double[][] data = new double[n][n];
        double[]   rhs  = new double[n];

        for (int i = 0; i < n; i++) {
            double v = 1;
            for (int j = 0; j < n; j++) {
                data[i][n-j-1] = v;
                v *= x[i];
            }

            rhs[i] = y[i];
        }

        Matrix m = new Matrix (data);
        Matrix b = new Matrix (rhs, n);

        Matrix s = m.solve(b);

        return s.getRowPackedCopy();
    }
}
class MNK { 
  
  public void drawPolynom(ArrayList<Point> points) {
    try{    
      tryToDraw(points);
    } catch (RuntimeException e) {
      // ignore
    }
  }
  
  private void tryToDraw(ArrayList<Point> points) {
    noFill();
    strokeWeight(2);
    stroke(255, 100, 0);
    
    if(points != null && !points.isEmpty() && points.size() > 2) {      
      double[] abcd = findPolynomialFactors(points);  
      beginShape();      
      for(int x = 30; x <= 530; x++) {
        int y = (int) (abcd[0] * Math.pow(x, 3) + abcd[1] * Math.pow(x, 2) + abcd[2] * x + abcd[3]);  
        curveVertex(x, y);
      }
      endShape();
  
      line(420, 15, 440, 15);
      text("МНК", 450, 20);
    }
  }
  
  private double[] findPolynomialFactors(ArrayList<Point> list) {
    double[] result = new double[4];
    
    double sumX6 = 0;
    double sumX5 = 0;
    double sumX4 = 0;
    double sumX3 = 0;
    double sumX2 = 0;
    double sumX = 0;
    double sumX3Y = 0;
    double sumX2Y = 0;
    double sumXY = 0;
    double sumY = 0;
    double n = 0;
    
    for(Point point : list) {
      sumX6 += Math.pow(point.getX(), 6);
      sumX5 += Math.pow(point.getX(), 5);
      sumX4 += Math.pow(point.getX(), 4);
      sumX3 += Math.pow(point.getX(), 3);
      sumX2 += Math.pow(point.getX(), 2);
      sumX += point.getX();
      sumX3Y += (Math.pow(point.getX(), 3) * point.getY());
      sumX2Y += (Math.pow(point.getX(), 2) * point.getY());
      sumXY += (point.getX() * point.getY());
      sumY += point.getY();
      n++;
    }
    
    double[][] array = {
      {sumX6, sumX5, sumX4, sumX3, sumX3Y},
      {sumX5, sumX4, sumX3, sumX2, sumX2Y},
      {sumX4, sumX3, sumX2, sumX, sumXY},
      {sumX3, sumX2, sumX, n, sumY}      
    };
    
    Gauss gauss = new Gauss(array);
    gauss.eliminate();
    
    for(int i = 0; i < 4; i++) {
      result[i] = gauss.getMatrix()[i][4];
    }    
    
    return result;
  }
}
class Point {
  
  private int x;
  private int y;
  private int d = 10;
  
  Point() {
    this.x = mouseX;
    this.y = mouseY;    
  }
  
  public void drawPoint() { 
    if(isMouseOver()) {      
      text(this.toString(), x - 30, y - 10);      
      strokeWeight(2);
      stroke(255);
      if(mousePressed) {
        fill(255);
      }
    }
    ellipse(x, y, d, d);
    
    fill(0);
    stroke(0);
    strokeWeight(0);    
  }
  
  public void move() {
    x = mouseX;
    y = mouseY;
  }
  
  public boolean isMouseOver() {
    boolean result = false;
    
    int disX = x - mouseX;
    int disY = y - mouseY;
  
    if(sqrt(sq(disX) + sq(disY)) < (d / 2)) {
       result = true;
    } else {
       result = false;
    }
    
    return result;
  }
  
  public int getX() {
    return this.x;
  }
  
  public int getY() {
    return this.y;
  }
  
  public String toString() {
    return "(" + (x - 30) + "; " + (height - y - 30) + ")";
  }
}
  public void settings() {  size(580, 430); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "App" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
