import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class neuralnetwork extends PApplet {


public static double sigmoid(double x){
    return 1/(1+Math.exp(-x));
}

public static double dsigmoid(double x){
  return x*(1-x);
}

 
 public static void arraytomat(Matrix output,double[] array){
    
     
     for(int i = 0;i<array.length;i++){
         output.Matrix[i][0]=array[i];
     }
 }
 
public static double[] mattoarray(Matrix mat){
    
     double[] output = new double[mat.Matrix.length];
     for(int i = 0;i<mat.Matrix.length;i++){
       for(int j=0;j<mat.Matrix[0].length;j++){
             output[i]=mat.Matrix[i][j];
         
       }
     }
     
     return output;
 }

class NeuralNetwork{
  int input_nodes;
  int hidden_nodes;
  int output_nodes;
  Matrix weights_ih;
  Matrix weights_ho;
  Matrix Vinputs_ih;
  Matrix Vhidden_ho;
  Matrix Voutput;
  Matrix hidden_bias;
  Matrix output_bias;
  double learningrate;

  
 NeuralNetwork(int input_nodes , int hidden_nodes,int output_nodes){
     this.input_nodes = input_nodes;
     this.hidden_nodes = hidden_nodes;
     this.output_nodes = output_nodes;
     
     this.weights_ih = new Matrix(hidden_nodes,input_nodes);
     this.weights_ho = new Matrix(output_nodes,hidden_nodes);
     this.weights_ih.randomize();
     this.weights_ho.randomize();
     
     this.hidden_bias = new Matrix(hidden_nodes,1);
     this.output_bias = new Matrix(output_nodes,1);
     this.hidden_bias.randomize();
     this.output_bias.randomize();
     this.learningrate=0.04f;
     
     
 }
   
  public double[] feedforward(double[]inputs){
    
    double[]guess;
    
    Vinputs_ih = new Matrix(inputs.length,1);
    arraytomat(Vinputs_ih,inputs);
    
    Matrix converted = new Matrix(inputs.length,1);
    arraytomat(converted,inputs);
    
    //converted.matrixsigmoid();
    //getting the weighted sum of the hidden
    Vhidden_ho = weights_ih.dotproduct(converted);
    Vhidden_ho.add(this.hidden_bias);
    Vhidden_ho.matrixsigmoid();
    
    
    //getting the output
    Voutput = weights_ho.dotproduct(Vhidden_ho);
    Voutput.add(this.output_bias);
    Voutput.matrixsigmoid();
    
   
    guess=mattoarray(Voutput);
    return guess;
    
 }  
 public NeuralNetwork copy(){
  NeuralNetwork clone = new NeuralNetwork(this.input_nodes,this.hidden_nodes,this.output_nodes);
  
     clone.weights_ih  = this.weights_ih.copy() ;
     clone.weights_ho  = this.weights_ho.copy() ;
     clone.hidden_bias = this.hidden_bias.copy();
     clone.output_bias = this.output_bias.copy();
     
     return clone;

 }
 public void mutate(float rate){
   
  
      this.weights_ih.mutate(rate);
      this.weights_ho.mutate(rate);
      this.hidden_bias.mutate(rate);
      this.output_bias.mutate(rate);
 }
 
 public void train(double[]inputsa,double[]targetsa){
      double[] guesseda = this.feedforward(inputsa);
      Matrix guess = new Matrix(guesseda.length,1);
      arraytomat(guess,guesseda);
      
      Matrix inputs =new Matrix(inputsa.length,1);
      arraytomat(inputs,inputsa);
      
      Matrix targets =new Matrix(targetsa.length,1);
      arraytomat(targets,targetsa);
      
      Matrix output_error = targets.subtract(guess);
      
      // lets calculate the gradient
        Matrix hidden_gradient = guess.matrixdsigmoid();
        hidden_gradient.multiply(output_error);
        hidden_gradient.multiply(learningrate);
        Matrix voutput_t = Vhidden_ho.transpose();
         
         //calculate the delat weights for hidden inputs and make changes
         Matrix delta_weights_ho = hidden_gradient.dotproduct(voutput_t);
         weights_ho.add(delta_weights_ho);// hidden weights are changed
         this.output_bias.add(hidden_gradient);
         
         
      // lets make input weights to change first the error
      // error = tansposeofweightedsum * erroronout
         Matrix weights_ho_t =weights_ho.transpose();;
         Matrix v_error_ih =weights_ho_t.dotproduct(output_error);
         
         //calculate the gradient
           Matrix input_gradient = Vhidden_ho.matrixdsigmoid();
           input_gradient.multiply(v_error_ih);
           input_gradient.multiply(learningrate);
           Matrix input_transpose = Vinputs_ih.transpose();
           
           // lets calculate the delta wieghts and make changes
               Matrix delta_inputweights_t = input_gradient.dotproduct(input_transpose);
               weights_ih.add(delta_inputweights_t);
               this.hidden_bias.add(input_gradient);
           
 }
 
 
 public void setLearningRate(float lr){
   this.learningrate = lr;
 }

 
 
 
 
 //void train(double[]inputs,double[] target){
 //      double[] guess = this.feedforward(inputs);

        
 //       Matrix converted = new Matrix(inputs.length,1);
 //         for(int i=0;i<inputs.length;i++){
 //              converted.Matrix[i][0]=inputs[i]; 
 //         }
          
 //      Matrix guessmat = new Matrix(inputs.length-1,1);
 //       for(int i=0;i<guess.length;i++){
 //            guessmat.Matrix[i][0]=guess[i]; 
 //       }
        
 //       Matrix targetmat = new Matrix(target.length,1);
 //       for(int i=0;i<target.length;i++){
 //            targetmat.Matrix[i][0]=target[i]; 
 //       }
        
 //      // deltas weight = lr*errors*gradient;
       
 //      Matrix Verror_o = targetmat.subtract(guessmat);
       
        
 //      Matrix gradient = guessmat.matrixdsigmoid();
       
 //      gradient.multiply(Verror_o);
 //      gradient.multiply(learningrate);
 //      Matrix v_hidden_ho_t = Vhidden_ho.transpose();
       
 //      Matrix delats_weight_ho=gradient.dotproduct(v_hidden_ho_t);
 //      weights_ho.add(delats_weight_ho);
       
       
 //      Matrix weight_hidden_t = weights_ho.transpose();
 //      Matrix Verror_hidden = weight_hidden_t.dotproduct(Verror_o);
       
 //      Matrix hidden_gradient = Verror_hidden.matrixdsigmoid();
 //      hidden_gradient.multiply(Verror_hidden);
 //      hidden_gradient.multiply(learningrate);
        
        
 //      //calculate hidden delats'
 //      Matrix input_transpose = converted.transpose();
 //      Matrix delats_weight_ih = hidden_gradient.dotproduct(input_transpose);
 //      weights_ih.add(delats_weight_ih);
       
      
 //  }
 
 }
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
  
  public void fly(){
   for(int i = 0;i<10000;i++){
    if(i%1000==0){
      if(this.y>0)this.y-=6;
    }
    }
  }
  
  public void survive(double[] inputs){ 
       double[] guess = this.brain.feedforward(inputs);
       if(guess[0]>=0.5f) this.fly();
    
  }
  
  public void skying(){
    
    score++;
    if(this.y<height-70)this.y+=3;
    fill(255,255,255,190);
    image(img,x,y,70,70);
    
  }
  
  public boolean iscollided(){
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

 
 public void newGeneration(){
     
     for(int i = 0 ;i<noofbirds;i++){
          Bird tempbird = new Bird();
          tempbird.brain = bestbird.brain.copy();
          tempbird.brain.mutate(0.5f);
          Birds.add(tempbird);
          
     }
 }
  
  
}



class Matrix{
  
  int rows;
  int cols;
  double[][] Matrix ;
  
 Matrix(int rows,int cols){
   
   this.rows = rows;
   this.cols = cols;
   this.Matrix = new double[rows][cols];
   
    for(int i = 0 ; i<rows;i++){
    for(int j = 0;j<cols;j++){
      this.Matrix[i][j]=0;
    }
   }
   
 }
 
  
  public void randomize(){
    
    for(int i = 0 ; i<this.Matrix.length;i++){
    for(int j = 0;j<this.Matrix[i].length;j++){
       this.Matrix[i][j]= random(-1.1f,1.1f);
    }
   }
  }
 
                         /* this is for scaler Operations*/

 public void add(double n){
  
  for(int i = 0 ; i<this.Matrix.length;i++){
    for(int j = 0;j<this.Matrix[i].length;j++){
       this.Matrix[i][j] +=n;
    }
   }
  
  
  }
  
  
  public Matrix copy(){
    
    Matrix m = new Matrix(this.rows,this.cols);
      for(int i = 0 ; i<this.Matrix.length;i++){
        for(int j = 0;j<this.Matrix[i].length;j++){
            m.Matrix[i][j]=this.Matrix[i][j] ;
        }
    }
    return m;
  }
  
  
  public void mutate(float rate){
    
     for(int i = 0 ; i<this.Matrix.length;i++){
        for(int j = 0;j<this.Matrix[i].length;j++){
          if(random(0,1)<rate){
            this.Matrix[i][j] += randomGaussian();
            
          }
        }
    }
    
  }
  

 
  
  
  public Matrix subtract(double n){
    
   Matrix output = new Matrix(this.Matrix.length,this.Matrix[0].length);
 
    for(int i = 0 ; i<this.Matrix.length;i++){
      for(int j = 0;j<this.Matrix[i].length;j++){
         output.Matrix[i][j] = this.Matrix[i][j]-n;
      }
    }
  
     return output;

  }
  
  public void multiply(double n){
  
  for(int i = 0 ; i<this.Matrix.length;i++){
    for(int j = 0;j<this.Matrix[i].length;j++){
       this.Matrix[i][j] *= n;
    }
   }
  
  
  }
  
  public void divide(double n){
  
  for(int i = 0 ; i<this.Matrix.length;i++){
    for(int j = 0;j<this.Matrix[i].length;j++){
       this.Matrix[i][j] /= n;
    }
   }
  
  
  }
  
                         /* this is for Matrix Operations*/  
                         
                         
  public void add(Matrix m){
  
  for(int i = 0 ; i<this.Matrix.length;i++){
    for(int j = 0;j<this.Matrix[i].length;j++){
       this.Matrix[i][j] +=m.Matrix[i][j];
    }
   }

  }  
  
  public Matrix subtract(Matrix m){
  Matrix output = new Matrix(this.Matrix.length,this.Matrix[0].length);
  
  for(int i = 0 ; i<this.Matrix.length;i++){
    for(int j = 0;j<this.Matrix[i].length;j++){
       output.Matrix[i][j]=this.Matrix[i][j] -m.Matrix[i][j];
    }
   }
   return output;

  }
  
  public void multiply(Matrix m){
  
  for(int i = 0 ; i<this.Matrix.length;i++){
    for(int j = 0;j<this.Matrix[0].length;j++){
       this.Matrix[i][j] *=m.Matrix[i][j];
    }
   }

  }
  
  //void getmultiply(Matrix m){
  //Matrix oupu
  //for(int i = 0 ; i<this.Matrix.length;i++){
  //  for(int j = 0;j<this.Matrix[i].length;j++){
  //     this.Matrix[i][j] *=m.Matrix[i][j];
  //  }
  // }
  public void divide(Matrix m){
  
  for(int i = 0 ; i<this.Matrix.length;i++){
    for(int j = 0;j<this.Matrix[i].length;j++){
       this.Matrix[i][j] /=m.Matrix[i][j];
    }
   }

  }


                   /* this is for Matrix dot operation (FOR NN)*/  
                   
                   
  public Matrix dotproduct(Matrix m){
    
    if(this.cols == m.rows){
        Matrix product = new Matrix(this.rows,m.cols);
        
         for(int i = 0 ; i<product.Matrix.length;i++){
            for(int j = 0;j<product.Matrix[0].length;j++){
                
              double sum =0;
              for(int k = 0 ;k<this.cols;k++){
                   sum += this.Matrix[i][k]*m.Matrix[k][j];  
              }
              product.Matrix[i][j]=sum;
            }
           }
           
           return product;
        
    }else{
      // error:
      println("Dot product rows and cols mismatch");
      return null;
    }
    
  }
  
  
   public Matrix transpose(){
      Matrix transposed = new Matrix(this.cols,this.rows);
      
        for(int i = 0 ; i<this.Matrix.length;i++){      
          for(int j = 0;j<this.Matrix[i].length;j++){
                  transposed.Matrix[j][i]=this.Matrix[i][j];
          }
     }
     
     return transposed;
  }
  
  
  public void matrixsigmoid(){
    
      for(int i = 0 ; i<this.Matrix.length;i++){
        for(int j = 0;j<this.Matrix[i].length;j++){
               double temp = this.Matrix[i][j];
               double sign = sigmoid(temp);
               this.Matrix[i][j] = sign;
                  
        }
   }
    
  }
  
   public Matrix matrixdsigmoid(){
     
     Matrix output = new Matrix(this.rows,this.cols);
     
      for(int i = 0 ; i<this.Matrix.length;i++){
        for(int j = 0;j<this.Matrix[i].length;j++){
               output.Matrix[i][j]= dsigmoid(this.Matrix[i][j]);         
        }
   }
   return output;
    
  }
  


  public void printmat(){
    
     for(int i = 0 ; i<this.Matrix.length;i++){
       print("\n");
        for(int j = 0;j<this.Matrix[0].length;j++){
               print(this.Matrix[i][j]+" ");         
        }
   }
   
   println("\n__________\n");
    
  }
 
}



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
  
  public void show(){
    
     this.x-=1.5f;
     fill(255);
     rect(x,ytop,pipewidth,pipeheighttop);
     rect(x,ybot,pipewidth,pipeheightbot);
    
  }
  
  
  
}

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

public void setup(){
  
 
  pipes.add(new Pipe());
  frameRate(300);

  
  for(int i =0;i<noofbirds;i++){
     Birds.add(new Bird()); 
  }
  
}

public void mouseClicked(){
    
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

public void draw(){
   
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



 

 
 

 
 
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "neuralnetwork" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
