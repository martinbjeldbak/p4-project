from pygments.lexer import RegexLexer
from pygments.token import *

__all__ = ['LudoLexer']

class LudoLexer(RegexLexer):
    name = 'Ludo'
    aliases = ['ludo']
    filenames = ['*.ludo']

    tokens = {
        'root': [
            (r'//.*\n', Comment),
            (r'[0-9]+', Number),
            (r'[A-Z]+[0-9]+', Generic.Subheading),
            (r'\b(in|let|define|game|piece|this|width|height|title|players|turnOrder|board|grid|setup|wall|name|possibleDrops|possibleMoves|winCondition|tieCondition|if|then|else)\b', Keyword.Reserved),
            (r'\b(and|or)\b', Operator.Word),
            (r'\b(friend|foe|empty)\b', Keyword.Reserved),
            (r'"([^"\\]|\\.)*"', String),
            (r'\b(n|s|e|w|ne|nw|se|sw)\b', Name.Entity),
            (r'[A-Z][a-zA-Z]*', Name.Constant),
            (r'[a-z][a-zA-Z]+', Name.Function),
            (r'\$[a-zA-z]+\b', Name.Variable),
			(r'.', Text)
        ]
    }
