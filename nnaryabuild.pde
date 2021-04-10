
ArrayList<Pipe> pipes = new ArrayList<Pipe>() ;
Bird b;

ArrayList<Bird> Birds = new ArrayList<Bird>() ;
ArrayList<Bird> DiedBirds = new ArrayList<Bird>() ;
ArrayList<Bird> legends = new ArrayList<Bird>() ;
int collision = 0;
int noofbirds = 10;
ArrayList<double[][]> weights = new ArrayList<double[][]>();


public void settings() {
  size(600, 600);
}

void setup(){
  
 
  pipes.add(new Pipe());
  frameRate(300);

  
  for(int i =0;i<noofbirds;i++){
     Birds.add(new Bird()); 
  }
  
}

void mouseClicked(){
    
    Bird b = Birds.get(0);
      b.brain.weights_ih.printmat(); 
      //weights.add(b.brain.weights_ih.Matrix);
      
     b.brain.weights_ho.printmat();
     // weights.add(b.brain.weights_ho.Matrix);
     
      b.brain.hidden_bias.printmat();
      //weights.add(b.brain.hidden_bias.Matrix);
     
      b.brain.output_bias.printmat(); 
      //weights.add(b.brain.output_bias.Matrix);
      
      
}

void draw(){
   
   background(0);
    ArrayList<Pipe>toremovepipes = new ArrayList<Pipe>();
    for(Pipe p : pipes){
       p.show();
       for(Bird b : Birds){
       if(p.x<b.x) toremovepipes.add(p);
       }  
       
  }
    pipes.removeAll(toremovepipes);
  
   
   
   ArrayList<Bird>toremove = new ArrayList<Bird>();
   
   for(Bird bird : Birds){
        double[]inputs = {bird.x,bird.y,pipes.get(0).x,pipes.get(0).ytop,pipes.get(0).ybot};
         bird.skying(); 
         bird.survive(inputs); 
         
         if(bird.iscollided()){
            collision++;
            toremove.add(bird);
            DiedBirds.add(bird);
         }
   }
   
   Birds.removeAll(toremove);
   
   if(Birds.isEmpty()){
       Generation g = new Generation();
       g.newGeneration();
       println("new Generation is called"); 
   }
   
   
  
   if(frameCount % 160==0){
        pipes.add(new Pipe());
   }
   
   textSize(16);
   text("Collisions : " + collision,width-300,50); 
   //text("Score : " + score,width-300,70); 
  
  
}



 

 
 

 
 
