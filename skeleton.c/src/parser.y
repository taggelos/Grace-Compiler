/* sample parser
*/
%{
 int yylex();
 int yyerror(const char* msg);
%}
%%

program : ;

%%

/*
*/


int yyerror(const char* msg) {
        return 0;
}
