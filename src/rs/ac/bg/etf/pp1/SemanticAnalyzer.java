package rs.ac.bg.etf.pp1;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

public class SemanticAnalyzer extends VisitorAdaptor {

	boolean errorDetected = false;
	int printCallCount = 0;
	Obj currentMethod = null;
	boolean returnFound = false;
	int nVars;
	int forLevel = 0;
	Stack<ArrayList<Struct>> parameters = new Stack<>();
	Struct recentTypeVisited;

	Logger log = Logger.getLogger(getClass());

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message); 
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.info(msg.toString());
	}
	
	public void visit(Program program) {		
		nVars = Tab.currentScope.getnVars();
		Tab.chainLocalSymbols(program.getProgName().obj);
		Tab.closeScope();
	}

	public void visit(ProgName progName) {
		progName.obj = Tab.insert(Obj.Prog, progName.getPName(), Tab.noType);
		Tab.openScope();
	}
	
	public void visit(ConstNum constNum) {
		Tab.insert(Obj.Var, constNum.getName(), recentTypeVisited);
	}

	public void visit(ConstChar constChar) {
		Tab.insert(Obj.Var, constChar.getName(), recentTypeVisited);
	}
	
	public void visit(ConstBool constBool) {
		Tab.insert(Obj.Var, constBool.getName(), recentTypeVisited);
	}
	
	public void visit(VarArray varArray) {
		Tab.insert(Obj.Var, varArray.getName(), new Struct(Struct.Array ,recentTypeVisited));
	}
	
	public void visit(SingleVar singleVar) {
		Tab.insert(Obj.Var, singleVar.getName(), recentTypeVisited);
	}
	
	public void visit(VarDeclError varDeclError) {
		report_info("Oporavak od greske kod deklarisanja promenljive (ispred ;)", varDeclError);
	}
	
	public void visit(SameTypeVarsError sameTypeVarsError) {
		report_info("Oporavak od greske kod deklarisanja promenljive (ispred ,)", sameTypeVarsError);
	}
	
	public void visit(Type type) {
		Obj typeNode = Tab.find(type.getTypeName());
		if (typeNode == Tab.noObj) {
			report_error("Nije pronadjen tip " + type.getTypeName() + " u tabeli simbola", null);
			type.struct = Tab.noType;
		} 
		else {
			if (Obj.Type == typeNode.getKind()) {
				type.struct = typeNode.getType();
			} 
			else {
				report_error("Greska: Ime " + type.getTypeName() + " ne predstavlja tip ", type);
				type.struct = Tab.noType;
			}
		}
		recentTypeVisited = type.struct;
	}
	
	public void visit(MethodDeclRegular methodDecl) {
		if (!returnFound && currentMethod.getType() != Tab.noType) {
			report_error("Semanticka greska na liniji " + methodDecl.getLine() + ": funcija " + currentMethod.getName() + " nema return iskaz!", null);
		}
		
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();
		
		returnFound = false;
		currentMethod = null;
	}

	public void visit(MethodRetTypeName methodRetTypeName) {
		currentMethod = Tab.insert(Obj.Meth, methodRetTypeName.getMethName(), methodRetTypeName.getType().struct);
		methodRetTypeName.obj = currentMethod;
		Tab.openScope();
		report_info("Obradjuje se funkcija " + methodRetTypeName.getMethName(), methodRetTypeName);
	}
	
	public void visit(MethodRetVoidName methodRetVoidName) {
		currentMethod = Tab.insert(Obj.Meth, methodRetVoidName.getMethName(), Tab.noType);
		methodRetVoidName.obj = currentMethod;
		Tab.openScope();
		report_info("Obradjuje se funkcija " + methodRetVoidName.getMethName(), methodRetVoidName);
	}
	
	public void visit(MethodDeclError methodDeclError) {
		report_info("Oporavak od greske kod deklarisanja formalnih parametara metode (ispred ')')", methodDeclError);
	}
	
	public void visit(FormalParamDeclsError formalParamDeclsError) {
		report_info("Oporavak od greske kod deklarisanja formalnih parametara metode (ispred ,)", formalParamDeclsError);
	}
	
	public void visit(FormalParamDeclSingle formalParamDeclSingle) {
		Tab.insert(Obj.Var, formalParamDeclSingle.getName(), recentTypeVisited);
	}
	
	public void visit(FormalParamDeclArray formalParamDeclArray) {
		Tab.insert(Obj.Var, formalParamDeclArray.getName(), new Struct(Struct.Array, recentTypeVisited));
	}
	
	public void visit(StatementForError statementForError) {
		report_info("Oporavak od greske u uslovu za for petlju (ispred ;)", statementForError);
	}
	
	public void visit(ForRightParen forRightParen) {
		forLevel++;
	}

	public void visit(StatementFor statementFor) {
		//report_info("Detektovano koriscenje for petlje", statementFor);
		forLevel--;
	}
	
	public void visit(StatementBreak statementBreak) {
		if (forLevel < 1) {
			report_error("Greska na liniji " + statementBreak.getLine() + " : " + " break van for petlje", null);
		}
	}

	public void visit(StatementContinue statementContinue) {
		if (forLevel < 1) {
			report_error("Greska na liniji " + statementContinue.getLine() + " : " + " continue van for petlje", null);
		}
	}
	
	public void visit(StatementReturn statementReturn) {
		OptionalExpression optexpr = statementReturn.getOptionalExpression();
		Struct methodType = currentMethod.getType();
		if(optexpr instanceof WithExpression) {
			returnFound = true;
			WithExpression expr = (WithExpression) optexpr;
			if (!methodType.compatibleWith(expr.getExpr().struct)) {
				report_error("Greska na liniji " + expr.getLine() + " : "
						+ "tip izraza u return naredbi ne slaze se sa tipom povratne vrednosti funkcije "
						+ currentMethod.getName(), statementReturn);
			}
		}
		else {
			if (Tab.noType != methodType) {
				report_error("Funkcija " + currentMethod.getName() + " mora biti void ", statementReturn);
			}
		}
	}
	
	public void visit(StatementRead statementRead) {
		int designatorKind = statementRead.getDesignator().obj.getKind();
		if(!kindCheck(designatorKind)) {
			report_error("Greska na liniji " + statementRead.getLine() + " : "
					+ " neodgovarajuci tip u read metodu ", null);
		}
		
		
		if (!isAssignable(statementRead.getDesignator().obj.getType())) {
			report_error("Greska na liniji " + statementRead.getLine() + " : "
					+ " nekompatibilni tipovi u read metodu ", null);
		}
	}

	private boolean isAssignable(Struct struct) {
		return Tab.intType.assignableTo(struct) || Tab.charType.assignableTo(struct) || (new Struct(Struct.Bool)).assignableTo(struct);
	}
	
	private boolean kindCheck(int kind) {
		return kind == Obj.Elem || kind == Obj.Var;
	}
	
	public void visit(StatementPrintNoConst statementPrintNoConst) {
		Struct expr = statementPrintNoConst.getExpr().struct;
		if (!isAssignable(expr)) {
			report_error("Neodgovarajuci tip izraza u metodi print", statementPrintNoConst.getExpr());
		}
	}
	
	public void visit(DesignatorStatementAssignop assignment) {
		int kind = assignment.getDesignator().obj.getKind();
		if (kindCheck(kind)) {
			Struct struct = assignment.getDesignator().obj.getType();
			if (!assignment.getExpr().struct.assignableTo(struct))
				report_error("Greska na liniji " + assignment.getLine() + " : "
						+ " nekompatibilni tipovi u dodeli vrednosti ", null);
		}
	}
	
	public void visit(DesignatorStatementAssignopError error) {
		report_info("Oporavak od greske kod dodele vrednosti (ispred ;)", error);
	}
	
	
	private void checkArgumentTypes(Obj currentMethod) {	
		Iterator<Obj> it = currentMethod.getLocalSymbols().iterator();
		ArrayList<Struct> actParams = parameters.pop();
		ArrayList<Struct> methodLocals = new ArrayList<>();
		
		while(it.hasNext()) methodLocals.add(it.next().getType());
		
		if (methodLocals.size() != actParams.size()) {
			report_error("Broj argumenata se ne poklapa sa brojem parametara funkcije " + currentMethod.getName(), null);
		}
		
		for(int i = 0; i < actParams.size(); i++) {
			if(!actParams.get(i).assignableTo(methodLocals.get(i))) {
				report_error("Nepodudaranje tipa " + i + " funkcije " + currentMethod.getName(), null);
			}
		}
	}
	
	public void visit(DesignatorStatementOptAct functionExpr) {
		if (Obj.Meth != functionExpr.getDesignator().obj.getKind()) {
			report_error(currentMethod.getName() + " nije funkcija ", functionExpr);
		} else {
			checkArgumentTypes(functionExpr.getDesignator().obj);
		}
	}
	
	public void visit(DesignatorStatementINC increment) {
		Obj obj = increment.getDesignator().obj;
		if (!kindCheck(obj.getKind())) {
			report_error("Neodgovarajuci tip designatora na liniji " + increment.getLine(), null);
		}
		if (!Tab.intType.assignableTo(obj.getType())) {
			report_error("Inkrementiranje dozvoljeno samo za tip int, linija " + increment.getLine(), null);
		}
	}
	
	public void visit(ActParsStartLeftParen actParsStartLeftParen) {
		parameters.push(new ArrayList<>());
	}
	
	public void visit(DesignatorStatementDEC decrement) {
		Obj obj = decrement.getDesignator().obj;
		if (!kindCheck(obj.getKind())) {
			report_error("Neodgovarajuci tip designatora na liniji " + decrement.getLine(), null);
		}
		if (!Tab.intType.assignableTo(obj.getType())) {
			report_error("Inkrementiranje dozvoljeno samo za tip int, linija " + decrement.getLine(), null);
		}
	}
	
	public void visit(ActPar actPar) {
		parameters.peek().add(actPar.getExpr().struct);
	}
	
	public void visit(RelopCondFact relopCondFact) {
		Struct left = relopCondFact.getExpr().struct;
		Struct right = relopCondFact.getExpr1().struct;
		
		if (!left.compatibleWith(right)) {
			report_error("Nekompatibilni tipovi za poredjenje ", null);
		}

		if (!(relopCondFact.getRelop() instanceof RelopNotEqual || relopCondFact.getRelop() instanceof RelopEqual) && left.getKind() == Struct.Array) {
			report_error("Za nizove dozvoljeno samo != i == ", null);
		}
	}
	
	public void visit(ExprCondFact exprCondFact) {
		if (!(new Struct(Struct.Bool)).assignableTo(exprCondFact.getExpr().struct)) {
			report_error("Uslovni izraz mora biti boolean",	null);
		}
	}
	
	public void visit(MinusTerm expr) {
		Struct ret;
		if (Tab.intType != expr.getTerm().struct) {
			ret = Tab.noType;
			report_error("Nekompatibilni tipovi na liniji " + expr.getLine(), null);
		}
		else {
			ret = expr.getTerm().struct;
		}
		expr.struct = ret;
	}
	
	public void visit(MultyTerms expr) {
		Struct expression = expr.getExpr().struct;
		Struct ret;
		if (expression == Tab.intType && expression.equals(expr.getTerm().struct))
			ret = expression;
		else {
			ret = Tab.noType;
			report_error("Nekompatibilni tipovi na liniji " + expr.getLine(), null);
		}
		expr.struct = ret;
	}
	
	public void visit(MulopFactorTerm mul) {
		Struct factor = mul.getFactor().struct;
		Struct ret;
		if (factor == Tab.intType && factor.equals(mul.getTerm().struct))
			ret = factor;
		else {
			ret = Tab.noType;
			report_error("Nekompatibilni tipovi na liniji " + mul.getLine(), null);
		}
		mul.struct = ret;
	}
	
	public void visit(PlusTerm addExpr) {
		addExpr.struct = addExpr.getTerm().struct;
	}
	
	public void visit(SingleFactorTerm mul) {
		mul.struct = mul.getFactor().struct;
	}
	
	public void visit(DesignatorWithoutActFactor designator) {
		designator.struct = designator.getDesignator().obj.getType();
	}
	
	public void visit(DesignatorWithActFactor functionExpr) {
		if (Obj.Meth != functionExpr.getDesignator().obj.getKind()) {
			report_error(currentMethod.getName() + " nije funkcija ", functionExpr);
		} else {
			checkArgumentTypes(functionExpr.getDesignator().obj);
			functionExpr.struct = functionExpr.getDesignator().obj.getType();
		}
	}
	
	public void visit(NumconstFactor numConst) {
		numConst.struct = Tab.intType;
	}

	public void visit(CharconstFactor charConst) {
		charConst.struct = Tab.charType;
	}

	public void visit(BoolconstFactor boolConst) {
		boolConst.struct = new Struct(Struct.Bool);
	}
	
	public void visit(NewTypeExprFactor typeExpr) {
		typeExpr.struct = new Struct(Struct.Array, typeExpr.getType().struct);
		if (!typeExpr.getExpr().struct.assignableTo(Tab.intType)) {
			report_error("Velicina niza nije mora biti tipa int ", null);
		}
	}

	public void visit(NewTypeFactor typeExpr) {
		typeExpr.struct = typeExpr.getType().struct;
	}

	public void visit(ParenthesesFactor typeExpr) {
		typeExpr.struct = typeExpr.getExpr().struct;
	}
	
	public void visit(SimpleDesignator designator) {
		Obj obj = Tab.find(designator.getName());
		if (obj == Tab.noObj) {
			report_error("Ime " + designator.getName() + " nije deklarisano! ", null);
		} else {
		}
		designator.obj = obj;
	}

	public void visit(DesignatorArray designatorArray) {
		Struct t = designatorArray.getOnlyArrayDesignator().getDesignator().obj.getType();
		
		if (t.getKind() != Struct.Array) {
			report_error("Designator nije tipa niza ", null);
		} else {
//			DumpSymbolTableVisitor printer = new DumpSymbolTableVisitor();
//			printer.visitObjNode(designatorArray.getOnlyArrayDesignator().getDesignator().obj);
//			report_info("Detektovan pristup elementu niza : " + printer.getOutput(), designatorArray);
		}
		
		designatorArray.obj = new Obj(Obj.Elem, "Elem", t.getElemType());
		
		if (!designatorArray.getExpr().struct.assignableTo(Tab.intType)) {
			report_error("Indeks niza mora biti tipa int ", null);
		}
	}
	
//	public void visit(DesignatorClass designatorClass) {
//		int kind = designatorClass.getDesignator().obj.getKind();
//		if (kindCheck(kind)) {
//			designatorClass.obj = designatorClass.getDesignator().obj.getType().getMembersTable()
//					.searchKey(designatorClass.getName());
//			if (designatorClass.obj == null) {
//				report_error("Greska na liniji " + designatorClass.getLine()
//						+ " : nije ubaceno nista u designator class " + kind, null);
//				designatorClass.obj = Tab.noObj;
//			}
//		}
//	}
	
	public boolean passed() {
		return !errorDetected;
	}
	
}

