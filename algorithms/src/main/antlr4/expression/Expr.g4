grammar Expr;
prog:	expr NEWLINE ;
expr:	expr op=('*'|'/') expr # MulDiv
    |	expr op=('+'|'-') expr # AddSub
    |	INT					# int
    |	'(' expr ')'		# parens
    ;
NEWLINE : [\r\n]+ ;
INT     : [0-9]+ ;

/*创建常量方便引入*/
MUL: '*';
DIV: '/';
Add: '+';
SUB: '-';
