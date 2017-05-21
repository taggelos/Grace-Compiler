fun solve () : nothing
      fun hanoi (rings : int; ref source, target, auxiliary : char[]) : int
         fun move (ref source, target : char[]) : nothing
         {
            puts("Moving from ");
            puts(source);
            puts(" to ");
            puts(target);
            puts(".\n");
         }
      {
         if rings >= 1 then {
            hanoi(rings-hanoi(1,source, auxiliary, target), source, auxiliary, target);
            move(source, target);
            hanoi(rings-1, auxiliary, target, source);
            return 1;
         }
      }

      var NumberOfRings : int;
{
  writeString("Rings: ");
  NumberOfRings <- geti();
  hanoi(NumberOfRings, "left", "right", "middle");
}
