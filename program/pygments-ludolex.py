from pygments.lexer import RegexLexer
from pygments.token import *

__all__ = ['LudoLexer']

class LudoLexer(RegexLexer):
    name = 'Ludo'
    aliases = ['ludo']
    filenames = ['*.junta', '*.game', '*.garry']

    tokens = {
        'root': [
            (r'//.*\n', Comment),
            (r'[0-9]+', Number),
            (r'[A-Z]+[0-9]+', Generic.Subheading),
            (r'\b(in|let|define|type|abstract|this|super|extends|if|then|else)\b', Keyword.Reserved),
            (r'\b(is|not|and|or)\b', Operator.Word),
            (r'\b(friend|foe|empty)\b', Keyword.Reserved),
            (r'"([^"\\]|\\.)*"', String),
            (r'\b(n|s|e|w|ne|nw|se|sw)\b', Name.Entity),
            (r'[A-Z][a-zA-Z0-9]*', Name.Constant),
            (r'[a-z][a-zA-Z0-9]+', Name.Function),
            (r'\$[a-zA-z0-9]+\b', Name.Variable),
			(r'.', Text)
        ]
    }
