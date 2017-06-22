$$fun main () : nothing
var x:int;
var a : int[];
{
	a[0] <- 0;
    x<-45;
    if x > 50 then
    	a[0] <- 1;
    else
    	a[0] <- 2;
    puti(a[0]);
}
$$
fun main () : nothing
var a : int[];
var x : int;
{
	a[0] <- 0;
	x <- a[0];
    puti(x);
    return;
}