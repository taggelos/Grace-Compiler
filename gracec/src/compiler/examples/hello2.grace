$$fun main () : nothing

var a : int;

    fun f() : nothing
    var b : int;
    {
        b <- 50;
        while b < 52 do 
        {
        puti(a);
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
var a : int[3];
var x : int;
{
	a[0] <- 0;
	$x <- a[0];
    puti(a[0]);
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

fun main () : nothing
var x:int;
    fun f() : nothing 
    {
        puti(x);
    }
{
    x<-45;
    f();
    
}