import Jama.Matrix;

class Lagrange {
  
  void drawPolynom(ArrayList<Point> points) {
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
