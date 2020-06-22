package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.symboltable.visitors.DumpSymbolTableVisitor;

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
	public void visit(FactorDesignatorNoActs designator) {
		Code.load(designator.getDesignator().obj);
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
	public void visit(AssignopDesigStatement assignment) {		
		Assignop assign = assignment.getAssignop();
		
		if(assign instanceof AssignopAddopRight) {
			
			AddopRight ar = ((AssignopAddopRight) assign).getAddopRight();
			if (ar instanceof MinusEqual) {
				Code.put(Code.sub);
				Code.put(Code.dup);
			}
			else if (ar instanceof PlusEqual) {
				Code.put(Code.add);
				Code.put(Code.dup);
			}
		}
		else if(assign instanceof AssignMulopRight) {
			MulopRight mr = ((AssignMulopRight) assign).getMulopRight();
			if(mr instanceof MulEqual) {
				Code.put(Code.mul);
				Code.put(Code.dup);
			}
			else if(mr instanceof DivEqual) {
				Code.put(Code.div);
				Code.put(Code.dup);
			}
			else if(mr instanceof ModEqual) {
				Code.put(Code.rem);
				Code.put(Code.dup);
			}
		}
		
		Code.store(assignment.getDesignator().obj);
	}
	
	@Override
	public void visit(ArrayDesignator designatorArray) {
		if(designatorArray.getParent() instanceof AssignopDesigStatement) {
			designatorArray.getArrDesig().getDesignator().obj = new Obj(Obj.Elem, "Elem", designatorArray.getArrDesig().getDesignator().obj.getType());
			Code.load(designatorArray.getArrDesig().getDesignator().obj);
		}
	}

	@Override
	public void visit(FactorDesignatorHasActs funcCall) {
		Code.put(Code.call);
		Code.put2(funcCall.getDesignator().obj.getAdr() - Code.pc);
	}
	
	private boolean isElem(Obj obj) {
		return obj.getKind() == Obj.Elem;
	}
	
	@Override
	public void visit(OptionalActualsDesigStatement funcCall) {
		Code.put(Code.call);
		Code.put2(funcCall.getDesignator().obj.getAdr() - Code.pc);
		if (funcCall.getDesignator().obj.getType() != Tab.noType) {
			Code.put(Code.pop);
		}
	}
	
	@Override
	public void visit(DecDesigStatement designator) {
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
	public void visit(IncDesigStatement designator) {
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
	public void visit(PrintStatement print) {
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
	public void visit(PrintConstStatement print) {
		Struct struct = print.getExpr().struct;
		Code.loadConst(print.getN2());
		if (Tab.charType != struct) {
			Code.put(Code.print);
		} else {
			Code.put(Code.bprint);
		}
	}
	
	@Override
	public void visit(ReturnStatement ret) {
		setMethodReturn();
	}
	
	@Override
	public void visit(MethodDeclaration method) {
		setMethodReturn();
	}
	
	@Override
	public void visit(FactorNumconst factorNumconst) {
		Code.loadConst(factorNumconst.getValue());
	}

	@Override
	public void visit(FactorCharconst factorCharconst) {
		Code.loadConst(factorCharconst.getValue());
	}

	@Override
	public void visit(FactorBoolconst factorBoolconst) {
		Code.loadConst(factorBoolconst.getBoolConst().obj.getAdr());
	}
	
	@Override
	public void visit(TermPlus termPlus) {
//		if(t.getParent() instanceof AssignopDesigStatement) {
			Code.put(Code.dup2);
//		}
	}
	
	@Override
	public void visit(TermMinus termMinus) {
		Code.put(Code.neg);
	}
	
	@Override
	public void visit(FactorNewTypeExpr array) {
		Code.put(Code.newarray);
		if (array.getType().struct == Tab.intType) Code.put(1);
		if (array.getType().struct == Tab.charType) Code.put(0);
	}
	
	@Override
	public void visit(ArrDesig designator) {
		Code.load(designator.getDesignator().obj);
	}

	public void visit(SimpleDesignator sd) {
		if(sd.getParent() instanceof AssignopDesigStatement) {
			Code.load(sd.obj);
		}
	}
	
//	@Override
//	public void visit(LeftAddop add) {
//		if (add.getAddopLeft() instanceof Minus) Code.put(Code.sub);
//		else if (add.getAddopLeft() instanceof Plus) Code.put(Code.add);
//	}
	
//	@Override
//	public void visit(Plus add) {
//		Code.put(Code.add);
//	}
//	
//	@Override
//	public void visit(Minus add) {
//		Code.put(Code.sub);
//	}
	
	@Override
	public void visit(TermsList t) {
		
//		if(t.getParent() instanceof AssignopDesigStatement) {
//			Code.put(Code.dup2);
//		}
		
		Addop add = t.getAddop();
		if (add instanceof RightAddop) {
			RightAddop ra = (RightAddop) add;
			if (ra.getAddopRight() instanceof MinusEqual) {
				Code.put(Code.sub);
				Code.put(Code.dup);
			}
			else if (ra.getAddopRight() instanceof PlusEqual) {
				Code.put(Code.add);
				Code.put(Code.dup);
			}
		}
		else if (add instanceof LeftAddop) {
			LeftAddop la = (LeftAddop) add;
			if (la.getAddopLeft() instanceof Minus) {
				Code.put(Code.sub);
			}
			else if (la.getAddopLeft() instanceof Plus) {
				Code.put(Code.add);
			}
		}
	}
	
//	@Override
//	public void visit(RightAddop add) {
//		if (add.getAddopRight() instanceof MinusEqual) {
//			Code.put(Code.sub);
//			Code.put(Code.dup);
//		}
//		else if (add.getAddopRight() instanceof PlusEqual) {
//			Code.put(Code.add);
//			Code.put(Code.dup);
//		}
//	}
	
	@Override
	public void visit(LeftMulop mulopLeft) {
		MulopLeft mulop = mulopLeft.getMulopLeft();
		if (mulop instanceof Mod) Code.put(Code.rem);
		else if (mulop instanceof Div) Code.put(Code.div);
		else if (mulop instanceof Mul) Code.put(Code.mul);
	}
	
	@Override
	public void visit(RightMulop mulopLeft) {
		
	}
	
	@Override
	public void visit(ReadStatement read) {
		Obj obj = read.getDesignator().obj;
		if (Tab.charType != obj.getType()) {
			Code.put(Code.read);
		} else {
			Code.put(Code.bread);
		}
		Code.store(obj);
	}
	
}
