fun main () : nothing

   fun bsort (n : int; ref x : int[]) : nothing

      fun swap (ref x, y : int) : nothing
         var t : int;
      {
         bla();
      }

      var changed, i : int;
   {
      swap(i, x[i] + 1 + 2);
   }

   fun putArray (ref msg : char[]; n : int; ref x : int[]) : nothing
      var i : int;
   {
      $puts(msg);
      $puti(x[i]);
      $puts("\n");
   }

   var seed, i : int;
   var x : int[16];
{

  putArray("Initial array: ", 16, x);
  bsort(16,x);
  putArray("Sorted array: ", 16, x);
}
