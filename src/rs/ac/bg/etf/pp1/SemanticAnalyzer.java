package rs.ac.bg.etf.pp1;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;
import rs.etf.pp1.symboltable.visitors.DumpSymbolTableVisitor;

public class SemanticAnalyzer extends VisitorAdaptor {

	boolean errorDetected = false;
	int printCallCount = 0;
	int formParams = 0;
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
	
	public SemanticAnalyzer() {
		Tab.currentScope.addToLocals(new Obj(Obj.Type, "bool", new Struct(Struct.Bool)));
	}
	
	public void visit(Program program) {
		Obj main = Tab.currentScope.findSymbol("main");
		
		if (main == null) {
			report_error("Funkcija main() ne postoji u programu ", program);
		} else {
			if (main.getKind() == Obj.Meth) {
				if (main.getLevel() > 0) {
					report_error("main() ne sme sadrzati parametre", program);
				}
				if (main.getType() != Tab.noType) {
					report_error("main() mora biti void ", program);
				}
			} else {
				report_error("main nije metoda ", program);
			}
		}
		
		nVars = Tab.currentScope.getnVars();
		Tab.chainLocalSymbols(program.getProgName().obj);
		Tab.closeScope();
	}

	public void visit(ProgName progName) {
		Obj symbol = Tab.currentScope.findSymbol(progName.getPName());
		if (symbol != null) {
			report_error("Ovo ime vec postoji " + progName.getPName(), progName);
			progName.obj = new Obj(Obj.Prog, progName.getPName(), Tab.noType);
		} else {
			progName.obj = Tab.insert(Obj.Prog, progName.getPName(), Tab.noType);
		}
		Tab.openScope();
	}
	
	public void visit(ConstNum constNum) {
		Obj symbol = Tab.currentScope.findSymbol(constNum.getName());
		
		if(symbol != null)
			report_error(constNum.getName() + " - vec postoji", constNum);
		else
			Tab.insert(Obj.Con, constNum.getName(), recentTypeVisited).setAdr(constNum.getValue());
	}

	public void visit(ConstChar constChar) {
		Obj symbol = Tab.currentScope.findSymbol(constChar.getName());
		
		if(symbol != null)
			report_error(constChar.getName() + " - vec postoji", constChar);
		else
			Tab.insert(Obj.Con, constChar.getName(), recentTypeVisited).setAdr(constChar.getValue());
	}
	
	public void visit(ConstBool constBool) {
		Obj symbol = Tab.currentScope.findSymbol(constBool.getName());
		if(symbol != null)
			report_error(constBool.getName() + " - vec postoji", constBool);
		else
			Tab.insert(Obj.Con, constBool.getName(), recentTypeVisited).setAdr(constBool.getBoolConst().obj.getAdr());
	}
	
	public void visit(False falseConst) {
		Obj falseValue = new Obj(Obj.Con, "", new Struct(Struct.Bool));
		falseValue.setAdr(0);
		falseConst.obj = falseValue;
	}
	
	public void visit(True trueConst) {
		Obj trueValue = new Obj(Obj.Con, "", new Struct(Struct.Bool));
		trueValue.setAdr(1);
		trueConst.obj = trueValue;
	}
	
	public void visit(VarArray varArray) {
		Obj symbol = Tab.currentScope.findSymbol(varArray.getName());
		
		if(symbol != null)
			report_error(varArray.getName() + " - vec postoji", varArray);
		else
			Tab.insert(Obj.Var, varArray.getName(), new Struct(Struct.Array ,recentTypeVisited));
	}
	
	public void visit(SingleVar singleVar) {
		Obj symbol = Tab.currentScope.findSymbol(singleVar.getName());
		
		if(symbol != null)
			report_error(singleVar.getName() + " - vec postoji", singleVar);
		else
			Tab.insert(Obj.Var, singleVar.getName(), recentTypeVisited);
	}
	
	public void visit(VarDeclarationError varDeclError) {
		report_info("Oporavak od greske kod deklarisanja promenljive (ispred ;)", varDeclError);
	}
	
	public void visit(VariableListError sameTypeVarsError) {
		report_info("Oporavak od greske kod deklarisanja promenljive (ispred ,)", sameTypeVarsError);
	}
	
	public void visit(Type type) {
		Obj typeNode = Tab.find(type.getTypeName());
		if (typeNode != Tab.noObj) {
			if (Obj.Type != typeNode.getKind()) {
				type.struct = Tab.noType;
				report_error("Greska: Ime " + type.getTypeName() + " ne predstavlja tip ", type);
			} 
			else {
				type.struct = typeNode.getType();
			}
		} 
		else {
			type.struct = Tab.noType;
			report_error("Tip " + type.getTypeName() + " ne postoji u tabeli simbola", null);
		}
		recentTypeVisited = type.struct;
	}
	
