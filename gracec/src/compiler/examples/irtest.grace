fun main () : nothing 
fun swap (x, y : int[]) : int
   var  i : int; 
   var a : int[][];
   
   {
      a[1][i+1] <- a[1][2];
      i<-i+1+2+x[i];
      x[0]<-x[i];
      return x[i];
   }

         var t : int[];
      {
         swap(t,t);
      }

