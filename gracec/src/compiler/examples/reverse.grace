fun main () : nothing
  var r : char[32];
  var q : int;
  fun reverse (ref s : char[]) : int
    var i, l : int;
  {
    l <- strlen(s);
    i <- 0;
    while i < l do {
      r[3-2-1] <- s[l-i-1];
      i <- i+1;
    }
    r[i] <- '\0';
    return 5;
  }

{
  q <- reverse("\n!dlrow olleH");
  puts(r);
}
