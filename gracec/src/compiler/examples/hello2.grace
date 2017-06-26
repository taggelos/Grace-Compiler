
fun main () : nothing
var x:int;
    fun f(ref a: int) : nothing 
    { 
     puti(4+a);
    }
{
 x<-4;
   f(x);
 
}


$$
fun main () : nothing
var x:int;
var y:int;
    fun f(ref a: int) : nothing 
    { 
     a<-y;
    }
{
    y<-666;
   f(x);
   puti(x);
}
$$

$$
fun main () : nothing
var x:int;
var t :int;
    fun f() : int 
    { 
     return x;
    }
{
   x <- 666; 
   t<- f();
   puti(t);
}
$$

$$
fun main () : nothing
var a : int[3];
var x : int;
{
    x <- 0;
    $x <- x;
    if x = 0 then
        puti(12);
    else
        puti(14);
}
$$


$$
fun main () : nothing
var a : int;
    fun f() : nothing
    var b : int;
    {
        b <- 50;
        while b < 52 do 
        {
        puti(b);
        b <- b + 1;
        }
        return;
    }
{
a <- 50;
f();
}
$$

$$
fun main () : nothing
var x:int;
    fun f() : nothing 
    {
        x<-4;
    }
{
    x<-45;
    f();
    puti(x);
}
$$
$$
fun main () : nothing
var x:int;
    fun f(a : int) : nothing 
    {

        puti(x);
    }
{
    x<-45;
    f(x);
    
}
$$

$$
fun main(): nothing
var i : int;
var x : int;
{
 x<-0; 
 i<-x*1+2;
 if x >0 or x<4 then
 return;
}
$$

$$
fun main () : nothing
var x:int;
var y : int;
    fun f(a : int) : nothing 
    {

        puti(a+a);
    }
{
    x<-45;
    y <- 10;
    f(x);
    
}
$$
$$
fun main () : nothing
var x,y :int;
    fun f(a,b : int) : nothing 
    {
        puti(a+b);
    }
{
    x<-45;
    y<- 10;
    f(x,y);
    
}
$$
$$
fun main () : nothing
var x:int;
     fun fa(z: int) : nothing 
    {
      puti(z);
    }
    fun f(a,b :int) : nothing 
    {
      fa(a);
    }

{
    x<-45;
    f(x,x);
    
}
$$
$$
fun main () : nothing
var x:int;
    fun f(a,b :int) : nothing 
    var x :int;
    {
   x<-5;
      a<-a;
      
      b<-b;
      b<-1;
      puti(b);
    }
{
    x<-45;
    f(x,x);
    
}
$$
