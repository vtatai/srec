grammar srec;

options {
output=AST;
ASTLabelType = CommonTree;
}

tokens {
	
	SCRIPT;	
	REQUIRE;	
	
	ASSIGN;
	
	METHOD_CALL_OR_VARREF;
	METHOD_CALL;
	
	METHOD_DEF;
	METHOD_DEF_PARAMS;
	METHOD_BODY;
	
	LITNUMBER;
	LITBOOLEAN;
	LITSTRING;
	LITNIL;
	
	QNAME;
}
	
@lexer::header {
package com.github.srec.command.parser;
}


@header {
package com.github.srec.command.parser;
}


script
	:	require* script_stmt_block?
		-> ^(SCRIPT require* script_stmt_block?)
	;
     
require
	:	'require' STRING NEWLINE 
		-> ^(REQUIRE STRING)
	;
	
script_stmt_block
	:	(expression (NEWLINE | EOF!))+
	;
	
expression
	:	method_call_or_varref | assignment | method_def
	;
	
method_call_or_varref
options {backtrack=true;}
	:	ID
		-> ^(METHOD_CALL_OR_VARREF ID)
	|	ID '(' ')'
		-> ^(METHOD_CALL ID)
	|	ID '(' method_call_param (',' method_call_param)* ')'
		-> ^(METHOD_CALL ID method_call_param+)
	|	ID method_call_param (',' method_call_param)*
		-> ^(METHOD_CALL ID method_call_param+)
	;

method_call_param
	:	literal | method_call_or_varref
	;
	
assignment
	:	ID '=' (literal | method_call_or_varref)
		-> ^(ASSIGN ID literal? method_call_or_varref?)
	;

method_def
	:	'def' ID NEWLINE method_body 'end'
		-> ^(METHOD_DEF ID ^(METHOD_DEF_PARAMS) method_body)
	|	'def' ID '(' ')' NEWLINE method_body 'end'
		-> ^(METHOD_DEF ID ^(METHOD_DEF_PARAMS) method_body)
	|	'def' ID '(' method_def_params ')' NEWLINE method_body 'end'
		-> ^(METHOD_DEF ID method_def_params method_body)
	;

method_body
	:	script_stmt_block?
		-> ^(METHOD_BODY script_stmt_block?)
	;

method_def_params
	:	ID (',' ID)*
		-> ^(METHOD_DEF_PARAMS ID+)
	;
		
literal
	:	NUMBER 
		-> ^(LITNUMBER NUMBER)
		
	|	BOOLEAN
		-> ^(LITBOOLEAN BOOLEAN)
		
	|	STRING 
		-> ^(LITSTRING STRING)
		
	|	NULL
		-> LITNIL
	;


BOOLEAN
	:	'true' | 'false'
	;

NULL
	:	'nil'
	;
		
fragment DIGIT
	:	'0'..'9'
	;	
	
fragment LETTER
	:	'a'..'z'|'A'..'Z'
	;
	
fragment ALPHANUM
	:	DIGIT |	 LETTER
	;
	
ID  
	:	(LETTER | '_') (ALPHANUM | '_')*
	;
	
NUMBER
	:	DIGIT+
	;
	
STRING
	:	'"' .* '"' {setText(getText().substring(1, getText().length()-1));}
	|	'\'' .* '\'' {setText(getText().substring(1, getText().length()-1));}
	;	
		
WS  
	:	(' '|'\t')+ {skip();}
	;
			
NEWLINE
	:	('\r'? '\n' ' '*)+ 
	;

COMMENT
	: '#' .* {skip();} NEWLINE
	;
			