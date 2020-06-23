package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;

%%

%{

	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type) {
		return new Symbol(type, yyline+1, yycolumn);
	}
	
	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type, Object value) {
		return new Symbol(type, yyline+1, yycolumn, value);
	}

%}

%cup
%line
%column

%xstate COMMENT

%eofval{
	return new_symbol(sym.EOF);
%eofval}

%%

// WHITESPACES
" " 	{ }
"\b" 	{ }
"\t" 	{ }
"\r\n" 	{ }
"\f" 	{ }


// COMMENTS
<YYINITIAL>	"//"	{ yybegin(COMMENT); 	}
<COMMENT> 	.		{ yybegin(COMMENT); 	}
<COMMENT> 	"\r\n" 	{ yybegin(YYINITIAL); 	}

// KEYWORDS
"program"   	{ return new_symbol(sym.PROGRAM, 	yytext()); }
"const"   		{ return new_symbol(sym.CONST, 		yytext()); }
"new" 			{ return new_symbol(sym.NEW, 		yytext()); }
//"abstract"   	{ return new_symbol(sym.ABSTRACT,	yytext()); }
//"class"			{ return new_symbol(sym.CLASS, 		yytext()); }
//"extends"		{ return new_symbol(sym.EXTENDS, 	yytext()); }
"break"			{ return new_symbol(sym.BREAK, 		yytext()); }
"continue"		{ return new_symbol(sym.CONTINUE, 	yytext()); }
"if"			{ return new_symbol(sym.IF, 		yytext()); }
"else"			{ return new_symbol(sym.ELSE, 		yytext()); }
"for"			{ return new_symbol(sym.FOR, 		yytext()); }
"print" 		{ return new_symbol(sym.PRINT, 		yytext()); }
"read" 			{ return new_symbol(sym.READ, 		yytext()); }
"return" 		{ return new_symbol(sym.RETURN, 	yytext()); }
"void" 			{ return new_symbol(sym.VOID, 		yytext()); }

//"true" 		{ return new_symbol(sym.TRUE, 	yytext()); }
//"false" 			{ return new_symbol(sym.FALSE, 		yytext()); }

// PREDECLARED PROCEDURES
//"chr" 	{ return new_symbol(sym.CHR, yytext()); }
//"ord" 	{ return new_symbol(sym.ORD, yytext()); }
//"len" 	{ return new_symbol(sym.LEN, yytext()); }

// TYPES
//"int" 	{ return new_symbol(sym.INT, 	yytext()); }
//"bool" 	{ return new_symbol(sym.BOOL,	yytext()); }
//"char" 	{ return new_symbol(sym.CHAR, 	yytext()); }

//"null" 	{ return new_symbol(sym.NULL, 	yytext()); }

// OPERATORS
"+" 		{ return new_symbol(sym.PLUS, 			yytext()); }
"-" 		{ return new_symbol(sym.MINUS, 			yytext()); }
"*" 		{ return new_symbol(sym.MUL, 			yytext()); }
"/" 		{ return new_symbol(sym.DIV, 			yytext()); }
"%" 		{ return new_symbol(sym.MOD,			yytext()); }
"==" 		{ return new_symbol(sym.EQUAL, 			yytext()); }
"!=" 		{ return new_symbol(sym.NOTEQUAL, 		yytext()); }
">" 		{ return new_symbol(sym.GREATER, 		yytext()); }
">=" 		{ return new_symbol(sym.GREATEREQUAL, 	yytext()); }
"<" 		{ return new_symbol(sym.LESS, 			yytext()); }
"<=" 		{ return new_symbol(sym.LESSEQUAL, 		yytext()); }
"&&" 		{ return new_symbol(sym.AND, 			yytext()); }
"||" 		{ return new_symbol(sym.OR, 			yytext()); }
"=" 		{ return new_symbol(sym.ASSIGN, 		yytext()); }
"++" 		{ return new_symbol(sym.INC, 			yytext()); }
"--" 		{ return new_symbol(sym.DEC, 			yytext()); }
"+=" 		{ return new_symbol(sym.PLUSEQ, 		yytext()); }
"-=" 		{ return new_symbol(sym.MINUSEQ, 		yytext()); }
"*=" 		{ return new_symbol(sym.MULEQ, 			yytext()); }
"/=" 		{ return new_symbol(sym.DIVEQ, 			yytext()); }
"%=" 		{ return new_symbol(sym.MODEQ, 			yytext()); }

// SEPARATORS
";" 		{ return new_symbol(sym.SEMI, 	yytext()); }
"," 		{ return new_symbol(sym.COMMA, 	yytext()); }
"." 		{ return new_symbol(sym.DOT, 	yytext()); }

// BRACES
"(" 		{ return new_symbol(sym.LPAREN, 	yytext()); }
")" 		{ return new_symbol(sym.RPAREN, 	yytext()); }
"[" 		{ return new_symbol(sym.LSQBRACE, 	yytext()); }
"]" 		{ return new_symbol(sym.RSQBRACE, 	yytext()); }
"{" 		{ return new_symbol(sym.LBRACE,		yytext()); }
"}"			{ return new_symbol(sym.RBRACE, 	yytext()); }


"true"|"false"  				{ return new_symbol(sym.BOOLCONST, new Boolean (yytext())); }
[0-9]+  						{ return new_symbol(sym.NUMCONST, new Integer (yytext())); }
"'"."'"  						{ return new_symbol(sym.CHARCONST, new Character (yytext().charAt(1))); }
([a-z]|[A-Z])[a-z|A-Z|0-9|_]* 	{return new_symbol (sym.IDENT, yytext()); }

. { System.err.println("Leksicka greska ("+yytext()+") u liniji "+(yyline+1)); }