	public void visit(MethodDeclaration methodDecl) {
		if (!returnFound && currentMethod.getType() != Tab.noType) {
			report_error("Semanticka greska na liniji " + methodDecl.getLine() + ": funcija " + currentMethod.getName() + " nema return iskaz!", null);
		}
		
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();
		currentMethod.setLevel(formParams);

		formParams = 0;
		
		returnFound = false;
		currentMethod = null;
	}

	public void visit(MethodRetTypeName methodRetTypeName) {
		Obj symbol = Tab.currentScope.findSymbol(methodRetTypeName.getMethName());
		
		if(symbol != null) {
			report_error(methodRetTypeName.getMethName() + " - vec postoji", methodRetTypeName);
			currentMethod = new Obj(Obj.Meth, methodRetTypeName.getMethName(), methodRetTypeName.getType().struct);
		}
		else {
			currentMethod = Tab.insert(Obj.Meth, methodRetTypeName.getMethName(), methodRetTypeName.getType().struct);
			methodRetTypeName.obj = currentMethod;
		}
		
		Tab.openScope();
	}
	
	public void visit(MethodRetVoidName methodRetVoidName) {
		Obj symbol = Tab.currentScope.findSymbol(methodRetVoidName.getMethName());
		
		if(symbol != null) {
			report_error(methodRetVoidName.getMethName() + " - vec postoji", methodRetVoidName);
			currentMethod = new Obj(Obj.Meth, methodRetVoidName.getMethName(), Tab.noType);
		}
		else {
			currentMethod = Tab.insert(Obj.Meth, methodRetVoidName.getMethName(), Tab.noType);
			methodRetVoidName.obj = currentMethod;
		}
		
		Tab.openScope();
	}
	
	public void visit(MethodDeclarationError methodDeclError) {
		report_info("Oporavak od greske kod deklarisanja formalnih parametara metode (ispred ')')", methodDeclError);
	}
	
	public void visit(FParamDeclListError formalParamDeclsError) {
		report_info("Oporavak od greske kod deklarisanja formalnih parametara metode (ispred ,)", formalParamDeclsError);
	}
	
	public void visit(FormalParamSingle formalParamDeclSingle) {
		Obj symbol = Tab.currentScope.findSymbol(formalParamDeclSingle.getName());
		
		if(symbol != null)
			report_error(formalParamDeclSingle.getName() + " - vec postoji", formalParamDeclSingle);
		else
			Tab.insert(Obj.Var, formalParamDeclSingle.getName(), recentTypeVisited).setFpPos(++formParams);
	}
	
	public void visit(FormalParamArray formalParamDeclArray) {
		Obj symbol = Tab.currentScope.findSymbol(formalParamDeclArray.getName());
		
		if(symbol != null)
			report_error(formalParamDeclArray.getName() + " - vec postoji", formalParamDeclArray);
		else
			Tab.insert(Obj.Var, formalParamDeclArray.getName(), new Struct(Struct.Array, recentTypeVisited)).setFpPos(++formParams);
	}
	
	public void visit(ForStatementError statementForError) {
		report_info("Oporavak od greske u uslovu za for petlju (ispred ;)", statementForError);
	}
	
	public void visit(ForParenEnd forRightParen) {
		forLevel++;
	}

	public void visit(ForStatement statementFor) {
		forLevel--;
	}
	
	public void visit(BreakStatement statementBreak) {
		if (forLevel < 1) {
			report_error("Greska na liniji " + statementBreak.getLine() + " : " + " break van for petlje", null);
		}
	}

	public void visit(ContinueStatement statementContinue) {
		if (forLevel < 1) {
			report_error("Greska na liniji " + statementContinue.getLine() + " : " + " continue van for petlje", null);
		}
	}
	
