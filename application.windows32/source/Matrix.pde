


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
 
  
  void randomize(){
    
    for(int i = 0 ; i<this.Matrix.length;i++){
    for(int j = 0;j<this.Matrix[i].length;j++){
       this.Matrix[i][j]= random(-1.1,1.1);
    }
   }
  }
 
                         /* this is for scaler Operations*/

 void add(double n){
  
  for(int i = 0 ; i<this.Matrix.length;i++){
    for(int j = 0;j<this.Matrix[i].length;j++){
       this.Matrix[i][j] +=n;
    }
   }
  
  
  }
  
  
  Matrix copy(){
    
    Matrix m = new Matrix(this.rows,this.cols);
      for(int i = 0 ; i<this.Matrix.length;i++){
        for(int j = 0;j<this.Matrix[i].length;j++){
            m.Matrix[i][j]=this.Matrix[i][j] ;
        }
    }
    return m;
  }
  
  
  void mutate(float rate){
    
     for(int i = 0 ; i<this.Matrix.length;i++){
        for(int j = 0;j<this.Matrix[i].length;j++){
          if(random(0,1)<rate){
            this.Matrix[i][j] += randomGaussian();
            
          }
        }
    }
    
  }
  

 
  
  
  Matrix subtract(double n){
    
   Matrix output = new Matrix(this.Matrix.length,this.Matrix[0].length);
 
    for(int i = 0 ; i<this.Matrix.length;i++){
      for(int j = 0;j<this.Matrix[i].length;j++){
         output.Matrix[i][j] = this.Matrix[i][j]-n;
      }
    }
  
     return output;

  }
  
  void multiply(double n){
  
  for(int i = 0 ; i<this.Matrix.length;i++){
    for(int j = 0;j<this.Matrix[i].length;j++){
       this.Matrix[i][j] *= n;
    }
   }
  
  
  }
  
  void divide(double n){
  
  for(int i = 0 ; i<this.Matrix.length;i++){
    for(int j = 0;j<this.Matrix[i].length;j++){
       this.Matrix[i][j] /= n;
    }
   }
  
  
  }
  
                         /* this is for Matrix Operations*/  
                         
                         
  void add(Matrix m){
  
  for(int i = 0 ; i<this.Matrix.length;i++){
    for(int j = 0;j<this.Matrix[i].length;j++){
       this.Matrix[i][j] +=m.Matrix[i][j];
    }
   }

  }  
  
  Matrix subtract(Matrix m){
  Matrix output = new Matrix(this.Matrix.length,this.Matrix[0].length);
  
  for(int i = 0 ; i<this.Matrix.length;i++){
    for(int j = 0;j<this.Matrix[i].length;j++){
       output.Matrix[i][j]=this.Matrix[i][j] -m.Matrix[i][j];
    }
   }
   return output;

  }
  
  void multiply(Matrix m){
  
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
  void divide(Matrix m){
  
  for(int i = 0 ; i<this.Matrix.length;i++){
    for(int j = 0;j<this.Matrix[i].length;j++){
       this.Matrix[i][j] /=m.Matrix[i][j];
    }
   }

  }


                   /* this is for Matrix dot operation (FOR NN)*/  
                   
                   
  Matrix dotproduct(Matrix m){
    
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
  
  
   Matrix transpose(){
      Matrix transposed = new Matrix(this.cols,this.rows);
      
        for(int i = 0 ; i<this.Matrix.length;i++){      
          for(int j = 0;j<this.Matrix[i].length;j++){
                  transposed.Matrix[j][i]=this.Matrix[i][j];
          }
     }
     
     return transposed;
  }
  
  
  void matrixsigmoid(){
    
      for(int i = 0 ; i<this.Matrix.length;i++){
        for(int j = 0;j<this.Matrix[i].length;j++){
               double temp = this.Matrix[i][j];
               double sign = sigmoid(temp);
               this.Matrix[i][j] = sign;
                  
        }
   }
    
  }
  
   Matrix matrixdsigmoid(){
     
     Matrix output = new Matrix(this.rows,this.cols);
     
      for(int i = 0 ; i<this.Matrix.length;i++){
        for(int j = 0;j<this.Matrix[i].length;j++){
               output.Matrix[i][j]= dsigmoid(this.Matrix[i][j]);         
        }
   }
   return output;
    
  }
  


  void printmat(){
    
     for(int i = 0 ; i<this.Matrix.length;i++){
       print("\n");
        for(int j = 0;j<this.Matrix[0].length;j++){
               print(this.Matrix[i][j]+" ");         
        }
   }
   
   println("\n__________\n");
    
  }
 
}
