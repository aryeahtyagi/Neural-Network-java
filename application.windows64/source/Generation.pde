


class Generation{
  
 int maxscore = 0;
 Bird bestbird;
 Bird legend;
 

 Generation(){
   
     for(Bird B : DiedBirds){
           if(maxscore<B.score){
             bestbird =B;
             maxscore = B.score;
           }
     }
     
     legends.add(bestbird);
   
 }
 
 //void legendry(){
 //  int scoreb = Integer.MIN_VALUE;
 //     for(Bird b : legends){
 //            if(scoreb<b.score){
 //                 scoreb = b.score;
 //                 legend = b;
 //            }
 //     }
 //}

 
 void newGeneration(){
     
     for(int i = 0 ;i<noofbirds;i++){
          Bird tempbird = new Bird();
          tempbird.brain = bestbird.brain.copy();
          tempbird.brain.mutate(0.5);
          Birds.add(tempbird);
          
     }
 }
  
  
}
