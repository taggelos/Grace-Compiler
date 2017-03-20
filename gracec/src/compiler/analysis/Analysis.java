/* This file was generated by SableCC (http://www.sablecc.org/). */

package compiler.analysis;

import compiler.node.*;

public interface Analysis extends Switch
{
    Object getIn(Node node);
    void setIn(Node node, Object o);
    Object getOut(Node node);
    void setOut(Node node, Object o);

    void caseStart(Start node);
    void caseAFactorExpr(AFactorExpr node);
    void caseAPlusExpr(APlusExpr node);
    void caseAMinusExpr(AMinusExpr node);
    void caseATermFactor(ATermFactor node);
    void caseAMultFactor(AMultFactor node);
    void caseADivFactor(ADivFactor node);
    void caseAIntegersTerm(AIntegersTerm node);
    void caseAExprTerm(AExprTerm node);

    void caseTWhiteSpace(TWhiteSpace node);
    void caseTEscapeSeq(TEscapeSeq node);
    void caseTAnd(TAnd node);
    void caseTChar(TChar node);
    void caseTDiv(TDiv node);
    void caseTDo(TDo node);
    void caseTElse(TElse node);
    void caseTFun(TFun node);
    void caseTIf(TIf node);
    void caseTInt(TInt node);
    void caseTMod(TMod node);
    void caseTNot(TNot node);
    void caseTNothing(TNothing node);
    void caseTOr(TOr node);
    void caseTRef(TRef node);
    void caseTReturn(TReturn node);
    void caseTThen(TThen node);
    void caseTVar(TVar node);
    void caseTWhile(TWhile node);
    void caseTIntegers(TIntegers node);
    void caseTStringLiteral(TStringLiteral node);
    void caseTMinus(TMinus node);
    void caseTPlus(TPlus node);
    void caseTStar(TStar node);
    void caseTSlash(TSlash node);
    void caseTHtag(THtag node);
    void caseTAssign(TAssign node);
    void caseTNeq(TNeq node);
    void caseTLt(TLt node);
    void caseTGt(TGt node);
    void caseTLteq(TLteq node);
    void caseTGteq(TGteq node);
    void caseTLPar(TLPar node);
    void caseTRPar(TRPar node);
    void caseTLBkt(TLBkt node);
    void caseTRBkt(TRBkt node);
    void caseTLBrc(TLBrc node);
    void caseTRBrc(TRBrc node);
    void caseTComma(TComma node);
    void caseTSemi(TSemi node);
    void caseTColon(TColon node);
    void caseTArrow(TArrow node);
    void caseTComment(TComment node);
    void caseEOF(EOF node);
    void caseInvalidToken(InvalidToken node);
}