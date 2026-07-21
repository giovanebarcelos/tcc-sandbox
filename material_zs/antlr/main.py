from antlr4 import *
from ExprLexer import ExprLexer
from ExprParser import ExprParser

def evaluate(expression):
    input_stream = InputStream(expression)
    lexer = ExprLexer(input_stream)
    token_stream = CommonTokenStream(lexer)
    parser = ExprParser(token_stream)

    return evaluate_expr(parser.expr())

def evaluate_expr(ctx):
    left = evaluate_term(ctx.term(0))
    for i in range(1, len(ctx.term())):
        op = ctx.getChild(2 * i - 1).getText()
        right = evaluate_term(ctx.term(i))
        if op == '+':
            left += right
        elif op == '-':
            left -= right
    return left

def evaluate_term(ctx):
    left = evaluate_factor(ctx.factor(0))
    for i in range(1, len(ctx.factor())):
        op = ctx.getChild(2 * i - 1).getText()
        right = evaluate_factor(ctx.factor(i))
        if op == '*':
            left *= right
        elif op == '/':
            left /= right
    return left

def evaluate_factor(ctx):
    if ctx.NUMBER():
        return int(ctx.NUMBER().getText())
    elif ctx.expr():
        return evaluate_expr(ctx.expr())

def main():
    expression = "2 + 3 * (4 - 1)"
    result = evaluate(expression)
    print(f"Evaluating '{expression}' gives result: {result}")

if __name__ == '__main__':
    main()
