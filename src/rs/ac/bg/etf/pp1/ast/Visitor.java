// generated with ast extension for cup
// version 0.8
// 16/7/2020 23:21:36


package rs.ac.bg.etf.pp1.ast;

public interface Visitor { 

    public void visit(MethodDecl MethodDecl);
    public void visit(Mulop Mulop);
    public void visit(Relop Relop);
    public void visit(FParamDeclList FParamDeclList);
    public void visit(FParamDeclaration FParamDeclaration);
    public void visit(Var Var);
    public void visit(StatementList StatementList);
    public void visit(Addop Addop);
    public void visit(OptDesigStatement OptDesigStatement);
    public void visit(Factor Factor);
    public void visit(CondTerm CondTerm);
    public void visit(VarList VarList);
    public void visit(ConstList ConstList);
    public void visit(AddopLeft AddopLeft);
    public void visit(DeclList DeclList);
    public void visit(Designator Designator);
    public void visit(Term Term);
    public void visit(Condition Condition);
    public void visit(MethodList MethodList);
    public void visit(AddopRight AddopRight);
    public void visit(VarDeclList VarDeclList);
    public void visit(Expr Expr);
    public void visit(ActPars ActPars);
    public void visit(MethodTypeName MethodTypeName);
    public void visit(DesignatorStatement DesignatorStatement);
    public void visit(Const Const);
    public void visit(Statement Statement);
    public void visit(VarDecl VarDecl);
    public void visit(MulopLeft MulopLeft);
    public void visit(CondFact CondFact);
    public void visit(OptActualParams OptActualParams);
    public void visit(MulopRight MulopRight);
    public void visit(OptExpr OptExpr);
    public void visit(FormPars FormPars);
    public void visit(OptCond OptCond);
    public void visit(LessEqual LessEqual);
    public void visit(Less Less);
    public void visit(GreaterEqual GreaterEqual);
    public void visit(Greater Greater);
    public void visit(NotEqual NotEqual);
    public void visit(Equal Equal);
    public void visit(AssignMulopRight AssignMulopRight);
    public void visit(AssignopAddopRight AssignopAddopRight);
    public void visit(Assignop Assignop);
    public void visit(ModEqual ModEqual);
    public void visit(DivEqual DivEqual);
    public void visit(MulEqual MulEqual);
    public void visit(Mod Mod);
    public void visit(Div Div);
    public void visit(Mul Mul);
    public void visit(RightMulop RightMulop);
    public void visit(LeftMulop LeftMulop);
    public void visit(MinusEqual MinusEqual);
    public void visit(PlusEqual PlusEqual);
    public void visit(Minus Minus);
    public void visit(Plus Plus);
    public void visit(RightAddop RightAddop);
    public void visit(LeftAddop LeftAddop);
    public void visit(Or Or);
    public void visit(ArrDesig ArrDesig);
    public void visit(SimpleDesignator SimpleDesignator);
    public void visit(ClassDesignator ClassDesignator);
    public void visit(ArrayDesignator ArrayDesignator);
    public void visit(FactorParenths FactorParenths);
    public void visit(FactorBoolconst FactorBoolconst);
    public void visit(FactorCharconst FactorCharconst);
    public void visit(FactorNumconst FactorNumconst);
    public void visit(FactorNewType FactorNewType);
    public void visit(FactorNewTypeExpr FactorNewTypeExpr);
    public void visit(FactorDesignatorNoActs FactorDesignatorNoActs);
    public void visit(FactorDesignatorHasActs FactorDesignatorHasActs);
    public void visit(FactorTermSingle FactorTermSingle);
    public void visit(TermMulopFactor TermMulopFactor);
    public void visit(TermMinus TermMinus);
    public void visit(TermPlus TermPlus);
    public void visit(TermsList TermsList);
    public void visit(NoExpression NoExpression);
    public void visit(HasExpression HasExpression);
    public void visit(ConditionFactExpr ConditionFactExpr);
    public void visit(ConditionFactRelop ConditionFactRelop);
    public void visit(And And);
    public void visit(SingleConditionTerm SingleConditionTerm);
    public void visit(ConditionTermList ConditionTermList);
    public void visit(SingleConditionFact SingleConditionFact);
    public void visit(ConditionFactList ConditionFactList);
    public void visit(NoCondition NoCondition);
    public void visit(HasCondition HasCondition);
    public void visit(Else Else);
    public void visit(IfCond IfCond);
    public void visit(ActualParam ActualParam);
    public void visit(SingleActParam SingleActParam);
    public void visit(ActParamsList ActParamsList);
    public void visit(NoActualParams NoActualParams);
    public void visit(HasActualParams HasActualParams);
    public void visit(DesigOptActLeftParen DesigOptActLeftParen);
    public void visit(OptionalActualsDesigStatement OptionalActualsDesigStatement);
    public void visit(DecDesigStatement DecDesigStatement);
    public void visit(IncDesigStatement IncDesigStatement);
    public void visit(AssignopDesigStatementError AssignopDesigStatementError);
    public void visit(AssignopDesigStatement AssignopDesigStatement);
    public void visit(NoDesignatorStatement NoDesignatorStatement);
    public void visit(HasDesigStatement HasDesigStatement);
    public void visit(ForParenEnd ForParenEnd);
    public void visit(IfCondBegin IfCondBegin);
    public void visit(FormalParamSingle FormalParamSingle);
    public void visit(FormalParamArray FormalParamArray);
    public void visit(NoFormalParameters NoFormalParameters);
    public void visit(FormalParameters FormalParameters);
    public void visit(ForFirstSemi ForFirstSemi);
    public void visit(MultyStatement MultyStatement);
    public void visit(ReadStatement ReadStatement);
    public void visit(PrintStatement PrintStatement);
    public void visit(PrintConstStatement PrintConstStatement);
    public void visit(ContinueStatement ContinueStatement);
    public void visit(BreakStatement BreakStatement);
    public void visit(ReturnStatement ReturnStatement);
    public void visit(IfElseStatement IfElseStatement);
    public void visit(IfStatement IfStatement);
    public void visit(ForStatementError ForStatementError);
    public void visit(ForStatement ForStatement);
    public void visit(StatementDesignator StatementDesignator);
    public void visit(MethodRetVoidName MethodRetVoidName);
    public void visit(MethodRetTypeName MethodRetTypeName);
    public void visit(MethodDeclarationError MethodDeclarationError);
    public void visit(MethodDeclaration MethodDeclaration);
    public void visit(Type Type);
    public void visit(SingleVar SingleVar);
    public void visit(VarArray VarArray);
    public void visit(VarDeclarationError VarDeclarationError);
    public void visit(VarDeclaration VarDeclaration);
    public void visit(VariableListError VariableListError);
    public void visit(VariableListSingleVar VariableListSingleVar);
    public void visit(VariableList VariableList);
    public void visit(ConstDecl ConstDecl);
    public void visit(FParamDeclListError FParamDeclListError);
    public void visit(FParamDeclSingle FParamDeclSingle);
    public void visit(FParamDeclarations FParamDeclarations);
    public void visit(NoMethodDecl NoMethodDecl);
    public void visit(MethodDeclarations MethodDeclarations);
    public void visit(NoStmt NoStmt);
    public void visit(Statements Statements);
    public void visit(NoVarDecl NoVarDecl);
    public void visit(VarDeclarations VarDeclarations);
    public void visit(Constant Constant);
    public void visit(Constants Constants);
    public void visit(NoDeclList NoDeclList);
    public void visit(VarsDeclList VarsDeclList);
    public void visit(ConstDeclList ConstDeclList);
    public void visit(ConstBool ConstBool);
    public void visit(ConstChar ConstChar);
    public void visit(ConstNum ConstNum);
    public void visit(ProgName ProgName);
    public void visit(Program Program);

}
