class Grid {
  
  private int axisGap = 30;
  private int axisNameGap = 20;  
  private int valueOfDivision = 50;
  private int smallTextSize = 12;
  private int bigTextSize = 20; 
  private int arrowSize = 30;
  
  void update() {
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
  
  boolean isAcceptableArea() {
    return (mouseX >= axisGap
            && mouseX <= width - valueOfDivision
            && mouseY >= valueOfDivision
            && mouseY <= height - axisGap);
  }
  
  //public int getAxisGap() {
  //  return this.axisGap;
  //}
}
