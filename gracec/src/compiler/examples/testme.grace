fun main(): nothing
var i : int;
var x : int;
var y: int;
var t: int;
{
 t<-0;
 y<-2 - 3;
 while  y>2 do 

 x<-t;
 	t<-y;
}

$$
fun main(): nothing
	var i : int;
	var x : int;
	var y: int;
	var t: int;
	fun bsort (n : int; ref ar : int[]) : nothing
      var changed, i : int;
   {
   		t <- x;
         x <- y;
         y <- t;
      changed <- 1;
      while changed > 0 do {
        changed <- 0;
        i <- 0;
        while i < n-1 do {
          if ar[i] > ar[i+1] then {
            ar[i]<-ar[i+1];
            changed <- 1;
          }
          i <- i+1;
        }
      }
   }
	{
	 t<-0;
	 x<-t;
	 while  1+1>2 do 
	 	t<-1;
	 y<-t;
	}

$$