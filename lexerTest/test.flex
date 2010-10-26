/** The lexer for scanning command tokens. */
%%

%class CommandLexer

Parameter = [:jletterdigit:]+
WhiteSpace = [ \n\t\f]

%%

[:digit:]+ { return new Yytoken(Integer.parseInt(yytext())); }
{Parameter} { return new Yytoken(yytext()); }
{WhiteSpace} { /* Ignore Whitespace */ }
"-" { return new Yytoken('-'); }
"," { return new Yytoken(','); }