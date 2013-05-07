" Vim syntax file
" Language:	      	Junta
" Last Change:		2013 March 04
" Original Author:	Niels Poulsen <a@apakoh.dk>
"
" Smid følgende i .vimrc for at bruge:
" au BufRead,BufNewFile *.junta set filetype=junta

syn match ConId "\(\<[A-Z][a-zA-Z0-9_']*\.\)\=\<[A-Z][a-zA-Z0-9_']*\>"
syn match VarId "\(\<[A-Z][a-zA-Z0-9_']*\.\)\=\<[a-z][a-zA-Z0-9_']*\>"
syn match Type "[A-Z][a-zA-Z_]*"
syn match Identifier "[a-z_][a-zA-Z_]*"
syn match Special "$[a-zA-Z_][a-zA-Z_]*"
syn match Constant "[A-Z][A-Z]*[0-9][0-9]*"
syn match Constant "\<\(n\|s\|w\|e\|ne\|nw\|se\|sw\)\>"
syn match Operator "[!@#%\^&*+\-=.,~><?:;|/\\]\+"
syn match Conditional "\<\(if\|then\|else\|branch\)\>"
syn match Statement "\<\(not\|is\|or\|and\|let\|in\|define\|abstract\|type\|extends\)\>"
syn match Keyword "\<\(this\|super\|friend\|any\|foe\|empty\)\>"
syn match Number "\<[0-9]\+\>"
syn match Number "\<0\(x\|X\)[0-9a-fA-F]\+\>"
syn match Number "\<0\(b\|B\)[0-1]\+\>"
syn match Float "\<[0-9]\+.[0-9]\+\(\(e\|E\)\(+\|-\)\?[0-9]\+\)\?\>"
syn match Float "\<[0-9]\+\(e\|E\)\(+\|-\)\?[0-9]\+\>"
syn match Boolean "\<\(true\|false\)\>"
syn match Constant "\<null\>"
syn match SpecialChar "\\\(n\|t\|r\)"
syn region String start=+"+ skip=+\\\\\|\\"+ end=+"+ contains=SpecialChar
"syn match String "\"\([^\"]\|\"|\\\)*\""

syn match Special "\<\(=>\|->\)\>"

syn region Comment start="//" end="\n"

let b:current_syntax = "junta"
