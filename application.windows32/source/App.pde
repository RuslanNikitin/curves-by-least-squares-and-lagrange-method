Grid grid;
Point currentPoint;
ArrayList<Point> allPoints;
MNK mnk;
Lagrange lagrange;

void setup() {
  size(580, 430);    
  grid = new Grid();
  allPoints = new ArrayList<Point>();
  mnk = new MNK();
  lagrange = new Lagrange();
}

void draw() {
  background(180);
  grid.update();
  drawPoints();
  checkCursor();
  mnk.drawPolynom(allPoints);   
  lagrange.drawPolynom(allPoints);
}

void mouseDragged() {
  if(currentPoint != null && grid.isAcceptableArea()) {
    currentPoint.move();
  } 
}

void mousePressed() {  
  if(!isMouseOverPoint() && grid.isAcceptableArea() && mouseButton == LEFT) {    
    allPoints.add(new Point());
  } else if(isMouseOverPoint() && mouseButton == RIGHT) {
    allPoints.remove(currentPoint);
  }
}

void checkCursor() {
  if(isMouseOverPoint()) {
    cursor(HAND);
  } else {
    cursor(ARROW);
  }
}

void drawPoints() {
  for(Point point : allPoints) {
    point.drawPoint();
  }
}

boolean isMouseOverPoint() {
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
