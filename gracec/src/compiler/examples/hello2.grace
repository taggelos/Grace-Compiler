fun main () : nothing
var b : int;
var a : int;

    fun f() : nothing
    {
        $while a < 52 do 
        ${
        puti(a);
        $a <- a + 1;
        $}
        return;
    }
{
a <- 50;
f();
}
$$
fun main () : nothing
var a : int[10];
var x : int;
{
	a[0] <- 0;
	x <- a[0];
    puti(x);
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