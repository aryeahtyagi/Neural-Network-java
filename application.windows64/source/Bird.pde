class Bird{
   float x;
   PImage img;
   float y;
   float gravity;
   int score=0;
   NeuralNetwork brain;
  Bird(){
    
    this.img = loadImage("bird.png");
    img.loadPixels();
    this.x = 68;
    this.y = height/2;
    gravity = 5;
    brain = new NeuralNetwork(5,10,1);
    
  }
  
  void fly(){
   for(int i = 0;i<10000;i++){
    if(i%1000==0){
      if(this.y>0)this.y-=6;
    }
    }
  }
  
  void survive(double[] inputs){ 
       double[] guess = this.brain.feedforward(inputs);
       if(guess[0]>=0.5) this.fly();
    
  }
  
  void skying(){
    
    score++;
    if(this.y<height-70)this.y+=3;
    fill(255,255,255,190);
    image(img,x,y,70,70);
    
  }
  
  boolean iscollided(){
    for(Pipe p : pipes){
       if(this.x+70>=p.x && this.x-67<=p.x ){
           if(this.y>p.ytop && this.y<p.ybot){
               return false;
           }else{
                
                return true;
           }
       }
      
    }
     return false;
  }
  
  
  
}
