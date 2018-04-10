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
  
  void move() {
    x = mouseX;
    y = mouseY;
  }
  
  boolean isMouseOver() {
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
  
  int getX() {
    return this.x;
  }
  
  int getY() {
    return this.y;
  }
  
  String toString() {
    return "(" + (x - 30) + "; " + (height - y - 30) + ")";
  }
}
