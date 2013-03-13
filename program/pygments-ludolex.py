from pygments.lexer import RegexLexer
from pygments.token import *

__all__ = ['LudoLexer']

class LudoLexer(RegexLexer):
    name = 'Ludo'
    aliases = ['ludo']
    filenames = ['*.ludo']

    tokens = {
        'root': [
            (r'[0-9]+', Number),
            (r'[A-Z]+[0-9]+', Generic.Subheading),
            (r'(define|game|piece|this|width|height|title|players|turnOrder|board|grid|setup|wall|name|possibleDrops|possibleMoves|winCondition|tieCondition|if|then|else)', Keyword.Reserved),
            (r'(and|or)', Operator.Word),
            (r'(frind|foe|empty)', Keyword.Reserved),
            (r'"([^"\\]|\\.)*"', String),
            (r'[A-Z][a-zA-Z]*', Name.Constant),
            (r'[a-z][a-zA-Z]+', Name.Function),
            (r'(n|s|e|w|ne|nw|se|sw)', Name.Entity),
            (r'\$[a-zA-z]+', Name.Variable),
			(r'.', Text)
        ]
    }