	public void visit(ReturnStatement statementReturn) {
		OptExpr optexpr = statementReturn.getOptExpr();
		Struct methodType = currentMethod.getType();
		if(optexpr instanceof HasExpression) {
			returnFound = true;
			HasExpression expr = (HasExpression) optexpr;
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
	
	public void visit(ReadStatement statementRead) {
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
	
	public void visit(PrintStatement statementPrintNoConst) {
		Struct expr = statementPrintNoConst.getExpr().struct;
		if (!isAssignable(expr)) {
			report_error("Neodgovarajuci tip izraza u metodi print", statementPrintNoConst.getExpr());
		}
	}
	
	public void visit(AssignopDesigStatement assignment) {
		int kind = assignment.getDesignator().obj.getKind();
		if (kindCheck(kind)) {
			Struct struct = assignment.getDesignator().obj.getType();
			if (!assignment.getExpr().struct.assignableTo(struct))
				report_error("Greska na liniji " + assignment.getLine() + " : "
						+ " nekompatibilni tipovi u dodeli vrednosti ", null);
		}
	}
	
	public void visit(AssignopDesigStatementError error) {
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
	
	public void visit(OptionalActualsDesigStatement functionExpr) {
		if (Obj.Meth != functionExpr.getDesignator().obj.getKind()) {
			report_error(currentMethod.getName() + " nije funkcija ", functionExpr);
		} else {
			checkArgumentTypes(functionExpr.getDesignator().obj);
		}
	}
	
	public void visit(IncDesigStatement increment) {
		Obj obj = increment.getDesignator().obj;
		if (!kindCheck(obj.getKind())) {
			report_error("Neodgovarajuci tip designatora na liniji " + increment.getLine(), null);
		}
		if (!Tab.intType.assignableTo(obj.getType())) {
			report_error("Inkrementiranje dozvoljeno samo za tip int, linija " + increment.getLine(), null);
		}
	}
	
	public void visit(DesigOptActLeftParen actParsStartLeftParen) {
		parameters.push(new ArrayList<>());
	}
	
	public void visit(DecDesigStatement decrement) {
		Obj obj = decrement.getDesignator().obj;
		if (!kindCheck(obj.getKind())) {
			report_error("Neodgovarajuci tip designatora na liniji " + decrement.getLine(), null);
		}
		if (!Tab.intType.assignableTo(obj.getType())) {
			report_error("Inkrementiranje dozvoljeno samo za tip int, linija " + decrement.getLine(), null);
		}
	}
	
	public void visit(ActualParam actPar) {
		parameters.peek().add(actPar.getExpr().struct);
	}
	
	public void visit(ConditionFactRelop conditionFactRelop) {
		Struct left = conditionFactRelop.getExpr().struct;
		Struct right = conditionFactRelop.getExpr1().struct;
		
		if (!left.compatibleWith(right)) {
			report_error("Nekompatibilni tipovi za poredjenje ", null);
		}

		if (!(conditionFactRelop.getRelop() instanceof NotEqual || conditionFactRelop.getRelop() instanceof Equal) && left.getKind() == Struct.Array) {
			report_error("Za nizove dozvoljeno samo != i == ", null);
		}
	}
	
	public void visit(ConditionFactExpr conditionFactExpr) {
		if (!(new Struct(Struct.Bool)).assignableTo(conditionFactExpr.getExpr().struct)) {
			report_error("Uslovni izraz mora biti boolean",	null);
		}
	}
	
	public void visit(TermMinus expr) {
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
	
	public void visit(TermsList expr) {
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
	
	public void visit(TermMulopFactor mul) {
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
	
	public void visit(TermPlus addExpr) {
		addExpr.struct = addExpr.getTerm().struct;
	}
	
	public void visit(FactorTermSingle mul) {
		mul.struct = mul.getFactor().struct;
	}
	
	public void visit(FactorDesignatorNoActs designator) {
		designator.struct = designator.getDesignator().obj.getType();
	}
	
	public void visit(FactorDesignatorHasActs functionExpr) {
		if (Obj.Meth != functionExpr.getDesignator().obj.getKind()) {
			report_error(currentMethod.getName() + " nije funkcija ", functionExpr);
		} else {
			checkArgumentTypes(functionExpr.getDesignator().obj);
			functionExpr.struct = functionExpr.getDesignator().obj.getType();
		}
	}
	
	public void visit(FactorNumconst numConst) {
		numConst.struct = Tab.intType;
	}

	public void visit(FactorCharconst charConst) {
		charConst.struct = Tab.charType;
	}

	public void visit(FactorBoolconst boolConst) {
		boolConst.struct = new Struct(Struct.Bool);
	}
	
	public void visit(FactorNewTypeExpr typeExpr) {
		typeExpr.struct = new Struct(Struct.Array, typeExpr.getType().struct);
		if (!typeExpr.getExpr().struct.assignableTo(Tab.intType)) {
			report_error("Velicina niza nije mora biti tipa int ", null);
		}
	}

	public void visit(FactorNewType typeExpr) {
		typeExpr.struct = typeExpr.getType().struct;
	}

	public void visit(FactorParenths typeExpr) {
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

	public void visit(ArrayDesignator designatorArray) {
		Struct struct = designatorArray.getArrDesig().getDesignator().obj.getType();
		
		if (struct.getKind() != Struct.Array) {
			report_error("Designator nije tipa niza ", null);
		}
		else {
			DumpSymbolTableVisitor printer = new DumpSymbolTableVisitor();
			printer.visitObjNode(designatorArray.getArrDesig().getDesignator().obj);
			report_info("Detektovan pristup elementu niza : " + printer.getOutput(), designatorArray);
		}
		
		designatorArray.obj = new Obj(Obj.Elem, "Elem", struct.getElemType());
		
		if (!designatorArray.getExpr().struct.assignableTo(Tab.intType)) {
			report_error("Indeks niza mora biti tipa int ", null);
		}
	}
	
//	public void visit(ArrDesig designator) {
//		designator.getDesignator().obj = new Obj(Obj.Elem, "Elem", designator.getDesignator().obj.getType());;
//	}
	
	public boolean passed() {
		return !errorDetected;
	}
	
}

