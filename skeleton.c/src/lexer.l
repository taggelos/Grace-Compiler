%{

#define T_and 0
#define T_while 1



%}
%%

"and"     { return T_and; }
"while"   { return T_while; }

%%
