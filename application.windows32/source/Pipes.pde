


class Pipe  {
  
  float x;
  float ytop,ybot;
  float pipewidth;
  float pipeheighttop = -height;
  float pipeheightbot = height;
  int spacing = 190;
  
  Pipe(){
      
    this.x = width-100;
    this.ytop = random(0,height-80);
    this.ybot = ytop+spacing;
    this.pipewidth =30;
    
  }
  
  void show(){
    
     this.x-=1.5;
     fill(255);
     rect(x,ytop,pipewidth,pipeheighttop);
     rect(x,ybot,pipewidth,pipeheightbot);
    
  }
  
  
  
}
