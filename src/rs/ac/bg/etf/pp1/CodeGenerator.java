package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.CounterVisitor.FormParamCounter;
import rs.ac.bg.etf.pp1.CounterVisitor.VarCounter;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CodeGenerator extends VisitorAdaptor {
	
	private int mainPc;
	
	public int getMainPc() {
		return mainPc;
	}
	
	private void setMethodReturn() {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	private void setOrdChr() {
		Tab.chrObj.setAdr(Code.pc);
		Tab.ordObj.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(1);
		Code.put(1);
		Code.put(Code.load_n + 0);
		setMethodReturn();
	}
	
	private void setLen() {
		Tab.lenObj.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(1);
		Code.put(1);
		Code.put(Code.load_n + 0);
		Code.put(Code.arraylength);
		setMethodReturn();
	}
	
	@Override
	public void visit(Program program) {
		Code.dataSize = program.getProgName().obj.getLocalSymbols().size();
	}
	
	@Override
	public void visit(ProgName progrName){
		setOrdChr();
		setLen();
	}

	@Override
	public void visit(MethodRetTypeName method) {
		method.obj.setAdr(Code.pc);
		
		Code.put(Code.enter);
		Code.put(method.obj.getLevel());
		Code.put(method.obj.getLocalSymbols().size());
	}
	
	@Override
	public void visit(MethodRetVoidName method) {
		if ("main".equalsIgnoreCase(method.getMethName())) {
			mainPc = Code.pc;
		}
		
		method.obj.setAdr(Code.pc);
		
		Code.put(Code.enter);
		Code.put(method.obj.getLevel());
		Code.put(method.obj.getLocalSymbols().size());
	}
	
	@Override
	public void visit(DesignatorStatementAssignop assignment) {
		Code.store(assignment.getDesignator().obj);
	}
	
	@Override
	public void visit(DesignatorWithoutActFactor designator) {
		Code.load(designator.getDesignator().obj);
	}

	@Override
	public void visit(DesignatorWithActFactor funcCall) {
		Code.put(Code.call);
		Code.put2(funcCall.getDesignator().obj.getAdr() - Code.pc);
	}
	
	private boolean isElem(Obj obj) {
		return obj.getKind() == Obj.Elem;
	}
	
	@Override
	public void visit(DesignatorStatementOptAct funcCall) {
		Code.put(Code.call);
		Code.put2(funcCall.getDesignator().obj.getAdr() - Code.pc);
		if (funcCall.getDesignator().obj.getType() != Tab.noType) {
			Code.put(Code.pop);
		}
	}
	
	@Override
	public void visit(DesignatorStatementDEC designator) {
		Obj obj = designator.getDesignator().obj;
		
		if (isElem(obj)) {
			Code.put(Code.dup2);
		}
		
		Code.load(obj);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.store(obj);
	}

	@Override
	public void visit(DesignatorStatementINC designator) {
		Obj obj = designator.getDesignator().obj;
		
		if (isElem(obj)) {
			Code.put(Code.dup2);
		}
		
		Code.load(obj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(obj);
	}
	
	@Override
	public void visit(StatementReturn ret) {
		setMethodReturn();
	}
	
	@Override
	public void visit(MethodDeclRegular method) {
		setMethodReturn();
	}
	
	@Override
	public void visit(StatementPrintNoConst print) {
		Struct struct = print.getExpr().struct;
		if (Tab.charType != struct) {
			Code.loadConst(4);
			Code.put(Code.print);
		} else {
			Code.loadConst(1);
			Code.put(Code.bprint);
		}
	}

	@Override
	public void visit(StatementPrintWithConst print) {
		Struct struct = print.getExpr().struct;
		Code.loadConst(print.getN2());
		if (Tab.charType != struct) {
			Code.put(Code.print);
		} else {
			Code.put(Code.bprint);
		}
	}
	
	@Override
	public void visit(NumconstFactor Const) {
		Code.loadConst(Const.getValue());
	}

	@Override
	public void visit(CharconstFactor Const) {
		Code.loadConst(Const.getValue());
	}

	@Override
	public void visit(BoolconstFactor Const) {
		Code.loadConst(Const.getBoolConst().obj.getAdr());
	}
	
	@Override
	public void visit(NewTypeExprFactor array) {
		Code.put(Code.newarray);
		if (array.getType().struct == Tab.intType) {
			Code.put(1);
		} else {
			Code.put(0);
		}
	}
	
	@Override
	public void visit(OnlyArrayDesignator designator) {
		Code.load(designator.getDesignator().obj);
	}
	
	@Override
	public void visit(MultyTerms addExpr) {
		if (addExpr.getAddop() instanceof AddopMinus) Code.put(Code.sub);
		if (addExpr.getAddop() instanceof AddopPlus) Code.put(Code.add);
	}

	@Override
	public void visit(MinusTerm minusTerm) {
		Code.put(Code.neg);
	}

	@Override
	public void visit(MulopFactorTerm mulFact) {
		if (mulFact.getMulop() instanceof MulopMod) Code.put(Code.rem);
		if (mulFact.getMulop() instanceof MulopDiv) Code.put(Code.div);
		if (mulFact.getMulop() instanceof MulopMul) Code.put(Code.mul);
	}
	
	@Override
	public void visit(StatementRead read) {
		Obj obj = read.getDesignator().obj;
		if (Tab.charType != obj.getType()) {
			Code.put(Code.read);
		} else {
			Code.put(Code.bread);
		}
		Code.store(obj);
	}
	
	
}
