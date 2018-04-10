class MNK { 
  
  void drawPolynom(ArrayList<Point> points) {
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
