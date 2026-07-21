import java_cup.runtime.Symbol;

class sym {
  public static final int IDENT = 1;
  public static final int INTEIRO = 2;
  public static final int EOF = -1;
}
%%
%cup
%debug
%line
%column
digito = [0-9]
letra = [A-Za-z]
ident = letra(letra|digito)*
inteiro = 0 | [1-9][0-9]*
%%
{ident} {return new Symbol(sym.IDENT, yyline, yycolumn, yytext());}

{inteiro} {
  int aux = Integer.parseInt(yytext());
  return new Symbol(sym.INTEIRO, yyline, yycolumn, Integer.valueOf(aux));

}

.|\n {
    return new Symbol(sym.EOF, yyline, yycolumn, yytext());
}
